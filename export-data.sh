#!/bin/bash

# ===============================================
# SmarterOJ 数据库数据导出脚本
# 用于导出当前数据库中的数据到 SQL 文件
# ===============================================

# 数据库配置
DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="smarter_oj_db"
DB_USER="root"
DB_PASS="cjw2295cjw"

# 输出文件
OUTPUT_FILE="docker/mysql/init/03-data.sql"

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  SmarterOJ 数据导出工具${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 检查 mysqldump 是否存在
if ! command -v mysqldump &> /dev/null; then
    echo -e "${RED}错误: mysqldump 命令未找到，请确保已安装 MySQL 客户端${NC}"
    exit 1
fi

echo -e "${YELLOW}正在导出数据...${NC}"
echo ""

# 导出数据（不包含建表语句）
mysqldump -h${DB_HOST} -P${DB_PORT} -u${DB_USER} -p${DB_PASS} \
  --no-create-info \
  --skip-add-drop-table \
  --complete-insert \
  --single-transaction \
  ${DB_NAME} \
  user question question_set question_set_item post room \
  > ${OUTPUT_FILE} 2>&1

# 检查导出是否成功
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ 数据导出成功！${NC}"
    echo -e "  输出文件: ${YELLOW}${OUTPUT_FILE}${NC}"
    echo ""
    
    # 显示文件大小
    FILE_SIZE=$(du -h ${OUTPUT_FILE} | cut -f1)
    echo -e "  文件大小: ${FILE_SIZE}"
    
    # 统计记录数
    RECORD_COUNT=$(grep -c "^INSERT INTO" ${OUTPUT_FILE})
    echo -e "  INSERT 语句数: ${RECORD_COUNT}"
    echo ""
    
    echo -e "${GREEN}提示：${NC}"
    echo -e "  1. 该文件已保存到 docker/mysql/init/ 目录"
    echo -e "  2. Docker 容器首次启动时会自动执行该脚本"
    echo -e "  3. 如果不需要导入历史数据，可以删除此文件"
    echo ""
    
    echo -e "${YELLOW}说明：${NC}"
    echo -e "  - 已导出的表: user, question, question_set, question_set_item, post, room"
    echo -e "  - 未导出的表: question_submit, post_favour, post_thumb, user_room, ws_message"
    echo -e "  - 原因: 这些表包含用户行为数据，通常不需要在新环境中导入"
    
else
    echo -e "${RED}✗ 数据导出失败！${NC}"
    echo ""
    echo -e "可能的原因："
    echo -e "  1. 数据库连接失败，请检查数据库是否启动"
    echo -e "  2. 用户名或密码错误"
    echo -e "  3. 数据库不存在"
    echo ""
    echo -e "请检查错误信息并重试。"
    exit 1
fi

echo -e "${GREEN}========================================${NC}"
