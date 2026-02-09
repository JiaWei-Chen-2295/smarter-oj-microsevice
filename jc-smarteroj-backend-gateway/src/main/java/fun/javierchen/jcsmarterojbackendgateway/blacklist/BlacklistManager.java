package fun.javierchen.jcsmarterojbackendgateway.blacklist;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 黑名单管理器
 * 
 * 基于 Nacos 配置中心 + Guava 布隆过滤器实现高效黑名单查询
 * 
 * 设计要点：
 * 1. Nacos 配置存储黑名单列表（支持 IP 和用户 ID）
 * 2. 布隆过滤器提供 O(1) 时间复杂度的查询
 * 3. 配置变更时原子替换布隆过滤器，保证线程安全
 * 4. 支持 CIDR 格式的 IP 段匹配
 * 
 * Nacos 配置格式示例：
 * {
 * "ipBlacklist": ["1.2.3.4", "10.0.0.0/8", "192.168.1.100"],
 * "userBlacklist": ["user_12345", "user_67890"],
 * "tempIpBlacklist": {"1.2.3.4": 1706679600000},
 * "tempUserBlacklist": {"user_12345": 1706679600000}
 * }
 * 
 * @author JavierChen
 * @since 2026-01-30
 */
@Component
@Slf4j
public class BlacklistManager {

    // ====== Nacos 配置 ======
    @Value("${spring.cloud.nacos.serverAddr:127.0.0.1:8848}")
    private String nacosServerAddr;

    // 黑名单配置的 Data ID
    private static final String BLACKLIST_DATA_ID = "sentinel-blacklist.json";
    // 配置分组
    private static final String BLACKLIST_GROUP = "SENTINEL_GROUP";

    // ====== 布隆过滤器配置 ======
    // 预期元素数量：10万
    private static final int EXPECTED_INSERTIONS = 100000;
    // 误判率：0.1%
    private static final double FPP = 0.001;

    // ====== 布隆过滤器实例 (原子引用保证线程安全) ======
    // 永久 IP 黑名单
    private final AtomicReference<BloomFilter<String>> permanentIpBlacklistRef = new AtomicReference<>();
    // 永久用户黑名单
    private final AtomicReference<BloomFilter<String>> permanentUserBlacklistRef = new AtomicReference<>();

    // ====== 临时黑名单（带过期时间，使用 ConcurrentHashMap）======
    // Key: IP/用户ID, Value: 过期时间戳 (毫秒)
    private final Map<String, Long> tempIpBlacklist = new ConcurrentHashMap<>();
    private final Map<String, Long> tempUserBlacklist = new ConcurrentHashMap<>();

    // ====== CIDR 网段列表（精确匹配需要） ======
    private volatile List<CidrRange> cidrRanges = Collections.emptyList();

    // Nacos 配置服务
    private ConfigService configService;

    /**
     * 初始化黑名单管理器
     * 1. 初始化空的布隆过滤器
     * 2. 连接 Nacos 并加载配置
     * 3. 注册配置变更监听器
     */
    @PostConstruct
    public void init() {
        log.info("[Blacklist] 初始化黑名单管理器...");

        // 1. 初始化空布隆过滤器
        initEmptyBloomFilters();

        // 2. 连接 Nacos 并加载配置
        try {
            initNacosListener();
        } catch (NacosException e) {
            log.error("[Blacklist] Nacos 连接失败，黑名单功能不可用", e);
        }

        log.info("[Blacklist] 黑名单管理器初始化完成");
    }

    /**
     * 初始化空的布隆过滤器
     */
    private void initEmptyBloomFilters() {
        permanentIpBlacklistRef.set(createBloomFilter());
        permanentUserBlacklistRef.set(createBloomFilter());
        log.info("[Blacklist] 空布隆过滤器初始化完成");
    }

