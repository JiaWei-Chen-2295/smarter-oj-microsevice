# Sa-Token 登录认证迁移方案

> **文档版本**: v1.0  
> **创建时间**: 2026-01-23  
> **适用项目**: smarter-oj-microsevice  
> **当前认证**: Spring Session + Redis (Cookie 存储 Session ID)  
> **目标认证**: Sa-Token 1.39.0 + Redis

---

## 一、迁移概述

### 1.1 当前架构分析

```
┌──────────────────────────────────────────────────────────────────────┐
│                           当前认证架构                                  │
├──────────────────────────────────────────────────────────────────────┤
│                                                                       │
│   ┌─────────┐     ┌─────────┐     ┌─────────────────────────────┐   │
│   │ 前端    │────▶│ Gateway │────▶│   各微服务                    │   │
│   │ (Cookie)│     │         │     │ (从 Session 获取用户信息)     │   │
│   └─────────┘     └─────────┘     └─────────────────────────────┘   │
│                         │                       │                    │
│                         ▼                       ▼                    │
│                   ┌─────────────────────────────────┐               │
│                   │          Redis                   │               │
│                   │  (spring:session:sessions:xxx)   │               │
│                   └─────────────────────────────────┘               │
└──────────────────────────────────────────────────────────────────────┘
```

**当前存在的问题**：

| 问题 | 影响 | 严重程度 |
|------|------|----------|
| Cookie 跨域限制 | 前后端分离场景下 Cookie 策略复杂 | ⚠️ 中 |
| 微服务间 Session 传递 | Feign 调用时需要手动处理 | 🔴 高 |
| WebSocket 认证困难 | 长连接无法使用 Cookie 认证 | 🔴 高 |
| 缺乏权限注解 | 每个接口需手动校验权限 | ⚠️ 中 |
| 无踢人下线 | 无法实现账号封禁、强制下线 | ⚠️ 中 |

### 1.2 目标架构

```
┌──────────────────────────────────────────────────────────────────────┐
│                         Sa-Token 认证架构                              │
├──────────────────────────────────────────────────────────────────────┤
│                                                                       │
│   ┌─────────┐     ┌─────────────────┐     ┌───────────────────────┐ │
│   │ 前端    │────▶│     Gateway     │────▶│      各微服务          │ │
│   │ (Token) │     │ (统一鉴权+透传) │     │ (注解式权限校验)       │ │
│   └─────────┘     └─────────────────┘     └───────────────────────┘ │
│       │                   │                         │                │
│       │           ┌───────┴───────┐                 │                │
│       │           ▼               ▼                 │                │
│       │   Same-Token 校验    Token 透传             │                │
│       │                                             │                │
│       └─────────────────────┬───────────────────────┘                │
│                             ▼                                        │
│                   ┌─────────────────────────────────┐               │
│                   │          Redis                   │               │
│                   │  (satoken:login:token:xxx)       │               │
│                   └─────────────────────────────────┘               │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 二、依赖配置

### 2.1 Sa-Token 版本说明

| 组件 | 版本 | 说明 |
|------|------|------|
| Sa-Token 核心 | 1.39.0 | 稳定版本，兼容 Spring Boot 2.6.x |
| Sa-Token Redis | 1.39.0 | Redis 整合（Jackson 序列化） |
| Sa-Token Reactor | 1.39.0 | Gateway 网关支持 |

### 2.2 父 POM 添加版本管理

**文件**: `pom.xml` (根目录)

```xml
<properties>
    <!-- 新增 Sa-Token 版本 -->
    <sa-token.version>1.39.0</sa-token.version>
</properties>
```

### 2.3 公共模块依赖

**文件**: `jc-smarteroj-backend-common/pom.xml`

```xml
<!-- Sa-Token 核心包 -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-spring-boot-starter</artifactId>
    <version>${sa-token.version}</version>
</dependency>

<!-- Sa-Token 整合 Redis（使用 Jackson 序列化） -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-redis-jackson</artifactId>
    <version>${sa-token.version}</version>
</dependency>

<!-- 提供 Redis 连接池 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

### 2.4 网关模块依赖

**文件**: `jc-smarteroj-backend-gateway/pom.xml`

```xml
<!-- Sa-Token Reactor 整合包（用于 Spring Cloud Gateway） -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-reactor-spring-boot-starter</artifactId>
    <version>${sa-token.version}</version>
</dependency>

<!-- Sa-Token 整合 Redis（网关也需要访问 Token 信息） -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-redis-jackson</artifactId>
    <version>${sa-token.version}</version>
</dependency>

<!-- 提供 Redis 连接池 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>

<!-- 需要添加 Redis 支持 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>
```

---

## 三、配置文件修改

### 3.1 用户服务配置

