# Smarter-OJ (Smart Online Judge)

[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Alibaba-brightgreen.svg)](https://spring.io/projects/spring-cloud)
[![Sa-Token](https://img.shields.io/badge/Security-Sa--Token-blue.svg)](https://sa-token.cc/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Smarter-OJ 是一款基于微服务架构的分布式在线算法评测系统（Online Judge）。项目采用业界主流的微服务治理方案，旨在提供高可用、可扩展的算法训练与评测平台。

---

## 项目亮点

- **多级缓存（L1 Caffeine + L2 Redis）**：题库读链路引入预热、击穿/雪崩/穿透保护与失效机制，并提供缓存监控接口。
- **网关统一治理**：Spring Cloud Gateway 聚合路由 + Sa-Token 统一鉴权，边界清晰、入口可控。
- **流量防护体系**：Sentinel 覆盖网关与业务端，支持限流/熔断/降级，规则可通过 Nacos 持久化。
- **判题服务解耦**：评测中心与业务逻辑解耦，可对接 Judge0 等沙箱引擎。
- **可验证的性能数据**：压测脚本与报告归档，支持缓存命中验证与网关/直连对比（见 `docs/stress_test/`）。

## 目录

- 项目亮点
- 技术栈
- 架构与模块
- 快速开始
- 文档与压测
- 路线图
- 贡献

## 技术栈

- **基础框架**：Spring Boot 2.6.x + Spring Cloud Alibaba 2021.x
- **服务治理**：Nacos（注册/配置） + Spring Cloud Gateway（路由） + Sentinel（流控）
- **安全认证**：Sa-Token + Redis（分布式会话/鉴权）
- **数据与缓存**：MySQL 8.0 + MyBatis-Plus + Redis 7.x + Caffeine
- **服务通信**：OpenFeign + OKHttp3
- **文档工具**：Knife4j (OpenAPI3)

## 架构与模块

```text
smarter-oj-microsevice
├── jc-smarteroj-backend-common            -- 公共组件、通用配置及异常处理
├── jc-smarteroj-backend-gateway           -- 统一接入层、鉴权过滤及流量转发
├── jc-smarteroj-backend-model             -- 核心领域模型（Entities, DTOs, VOs）
├── jc-smarteroj-backend-user-service      -- 用户服务（注册登录、鉴权、权限）
├── jc-smarteroj-backend-question-service  -- 题库服务（题目维护、查询、缓存）
├── jc-smarteroj-backend-judge-service     -- 判题服务（任务调度、沙箱对接）
├── jc-smarteroj-backend-post-service      -- 社区服务（帖子、评论）
├── jc-smarteroj-backend-room-service      -- 协作服务（房间、对战/协作逻辑）
└── jc-smarter-oj-server-client            -- Feign Client 定义
```

## 快速开始

### 前置要求

- JDK 8+（或项目要求的 Java 版本）
- Docker + Docker Compose

### 启动基础依赖（MySQL/Redis/Nacos/Sentinel）

```bash
docker-compose up -d
```

默认端口（以 `docker-compose.yml` 为准）：

- MySQL: `3306`
- Redis: `6379`
- Nacos: `8848`
- Sentinel Dashboard: `8858`

### 启动微服务

建议启动顺序：

1. `jc-smarteroj-backend-gateway`
2. `jc-smarteroj-backend-user-service`
3. `jc-smarteroj-backend-question-service`
4. `jc-smarteroj-backend-judge-service`（以及对应沙箱）

接口文档（聚合/路由入口）：`http://localhost:8101/api/question/doc.html`

## 文档与压测

- 压测脚本与报告：`docs/stress_test/README.md`
- 压测结果归档：`docs/stress_test/results/`

缓存相关的可验证数据（示例）：

- 缓存命中率与统计：`jc-smarteroj-backend-question-service/src/main/java/fun/javierchen/jcojbackendquestionservice/controller/CacheStatsController.java`
- 缓存命中后的网关/直连对比报告：`docs/stress_test/results/cache_test_20260209_compare.md`

## 路线图

- [X] 微服务治理（Nacos/Gateway/Sentinel）
- [X] 分布式认证（Sa-Token + Redis）
- [X] 题库服务多级缓存与压测归档
- [X] 判题中心与沙箱解耦
- [ ] 监控体系（Prometheus + Grafana）

## 贡献

欢迎提交 Issue / Pull Request。

**作者**：JavierChen
