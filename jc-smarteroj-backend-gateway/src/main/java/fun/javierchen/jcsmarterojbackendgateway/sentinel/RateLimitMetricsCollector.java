package fun.javierchen.jcsmarterojbackendgateway.sentinel;

import fun.javierchen.jcsmarterojbackendgateway.blacklist.BlacklistManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流指标收集器
 * 
 * 功能：
 * 1. 记录每个 IP/用户触发限流的次数
 * 2. 当触发次数达到阈值时，自动将其加入临时黑名单
 * 3. 定期清理过期的统计数据
 * 
 * 黑名单规则（参考规划文档）：
 * - 临时黑名单：1小时内触发限流 ≥ 3次 → 封禁 30 分钟
 * - 永久黑名单：进入临时黑名单 ≥ 3次 → 永久封禁（需人工审核）
 * 
 * @author JavierChen
 * @since 2026-01-30
 */
@Component
@Slf4j
public class RateLimitMetricsCollector {

    @Resource
    private BlacklistManager blacklistManager;

    // ====== 阈值配置 ======

    // 触发限流次数阈值：1小时内触发 ≥ 3次 加入临时黑名单
    private static final int RATE_LIMIT_THRESHOLD = 3;

    // 临时黑名单封禁时长（分钟）
    private static final int TEMP_BAN_DURATION_MINUTES = 30;

    // 统计时间窗口（毫秒）：1小时
    private static final long STAT_WINDOW_MS = 60 * 60 * 1000L;

    // 进入临时黑名单次数阈值：≥ 3次 触发永久黑名单警告
    private static final int PERM_BAN_THRESHOLD = 3;

    // ====== 统计数据结构 ======

    /**
     * IP 限流统计
     * Key: IP 地址
     * Value: 统计记录
     */
    private final Map<String, RateLimitRecord> ipRateLimitStats = new ConcurrentHashMap<>();

    /**
     * 用户限流统计
     * Key: 用户 ID
     * Value: 统计记录
     */
    private final Map<String, RateLimitRecord> userRateLimitStats = new ConcurrentHashMap<>();

    /**
     * 临时黑名单次数统计（用于判断是否触发永久黑名单）
     * Key: IP/用户 ID
     * Value: 进入临时黑名单的次数
     */
    private final Map<String, AtomicInteger> tempBanCountStats = new ConcurrentHashMap<>();

    /**
     * 记录 IP 触发限流
     * 
     * @param ip   客户端 IP
     * @param path 请求路径
     */
    public void recordIpRateLimit(String ip, String path) {
        if (ip == null || ip.isEmpty() || "unknown".equals(ip)) {
            return;
        }

        RateLimitRecord record = ipRateLimitStats.computeIfAbsent(ip, k -> new RateLimitRecord());
        int count = record.increment();

        log.info("[RateLimitMetrics] IP {} 触发限流，当前计数: {}，路径: {}", ip, count, path);

        // 检查是否需要加入临时黑名单
        if (count >= RATE_LIMIT_THRESHOLD && !blacklistManager.isIpBlocked(ip)) {
            addToTempBlacklist(ip, true);
        }
    }

    /**
     * 记录用户触发限流
     * 
     * @param userId 用户 ID
     * @param path   请求路径
     */
    public void recordUserRateLimit(String userId, String path) {
        if (userId == null || userId.isEmpty()) {
            return;
        }

        RateLimitRecord record = userRateLimitStats.computeIfAbsent(userId, k -> new RateLimitRecord());
        int count = record.increment();

        log.info("[RateLimitMetrics] 用户 {} 触发限流，当前计数: {}，路径: {}", userId, count, path);

        // 检查是否需要加入临时黑名单
        if (count >= RATE_LIMIT_THRESHOLD && !blacklistManager.isUserBlocked(userId)) {
            addToTempBlacklist(userId, false);
        }
    }

    /**
     * 将目标加入临时黑名单
     * 
     * @param target IP 或用户 ID
     * @param isIp   true 表示 IP，false 表示用户
     */
    private void addToTempBlacklist(String target, boolean isIp) {
        // 增加临时黑名单计数
        int tempBanCount = tempBanCountStats
                .computeIfAbsent(target, k -> new AtomicInteger(0))
                .incrementAndGet();

        // 检查是否需要警告永久黑名单
        if (tempBanCount >= PERM_BAN_THRESHOLD) {
            log.error("[RateLimitMetrics] ⚠️ 警告：{} {} 已进入临时黑名单 {} 次，建议加入永久黑名单！",
                    isIp ? "IP" : "用户", target, tempBanCount);
            // TODO: 发送告警通知（邮件/钉钉/企业微信）
        }

        // 加入临时黑名单
        if (isIp) {
            blacklistManager.addTempIpBlacklist(target, TEMP_BAN_DURATION_MINUTES);
            log.warn("[RateLimitMetrics] IP {} 已加入临时黑名单，封禁 {} 分钟（第 {} 次）",
                    target, TEMP_BAN_DURATION_MINUTES, tempBanCount);
        } else {
            blacklistManager.addTempUserBlacklist(target, TEMP_BAN_DURATION_MINUTES);
            log.warn("[RateLimitMetrics] 用户 {} 已加入临时黑名单，封禁 {} 分钟（第 {} 次）",
                    target, TEMP_BAN_DURATION_MINUTES, tempBanCount);
        }

        // 重置限流计数
        if (isIp) {
            ipRateLimitStats.remove(target);
        } else {
            userRateLimitStats.remove(target);
        }
    }

    /**
     * 限流记录
     * 包含计数和时间窗口
     */
    private static class RateLimitRecord {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile long windowStart = System.currentTimeMillis();

        /**
         * 增加计数
         * 如果超出时间窗口，重置计数
         */
        public int increment() {
            long now = System.currentTimeMillis();

            // 检查是否需要重置时间窗口
            if (now - windowStart > STAT_WINDOW_MS) {
                synchronized (this) {
                    if (now - windowStart > STAT_WINDOW_MS) {
                        count.set(0);
                        windowStart = now;
                    }
                }
            }

            return count.incrementAndGet();
        }

        public int getCount() {
            return count.get();
        }
    }
}