**文件**: `jc-smarteroj-backend-user-service/src/main/resources/application.yml`

```yaml
# 新增 Sa-Token 配置
sa-token:
  # token 名称（同时也是 cookie 名称）
  token-name: satoken
  # token 有效期（单位：秒），默认 30 天，-1 代表永久有效
  timeout: 2592000
  # token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认 -1 代表不限制，永不冻结
  active-timeout: -1
  # 是否允许同一账号多地同时登录（为 true 时允许一起登录，为 false 时新登录挤掉旧登录）
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个 token（为 true 时所有登录共用一个 token，为 false 时每次登录都新建一个 token）
  is-share: true
  # token 风格（可选：uuid、simple-uuid、random-32、random-64、random-128、tik）
  token-style: uuid
  # 是否输出操作日志
  is-log: true
  # token 前缀（前端传递 token 时需要带上此前缀）
  # token-prefix: "Bearer"

# 保留原有 Redis 配置即可，Sa-Token 会自动使用
spring:
  redis:
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
```

### 3.2 网关服务配置

**文件**: `jc-smarteroj-backend-gateway/src/main/resources/application.yml`

```yaml
# 新增 Sa-Token 配置（与服务端保持一致）
sa-token:
  token-name: satoken
  timeout: 2592000
  active-timeout: -1
  is-concurrent: true
  is-share: true
  token-style: uuid
  is-log: true
  # 开启内网服务校验
  check-same-token: true

spring:
  redis:
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
```

---

## 四、代码修改

### 4.1 新建用户常量类（迁移准备）

**文件**: `jc-smarteroj-backend-common/src/main/java/fun/javierchen/jcojbackendcommon/constant/UserConstant.java`

**修改**：添加 Sa-Token Session 存储 Key

```java
/**
 * Sa-Token Session 中存储用户信息的 Key
 */
public static final String SA_SESSION_USER_KEY = "user_info";
```

### 4.2 修改 UserServiceImpl 登录逻辑

**文件**: `jc-smarteroj-backend-user-service/.../service/impl/UserServiceImpl.java`

**原代码**：
```java
public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
    // ... 校验逻辑 ...
    
    // 3. 记录用户的登录态
    request.getSession().setAttribute(USER_LOGIN_STATE, user);
    return this.getLoginUserVO(user);
}
```

**新代码**：
```java
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.session.SaSession;  
import static fun.javierchen.jcojbackendcommon.constant.UserConstant.SA_SESSION_USER_KEY;

public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
    // ... 校验逻辑保持不变 ...
    
    // 3. 使用 Sa-Token 记录用户的登录态
    StpUtil.login(user.getId());
    // 将用户信息存入 Session
    SaSession session = StpUtil.getSession();
    session.set(SA_SESSION_USER_KEY, user);
    
    return this.getLoginUserVO(user);
}
```

### 4.3 修改 getLoginUser 方法

**原代码**：
```java
public User getLoginUser(HttpServletRequest request) {
    Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
    User currentUser = (User) userObj;
    if (currentUser == null || currentUser.getId() == null) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    // 从数据库查询
    long userId = currentUser.getId();
    currentUser = this.getById(userId);
    if (currentUser == null) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    return currentUser;
}
```

**新代码**：
```java
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.session.SaSession;

public User getLoginUser(HttpServletRequest request) {
    // 1. 判断是否已登录
    if (!StpUtil.isLogin()) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    
    // 2. 从 Session 获取用户信息
    SaSession session = StpUtil.getSession();
    User currentUser = session.getModel(SA_SESSION_USER_KEY, User.class);
    
    if (currentUser == null || currentUser.getId() == null) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    
    // 3. 从数据库查询最新信息（可选，追求性能可注释）
    long userId = currentUser.getId();
    currentUser = this.getById(userId);
    if (currentUser == null) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    
    return currentUser;
}
```

### 4.4 修改 getLoginUserPermitNull 方法

**新代码**：
```java
public User getLoginUserPermitNull(HttpServletRequest request) {
    // 1. 判断是否已登录
    if (!StpUtil.isLogin()) {
        return null;
    }
    
    // 2. 从 Session 获取用户信息
    SaSession session = StpUtil.getSession();
    User currentUser = session.getModel(SA_SESSION_USER_KEY, User.class);
    
    if (currentUser == null || currentUser.getId() == null) {
        return null;
    }
    
    // 3. 从数据库查询最新信息
    return this.getById(currentUser.getId());
}
```

### 4.5 修改 isAdmin 方法

**新代码**：
```java
public boolean isAdmin(HttpServletRequest request) {
    if (!StpUtil.isLogin()) {
        return false;
    }
    SaSession session = StpUtil.getSession();
    User user = session.getModel(SA_SESSION_USER_KEY, User.class);
    return isAdmin(user);
}
```

