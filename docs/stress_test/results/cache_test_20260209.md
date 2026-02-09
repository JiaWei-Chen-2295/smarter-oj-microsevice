# Question Service 多级缓存后压力测试报告

> **测试日期**: 2026-02-09  
> **测试工具**: wrk  
> **测试环境**: 本地开发环境 (WSL2 -> Windows)  
> **服务状态**: Sentinel 限流已关闭  
> **说明**: 已启用多级缓存，完整链路（网关 + Token）测试

---

## 📊 测试结果汇总

| 测试场景 | 接口 | 测试方式 | QPS | 平均延迟 | 最大延迟 | 备注 |
|---------|-----|---------|-----|---------|---------|-----|
| Test 1 | GET /get/vo | 网关 + Token | **712.97** | 175.79ms | 2.00s | 有 10 次超时 |
| Test 2 | POST /list/page/vo | 网关 + Token | **264.16** | 257.22ms | 1.89s | 有 71 次超时 |

---

## 📈 详细测试结果

### Test 1: GET /api/question/get/vo (通过网关)

```
URL: http://localhost:8101/api/question/get/vo?id=1907
配置: 4 threads, 50 connections, 30s duration

Running 30s test @ http://localhost:8101/api/question/get/vo?id=1907
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   175.79ms  277.15ms   2.00s    87.44%
    Req/Sec   253.59    294.89     0.94k    74.24%
  21411 requests in 30.03s, 34.31MB read
  Socket errors: connect 0, read 0, write 0, timeout 10
Requests/sec:    712.97
Transfer/sec:      1.14MB
```

### Test 2: POST /api/question/list/page/vo (通过网关)

```
URL: http://localhost:8101/api/question/list/page/vo
配置: 4 threads, 50 connections, 30s duration
Body: {"current":1,"pageSize":10}

Running 30s test @ http://localhost:8101/api/question/list/page/vo
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   257.22ms  335.46ms   1.89s    84.84%
    Req/Sec   118.21    149.02   520.00     76.37%
  7953 requests in 30.11s, 147.17MB read
  Socket errors: connect 0, read 0, write 0, timeout 71
Requests/sec:    264.16
Transfer/sec:      4.89MB
```

---

## 🔍 与基准测试（2026-02-06）对比

| 接口 | 2026-02-06 QPS | 2026-02-09 QPS | 变化 |
|-----|----------------|----------------|------|
| GET /get/vo | 682 | 713 | +4.5% |
| POST /list/page/vo | 401 | 264 | -34.2% |

**观察**:
- GET /get/vo 有小幅提升，但平均延迟明显升高（80ms -> 176ms）。
- POST /list/page/vo QPS 下降且延迟上升（128ms -> 257ms），并出现较多超时。
- 当前结果未显示预期的缓存提升，需要进一步确认缓存命中率和调用链瓶颈。

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

## 🎯 下一步建议

1. 观察缓存命中率（本地缓存 + Redis）与失效策略是否生效
2. 确认多级缓存是否覆盖 `get/vo` 与 `list/page/vo` 的主要瓶颈路径
3. 排查超时原因（网关或下游 Feign 调用线程池）