    /**
     * 创建新的布隆过滤器实例
     */
    private BloomFilter<String> createBloomFilter() {
        return BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                EXPECTED_INSERTIONS,
                FPP);
    }

    /**
     * 初始化 Nacos 配置监听
     */
    private void initNacosListener() throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", nacosServerAddr);

        configService = NacosFactory.createConfigService(properties);

        // 获取初始配置
        String config = configService.getConfig(BLACKLIST_DATA_ID, BLACKLIST_GROUP, 5000);
        if (config != null && !config.isEmpty()) {
            loadBlacklistConfig(config);
        } else {
            log.info("[Blacklist] Nacos 中暂无黑名单配置，使用空黑名单");
        }

        // 注册配置变更监听器
        configService.addListener(BLACKLIST_DATA_ID, BLACKLIST_GROUP, new Listener() {
            @Override
            public Executor getExecutor() {
                return Executors.newSingleThreadExecutor();
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("[Blacklist] 检测到 Nacos 配置变更，重新加载黑名单...");
                loadBlacklistConfig(configInfo);
            }
        });

        log.info("[Blacklist] Nacos 监听器注册完成，DataId={}, Group={}",
                BLACKLIST_DATA_ID, BLACKLIST_GROUP);
    }

    /**
     * 加载黑名单配置
     * 解析 JSON 配置并重建布隆过滤器
     */
    private void loadBlacklistConfig(String configJson) {
        try {
            JSONObject config = JSON.parseObject(configJson);

            // 1. 加载永久 IP 黑名单
            List<String> ipList = config.getJSONArray("ipBlacklist") != null
                    ? config.getJSONArray("ipBlacklist").toJavaList(String.class)
                    : Collections.emptyList();
            rebuildIpBloomFilter(ipList);

            // 2. 加载永久用户黑名单
            List<String> userList = config.getJSONArray("userBlacklist") != null
                    ? config.getJSONArray("userBlacklist").toJavaList(String.class)
                    : Collections.emptyList();
            rebuildUserBloomFilter(userList);

            // 3. 加载临时 IP 黑名单
            JSONObject tempIps = config.getJSONObject("tempIpBlacklist");
            if (tempIps != null) {
                tempIpBlacklist.clear();
                tempIps.forEach((ip, expireTime) -> tempIpBlacklist.put(ip, Long.parseLong(expireTime.toString())));
            }

            // 4. 加载临时用户黑名单
            JSONObject tempUsers = config.getJSONObject("tempUserBlacklist");
            if (tempUsers != null) {
                tempUserBlacklist.clear();
                tempUsers.forEach(
                        (userId, expireTime) -> tempUserBlacklist.put(userId, Long.parseLong(expireTime.toString())));
            }

            log.info("[Blacklist] 黑名单配置加载完成: 永久IP={}个, 永久用户={}个, 临时IP={}个, 临时用户={}个",
                    ipList.size(), userList.size(),
                    tempIpBlacklist.size(), tempUserBlacklist.size());

        } catch (Exception e) {
            log.error("[Blacklist] 解析黑名单配置失败", e);
        }
    }

    /**
     * 重建 IP 布隆过滤器
     * 使用原子替换保证线程安全
     */
    private void rebuildIpBloomFilter(List<String> ipList) {
        BloomFilter<String> newFilter = createBloomFilter();
        List<CidrRange> newCidrRanges = new java.util.ArrayList<>();

        for (String ip : ipList) {
            if (ip.contains("/")) {
                // CIDR 格式，需要特殊处理
                try {
                    newCidrRanges.add(new CidrRange(ip));
                } catch (Exception e) {
                    log.warn("[Blacklist] 解析 CIDR 失败: {}", ip);
                }
            } else {
                // 普通 IP，直接加入布隆过滤器
                newFilter.put(ip);
            }
        }

        // 原子替换
        permanentIpBlacklistRef.set(newFilter);
        cidrRanges = newCidrRanges;
    }

    /**
     * 重建用户布隆过滤器
     */
    private void rebuildUserBloomFilter(List<String> userList) {
        BloomFilter<String> newFilter = createBloomFilter();
        for (String userId : userList) {
            newFilter.put(userId);
        }
        permanentUserBlacklistRef.set(newFilter);
    }

    // ==================== 公开查询接口 ====================

    /**
     * 检查 IP 是否在黑名单中
     * 
     * @param ip 客户端 IP 地址
     * @return true 表示在黑名单中
     */
    public boolean isIpBlocked(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        // 1. 检查永久黑名单（布隆过滤器）
        if (permanentIpBlacklistRef.get().mightContain(ip)) {
            return true;
        }

        // 2. 检查 CIDR 网段匹配
        for (CidrRange range : cidrRanges) {
            if (range.contains(ip)) {
                return true;
            }
        }

        // 3. 检查临时黑名单
        Long expireTime = tempIpBlacklist.get(ip);
        if (expireTime != null) {
            if (System.currentTimeMillis() < expireTime) {
                return true;
            } else {
                // 已过期，移除
                tempIpBlacklist.remove(ip);
            }
        }

        return false;
    }

    /**
     * 检查用户是否在黑名单中
     * 
     * @param userId 用户 ID
     * @return true 表示在黑名单中
     */
    public boolean isUserBlocked(String userId) {
        if (userId == null || userId.isEmpty()) {
            return false;
        }

        // 1. 检查永久黑名单
        if (permanentUserBlacklistRef.get().mightContain(userId)) {
            return true;
        }

        // 2. 检查临时黑名单
        Long expireTime = tempUserBlacklist.get(userId);
        if (expireTime != null) {
            if (System.currentTimeMillis() < expireTime) {
                return true;
            } else {
                tempUserBlacklist.remove(userId);
            }
        }

        return false;
    }

    /**
     * 将 IP 加入临时黑名单
     * 
     * @param ip              客户端 IP
     * @param durationMinutes 封禁时长（分钟）
     */
    public void addTempIpBlacklist(String ip, int durationMinutes) {
        long expireTime = System.currentTimeMillis() + durationMinutes * 60 * 1000L;
        tempIpBlacklist.put(ip, expireTime);
        log.info("[Blacklist] IP {} 已加入临时黑名单，{}分钟后解封", ip, durationMinutes);
    }

    /**
     * 将用户加入临时黑名单
     * 
     * @param userId          用户 ID
     * @param durationMinutes 封禁时长（分钟）
     */
    public void addTempUserBlacklist(String userId, int durationMinutes) {
        long expireTime = System.currentTimeMillis() + durationMinutes * 60 * 1000L;
        tempUserBlacklist.put(userId, expireTime);
        log.info("[Blacklist] 用户 {} 已加入临时黑名单，{}分钟后解封", userId, durationMinutes);
    }

    // ==================== CIDR 网段匹配工具类 ====================

    /**
     * CIDR 网段范围
     * 支持如 "192.168.0.0/16" 格式的 IP 段匹配
     */
    private static class CidrRange {
        private final long networkAddress;
        private final long subnetMask;

        public CidrRange(String cidr) throws UnknownHostException {
            String[] parts = cidr.split("/");
            String ip = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);

            byte[] ipBytes = InetAddress.getByName(ip).getAddress();
            this.networkAddress = bytesToLong(ipBytes);
            this.subnetMask = prefixLength == 0 ? 0 : (0xFFFFFFFFL << (32 - prefixLength));
        }

        public boolean contains(String ip) {
            try {
                byte[] ipBytes = InetAddress.getByName(ip).getAddress();
                long ipLong = bytesToLong(ipBytes);
                return (ipLong & subnetMask) == (networkAddress & subnetMask);
            } catch (UnknownHostException e) {
                return false;
            }
        }

        private static long bytesToLong(byte[] bytes) {
            long result = 0;
            for (byte b : bytes) {
                result = (result << 8) | (b & 0xFF);
            }
            return result;
        }
    }
}
