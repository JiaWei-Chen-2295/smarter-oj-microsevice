package fun.javierchen.jcsmarterojbackendgateway.filter;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 用户 ID 注入过滤器
 * 
 * 功能：
 * 1. 从 Sa-Token 获取当前登录用户 ID
 * 2. 将用户 ID 注入到请求头中（X-User-Id）
 * 3. 供下游服务和 Sentinel 限流使用
 * 
 * 执行顺序：
 * 1. BlacklistFilter（最高优先级）
 * 2. UserIdInjectionFilter（本过滤器）
 * 3. SentinelGatewayFilter
 * 4. 其他过滤器
 * 
 * @author JavierChen
 * @since 2026-01-30
 */
@Component
@Slf4j
public class UserIdInjectionFilter implements GlobalFilter, Ordered {

    /**
     * 请求头名称：用户 ID
     */
    private static final String HEADER_USER_ID = "X-User-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            // 检查用户是否已登录
            if (StpUtil.isLogin()) {
                // 获取用户 ID
                String userId = StpUtil.getLoginIdAsString();

                // 将用户 ID 注入请求头
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header(HEADER_USER_ID, userId)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }
        } catch (Exception e) {
            // 未登录或获取用户 ID 失败，忽略错误继续处理
            log.debug("[UserIdInjection] 获取用户 ID 失败: {}", e.getMessage());
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 在黑名单过滤器之后，Sentinel 过滤器之前执行
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
