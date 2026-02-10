# SmarterOJ 微服务 Docker Compose 部署指南

## 概述

本项目使用 Docker Compose 进行一键部署，包含以下服务：

**基础设施服务：**
- MySQL 8.0 - 数据库
- Redis 7 - 缓存和会话存储
- Nacos 2.2.3 - 服务注册与发现

**业务服务：**
- Gateway - API 网关
- User Service - 用户服务
- Question Service - 题目服务
- Judge Service - 判题服务
- Post Service - 帖子服务
- Room Service - 协作房间服务

## 前置要求

### 必需软件

1. **Docker** (版本 20.10+)
   - Windows: 安装 [Docker Desktop](https://www.docker.com/products/docker-desktop)
   - Linux: 参考 [Docker 官方文档](https://docs.docker.com/engine/install/)
   - macOS: 安装 [Docker Desktop](https://www.docker.com/products/docker-desktop)

2. **Docker Compose** (版本 2.0+)
   - Docker Desktop 自带 Docker Compose
   - Linux 独立安装: `sudo apt-get install docker-compose-plugin`

### 系统要求

- **内存**: 至少 4GB 可用内存（推荐 8GB+）
- **磁盘**: 至少 10GB 可用空间
- **CPU**: 2 核心以上（推荐 4 核心+）

## 快速开始

### 1. 准备配置文件

首次部署需要创建 `.env.prod` 文件：

```bash
# 复制环境变量模板
cp env.prod.example .env.prod
```

编辑 `.env.prod` 文件，配置必要的环境变量：

```env
# MySQL 配置
MYSQL_ROOT_PASSWORD=your_strong_password

# Nacos 认证配置（保持默认即可）
NACOS_AUTH_TOKEN=SecretKey012345678901234567890123456789012345678901234567890123456789
NACOS_AUTH_IDENTITY_KEY=nacos
NACOS_AUTH_IDENTITY_VALUE=nacos

# 阿里云短信配置（可选，如不使用保持默认）
ALIYUN_ACCESS_KEY=YOUR_ACCESS_KEY
ALIYUN_SECRET=YOUR_SECRET
ALIYUN_SIGN_NAME=YOUR_SIGN_NAME
ALIYUN_TEMPLATE_CODE=YOUR_TEMPLATE_CODE

# 房间服务密钥（建议修改）
ROOM_AUTH_SECRET=your_room_secret_key
```

### 2. 准备数据库初始化脚本

数据库初始化脚本已经准备就绪：

```
docker/mysql/init/
├── 01-nacos.sql        # Nacos 配置数据库（约 10KB）
├── 02-smarteroj.sql    # 业务数据库表结构（约 11KB）
└── 03-data.sql         # 业务数据（约 165KB）- 可选
```

**说明：**
- `01-nacos.sql`: Nacos 服务发现所需的配置数据库
- `02-smarteroj.sql`: 包含所有业务表的建表语句（user, question, post, room 等）
- `03-data.sql`: 当前数据库中的数据导出（包含用户、题目、帖子、房间等数据）

**可选操作 - 重新导出数据：**

如果需要重新导出当前数据库的数据，可以运行：

```bash
# Windows 用户
mysqldump -hlocalhost -P3306 -uroot -p[密码] --no-create-info --skip-add-drop-table --complete-insert --single-transaction smarter_oj_db user question question_set question_set_item post room > docker\mysql\init\03-data.sql

# Linux/macOS 用户
mysqldump -hlocalhost -P3306 -uroot -p[密码] --no-create-info --skip-add-drop-table --complete-insert --single-transaction smarter_oj_db user question question_set question_set_item post room > docker/mysql/init/03-data.sql
```

**注意：** 如果不需要导入历史数据（从零开始），可以删除 `03-data.sql` 文件。

### 3. 部署服务

#### 手动部署（推荐）

如果不使用脚本，可以手动执行以下命令：

```bash
# 1. 启动基础服务
docker compose --env-file .env.prod up -d mysql redis nacos

# 2. 等待 Nacos 启动（约 60 秒）
# 检查 Nacos 健康状态
curl http://localhost:8848/nacos/v1/console/health/readiness

# 3. 构建 JAR（Windows 可用 build-jars.ps1）
./build-jars.sh

# 4. 启动业务服务
docker compose --env-file .env.prod -f docker-compose-services.yml up -d user-service question-service judge-service post-service room-service

# 5. 启动网关
docker compose --env-file .env.prod -f docker-compose-services.yml up -d gateway
```

## 服务访问

部署成功后，可以通过以下地址访问服务：

| 服务 | 地址 | 说明 |
|-----|------|------|
| API 网关 | http://localhost:8101 | 统一入口 |
| 用户服务（直连） | http://localhost:8201/api/user | 直连调用 |
| 题目服务（直连） | http://localhost:8202/api/question | 直连调用 |
| 判题服务（直连） | http://localhost:8203/api/judge | 直连调用 |
| 帖子服务（直连） | http://localhost:8204/api/post | 直连调用 |
| 房间服务（直连） | http://localhost:8205/api/room | 直连调用 |
| Nacos 控制台 | http://localhost:8848/nacos | 账号: nacos / nacos |
| MySQL | localhost:3306 | 数据库端口 |
| Redis | localhost:6379 | 缓存端口 |

### API 接口

通过网关访问各服务：

- 用户服务: `http://localhost:8101/api/user/**`
- 题目服务: `http://localhost:8101/api/question/**`
- 判题服务: `http://localhost:8101/api/judge/**`
- 帖子服务: `http://localhost:8101/api/post/**`
- 房间服务: `http://localhost:8101/api/room/**`

### Knife4j 接口文档

访问 `http://localhost:8101/doc.html` 查看聚合的 API 文档。

## 常用命令

### 查看服务状态

```bash
# 基础服务（MySQL / Redis / Nacos）
docker compose --env-file .env.prod ps

# 业务服务（Gateway / User / Question / Judge / Post / Room）
docker compose --env-file .env.prod -f docker-compose-services.yml ps
```

### 查看服务日志

```bash
# 查看基础服务日志
docker compose --env-file .env.prod logs mysql
docker compose --env-file .env.prod logs redis
docker compose --env-file .env.prod logs nacos

# 查看业务服务日志
docker compose --env-file .env.prod -f docker-compose-services.yml logs -f gateway
docker compose --env-file .env.prod -f docker-compose-services.yml logs -f user-service

# 查看最近 100 行日志
docker compose --env-file .env.prod -f docker-compose-services.yml logs --tail=100 gateway
```

### 重启服务

```bash
# 重启业务服务
docker compose --env-file .env.prod -f docker-compose-services.yml restart

# 重启特定服务
docker compose --env-file .env.prod -f docker-compose-services.yml restart gateway
docker compose --env-file .env.prod -f docker-compose-services.yml restart user-service
```

### 停止服务

```bash
# 停止所有服务
docker compose --env-file .env.prod -f docker-compose-services.yml stop
docker compose --env-file .env.prod stop

# 停止并删除容器（保留数据卷）
docker compose --env-file .env.prod -f docker-compose-services.yml down
docker compose --env-file .env.prod down

# 停止并删除容器和数据卷（危险操作！）
docker compose --env-file .env.prod -f docker-compose-services.yml down -v
docker compose --env-file .env.prod down -v
```

### 重新构建 / 更新服务

本项目业务服务通过挂载本地 `docker/jars/*.jar` 运行，更新代码后按以下方式更新：

```bash
# 1) 重新构建并输出最新 JAR 到 docker/jars/
./build-jars.sh

# 2) 重启业务服务（让容器重新加载挂载的 JAR）
docker compose --env-file .env.prod -f docker-compose-services.yml restart
```

### 进入容器

```bash
# 进入 MySQL 容器
docker exec -it smarteroj-mysql bash

# 进入 Redis 容器
docker exec -it smarteroj-redis sh

# 进入业务服务容器
docker exec -it smarteroj-gateway bash
```

## 故障排查

### Nacos 无法启动

1. 检查 MySQL 是否正常启动：
   ```bash
   docker compose logs mysql
   ```

2. 确保 Nacos 配置数据库已初始化：
   ```bash
   docker exec -it smarteroj-mysql mysql -uroot -p
   # 输入密码后执行
   SHOW DATABASES;
   # 应该能看到 nacos_config 数据库
   ```

3. 检查 Nacos 日志：
   ```bash
   docker compose logs nacos
   ```

### 业务服务无法启动

1. 检查 Nacos 是否已就绪：
   ```bash
   curl http://localhost:8848/nacos/v1/console/health/readiness
   ```

2. 检查服务日志：
   ```bash
   docker compose --env-file .env.prod -f docker-compose-services.yml logs user-service
   ```

3. 常见问题：
   - 数据库连接失败：检查 MySQL 是否启动，密码是否正确
   - Redis 连接失败：检查 Redis 是否启动
   - Nacos 注册失败：确保 Nacos 已完全启动

### 网关无法访问

1. 检查网关日志：
   ```bash
   docker compose --env-file .env.prod -f docker-compose-services.yml logs gateway
   ```

2. 确认所有业务服务已注册到 Nacos：
   - 访问 Nacos 控制台: http://localhost:8848/nacos
   - 登录后查看"服务管理" -> "服务列表"
   - 应该能看到所有业务服务

### 数据库初始化失败

1. 检查初始化脚本：
   ```bash
   ls -la docker/mysql/init/
   ```

2. 查看 MySQL 初始化日志：
   ```bash
   docker compose --env-file .env.prod logs mysql | grep "entrypoint"
   ```

3. 如需重新初始化，删除数据卷：
   ```bash
   docker compose --env-file .env.prod down -v
   docker compose --env-file .env.prod up -d mysql redis nacos
   ```

## 生产环境建议

### 安全配置

1. **修改默认密码**
   - MySQL root 密码
   - Nacos 认证密钥
   - Room 服务密钥

2. **配置防火墙**
   - 仅开放必要端口（8101 网关端口）
   - 限制 Nacos、MySQL、Redis 端口仅内网访问

3. **启用 HTTPS**
   - 在网关前配置 Nginx 反向代理
   - 申请 SSL 证书

### 性能优化

1. **资源限制**
   
   在 `docker-compose.yml` 中为每个服务添加资源限制：
   ```yaml
   services:
     user-service:
       # ...
       deploy:
         resources:
           limits:
             cpus: '1'
             memory: 512M
           reservations:
             memory: 256M
   ```

2. **数据持久化**
   
   确保数据卷已正确配置，避免数据丢失。

3. **日志管理**
   
   配置日志轮转，避免日志文件过大：
   ```yaml
   services:
     user-service:
       # ...
       logging:
         driver: "json-file"
         options:
           max-size: "10m"
           max-file: "3"
   ```

### 监控

1. 使用 Prometheus + Grafana 监控服务
2. 配置 Nacos 监控告警
3. 设置数据库慢查询日志

## 扩展说明

### 添加代码沙箱服务

如果需要部署代码沙箱服务，在 `docker-compose.yml` 中添加：

```yaml
  code-sandbox:
    image: your-code-sandbox-image
    container_name: smarteroj-code-sandbox
    restart: always
    ports:
      - "8080:8080"
    networks:
      - smarteroj-network
```

并更新生产配置中的 `codesandbox.remote-url`。

### 水平扩展

使用 Docker Compose 扩展服务实例：

```bash
# 扩展用户服务到 3 个实例
docker compose --env-file .env.prod -f docker-compose-services.yml up -d --scale user-service=3
```

## 参考资料

- [Docker 官方文档](https://docs.docker.com/)
- [Docker Compose 文档](https://docs.docker.com/compose/)
- [Nacos 官方文档](https://nacos.io/zh-cn/docs/quick-start-docker.html)
- [Spring Cloud Alibaba 文档](https://spring-cloud-alibaba-group.github.io/github-pages/hoxton/zh-cn/index.html)

## 联系支持

如遇到问题，请：

1. 查看本文档的故障排查章节
2. 查看服务日志获取详细错误信息
3. 在项目 Issue 中提问

---

最后更新: 2026-01-26
