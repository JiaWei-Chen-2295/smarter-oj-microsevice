package fun.javierchen.jcsmarterojbackendgateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关请求计时过滤器
 * 
 * <p>
 * 用于测量请求从进入网关到响应完成的完整耗时，
 * 帮助分析网关层的性能瓶颈。
 * </p>
 *
 * @author JavierChen
 */
@Component
public class RequestTimingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RequestTimingFilter.class);

    private static final String START_TIME_ATTR = "gateway_start_time";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethodValue();

        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        exchange.getAttributes().put(START_TIME_ATTR, startTime);

        log.info("[网关计时] 请求开始 - {} {}", method, path);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long gatewayStartTime = exchange.getAttribute(START_TIME_ATTR);
            if (gatewayStartTime != null) {
                long duration = System.currentTimeMillis() - gatewayStartTime;
                int statusCode = exchange.getResponse().getStatusCode() != null
                        ? exchange.getResponse().getStatusCode().value()
                        : 0;

                // 对于超过 1000ms 的请求，使用 WARN 级别日志
                if (duration > 1000) {
                    log.warn("[网关计时] 请求完成(慢) - {} {} - 状态码: {} - 耗时: {}ms",
                            method, path, statusCode, duration);
                } else {
                    log.info("[网关计时] 请求完成 - {} {} - 状态码: {} - 耗时: {}ms",
                            method, path, statusCode, duration);
                }
            }
        }));
    }

    @Override
    public int getOrder() {
        // 设置最高优先级，确保是第一个执行的过滤器
        // 这样可以测量完整的请求处理时间
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
