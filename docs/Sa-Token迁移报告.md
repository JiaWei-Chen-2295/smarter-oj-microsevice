# Sa-Token 登录认证迁移报告

> **项目**: smarter-oj-microsevice  
> **迁移时间**: 2026-01-23  
> **迁移内容**: Spring Session → Sa-Token  
> **Sa-Token 版本**: 1.39.0

---

## 一、迁移背景

### 1.1 为什么要迁移？

| 问题 | Spring Session 现状 | Sa-Token 解决方案 |
|------|---------------------|------------------|
| **微服务调用** | Feign 调用时 Session 传递困难 | Token 透传简单，内置 Same-Token 机制 |
| **WebSocket 支持** | Cookie 在长连接中传递不可靠 | 支持 URL 参数传递 Token |
| **权限控制** | 需手写每个接口的权限校验 | `@SaCheckRole` 注解式权限 |
| **踢人下线** | 需自己实现 | `StpUtil.kickout()` 开箱即用 |
| **账号封禁** | 需自己实现 | `StpUtil.disable()` 开箱即用 |
| **代码依赖** | 强依赖 HttpServletRequest | 不依赖 Request，任何地方可用 |

### 1.2 迁移目标

- ✅ 保持 API 向后兼容（前端无需改动）
- ✅ 使用 Sa-Token 替换 Spring Session
- ✅ 配置网关统一鉴权
- ✅ 实现服务间 Token 透传
- ✅ 为未来 WebSocket 集成做准备

---

## 二、变更清单

### 2.1 依赖变更

| 模块 | 变更类型 | 说明 |
|------|---------|------|
| `pom.xml` (根) | 新增版本管理 | `<sa-token.version>1.39.0</sa-token.version>` |
| `jc-smarteroj-backend-common` | 新增依赖 | Sa-Token 核心包 + Redis 整合 + Feign |
| `jc-smarteroj-backend-gateway` | 新增依赖 | Sa-Token Reactor 包（WebFlux 专用）|
| `jc-smarter-oj-server-client` | 新增依赖 | 引用 common 模块获取 Sa-Token |

### 2.2 配置文件变更

| 文件 | 变更内容 |
|------|---------|
| `gateway/application.yml` | 新增 Sa-Token 配置 + Redis 连接 |
| `gateway/application-local.yml` | 新增 Sa-Token 配置 + Redis 连接 |
| `user-service/application.yml` | 新增 Sa-Token 配置 |

**Sa-Token 统一配置**:
```yaml
sa-token:
  token-name: satoken          # Token 名称
  timeout: 2592000             # 有效期 30 天
  active-timeout: -1           # 不限制活跃时间
  is-concurrent: true          # 允许多端登录
  is-share: true               # 共用 Token
  token-style: uuid            # Token 格式
  is-log: true                 # 输出日志
```

### 2.3 代码变更

#### 2.3.1 核心认证逻辑 (`UserServiceImpl.java`)

| 方法 | 原实现 | 新实现 |
|------|-------|-------|
| `userLogin()` | `request.getSession().setAttribute()` | `StpUtil.login()` + `SaSession.set()` |
| `getLoginUser()` | `request.getSession().getAttribute()` | `StpUtil.getSession().getModel()` |
| `getLoginUserPermitNull()` | `request.getSession().getAttribute()` | `StpUtil.isLogin()` + `SaSession` |
| `isAdmin()` | `request.getSession().getAttribute()` | `StpUtil.getSession().getModel()` |
| `userLogout()` | `request.getSession().removeAttribute()` | `StpUtil.logout()` |

**关键变更示例**:
```java
// 登录 - 原代码
request.getSession().setAttribute(USER_LOGIN_STATE, user);

// 登录 - 新代码
StpUtil.login(user.getId());
SaSession session = StpUtil.getSession();
session.set(SA_SESSION_USER_KEY, user);
```

#### 2.3.2 Feign 客户端 (`UserFeignClient.java`)

