# Docker Compose 部署说明

## 📁 文件结构

```
├── docker-compose.yml              # 基础依赖服务（MySQL、Redis、Nacos）
├── docker-compose-services.yml     # 业务微服务（Gateway、User、Question等）
├── deploy.ps1                      # Windows 部署脚本（交互式）
├── deploy.sh                       # Linux/macOS 部署脚本
└── .env                           # 环境变量配置
```

## 🚀 快速开始

### Windows 用户

运行交互式部署脚本：
```powershell
.\deploy.ps1
```

然后选择：
1. 全部部署（基础服务 + 业务服务）
2. 仅部署基础服务（MySQL + Redis + Nacos）
3. 仅部署业务服务（需先启动基础服务）
4. 停止所有服务
5. 查看服务状态

### 手动部署

#### 1. 启动基础依赖服务

```bash
# 启动 MySQL、Redis、Nacos
docker-compose up -d

# 等待 30-60 秒让服务初始化完成
# 可以查看日志确认 Nacos 已就绪
docker-compose logs -f nacos
```

#### 2. 启动业务微服务

```bash
# 构建并启动所有业务服务
docker-compose -f docker-compose-services.yml up -d --build

# 或启动单个服务
docker-compose -f docker-compose-services.yml up -d gateway
docker-compose -f docker-compose-services.yml up -d user-service
```

## 📋 常用命令

### 查看服务状态

```bash
# 基础服务
docker-compose ps

# 业务服务
docker-compose -f docker-compose-services.yml ps

# 所有容器
docker ps
```

### 查看服务日志

```bash
# 基础服务日志
docker-compose logs -f [mysql|redis|nacos]

# 业务服务日志
docker-compose -f docker-compose-services.yml logs -f [服务名]

# 例如：查看网关日志
docker-compose -f docker-compose-services.yml logs -f gateway
```

### 停止服务

```bash
# 停止业务服务
docker-compose -f docker-compose-services.yml down

# 停止基础服务
docker-compose down

# 停止所有服务（包含数据卷）
docker-compose down -v
docker-compose -f docker-compose-services.yml down
```

### 重启服务

```bash
# 重启单个业务服务
docker-compose -f docker-compose-services.yml restart gateway

# 重启所有业务服务
docker-compose -f docker-compose-services.yml restart

# 重启基础服务
docker-compose restart nacos
```

### 重新构建

```bash
# 重新构建并启动特定服务
docker-compose -f docker-compose-services.yml up -d --build user-service

# 重新构建所有业务服务
docker-compose -f docker-compose-services.yml up -d --build
```

## 🔧 配置说明

### 环境变量（.env）

复制 `.env.example` 到 `.env` 并修改：

```properties
# MySQL 配置
MYSQL_ROOT_PASSWORD=root123456

# Nacos 认证配置
NACOS_AUTH_TOKEN=your-secret-token
NACOS_AUTH_IDENTITY_KEY=nacos
NACOS_AUTH_IDENTITY_VALUE=nacos

# 阿里云短信配置（可选）
ALIYUN_ACCESS_KEY=your-access-key
ALIYUN_SECRET=your-secret
```

## 🌐 服务访问

启动成功后：

- **网关**: http://localhost:8101
- **Nacos 控制台**: http://localhost:8848/nacos
  - 默认账号: `nacos` / `nacos`
- **MySQL**: localhost:3306
  - 账号: `root` / `${MYSQL_ROOT_PASSWORD}`
- **Redis**: localhost:6379

## 📊 数据持久化

数据通过 Docker 卷持久化：

- `mysql-data`: MySQL 数据
- `redis-data`: Redis 数据
- `nacos-data`: Nacos 数据
- `nacos-logs`: Nacos 日志

## ⚠️ 注意事项

1. **首次启动**：基础服务启动后需要等待 30-60 秒让 Nacos 完全初始化
2. **服务依赖**：业务服务必须在基础服务启动后才能正常运行
3. **网络**：所有服务都在 `smarteroj-network` 网络中通信
4. **构建时间**：首次构建业务服务镜像可能需要 5-10 分钟（下载依赖）
5. **资源要求**：建议至少 4GB 内存

## 🐛 故障排查

### Nacos 启动失败

```bash
# 查看 Nacos 日志
docker-compose logs nacos

# 常见问题：MySQL 未就绪
# 解决：等待 MySQL 健康检查通过后重启 Nacos
docker-compose restart nacos
```

### 业务服务无法连接 Nacos

```bash
# 1. 确认 Nacos 运行正常
curl http://localhost:8848/nacos/v1/console/health/readiness

# 2. 检查网络
docker network inspect smarteroj-network

# 3. 重启业务服务
docker-compose -f docker-compose-services.yml restart
```

### 服务无法注册到 Nacos

检查 `application-prod.yml` 中的 Nacos 配置：
```yaml
spring:
  cloud:
    nacos:
      serverAddr: nacos:8848  # 容器内使用服务名
```

## 📝 开发建议

### 本地开发模式

如果只是本地开发测试，可以：

1. 只启动基础服务：
   ```bash
   docker-compose up -d
   ```

2. 在 IDE 中直接运行业务服务（使用 `application-dev.yml`）

### 生产部署建议

1. 修改 `.env` 中的敏感信息
2. 使用外部 MySQL 和 Redis
3. 配置 Nacos 集群模式
4. 设置合适的资源限制
5. 启用日志持久化