### 4.6 修改 userLogout 方法

**原代码**：
```java
public boolean userLogout(HttpServletRequest request) {
    if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
    }
    request.getSession().removeAttribute(USER_LOGIN_STATE);
    return true;
}
```

**新代码**：
```java
public boolean userLogout(HttpServletRequest request) {
    if (!StpUtil.isLogin()) {
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
    }
    StpUtil.logout();
    return true;
}
```

---

## 五、网关鉴权配置

### 5.1 创建 Sa-Token 配置类

**文件**: `jc-smarteroj-backend-gateway/src/main/java/fun/javierchen/jcsmarterojbackendgateway/config/SaTokenConfig.java`

```java
package fun.javierchen.jcsmarterojbackendgateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token 网关鉴权配置
 */
@Configuration
public class SaTokenConfig {

    /**
     * 注册 Sa-Token 全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截所有路由
                .addInclude("/**")
                // 开放路由（不需要登录）
                .addExclude(
                        "/api/user/user/login",       // 登录接口
                        "/api/user/user/register",    // 注册接口
                        "/api/user/user/logout",      // 登出接口
                        "/doc.html",                   // 接口文档
                        "/webjars/**",                 // 静态资源
                        "/v3/api-docs/**",            // OpenAPI
                        "/swagger-resources/**",       // Swagger
                        "/favicon.ico"                 // 图标
                )
                // 鉴权规则
                .setAuth(obj -> {
                    // 打印请求路径
                    System.out.println(">>> 请求路径: " + SaRouter.getRequest().getRequestPath());
                    
                    // 1. 拦截内网接口（仅允许服务间调用）
                    SaRouter.match("/**/inner/**", r -> {
                        throw new RuntimeException("内网接口禁止外部访问");
                    });
                    
                    // 2. 其他接口需要登录
                    SaRouter.match("/**", r -> StpUtil.checkLogin());
                })
                // 异常处理
                .setError(e -> {
                    return SaResult.error(e.getMessage());
                });
    }
}
```

### 5.2 修改 GlobalAuthFilter（可选保留）

原有的 `GlobalAuthFilter` 可以保留或移除，因为 Sa-Token 配置类已经处理了内网接口的拦截。

**如需保留**，修改为：

```java
package fun.javierchen.jcsmarterojbackendgateway.filter;

import cn.dev33.satoken.same.SaSameUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器：为请求添加 Same-Token（服务间调用凭证）
 */
@Component
public class ForwardAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest newRequest = exchange.getRequest()
                .mutate()
                // 为请求追加 Same-Token 参数
                .header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken())
                .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
```

---

## 六、Feign 调用透传 Token

### 6.1 创建 Feign 拦截器

**文件**: `jc-smarteroj-backend-common/src/main/java/fun/javierchen/jcojbackendcommon/config/FeignInterceptor.java`

```java
package fun.javierchen.jcojbackendcommon.config;

import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * Feign 拦截器：为 RPC 调用添加认证信息
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 1. 添加 Same-Token（服务间调用凭证）
        requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
        
        // 2. 添加用户 Token（保持会话状态）
        if (StpUtil.isLogin()) {
            requestTemplate.header(StpUtil.getTokenName(), StpUtil.getTokenValue());
        }
    }
}
```

### 6.2 修改 UserFeignClient

**文件**: `jc-smarter-oj-server-client/src/main/java/fun/javierchen/jcojbackendserverclient/UserFeignClient.java`

**原代码**（使用 HttpSession）：
```java
default User getLoginUser(HttpServletRequest request) {
    Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
    User currentUser = (User) userObj;
    if (currentUser == null || currentUser.getId() == null) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    return currentUser;
}
```

**新代码**（使用 Sa-Token）：
```java
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.session.SaSession;
import static fun.javierchen.jcojbackendcommon.constant.UserConstant.SA_SESSION_USER_KEY;

default User getLoginUser(HttpServletRequest request) {
    // 1. 判断是否已登录
    if (!StpUtil.isLogin()) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    
    // 2. 从 Sa-Token Session 获取用户信息
    SaSession session = StpUtil.getSession();
    User currentUser = session.getModel(SA_SESSION_USER_KEY, User.class);
    
    if (currentUser == null || currentUser.getId() == null) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    
    return currentUser;
}
```

---

## 七、移除旧依赖（可选）

迁移完成并测试通过后，可以移除旧的 Spring Session 依赖：

**文件**: `pom.xml` (根目录)

```xml
<!-- 可以移除以下依赖 -->
<!--
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
-->
```

**配置文件中移除**：
```yaml
# 可以移除以下配置
# spring:
#   session:
#     store-type: redis
#     timeout: 259200000
```

---

