package fun.javierchen.jcsmarterojbackendgateway.filter;

import com.alibaba.fastjson.JSON;
import fun.javierchen.jcsmarterojbackendgateway.blacklist.BlacklistManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * IP 黑名单全局过滤器
 * 
 * 在请求进入网关时首先检查 IP 是否在黑名单中，
 * 如果在黑名单中则直接拒绝请求，不再转发到后端服务。
 * 
 * 执行顺序：
 * 1. BlacklistFilter（本过滤器，优先级最高）
 * 2. SentinelGatewayFilter（限流过滤器）
 * 3. GlobalAuthFilter（权限过滤器）
 * 4. 其他业务过滤器
 * 
 * @author JavierChen
 * @since 2026-01-30
 */
@Component
@Slf4j
public class BlacklistFilter implements GlobalFilter, Ordered {

    @Resource
    private BlacklistManager blacklistManager;

    // ====== 常用请求头，用于获取真实客户端 IP ======
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_REAL_IP = "X-Real-IP";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    /**
     * 过滤器执行逻辑
     * 
     * @param exchange 请求上下文
     * @param chain    过滤器链
     * @return Mono<Void>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 1. 获取客户端真实 IP
        String clientIp = getClientIp(request);

        // 2. 检查 IP 黑名单
        if (blacklistManager.isIpBlocked(clientIp)) {
            log.warn("[Blacklist] IP 被拦截: {} - Path: {}",
                    clientIp, request.getURI().getPath());
            return buildBlockedResponse(exchange, "IP 已被加入黑名单");
        }

        // 3. 获取用户 ID（从请求头中获取，由 Sa-Token 注入）
        String userId = request.getHeaders().getFirst("X-User-Id");

        // 4. 检查用户黑名单
        if (userId != null && blacklistManager.isUserBlocked(userId)) {
            log.warn("[Blacklist] 用户被拦截: {} - IP: {} - Path: {}",
                    userId, clientIp, request.getURI().getPath());
            return buildBlockedResponse(exchange, "用户已被加入黑名单");
        }

        // 5. 将客户端 IP 注入请求头，供后续限流使用
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Real-Client-IP", clientIp)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    /**
     * 获取客户端真实 IP
     * 考虑代理服务器和负载均衡器的情况
     * 
     * @param request HTTP 请求
     * @return 客户端 IP 地址
     */
    private String getClientIp(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();

        // 1. 尝试从 X-Forwarded-For 头获取（经过代理时）
        String ip = headers.getFirst(X_FORWARDED_FOR);
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For 可能包含多个 IP，取第一个（客户端真实 IP）
            int idx = ip.indexOf(',');
            return idx > 0 ? ip.substring(0, idx).trim() : ip.trim();
        }

        // 2. 尝试从 X-Real-IP 头获取（Nginx 代理常用）
        ip = headers.getFirst(X_REAL_IP);
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        // 3. 尝试从 Proxy-Client-IP 头获取
        ip = headers.getFirst(PROXY_CLIENT_IP);
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        // 4. 尝试从 WL-Proxy-Client-IP 头获取（WebLogic）
        ip = headers.getFirst(WL_PROXY_CLIENT_IP);
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        // 5. 直接从连接获取
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress != null && remoteAddress.getAddress() != null) {
            return remoteAddress.getAddress().getHostAddress();
        }

        return "unknown";
    }

    /**
     * 构建被拦截的响应
     * 
     * @param exchange 请求上下文
     * @param message  错误消息
     * @return Mono<Void>
     */
    private Mono<Void> buildBlockedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 构建标准错误响应
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("code", 40301);
        errorBody.put("message", message);
        errorBody.put("data", null);

        byte[] bytes = JSON.toJSONString(errorBody).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 设置过滤器优先级
     * 返回最高优先级，确保在所有过滤器之前执行黑名单检查
     */
    @Override
    public int getOrder() {
        // 优先级最高，在 Sentinel 限流之前执行
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
