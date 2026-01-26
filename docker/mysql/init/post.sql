INSERT INTO smarter_oj_db.post (id, title, content, tags, thumbNum, favourNum, userId, createTime, updateTime, isDelete) VALUES (1993518106794217473, 'Hello World', '# 你好

##
', '["java"]', 0, 0, 1903672737810321410, '2025-11-26 11:12:09', '2025-11-26 11:33:41', 0);
INSERT INTO smarter_oj_db.post (id, title, content, tags, thumbNum, favourNum, userId, createTime, updateTime, isDelete) VALUES (1993518106794217474, 'Java 多线程编程最佳实践', '# Java 多线程编程最佳实践

在现代软件开发中，多线程编程是一个不可或缺的技能。本文将分享一些在实际项目中总结的最佳实践。

## 1. 线程池的使用

使用 `ThreadPoolExecutor` 而不是直接创建线程：

```java
ExecutorService executor = new ThreadPoolExecutor(
    5, 10, 60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(100),
    new ThreadFactoryBuilder().setNameFormat("worker-%d").build()
);
```

## 2. 避免死锁

- 保持锁的获取顺序一致
- 使用超时机制
- 尽量减少锁的持有时间

## 3. 线程安全的集合

优先使用 `ConcurrentHashMap`、`CopyOnWriteArrayList` 等并发集合类。

## 4. 原子操作

使用 `AtomicInteger`、`AtomicLong` 等原子类替代锁：

```java
private final AtomicInteger counter = new AtomicInteger(0);

public void increment() {
    counter.incrementAndGet();
}
```

## 5. 避免共享可变状态

尽可能使用不可变对象和线程封闭。

---

**总结：** 多线程编程的核心是理解并发的本质，合理使用工具类，避免过度设计。

', '["Java","多线程","并发编程"]', 0, 0, 1903672737810321410, '2025-12-01 13:00:00', '2025-12-01 13:00:00', 0);
INSERT INTO smarter_oj_db.post (id, title, content, tags, thumbNum, favourNum, userId, createTime, updateTime, isDelete) VALUES (1993518106794217475, '算法学习路线图', '# 算法学习路线图

很多初学者不知道如何系统地学习算法，这里分享一个完整的学习路径。

## 第一阶段：基础数据结构（1-2个月）

### 必须掌握：
- 数组、链表、栈、队列
- 哈希表
- 树和二叉树
- 图的基础概念

### 推荐资源：
- 《算法（第4版）》
- LeetCode 简单和中等难度题目

## 第二阶段：算法思想（2-3个月）

### 核心思想：
- 双指针
- 滑动窗口
- 二分查找
- 递归与回溯
- 动态规划
- 贪心算法

### 刷题策略：
每天 2-3 题，坚持最重要

## 第三阶段：高级算法（1-2个月）

- 并查集
- 字符串算法（KMP、Trie）
- 高级数据结构（线段树、树状数组）
- 图论算法（最短路径、最小生成树）

## 第四阶段：实战与优化

- 算法竞赛题目
- 系统设计中的算法应用
- 性能优化和调优

---

**建议：** 理论学习 + 大量练习 + 定期复盘

', '["算法","学习路线","数据结构"]', 0, 0, 1903672737810321410, '2025-12-01 13:05:00', '2025-12-01 13:05:00', 0);
INSERT INTO smarter_oj_db.post (id, title, content, tags, thumbNum, favourNum, userId, createTime, updateTime, isDelete) VALUES (1993518106794217476, 'Spring Boot 微服务架构实践', '# Spring Boot 微服务架构实践

随着业务规模的增长，单体应用逐渐暴露出问题，微服务架构成为必然选择。

## 为什么选择微服务？

1. **技术栈灵活**：不同服务可以使用不同技术
2. **独立部署**：服务之间互不影响
3. **弹性扩展**：按需扩展特定服务
4. **故障隔离**：单点故障不会影响全局

## 核心组件

### 1. 服务注册与发现
使用 Nacos 或 Eureka 实现服务注册

### 2. 配置中心
统一管理各环境配置

### 3. 熔断与降级
- Hystrix
- Sentinel
- Resilience4j

### 4. 网关
Spring Cloud Gateway 统一入口

## 实践建议

- 服务粒度要适中，避免过细
- 保持服务的独立性
- 重视监控和日志
- 做好数据一致性处理

## 常见问题

**Q: 如何处理分布式事务？**
A: 可以使用 Seata 或采用最终一致性方案

**Q: 服务间如何通信？**
A: RESTful API 或 gRPC

---

微服务不是银弹，要根据实际场景选择合适的架构。

', '["Spring Boot","微服务","架构"]', 0, 0, 1903672737810321410, '2025-12-01 13:10:00', '2025-12-01 13:10:00', 0);
INSERT INTO smarter_oj_db.post (id, title, content, tags, thumbNum, favourNum, userId, createTime, updateTime, isDelete) VALUES (1993518106794217477, 'MySQL 索引优化指南', '# MySQL 索引优化指南

索引是数据库性能优化的关键，但用不好反而会适得其反。

## 索引类型

### B-Tree 索引
最常用的索引类型，适合范围查询

### Hash 索引
仅支持精确匹配，不支持范围查询

### 覆盖索引
查询字段都在索引中，避免回表

## 索引优化原则

### 1. 最左前缀原则
```sql
-- 联合索引 (a, b, c)
WHERE a = ? AND b = ?        -- ✓ 使用索引
WHERE a = ? AND b = ? AND c = ?  -- ✓ 使用索引
WHERE b = ? AND c = ?        -- ✗ 无法使用索引
```

### 2. 避免索引失效
- 不要在索引列上使用函数
- 避免 LIKE 以 % 开头
- OR 条件可能导致索引失效

### 3. 索引覆盖
尽量使用覆盖索引，减少回表操作

## 实战案例

**问题：** 慢查询
```sql
SELECT * FROM user WHERE name LIKE \'%张%\';
```

**优化：**
```sql
-- 使用全文索引或倒排索引
-- 或者改为前缀匹配
SELECT * FROM user WHERE name LIKE \'张%\';
```

## 监控与维护

- 定期分析慢查询日志
- 使用 `EXPLAIN` 分析执行计划
- 注意索引碎片问题

', '["MySQL","数据库","索引优化"]', 0, 0, 1903672737810321410, '2025-12-01 13:15:00', '2025-12-01 13:15:00', 0);
INSERT INTO smarter_oj_db.post (id, title, content, tags, thumbNum, favourNum, userId, createTime, updateTime, isDelete) VALUES (1993518106794217478, 'Redis 缓存设计最佳实践', '# Redis 缓存设计最佳实践

Redis 作为高性能缓存，在现代架构中扮演着重要角色。如何设计合理的缓存策略是关键。

## 缓存穿透

**问题：** 大量请求查询不存在的数据，直接打到数据库

**解决方案：**
1. 布隆过滤器
2. 缓存空对象

```java
// 缓存空对象示例
String value = redis.get(key);
if (value == null) {
    value = database.query(key);
    if (value == null) {
        value = "NULL";  // 缓存空值
    }
    redis.setex(key, 3600, value);
}
```

## 缓存雪崩

**问题：** 大量缓存同时失效，导致数据库压力暴增

**解决方案：**
1. 设置随机过期时间
2. 热点数据永不过期，后台异步更新

## 缓存击穿

**问题：** 热点 key 失效瞬间，大量请求涌入数据库

**解决方案：**
1. 互斥锁
2. 逻辑过期

## 数据一致性

### 强一致性方案
- 更新数据库后删除缓存
- 使用分布式事务

### 最终一致性方案
- Canal 监听 Binlog
- 延迟双删

## 内存优化

1. **合理选择数据结构**
   - String：简单 KV
   - Hash：对象存储
   - ZSet：排行榜

2. **压缩存储**
   - 使用整数而非字符串
   - 批量写入减少网络开销

3. **淘汰策略**
   - LRU / LFU 根据业务选择

', '["Redis","缓存","高并发"]', 0, 0, 1903672737810321410, '2025-12-01 13:20:00', '2025-12-01 13:20:00', 0);
INSERT INTO smarter_oj_db.post (id, title, content, tags, thumbNum, favourNum, userId, createTime, updateTime, isDelete) VALUES (1993518106794217479, '设计模式实战：单例模式', '# 设计模式实战：单例模式

单例模式是最常用的设计模式之一，但实现方式有多种，各有优劣。

## 饿汉式

```java
public class Singleton {
    private static final Singleton instance = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() {
        return instance;
    }
}
```

**优点：** 线程安全，简单
**缺点：** 类加载时就初始化，可能浪费内存

## 懒汉式（线程不安全）

```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

**问题：** 多线程下不安全

## 双重检查锁（DCL）

```java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

**注意：** 必须使用 `volatile` 防止指令重排序

## 静态内部类

```java
public class Singleton {
    private Singleton() {}

    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

**优点：** 线程安全，懒加载，推荐使用

## 枚举（最佳）

```java
public enum Singleton {
    INSTANCE;

    public void doSomething() {
        // ...
    }
}
```

**优点：** 天然线程安全，防止反射攻击，防止反序列化

## 实际应用场景

- 数据库连接池
- 配置管理器
- 日志记录器
- 缓存管理器

', '["设计模式","单例模式","Java"]', 0, 0, 1903672737810321410, '2025-12-01 13:25:00', '2025-12-01 13:25:00', 0);
INSERT INTO smarter_oj_db.post (id, title, content, tags, thumbNum, favourNum, userId, createTime, updateTime, isDelete) VALUES (1993525293285330946, '🔧 技术深潜｜为什么我们选择 GraphQL？—— 从“请求焦虑”到“精准获取”的架构进化', '**你是否也经历过这样的场景？**
前端想加一个字段展示用户头像，后端却要改三层接口；
一个页面要等 5 个 API 串行请求才能渲染；
或者——明明只需要 `name` 和 `avatar`，却被迫接收一整个 `200KB` 的用户对象？

**这不只是“接口设计问题”，本质是：****传统 REST 架构在复杂业务演进中，难以平衡灵活性与稳定性****。**

**我们最近在新项目中引入了 ****GraphQL****，它不是“另一个 API 协议”，而是一种****面向客户端需求的数据查询语言 + 运行时****。它的核心哲学很简单：**

> **让客户端决定它需要什么，而不是由服务端预设“套餐”。**

### 🧩 它如何工作？—— 从建模开始

1. **强类型 Schema 定义**
   用声明式语法定义数据图谱（例如 `User { id, name, posts { title } }`），天然支持文档生成与类型校验（配合 TypeScript 效果极佳 ✅）。
2. **单入口 + 精准查询**
   一个 `/graphql` endpoint，客户端按需“拼装”查询语句，避免过度获取（over-fetching）与多次往返（N+1 问题可通过 DataLoader 优雅解决）。
3. **变更（Mutation）与订阅（Subscription）统一范式**
   写操作也纳入类型系统，实现“查询 + 修改”的对称设计，提升心智一致性。

### 🏗️ 我们看重的，不只是“方便”

* **✅ ****解耦前后端迭代节奏****：前端可自主组合字段，后端专注能力沉淀；**
* **✅ ****提升性能感知****：减少带宽、降低请求数，首屏加载明显提速；**
* **✅ ****增强可观测性****：每条 query 可追踪、可缓存、可分析热点；**
* **✅ ****为未来留出扩展性****：微服务聚合、BFF 层构建、甚至跨平台（App/Web/小程序）复用同一套数据协议。**

> **🌱 小提醒：它不是银弹。**
> 复杂查询可能带来性能风险（需限深/限宽）、缓存策略更精细、调试初期略陡峭……但正如你所说：“**好的架构不是消灭复杂度，而是把它放在最合适的位置****。”**

**我们用 ****NestJS + GraphQL + Prisma** 搭建了轻量 BFF 层，配合 Apollo Studio 做 schema 治理，目前已平稳支撑核心业务模块。后续计划接入 persisted queries 与 query cost analysis，进一步加固稳定性。
', '[]', 0, 0, 1903672737810321410, '2025-11-26 11:40:42', '2025-11-26 11:40:42', 0);
INSERT INTO smarter_oj_db.post (id, title, content, tags, thumbNum, favourNum, userId, createTime, updateTime, isDelete) VALUES (1993548896617865217, '分层状态机与事件驱动架构：构建高内聚、低耦合的业务规则引擎', '# **分层状态机与事件驱动架构：构建高内聚、低耦合的业务规则引擎**

> *——从模糊需求到可演化的系统建模实践*

## 一、引言：业务复杂性是软件衰败的主因

**在后端系统开发中，我们常面临一类“看似简单、实则暗流涌动”的需求：****业务规则密集、状态转换繁复、外部条件耦合强****。例如：**

* **支付订单的生命周期（待支付 → 已支付 → 已发货 → 退款中 → 已完成/已关闭）；**
* **用户成长体系（普通用户 → 实名用户 → 会员 → VIP → 黑名单）；**
* **自动化运维中的故障自愈流程（检测 → 隔离 → 诊断 → 尝试恢复 → 升级告警）。**

**这些场景的共性在于：****状态多、转移条件复杂、副作用密集、且规则持续演进****。若以传统 **`if-else` 堆叠或硬编码状态流转实现，系统很快陷入“面条式逻辑”——测试困难、修改危险、扩展僵硬。


---

## 二、问题建模：为什么状态机天然适合业务规则？

**状态机（Finite State Machine, FSM）由五元组定义：**
  M=(S,Σ,δ,s0,F)**M**=**(**S**,**Σ**,**δ**,**s**0****,**F**)**
其中：

* S**S**：有限状态集合；
* Σ**Σ**：事件（输入）集合；
* δ:S×Σ→S**δ**:**S**×**Σ**→**S**：状态转移函数；
* s0∈S**s**0****∈**S**：初始状态；
* F⊆S**F**⊆**S**：终态集合（可选）。


**→ 这正是****分层状态机（HSM）** 的用武之地。

### HSM 的核心思想：状态嵌套 + 行为继承

**HSM 引入“复合状态”（Composite State）与“子状态”（Substate）概念，并支持：**

* **进入/退出动作（entry/exit action）****：在进入某状态时自动执行初始化逻辑（如开启事务、订阅事件），退出

---

##
', '["java"]', 0, 0, 1903672737810321410, '2025-11-26 13:14:29', '2025-11-26 13:14:29', 0);
