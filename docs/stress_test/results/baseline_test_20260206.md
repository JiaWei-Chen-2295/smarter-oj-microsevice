# Question Service 基准压力测试报告

> **测试日期**: 2026-02-06  
> **测试工具**: wrk  
> **测试环境**: 本地开发环境 (WSL2 -> Windows)  
> **服务状态**: Sentinel 限流已关闭

---

## 📊 测试结果汇总

| 测试场景 | 接口 | 测试方式 | QPS | 平均延迟 | 最大延迟 | 备注 |
|---------|-----|---------|-----|---------|---------|-----|
| Test 1 | GET /get/vo | 直连服务 (8202) | **1,317** | 39.73ms | 415.58ms | 无网关开销 |
| Test 2 | GET /get/vo | 通过网关 (无Token) | 15,342 | 3.71ms | 153.62ms | ⚠️ 快速返回错误 |
| Test 3 | POST /list/page/vo | 直连服务 (8202) | **560** | 101.64ms | 1.01s | 批量 Feign 调用 |
| Test 4 | POST /list/page/vo | 通过网关 (无Token) | 15,823 | 3.37ms | 122.79ms | ⚠️ 快速返回错误 |
| **Test 5** | GET /get/vo | **网关+Token** | **682** | 80.31ms | 1.04s | ✅ 完整链路 |
| **Test 6** | POST /list/page/vo | **网关+Token** | **401** | 127.97ms | 1.15s | ✅ 完整链路 |

### 🎯 有效基准数据（完整链路）

| 接口 | QPS | 平均延迟 | 测试方式 |
|-----|-----|---------|---------|
| GET /get/vo | **682** | 80ms | 网关+Token |
| POST /list/page/vo | **401** | 128ms | 网关+Token |

---

## 📈 详细测试结果

### Test 1: GET /api/question/get/vo (直连服务)

```
URL: http://localhost:8202/api/question/get/vo?id=1907
配置: 4 threads, 50 connections, 30s duration

Running 30s test @ http://localhost:8202/api/question/get/vo?id=1907
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    39.73ms   30.32ms 415.58ms   89.56%
    Req/Sec   332.01     96.19   540.00     74.75%
  39570 requests in 30.03s, 63.40MB read
Requests/sec:   1317.64
Transfer/sec:      2.11MB
```

**分析**:
- 平均延迟 ~40ms，符合预期（1次DB查询 + 1次Feign调用）
- QPS ~1300，受限于同步阻塞的 Feign 调用

---

### Test 2: GET /api/question/get/vo (通过网关)

```
URL: http://localhost:8101/api/question/get/vo?id=1907
配置: 4 threads, 50 connections, 30s duration

Running 30s test @ http://localhost:8101/api/question/get/vo?id=1907
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.71ms    5.16ms 153.62ms   94.88%
    Req/Sec     3.86k   753.44    12.17k    77.60%
  461796 requests in 30.10s, 88.16MB read
Requests/sec:  15342.56
Transfer/sec:      2.93MB
```

**分析**:
- ⚠️ QPS 异常高，可能是因为无 Token 直接返回错误
- 网关层 WebFlux 非阻塞处理错误响应速度极快

---

### Test 3: POST /api/question/list/page/vo (直连服务)

```
URL: http://localhost:8202/api/question/list/page/vo
配置: 4 threads, 50 connections, 30s duration
Body: {"current":1,"pageSize":10}

Running 30s test @ http://localhost:8202/api/question/list/page/vo
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   101.64ms  108.25ms   1.01s    95.37%
    Req/Sec   147.03     35.24   232.00     75.31%
  16840 requests in 30.04s, 311.19MB read
Requests/sec:    560.55
Transfer/sec:     10.36MB
```

**分析**:
- 平均延迟 ~100ms，符合预期（分页查询 + 批量Feign调用获取UserVO）
- QPS ~560，受限于：
  1. 分页查询性能
  2. 每个 QuestionVO 需要 Feign 调用获取 UserVO
- **瓶颈确认**: Feign 批量调用是主要瓶颈

---

### Test 4: POST /api/question/list/page/vo (通过网关)

```
URL: http://localhost:8101/api/question/list/page/vo
配置: 4 threads, 50 connections, 30s duration
Body: {"current":1,"pageSize":10}

Running 30s test @ http://localhost:8101/api/question/list/page/vo
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.37ms    2.96ms 122.79ms   89.80%
    Req/Sec     3.98k   579.62     7.39k    67.83%
  476244 requests in 30.10s, 90.92MB read
Requests/sec:  15823.19
Transfer/sec:      3.02MB
```

**分析**:
- ⚠️ QPS 异常高，原因是无 Token 直接返回 40100 错误（未登录）
- 网关快速拒绝未认证请求

---

## 🔍 关键发现

### 1. 真实性能基准（完整链路：网关+Token）

| 接口 | QPS | 平均延迟 | 瓶颈分析 |
|-----|-----|---------|---------|
| GET /get/vo | **682** | 80ms | Token验证 + 网关路由 + DB + Feign |
| POST /list/page/vo | **401** | 128ms | Token验证 + 网关路由 + 分页 + 批量Feign |

### 2. 直连服务 vs 网关+Token 对比

| 接口 | 直连QPS | 网关+Token QPS | 性能损耗 |
|-----|---------|---------------|---------|
| GET /get/vo | 1,317 | 682 | 48% ↓ |
| POST /list/page/vo | 560 | 401 | 28% ↓ |

**分析**: 网关层带来的开销主要来自：
- Sa-Token 验证（Redis 查询）
- 网关路由转发
- 负载均衡决策

### 3. 缓存优化潜力

| 场景 | 当前 QPS | 预期优化后 QPS | 优化倍数 |
|-----|---------|---------------|---------|
| GET /get/vo | 682 | 5,000+ | 7-10x |
| POST /list/page/vo | 401 | 3,000+ | 7-10x |

---

## 📝 测试配置

```yaml
测试主机: Windows 11 (localhost)
压测工具: wrk (WSL2)
并发配置:
  threads: 4
  connections: 50
  duration: 30s
Sentinel: 已关闭
Token: a95a5e0f-f1cb-40d9-bda9-9bc597057352
```

---

## 🎯 下一步

1. ✅ ~~使用真实 Token 测试通过网关的完整链路~~
2. 🔜 **实现多级缓存** 后重新进行压测
3. 🔜 观察缓存命中后的 QPS 提升效果
4. 🔜 恢复 Sentinel 限流配置
