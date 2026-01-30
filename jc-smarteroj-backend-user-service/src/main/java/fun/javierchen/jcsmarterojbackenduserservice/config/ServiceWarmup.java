package fun.javierchen.jcsmarterojbackenduserservice.config;

import cn.dev33.satoken.stp.StpUtil;
import fun.javierchen.jcsmarterojbackenduserservice.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务预热组件
 * 
 * <p>
 * 在服务启动完成后，自动预热数据库连接池、Redis 连接池和 Sa-Token，
 * 解决首次请求因连接建立导致的延迟问题。
 * 同时提供定时保活功能，防止长时间空闲导致连接失效。
 * </p>
 *
 * @author JavierChen
 */
@Component
public class ServiceWarmup {

    private static final Logger log = LoggerFactory.getLogger(ServiceWarmup.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 应用启动完成后执行预热
     */
    @EventListener(ApplicationReadyEvent.class)
    public void warmup() {
        log.info("[User Service 预热] 开始预热...");
        long startTime = System.currentTimeMillis();
        long stepTime = startTime;

        // 1. 预热数据库连接池
        try {
            // 执行一个简单的查询来触发连接池初始化
            userMapper.selectCount(null);
            log.info("[User Service 预热] 数据库连接池预热完成，耗时: {}ms",
                    System.currentTimeMillis() - stepTime);
        } catch (Exception e) {
            log.warn("[User Service 预热] 数据库预热失败: {}", e.getMessage());
        }
        stepTime = System.currentTimeMillis();

        // 2. 预热 Redis 连接池（多次访问确保连接池充分初始化）
        try {
            for (int i = 0; i < 5; i++) {
                stringRedisTemplate.opsForValue().get("warmup:test:" + i);
            }
            log.info("[User Service 预热] Redis 连接池预热完成，耗时: {}ms",
                    System.currentTimeMillis() - stepTime);
        } catch (Exception e) {
            log.warn("[User Service 预热] Redis 预热失败: {}", e.getMessage());
        }
        stepTime = System.currentTimeMillis();

        // 3. 预热 Sa-Token Redis 连接（关键！解决 isLogin/getSession 慢的问题）
        try {
            // 使用一个不存在的 token 来触发 Sa-Token 的 Redis 连接初始化
            // 这会触发 Sa-Token 内部的 Redis 数据访问层初始化
            for (int i = 0; i < 3; i++) {
                try {
                    StpUtil.getLoginIdByToken("warmup-token-" + i);
                } catch (Exception ignored) {
                    // 预期会抛出异常，忽略
                }
            }
            log.info("[User Service 预热] Sa-Token Redis 预热完成，耗时: {}ms",
                    System.currentTimeMillis() - stepTime);
        } catch (Exception e) {
            log.warn("[User Service 预热] Sa-Token 预热失败: {}", e.getMessage());
        }

        log.info("[User Service 预热] 预热完成，总耗时: {}ms",
                System.currentTimeMillis() - startTime);
    }

    /**
     * 定期保持连接活跃 - 每2分钟执行一次
     * 
     * <p>
     * 防止数据库连接、Redis 连接和 Sa-Token Redis 连接因空闲超时被关闭。
     * </p>
     */
    @Scheduled(fixedRate = 120000) // 2分钟 = 120000ms
    public void keepConnectionsAlive() {
        try {
            // 1. 数据库心跳
            userMapper.selectCount(null);

            // 2. Redis 心跳
            stringRedisTemplate.hasKey("__heartbeat__");

            // 3. Sa-Token Redis 心跳
            try {
                StpUtil.getLoginIdByToken("heartbeat-token");
            } catch (Exception ignored) {
                // 预期会抛出异常，忽略
            }

            log.debug("[User Service 保活] 心跳发送成功");
        } catch (Exception e) {
            log.warn("[User Service 保活] 心跳失败: {}", e.getMessage());
        }
    }
}
