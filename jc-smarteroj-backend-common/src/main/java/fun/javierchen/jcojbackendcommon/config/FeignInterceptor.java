package fun.javierchen.jcojbackendcommon.config;

import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * Feign 拦截器：为 RPC 调用添加认证信息
 * 
 * <p>
 * 设计说明：
 * </p>
 * <p>
 * 在微服务架构中，服务间调用需要传递两种凭证：
 * </p>
 * <ul>
 * <li>1. Same-Token：服务间调用身份凭证（证明请求来自可信的内部服务）</li>
 * <li>2. User Token：用户登录凭证（保持用户会话状态）</li>
 * </ul>
 * 
 * <p>
 * 工作原理：
 * </p>
 * 
 * <pre>
 * 用户请求 → Gateway(透传Token) → ServiceA → [Feign透传Token] → ServiceB
 *                                                ↓
 *                                        自动添加 Same-Token + User Token
 * </pre>
 *
 * @author JavierChen
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 1. 添加 Same-Token（服务间调用凭证）
        // 下游服务可以通过校验 Same-Token 来确认请求来自可信的内部服务
        requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());

        // 2. 添加用户 Token（保持会话状态）
        // 如果当前上下文中存在登录用户，将其 Token 传递给下游服务
        // 这样下游服务也能获取到当前登录用户的信息
        try {
            if (StpUtil.isLogin()) {
                requestTemplate.header(StpUtil.getTokenName(), StpUtil.getTokenValue());
            }
        } catch (Exception e) {
            // 如果获取 Token 失败（如在非 Web 上下文中调用），忽略异常
            // 此时仅传递 Same-Token，不传递用户 Token
        }
    }
}
