# Question Service 压力测试

## 测试目标

对 `jc-smarteroj-backend-question-service` 中的读接口进行压力测试，观察缓存效果。

### 测试接口

| 接口 | 方法 | 路径 | 预期耗时 | 瓶颈分析 |
|-----|-----|------|---------|---------|
| 获取题目详情 | GET | `/api/question/get/vo` | ~50ms | 1次DB查询 + 1次Feign调用(UserService) |
| 分页查询题目 | POST | `/api/question/list/page/vo` | ~100ms | 1次分页查询 + 1次批量Feign调用 |

### 性能瓶颈

1. **VO转换的Feign调用** 是最大瓶颈：每个 QuestionVO 都需通过网络获取 UserVO
2. 当前所有读接口均直连 MySQL，无应用级缓存
3. Heatmap 接口有简易 ConcurrentHashMap 缓存，其余接口全部裸查

## 基准测试结果（无缓存）

### 完整链路测试结果（网关+Token）

| 接口 | QPS | 平均延迟 | 最大延迟 |
|-----|-----|---------|---------|
| GET /get/vo | **682** | 80ms | 1.04s |
| POST /list/page/vo | **401** | 128ms | 1.15s |

### 直连服务测试结果（绕过网关）

| 接口 | QPS | 平均延迟 | 最大延迟 |
|-----|-----|---------|---------|
| GET /get/vo | **1,317** | 40ms | 415ms |
| POST /list/page/vo | **560** | 102ms | 1.01s |

详细报告见: `results/baseline_test_20260206.md`

## 前置步骤

### 1. 关闭 Sentinel 限流（已完成）

配置文件已修改（`filter.enabled: false`）：
- `jc-smarteroj-backend-question-service/src/main/resources/application.yml`
- `jc-smarteroj-backend-gateway/src/main/resources/application.yml`

### 2. 恢复 Sentinel 限流（压测完成后）

将以下配置改回 `true`：

```yaml
spring.cloud.sentinel:
  filter:
    enabled: true  # 恢复限流
```

### 3. 确保服务运行

- Gateway: http://localhost:8101
- Question Service: http://localhost:8202

## 使用 WSL 中的 wrk 进行压测

### 执行压测（带 Token）

```bash
# 进入 WSL
wsl

# 进入压测脚本目录
cd /mnt/d/a_project_with_yupi/d-smarterOJ/smarter-oj-microsevice/docs/stress_test

# 设置有效的 Token（从浏览器复制）
export SATOKEN="your-satoken-here"

# 运行带 Token 的测试
bash run_with_token.sh
```

### 直接使用 wrk 命令

```bash
# GET 请求测试（带 Token）
wrk -t4 -c50 -d30s -H 'Cookie: satoken=YOUR_TOKEN' 'http://localhost:8101/api/question/get/vo?id=1907'

# POST 请求测试（带 Token）
wrk -t4 -c50 -d30s -s list_page_vo_with_token.lua 'http://localhost:8101/api/question/list/page/vo'
```

## 文件说明

```
stress_test/
├── README.md                       # 本文档
├── run_with_token.sh               # 压测脚本（带 Token）
├── list_page_vo_with_token.lua     # POST 请求 wrk 配置
└── results/
    └── baseline_test_20260206.md   # 基准测试报告
```
