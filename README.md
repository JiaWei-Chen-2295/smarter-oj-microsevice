# Smarter-OJ (Smart Online Judge)

[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Alibaba-brightgreen.svg)](https://spring.io/projects/spring-cloud)
[![Sa-Token](https://img.shields.io/badge/Security-Sa--Token-blue.svg)](https://sa-token.cc/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Smarter-OJ 是一款基于微服务架构的分布式在线算法评测系统（Online Judge）。项目采用业界主流的微服务治理方案，旨在提供高可用、可扩展的算法训练与评测平台。

---

## 核心特性

- **微服务治理架构**：基于 Spring Cloud Alibaba 生态（Nacos, Sentinel, Gateway），实现服务的高可靠、弹性伸缩。
- **分布式流量防护**：集成 Sentinel 实现精细化的限流、熔断与降级策略，确保核心评测业务在极端负载下的稳定性。
- **分布式安全认证**：整合 Sa-Token 权限中心，配合 Redis 实现高性能的分布式 Session 管理，支持全服务状态共享与鉴权。
- **高性能判题引擎**：集成 Judge0 核心，支持多语言并发评测，判题逻辑异步化处理，显著降低系统响应时延。
- **协同办公增强**：内置协作沙盒与实时讨论模块，支持代码协同与知识分享。
- **灵活的接入能力**：支持多种认证模式（手机号、账号）及灵活的题库管理协议。
- **多种判题引擎支持**：系统架构支持多种判题沙箱（手写 Docker 代码沙箱、Judge0 等）的灵活切换。

---

## 技术演进：从自己手写到使用成熟的轮子

本项目判题核心经历了从 **手写 Docker 代码沙箱** 到 **集成专业 Judge0 引擎** 的演进过程，沉淀了深度的沙箱安全与性能优化经验：

1. **手写阶段**：基于 Java Process API 和 Docker Java API 手写代码沙箱，实现了 Java/C++ 代码的代码级别安全保证和容器化安全隔离、资源限制（内存、时间），使用了 Java 的安全管理器特性。
2. **专业化阶段**：为支持更多编程语言、更极致的并发能力，引入了主流的开源判题引擎 Judge0，并在系统层对 Judge0 进行微服务化封装，实现判题逻辑与业务逻辑的深度解耦。

---

## 技术架构

### 后端核心栈

- **基础框架**：Spring Boot 2.6.x + Spring Cloud Alibaba 2021.x
- **服务发现与配置**：Nacos
- **服务网关**：Spring Cloud Gateway
- **流量治理**：Sentinel
- **安全认证**：Sa-Token + Redis (Distributed Session)
- **持久化层**：MySQL 8.0 + MyBatis-Plus
- **缓存层**：Redis 6.x
- **通信组件**：OpenFeign + OKHttp3
- **开发工具**：Lombok, Hutool, Knife4j

### 部署与基础设施

- **沙箱环境**：Judge0 (Docker 容器化部署)
- **负载均衡**：Spring Cloud LoadBalancer
- **容器化方案**：Docker + Docker Compose

---

## 项目结构说明

```text
smarter-oj-microsevice
├── jc-smarteroj-backend-common       -- 公共组件、通用配置及异常处理
├── jc-smarteroj-backend-gateway      -- 统一接入层、鉴权过滤及流量转发
├── jc-smarteroj-backend-model        -- 核心领域模型（Entities, DTOs, VOs）
├── jc-smarteroj-backend-user-service  -- 用户中台（注册登录、鉴权、权限分配）
├── jc-smarteroj-backend-question-service -- 题库中台（题目维护、测试数据管理）
├── jc-smarteroj-backend-judge-service -- 评测中心（任务调度、判题策略、沙箱对接）
├── jc-smarteroj-backend-post-service  -- 社区中台（讨论帖、评论体系）
├── jc-smarteroj-backend-room-service  -- 协作中心（对战房间、即时通信逻辑）
└── jc-smarter-oj-server-client       -- 跨服务调用 Feign Client 定义
```

---

## 快速上手

### 部署流程

1. **基础组件启动**：确保 MySQL, Redis, Nacos 及 Judge0 处于可用状态。
2. **环境配置**：根据本地环境修改各模块 `application.yml` 或 Nacos 中的连接参数。
3. **服务启动顺序**：
   - 首先启动 `Gateway` 网关服务。
   - 依次启动 `User`, `Question`, `Judge` 等业务微服务。
4. **接口文档查看**：通过 `http://localhost:8201/api/doc.html` 访问 Knife4j 聚合接口文档进行调试。

---

## 项目路线图 (Development Roadmap)

- [X]  微服务治理架构搭建与注册中心集成
- [X]  分布式安全框架 Sa-Token 高可靠整合
- [X]  基于 Judge0 的高性能判题中心实现
- [X]  动态题库与管理系统闭环
- [ ]  系统监控接入 (Prometheus + Grafana)
- [ ]  自动化评测语言模板注入演进
- [ ]  实时编程协作引擎性能调优

---

## 参与贡献

如果您在使用过程中发现 Bug 或有功能建议，欢迎提交 Pull Request 或创建 Issue。

**作者**：JavierChen