| 变更 | 说明 |
|------|------|
| 新增 `getLoginUser()` 无参方法 | 不依赖 Request，直接使用 Sa-Token |
| 保留 `getLoginUser(request)` | 向后兼容，内部委托给无参版本 |
| 新增 `isAdmin()` 无参方法 | 同上 |
| 保留 `isAdmin(request)` | 向后兼容 |

**关键变更**:
```java
// 原代码 - 依赖 HttpServletRequest
default User getLoginUser(HttpServletRequest request) {
    Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
    // ...
}

// 新代码 - 不依赖 Request
default User getLoginUser() {
    if (!StpUtil.isLogin()) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    SaSession session = StpUtil.getSession();
    return session.getModel(SA_SESSION_USER_KEY, User.class);
}

// 兼容旧代码
default User getLoginUser(HttpServletRequest request) {
    return getLoginUser();  // 委托给无参版本
}
```

### 2.4 新增文件

| 文件 | 位置 | 功能 |
|------|------|------|
| `SaTokenConfig.java` | gateway/config/ | 网关统一鉴权配置 |
| `ForwardAuthFilter.java` | gateway/filter/ | Token 透传过滤器 |
| `FeignInterceptor.java` | common/config/ | Feign 调用 Token 透传 |

---

## 三、架构变化

### 3.1 认证流程对比

**原架构 (Spring Session)**:
```
前端 (Cookie) → Gateway → 微服务 → request.getSession() → Redis (spring:session:xxx)
```

**新架构 (Sa-Token)**:
```
前端 (Cookie/Header) → Gateway (统一鉴权) → 微服务 → StpUtil.getSession() → Redis (satoken:login:xxx)
```

### 3.2 鉴权层次

```
┌─────────────────────────────────────────────────────────────────────┐
│                          第一层：网关鉴权                            │
│  SaTokenConfig.java                                                 │
│  - 白名单：/login, /register, /doc.html 等                          │
│  - 黑名单：/**/inner/** 禁止外部访问                                 │
│  - 其他接口：要求登录                                                │
├─────────────────────────────────────────────────────────────────────┤
│                          第二层：服务鉴权                            │
│  GlobalAuthFilter.java (保留)                                       │
│  - 双重保护：拦截 /**/inner/** 内网接口                              │
├─────────────────────────────────────────────────────────────────────┤
│                          第三层：Token 透传                          │
│  ForwardAuthFilter.java (Gateway)                                   │
│  FeignInterceptor.java (Feign)                                      │
│  - Gateway → 微服务：透传用户 Token + Same-Token                     │
│  - 微服务 → 微服务：透传用户 Token + Same-Token                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 四、向后兼容性

### 4.1 前端兼容

| 项目 | 兼容性 |
|------|--------|
| Cookie 传递 | ✅ 完全兼容，Sa-Token 自动管理 Cookie |
| 登录接口 | ✅ 完全兼容，请求/响应格式不变 |
| 其他接口 | ✅ 完全兼容，无需修改 |

**结论：前端无需任何改动**

### 4.2 后端兼容

| 代码模式 | 兼容性 |
|---------|--------|
| `userFeignClient.getLoginUser(request)` | ✅ 继续工作 |
| `userFeignClient.isAdmin(request)` | ✅ 继续工作 |
| `userService.getLoginUser(request)` | ✅ 继续工作 |

**结论：现有代码无需修改，可渐进式迁移**

---

## 五、Redis 数据格式变化

### 5.1 Key 格式对比

| 框架 | Key 格式 |
|------|---------|
| Spring Session | `spring:session:sessions:{sessionId}` |
| Sa-Token | `satoken:login:token:{token}` |
| Sa-Token | `satoken:login:session:{userId}` |

### 5.2 迁移影响

⚠️ **重要**: 迁移后，所有用户需要**重新登录**。因为：
- 原有 Session 数据格式与 Sa-Token 不兼容
- Token 存储 Key 完全不同

---

## 六、扩展功能（已支持，可直接使用）

### 6.1 踢人下线

```java
// 根据 userId 踢下线
StpUtil.kickout(10001);

