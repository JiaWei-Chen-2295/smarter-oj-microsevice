#!/bin/bash

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  SmarterOJ 微服务部署脚本${NC}"
echo -e "${GREEN}========================================${NC}"

# 检查 Docker 和 Docker Compose
if ! command -v docker &> /dev/null; then
    echo -e "${RED}错误: Docker 未安装，请先安装 Docker${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo -e "${RED}错误: Docker Compose 未安装，请先安装 Docker Compose${NC}"
    exit 1
fi

# 检查 .env.prod 文件
if [ ! -f .env.prod ]; then
    echo -e "${YELLOW}未找到 env.prod 文件，从 env.prod.example 复制...${NC}"
    cp .env.prod.example .env.prod
    echo -e "${YELLOW}请编辑 env.prod 文件，配置必要的环境变量${NC}"
    read -p "按 Enter 继续..."
fi

# 构建并启动服务
echo -e "${GREEN}正在构建和启动服务...${NC}"

# 使用 docker-compose 或 docker compose
if command -v docker-compose &> /dev/null; then
    COMPOSE_CMD="docker-compose"
else
    COMPOSE_CMD="docker compose"
fi

# 启动基础服务
echo -e "${GREEN}步骤 1/4: 启动基础服务 (MySQL, Redis, Nacos)...${NC}"
$COMPOSE_CMD --env-file .env.prod up -d mysql redis nacos

# 等待 Nacos 启动
echo -e "${YELLOW}等待 Nacos 服务启动 (预计 60 秒)...${NC}"
sleep 60

# 检查 Nacos 健康状态
echo -e "${GREEN}检查 Nacos 健康状态...${NC}"
for i in {1..10}; do
    if curl -f http://localhost:8848/nacos/v1/console/health/readiness > /dev/null 2>&1; then
        echo -e "${GREEN}Nacos 已就绪${NC}"
        break
    fi
    echo -e "${YELLOW}等待 Nacos 就绪... ($i/10)${NC}"
    sleep 10
done

# 构建 JAR
echo -e "${GREEN}步骤 2/4: 构建 JAR...${NC}"
bash ./build-jars.sh

# 启动业务服务
echo -e "${GREEN}步骤 3/4: 启动业务服务...${NC}"
$COMPOSE_CMD --env-file .env.prod -f docker-compose-services.yml up -d user-service question-service judge-service post-service room-service

# 等待业务服务启动
echo -e "${YELLOW}等待业务服务启动 (预计 30 秒)...${NC}"
sleep 30

# 启动网关
echo -e "${GREEN}步骤 4/4: 启动网关服务...${NC}"
$COMPOSE_CMD --env-file .env.prod -f docker-compose-services.yml up -d gateway

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}部署完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "服务访问地址："
echo -e "  - 网关: ${GREEN}http://localhost:8101${NC}"
echo -e "  - Nacos 控制台: ${GREEN}http://localhost:8848/nacos${NC}"
echo -e "    默认账号: nacos / nacos"
echo ""
echo -e "查看服务状态："
echo -e "  ${YELLOW}$COMPOSE_CMD --env-file env.prod ps${NC}"
echo ""
echo -e "查看服务日志："
echo -e "  ${YELLOW}$COMPOSE_CMD --env-file env.prod -f docker-compose-services.yml logs -f [服务名]${NC}"
echo ""
echo -e "停止所有服务："
echo -e "  ${YELLOW}$COMPOSE_CMD --env-file env.prod -f docker-compose-services.yml down${NC}"
echo -e "  ${YELLOW}$COMPOSE_CMD --env-file env.prod down${NC}"
echo ""
