package fun.javierchen.jcsmarterojbackendgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * UTF-8 编码全局过滤器
 * 解决 Spring Cloud Gateway 转发响应时的中文乱码问题
 */
@Component
public class Utf8EncodingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 为响应设置 UTF-8 编码
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders headers = exchange.getResponse().getHeaders();
            MediaType contentType = headers.getContentType();
            
            // 如果是 JSON 响应，确保使用 UTF-8 编码
            if (contentType != null && contentType.includes(MediaType.APPLICATION_JSON)) {
                headers.setContentType(new MediaType(
                    MediaType.APPLICATION_JSON,
                    StandardCharsets.UTF_8
                ));
            }
        }));
    }

    @Override
    public int getOrder() {
        // 设置较高优先级，确保在其他过滤器之后执行
        return Ordered.LOWEST_PRECEDENCE;
    }
}