// 根据 Token 踢下线
StpUtil.kickoutByTokenValue("xxxx-xxxx-xxxx");
```

### 6.2 账号封禁

```java
// 封禁账号 1 天
StpUtil.disable(10001, 86400);

// 解除封禁
StpUtil.untieDisable(10001);
```

### 6.3 权限注解（需额外配置）

```java
@SaCheckLogin                        // 必须登录
@SaCheckRole("admin")                // 必须是管理员
@SaCheckPermission("user:delete")    // 必须有删除权限
```

### 6.4 WebSocket 认证

```java
@OnOpen
public void onOpen(Session session, @PathParam("token") String token) {
    Object loginId = StpUtil.getLoginIdByToken(token);
    if (loginId == null) {
        session.close();
        return;
    }
    // Token 有效
}
```

---

## 七、测试验证清单

### 7.1 功能测试

- [ ] 用户注册
- [ ] 用户登录
- [ ] 获取当前登录用户
- [ ] 用户登出
- [ ] 需要登录的接口（无 Token 时返回 401）
- [ ] 内网接口拦截（返回 403）
- [ ] 服务间 Feign 调用

### 7.2 集成测试

- [ ] Gateway 启动正常
- [ ] User Service 启动正常
- [ ] Question Service 启动正常
- [ ] Post Service 启动正常
- [ ] Judge Service 启动正常

---

## 八、风险与回滚

### 8.1 风险点

| 风险 | 影响 | 缓解措施 |
|------|------|---------|
| 用户需重新登录 | 低 | 提前通知用户 |
| Redis 连接配置错误 | 高 | 确保所有服务 Redis 配置一致 |
| Token 不同步 | 中 | 检查网关和服务的 Sa-Token 配置一致性 |

### 8.2 回滚方案

如需回滚，执行以下步骤：
1. 恢复 Git 提交到迁移前版本
2. 重新部署所有服务
3. 清理 Redis 中的 `satoken:*` 数据（可选）

---

## 九、后续优化建议

1. **移除 Spring Session 依赖**：迁移稳定后，可移除 `spring-session-data-redis` 依赖
2. **配置权限注解**：为管理员接口添加 `@SaCheckRole("admin")` 注解
3. **WebSocket 集成**：协作功能开发时，使用 URL 参数传递 Token
4. **监控告警**：配置 Sa-Token 登录/登出事件监听

---

## 十、变更文件汇总

```
smarter-oj-microsevice/
├── pom.xml                                    # +2 行：Sa-Token 版本
├── jc-smarteroj-backend-common/
│   ├── pom.xml                                # +20 行：Sa-Token 依赖
│   └── src/.../config/
│       └── FeignInterceptor.java              # 新增：Feign Token 透传
├── jc-smarteroj-backend-gateway/
│   ├── pom.xml                                # +26 行：Sa-Token Reactor
│   ├── src/.../resources/
│   │   ├── application.yml                    # +22 行：Sa-Token + Redis
│   │   └── application-local.yml              # +16 行：Sa-Token + Redis
│   └── src/.../config/
│       ├── SaTokenConfig.java                 # 新增：网关鉴权
│       └── ForwardAuthFilter.java             # 新增：Token 透传
├── jc-smarteroj-backend-user-service/
│   ├── src/.../resources/
│   │   └── application.yml                    # +17 行：Sa-Token 配置
│   └── src/.../service/impl/
│       └── UserServiceImpl.java               # 核心改造：Session → Sa-Token
├── jc-smarter-oj-server-client/
│   ├── pom.xml                                # +6 行：common 依赖
│   └── src/.../UserFeignClient.java           # 核心改造：支持无参 getLoginUser()
└── docs/
    └── Sa-Token迁移报告.md                     # 本报告
```

---

**迁移完成！** 🎉
