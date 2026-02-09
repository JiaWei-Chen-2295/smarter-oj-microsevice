package fun.javierchen.jcojbackendcommon.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sentinel 自动配置类
 * 
 * 条件加载说明：
 * - 仅当 Sentinel 依赖存在时才加载
 * - 可通过 sentinel.enabled=false 禁用
 * 
 * @author JavierChen
 * @since 2026-01-30
 */
@Configuration
@ConditionalOnClass(name = "com.alibaba.csp.sentinel.SphU")
@ConditionalOnProperty(name = "sentinel.enabled", havingValue = "true", matchIfMissing = true)
public class SentinelAutoConfiguration {

    /**
     * 注册 Sentinel Web MVC 限流异常处理器
     * 处理 @SentinelResource 注解和 URL 资源的限流异常
     */
    @Bean
    public BlockExceptionHandler sentinelBlockExceptionHandler() {
        return new SentinelBlockExceptionHandler();
    }
}
