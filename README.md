# Smarter-OJ (Smart Online Judge)

[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Alibaba-brightgreen.svg)](https://spring.io/projects/spring-cloud)
[![Sa-Token](https://img.shields.io/badge/Security-Sa--Token-blue.svg)](https://sa-token.cc/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Smarter-OJ 是一个基于 **Spring Cloud Alibaba** 的 OJ（Online Judge）后端微服务项目，覆盖用户、题库、判题、社区与协作房间等核心服务，并提供 Docker Compose 部署与压测材料，方便本地一键跑通与验证关键链路。

---

## 项目概览

这个仓库更偏向「可落地的微服务工程实践」：你可以从 0 启动一整套 OJ 后端，并沿着真实链路看到网关、鉴权、服务治理、缓存与压测是如何串起来的。

你可以在这里获得：

- 一套可运行的 OJ 后端原型（用户 / 题库 / 判题 / 社区 / 房间协作）
- Spring Cloud Alibaba 典型组件的组合落地（Nacos / Gateway / Sentinel）
- 以“可复现实验”为目标的性能优化材料（多级缓存命中验证、网关 vs 直连对比压测）

## 项目亮点

- **多级缓存（L1 Caffeine + L2 Redis）**：题库读链路引入预热、击穿/雪崩/穿透保护与失效机制，并提供缓存监控接口。
- **网关统一治理**：Spring Cloud Gateway 聚合路由 + Sa-Token 统一鉴权，边界清晰、入口可控。
- **流量防护体系**：Sentinel 覆盖网关与业务端，支持限流/熔断/降级，规则可通过 Nacos 持久化。
- **判题服务解耦**：评测中心与业务逻辑解耦，可对接 Judge0 等沙箱引擎。
- **可观测性**：Prometheus + Grafana 监控体系与业务指标（详见 `docs/observability.md`）。
- **可验证的性能数据**：压测脚本与报告归档，支持缓存命中验证与网关/直连对比（见 `docs/stress_test/`）。

## 目录

- [项目概览](#项目概览)
- [项目亮点](#项目亮点)
- [技术栈](#技术栈)
- [架构与模块](#架构与模块)
- [快速开始](#快速开始)
- [文档与压测](#文档与压测)
- [路线图](#路线图)
- [贡献](#贡献)

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

### 核心调用链（简述）

- **读题**：Gateway → Question Service →（Caffeine / Redis）→ MySQL → 返回 QuestionVO（含必要的用户信息）
- **提交判题**：Gateway → Judge Service → Code Sandbox（本地示例 / Remote / Judge0 等）→ 聚合结果并落库

## 快速开始

### 前置要求

- JDK 8+（或项目要求的 Java 版本）
- Docker + Docker Compose

### 1) 准备环境变量

项目的 Docker Compose 默认读取 `.env.prod`：

```bash
cp env.prod.example .env.prod
```

Windows PowerShell 可用：`Copy-Item env.prod.example .env.prod`

如果你需要短信/验证码等第三方能力，请在 `.env.prod` 中填入自己的配置；生产环境请务必自行管理密钥，不要将真实密钥提交到仓库。

### 2) 启动基础依赖（MySQL/Redis/Nacos/Sentinel）

```bash
docker compose up -d
```

如你的环境只有 `docker-compose`，请把上面命令中的 `docker compose` 替换为 `docker-compose`。

默认端口（以 `docker-compose.yml` 为准）：

- MySQL: `3306`
- Redis: `6379`
- Nacos: `8848`
- Sentinel Dashboard: `8858`

### 3) 启动微服务（本地开发）

建议启动顺序：

1. `jc-smarteroj-backend-gateway`
2. `jc-smarteroj-backend-user-service`
3. `jc-smarteroj-backend-question-service`
4. `jc-smarteroj-backend-judge-service`（以及对应沙箱）

接口文档（聚合入口）：`http://localhost:8101/doc.html`

### 4) 可选：用 Docker 一键拉起全部微服务

如果你希望把业务服务也容器化运行：

```bash
# 先构建各服务 jar（Windows 可用 `build-jars.ps1`）
./build-jars.sh

# 再启动业务服务与网关
docker compose -f docker-compose-services.yml up -d
```

常用访问地址：

- 网关：`http://localhost:8101`
- Knife4j 聚合文档：`http://localhost:8101/doc.html`
- Nacos：`http://localhost:8848/nacos`（默认账号密码 `nacos/nacos`）
- Sentinel Dashboard：`http://localhost:8858`

## 文档与压测

- Docker Compose 一键部署：`docs/DOCKER_DEPLOYMENT.md`
- 可观测性与监控：`docs/observability.md`
- 压测脚本与报告：`docs/stress_test/README.md`
- 压测结果归档：`docs/stress_test/results/`

其他设计文档索引：

- 题库多级缓存方案：`docs/题目列表多级缓存方案.md`
- Sentinel 限流与黑名单规划：`docs/Sentinel限流与黑名单规划方案.md`
- Sa-Token 迁移方案与报告：`docs/Sa-Token迁移方案.md`、`docs/Sa-Token迁移报告.md`
- 协作功能设计：`docs/协作功能设计文档.md`

缓存相关的可验证数据（示例）：

- 缓存命中率与统计：`jc-smarteroj-backend-question-service/src/main/java/fun/javierchen/jcojbackendquestionservice/controller/CacheStatsController.java`
- 缓存命中后的网关/直连对比报告：`docs/stress_test/results/cache_test_20260209_compare.md`

## 路线图

- [X] 微服务治理（Nacos/Gateway/Sentinel）
- [X] 分布式认证（Sa-Token + Redis）
- [X] 题库服务多级缓存与压测归档
- [X] 判题中心与沙箱解耦
- [X] 监控体系（Prometheus + Grafana）

## 贡献

欢迎提交 Issue / Pull Request。

**作者**：JavierChen
