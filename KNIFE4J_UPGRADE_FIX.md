# Knife4j 4.x 统一升级与故障修复文档

本文档记录了将本项目微服务统一升级至 Knife4j 4.x 并配置 OpenAPI3 的全过程，以及针对“文档请求异常”问题的修复细节。

## 1. 升级目标
- 将所有微服务（User, Question, Judge）统一升级至 Knife4j 4.4.0 版本。
- 将文档规范从 Swagger2 (OpenAPI2) 迁移至 OpenAPI3。
- 实现网关（Gateway）自动发现并聚合所有微服务的 OpenAPI3 文档。

## 2. 核心问题与解决方案

### 2.1 网关 NPE 异常修复 (关键)
**现象**：访问 `doc.html` 时报错“Knife4j文档请求异常”，网关后台抛出 `java.lang.NullPointerException: The Mono returned by the supplier is null`。

**原因**：
网关全局过滤器 `GlobalAuthFilter.java` 在权限校验逻辑中，当路径不涉及敏感操作（如 `/**/inner/**`）时，错误地返回了 `null`。在 Spring Cloud Gateway (基于 WebFlux) 的响应式编程模型中，过滤器必须返回一个 `Mono<Void>`，返回 `null` 会直接导致 Reactor 链中断并抛出 NPE。

**修复方案**：
```java
// 修改前
return null; 

// 修改后
return chain.filter(exchange); // 正确继续过滤器链
```

### 2.2 依赖冲突解决
**现象**：网关运行环境不纯净，可能导致文档路由失效。

**原因**：
网关模块继承了父项目的 `spring-boot-starter-web` (基于 Servlet)，而 Spring Cloud Gateway 必须运行在 WebFlux 环境下。

**修复方案**：
在网关的 `pom.xml` 中将 `spring-boot-starter-web` 的作用域设为 `provided`，强制其不参与打包和运行时加载。

### 2.3 配置文件修正
**现象**：聚合后的文档中无法看到具体接口，或扫描不到 Controller。

**原因**：
1. 各微服务的 `api-rule-resources` 扫描路径写错（部分子服务写成了其他服务的包名）。
2. 网关聚合版本未指定为 `openapi3`。

**修复方案**：
- 修正各子服务 `application.yml` 中的包扫描路径。
- 更新网关配置启用服务发现模式并指定规范：
```yaml
knife4j:
  gateway:
    enabled: true
    strategy: discover
    discover:
      enabled: true
      version: openapi3
```

## 3. 最终变更清单

| 模块 | 变更详情 |
| :--- | :--- |
| **All Services** | 升级依赖为 `knife4j-openapi3-spring-boot-starter:4.4.0` |
| **User Service** | 修正 `application.yml` 扫描路径为 `fun.javierchen.jcsmarterojbackenduserservice.controller` |
| **Question Service** | 修正 `application.yml` 扫描路径为 `fun.javierchen.jcojbackendquestionservice.controller` |
| **Judge Service** | 修正 `application.yml` 扫描路径为 `fun.javierchen.jcsmarterojbackendjudgeservice.controller` |
| **Gateway** | 修复 `GlobalAuthFilter.java` 中的 `null` 返回 bug |
| **Gateway** | 在 `pom.xml` 中排除 `spring-boot-starter-web` 干扰 |

## 4. 验证步骤
1. 编译项目：`mvn clean install`
2. 启动 Nacos 和所有微服务。
3. 访问网关文档地址：`http://localhost:8101/doc.html`。
4. 在左侧服务切换下拉框中，依次选择不同服务，确认接口列表加载正常且显示为 OpenAPI 3.0 规范。
