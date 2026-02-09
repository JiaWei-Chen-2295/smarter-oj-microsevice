# Question Service 缓存有效性对比压测报告

> **测试日期**: 2026-02-09  
> **测试工具**: wrk  
> **测试环境**: 本地开发环境 (WSL2 -> Windows)  
> **服务状态**: Sentinel 限流已关闭  
> **说明**: 压测前已进行缓存预热（网关 + 直连），确保缓存命中

---

## 📊 测试结果汇总（缓存已命中）

| 测试场景 | 接口 | 测试方式 | QPS | 平均延迟 | 最大延迟 | 备注 |
|---------|-----|---------|-----|---------|---------|-----|
| Test 1 | GET /get/vo | 网关 + Token | **981.97** | 53.52ms | 468.25ms | 无超时 |
| Test 2 | POST /list/page/vo | 网关 + Token | **494.77** | 99.79ms | 493.28ms | 无超时 |
| Test 3 | GET /get/vo | 直连服务 + Token | **8,403.35** | 5.96ms | 790.91ms | 无超时 |
| Test 4 | POST /list/page/vo | 直连服务 + Token | **995.37** | 113.47ms | 1.42s | 超时 13 |

---

## 📈 详细测试结果

### Test 1: GET /api/question/get/vo (通过网关)

```
URL: http://localhost:8101/api/question/get/vo?id=1907
配置: 4 threads, 50 connections, 30s duration

Running 30s test @ http://localhost:8101/api/question/get/vo?id=1907
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    53.52ms   40.73ms 468.25ms   93.78%
    Req/Sec   250.49     68.50   363.00     74.24%
  29494 requests in 30.04s, 47.26MB read
Requests/sec:    981.97
Transfer/sec:      1.57MB
```

### Test 2: POST /api/question/list/page/vo (通过网关)

```
URL: http://localhost:8101/api/question/list/page/vo
配置: 4 threads, 50 connections, 30s duration
Body: {"current":1,"pageSize":20}

Running 30s test @ http://localhost:8101/api/question/list/page/vo
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    99.79ms   43.61ms 493.28ms   89.52%
    Req/Sec   125.33     34.26   242.00     71.34%
  14866 requests in 30.05s, 563.20MB read
Requests/sec:    494.77
Transfer/sec:     18.74MB
```

### Test 3: GET /api/question/get/vo (直连服务)

```
URL: http://localhost:8202/api/question/get/vo?id=1907
配置: 4 threads, 50 connections, 30s duration

Running 30s test @ http://localhost:8202/api/question/get/vo?id=1907
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.96ms   15.20ms 790.91ms   97.77%
    Req/Sec     2.11k   648.25     4.05k    65.58%
  252213 requests in 30.01s, 404.14MB read
Requests/sec:   8403.35
Transfer/sec:     13.47MB
```

### Test 4: POST /api/question/list/page/vo (直连服务)

```
URL: http://localhost:8202/api/question/list/page/vo
配置: 4 threads, 50 connections, 30s duration
Body: {"current":1,"pageSize":20}

Running 30s test @ http://localhost:8202/api/question/list/page/vo
  4 threads and 50 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   113.47ms  219.25ms   1.42s    90.32%
    Req/Sec   249.54     73.92   575.00     72.64%
  29892 requests in 30.03s, 1.11GB read
  Socket errors: connect 0, read 0, write 0, timeout 13
Requests/sec:    995.37
Transfer/sec:     37.71MB
```

---

## ✅ 缓存命中验证（压测后）

缓存监控接口：`/api/question/admin/cache/stats`

| 缓存 | size | hitCount | missCount | hitRate |
|------|------|----------|-----------|---------|
| questionVO | 1 | 586,971 | 60 | **99.99%** |
| questionList | 1 | 85,671 | 49 | **99.94%** |

---

## 🔍 结论

1. 多级缓存已生效（`questionVO`、`questionList` 命中率均 > 99%）。
2. **直连服务 QPS 明显高于网关**，网关 + Token 带来明显开销：
   - GET /get/vo：8403 → 982（约 8.6x 差距）
   - POST /list/page/vo：995 → 495（约 2.0x 差距）
3. 进一步瓶颈更可能在 **网关鉴权/路由** 或 **Token/Redis 校验** 层，而不是题目缓存本身。

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

