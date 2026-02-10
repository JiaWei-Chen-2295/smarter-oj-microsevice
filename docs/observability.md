# 可观测性与监控

本项目已接入 **Prometheus + Grafana**，并通过 Actuator + Micrometer 输出 JVM / HTTP / 连接池 / 缓存 / 业务指标。

## 管理端口（本地并行运行）

为避免冲突，本地同时跑所有服务时使用独立管理端口：

- Gateway: `9990`
- User: `9991`
- Question: `9992`
- Judge: `9993`
- Post: `9994`
- Room: `9995`

指标地址示例：`http://localhost:9991/actuator/prometheus`

## Prometheus（本地）

使用本地 Prometheus（已下载在 `D:\dev\prometheus-2.51.0.windows-amd64`）示例配置：

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'gateway'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:9990']
  - job_name: 'user'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:9991']
  - job_name: 'question'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:9992']
  - job_name: 'judge'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:9993']
  - job_name: 'post'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:9994']
  - job_name: 'room'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:9995']
```

启动命令：

```powershell
cd D:\dev\prometheus-2.51.0.windows-amd64
.\prometheus.exe --config.file=.\prometheus.yml
```

Prometheus Targets：`http://localhost:9090/targets`

## Grafana（本地）

启动 Grafana Windows 版：

```powershell
cd D:\dev\grafana\bin
.\grafana-server.exe
```

访问 `http://localhost:3000`，默认账号密码 `admin/admin`，首次登录需改密码。

添加 Prometheus 数据源：URL `http://localhost:9090`。

推荐导入的社区 Dashboard：

| Dashboard | ID | 覆盖范围 |
|---|---|---|
| JVM (Micrometer) | 4701 | JVM 内存、GC、线程 |
| Spring Boot Statistics | 12900 | HTTP 请求速率/延迟/错误率 |
| Spring Cloud Gateway | 11506 | 网关路由级指标 |
| HikariCP | 6083 | 连接池活跃/空闲/等待 |

## 自定义演示面板（推荐）

项目已提供一套简洁专业的演示面板：`docker/grafana/dashboards/smarteroj-live-overview.json`。

导入方式：

1. Grafana 左侧菜单 -> Dashboards -> Import
2. Upload JSON file
3. 选择 `docker/grafana/dashboards/smarteroj-live-overview.json`

## 自定义业务指标

- 判题：`oj.judge.submit.total` / `oj.judge.result.total{result=success|fail|error}` / `oj.judge.execution.duration`
- 用户：`oj.user.login.total{result=success|fail}` / `oj.user.register.total`
- 缓存：`cache_gets_total` / `cache_size` / `cache_evictions_total`

## 验证清单

1. 指标输出：`http://localhost:999X/actuator/prometheus`
2. Prometheus Targets 全部 UP：`http://localhost:9090/targets`
3. Grafana 数据源可用 & Dashboard 正常显示
4. 触发判题/登录后，自定义指标有递增
