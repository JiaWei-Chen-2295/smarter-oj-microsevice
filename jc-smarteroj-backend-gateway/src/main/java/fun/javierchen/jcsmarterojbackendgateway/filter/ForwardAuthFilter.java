package fun.javierchen.jcsmarterojbackendgateway.filter;

import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关全局过滤器：为下游服务请求添加认证信息
 * 
 * <p>
 * 设计说明：
 * </p>
 * <p>
 * 当请求经过网关转发到下游微服务时，需要携带以下信息：
 * </p>
 * <ul>
 * <li>1. Same-Token：证明请求来自网关（可信来源）</li>
 * <li>2. User Token：透传用户登录凭证（如果存在）</li>
 * </ul>
 * 
 * <p>
 * 这样下游服务可以：
 * </p>
 * <ul>
 * <li>通过 Same-Token 验证请求来源的合法性</li>
 * <li>通过 User Token 获取当前登录用户信息</li>
 * </ul>
 *
 * @author JavierChen
 */
@Component
public class ForwardAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();

        // 1. 为请求添加 Same-Token（服务间调用凭证）
        requestBuilder.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());

        // 2. 透传用户 Token（如果请求中包含）
        // 注意：在响应式环境中，StpUtil 可能无法直接获取 Token
        // 所以我们从原始请求头中获取并透传
        String tokenName = StpUtil.getTokenName();
        String tokenValue = exchange.getRequest().getHeaders().getFirst(tokenName);
        if (tokenValue != null && !tokenValue.isEmpty()) {
            requestBuilder.header(tokenName, tokenValue);
        }

        // 构建新请求并继续过滤器链
        ServerHttpRequest newRequest = requestBuilder.build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        // 设置较高优先级，确保在其他过滤器之前执行
        return -100;
    }
}