## 八、前端适配

### 8.1 Token 传递方式

Sa-Token 默认支持多种方式传递 Token：

1. **请求头 (推荐)**: `satoken: {token值}`
2. **Cookie**: 自动管理
3. **请求参数**: `?satoken={token值}`

### 8.2 前端登录响应处理

登录成功后，后端会返回 Token 信息：

```javascript
// 登录请求
const response = await fetch('/api/user/user/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ userAccount, userPassword })
});

// 可以通过响应头或 Cookie 获取 Token
// 方式1: 从响应头获取（需后端配置）
const token = response.headers.get('satoken');

// 方式2: 自动从 Cookie 获取（浏览器自动管理）
```

### 8.3 后续请求携带 Token

```javascript
// 方式1: 在请求头中携带
fetch('/api/xxx', {
    headers: {
        'satoken': localStorage.getItem('satoken')
    }
});

// 方式2: 使用 Cookie（浏览器自动携带）
fetch('/api/xxx', {
    credentials: 'include'
});
```

---

## 九、迁移步骤清单

### Phase 1: 准备工作
- [ ] 备份当前代码
- [ ] 确认 Redis 服务正常运行

### Phase 2: 添加依赖
- [ ] 修改父 POM，添加 Sa-Token 版本管理
- [ ] 修改 `jc-smarteroj-backend-common/pom.xml`
- [ ] 修改 `jc-smarteroj-backend-gateway/pom.xml`

### Phase 3: 配置文件
- [ ] 添加所有服务的 `sa-token` 配置
- [ ] 确保所有服务的 Redis 配置一致

### Phase 4: 代码修改（用户服务）
- [ ] 添加 `SA_SESSION_USER_KEY` 常量
- [ ] 修改 `userLogin` 方法
- [ ] 修改 `getLoginUser` 方法
- [ ] 修改 `getLoginUserPermitNull` 方法
- [ ] 修改 `isAdmin` 方法
- [ ] 修改 `userLogout` 方法

### Phase 5: 网关配置
- [ ] 创建 `SaTokenConfig` 配置类
- [ ] 创建/修改 `ForwardAuthFilter`

### Phase 6: Feign 透传
- [ ] 创建 `FeignInterceptor`
- [ ] 修改 `UserFeignClient`

### Phase 7: 测试验证
- [ ] 测试登录功能
- [ ] 测试登出功能
- [ ] 测试需要登录的接口
- [ ] 测试服务间调用
- [ ] 测试内网接口拦截

### Phase 8: 清理工作
- [ ] 移除旧的 Spring Session 代码
- [ ] 移除旧的依赖（可选）
- [ ] 更新接口文档

---

## 十、扩展功能（后续可选）

### 10.1 踢人下线

```java
// 根据用户 ID 踢人下线
StpUtil.kickout(userId);

// 根据 Token 踢人下线
StpUtil.kickoutByTokenValue(token);
```

### 10.2 账号封禁

```java
// 封禁账号（1天）
StpUtil.disable(userId, 86400);

// 解除封禁
StpUtil.untieDisable(userId);
```

### 10.3 权限注解

```java
// 登录校验
@SaCheckLogin
@GetMapping("/info")
public UserVO getUserInfo() { ... }

// 角色校验
@SaCheckRole("admin")
@PostMapping("/delete")
public void deleteUser() { ... }

// 权限校验
@SaCheckPermission("user:update")
@PostMapping("/update")
public void updateUser() { ... }
```

### 10.4 WebSocket 认证

```java
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 验证 Token
        Object loginId = StpUtil.getLoginIdByToken(token);
        if (loginId == null) {
            session.close();
            return;
        }
        // Token 有效，保存会话
    }
}
```

---

## 十一、常见问题

### Q1: Sa-Token 和 Spring Session 可以并存吗？
**A**: 可以并存，但建议迁移完成后移除 Spring Session 依赖，避免冲突。

### Q2: 迁移后原有的 Session 数据会丢失吗？
**A**: 是的，Redis 中的 Spring Session 数据与 Sa-Token 数据格式不同，用户需要重新登录。

### Q3: 如何处理 Token 过期？
**A**: Sa-Token 提供了自动续期机制，配置 `active-timeout` 即可。也可以实现双 Token (Access Token + Refresh Token) 方案。

### Q4: 前端需要大改吗？
**A**: 改动较小，主要是将 Token 存储到 localStorage 并在请求头中携带。如果使用 Cookie 方式，前端几乎不需要改动。

---

## 十二、参考资料

- [Sa-Token 官方文档](https://sa-token.cc/)
- [Sa-Token 微服务整合](https://sa-token.cc/doc.html#/micro/same-token)
- [Sa-Token GitHub](https://github.com/dromara/sa-token)
