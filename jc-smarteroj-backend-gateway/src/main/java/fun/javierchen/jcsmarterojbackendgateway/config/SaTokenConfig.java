package fun.javierchen.jcsmarterojbackendgateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 网关鉴权配置
 * 
 * <p>
 * 配置网关层面的统一鉴权规则，所有请求经过网关时都会进行权限校验
 * </p>
 * <p>
 * 核心功能：
 * </p>
 * <ul>
 * <li>1. 开放登录、注册等公共接口</li>
 * <li>2. 拦截内网接口，禁止外部直接访问</li>
 * <li>3. 其他接口要求登录后访问</li>
 * </ul>
 *
 * @author JavierChen
 */
@Configuration
public class SaTokenConfig {

    /**
     * 注册 Sa-Token 全局过滤器
     * 
     * <p>
     * 设计说明：
     * </p>
     * <ul>
     * <li>使用 SaReactorFilter 适配 Spring Cloud Gateway 的响应式架构</li>
     * <li>白名单设计：登录/注册/文档等接口不需要鉴权</li>
     * <li>内网保护：/inner/** 接口只能服务间调用，外部禁止访问</li>
     * </ul>
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截所有路由
                .addInclude("/**")
                // 开放路由（不需要登录即可访问）
                .addExclude(
                        // 用户服务 - 登录注册相关（路径是 /api/user + /）
                        "/api/user/login",
                        "/api/user/login/phone", // 手机号登录
                        "/api/user/register",
                        "/api/user/logout",
                        "/api/user/captcha/**", // 验证码接口
                        "/api/user/get/login", // 获取当前登录用户（前端轮询）
                        // 接口文档
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/favicon.ico",
                        // 健康检查
                        "/actuator/**",
                        // 内网接口（由 GlobalAuthFilter 单独处理）
                        "/api/user/inner/**",
                        "/api/question/inner/**",
                        "/api/judge/inner/**",
                        "/api/post/inner/**")
                // 鉴权规则
                .setAuth(obj -> {
                    // 所有其他接口需要登录
                    SaRouter.match("/**", r -> StpUtil.checkLogin());
                })
                // 异常处理
                .setError(e -> {
                    // 返回统一格式的错误响应
                    return SaResult.error(e.getMessage()).setCode(40100);
                });
    }
}
