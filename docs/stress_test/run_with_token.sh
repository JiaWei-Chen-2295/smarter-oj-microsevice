#!/bin/bash
# 带 Token 的压力测试脚本
# 需要在执行前设置有效的 SATOKEN 环境变量
# 使用方法: SATOKEN=your-token-here ./run_with_token.sh

set -e

# =============== 配置区域 ===============
# 网关地址
GATEWAY_HOST="localhost:8101"
# 直连 Question Service
QUESTION_SERVICE_HOST="localhost:8202"

# 测试用的题目 ID
QUESTION_ID=1907

# 从环境变量获取 Token（如果未设置则使用默认值）
SATOKEN="${SATOKEN:-your-satoken-here}"

# 压测参数
THREADS=4          # 线程数
CONNECTIONS=50     # 并发连接数
DURATION="30s"     # 测试持续时间

# 预热参数
WARMUP_COUNT=5     # 预热请求次数
WARMUP_SLEEP="0.2" # 每次预热间隔（秒）

# 结果输出目录
RESULT_DIR="./results"
# =======================================

# 创建结果目录
mkdir -p "$RESULT_DIR"

# 生成时间戳
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
RESULT_FILE="${RESULT_DIR}/stress_test_with_token_${TIMESTAMP}.txt"

echo "=========================================="
echo " Question Service 压力测试（带 Token）"
echo "=========================================="
echo "测试时间: $(date)"
echo "Token: ${SATOKEN:0:10}..."  # 只显示前10个字符
echo "配置: $THREADS 线程, $CONNECTIONS 连接, $DURATION 持续"
echo ""

# 验证 Token 有效性
echo "验证 Token..."
HTTP_CODE=$(curl -s -o /dev/null -w '%{http_code}' \
    -H "Cookie: satoken=$SATOKEN" \
    "http://$GATEWAY_HOST/api/question/get/vo?id=$QUESTION_ID")

if [ "$HTTP_CODE" != "200" ]; then
    echo "警告: Token 可能无效 (HTTP $HTTP_CODE)"
    echo "请确保设置了正确的 SATOKEN 环境变量"
    echo "继续执行压测（可能导致结果无效）"
else
    echo "Token 验证成功 (HTTP 200)"
fi

echo ""

# 预热缓存（确保缓存命中）
echo "预热缓存（网关 + 直连）..."
for i in $(seq 1 $WARMUP_COUNT); do
    curl -s -o /dev/null -H "Cookie: satoken=$SATOKEN" \
        "http://$GATEWAY_HOST/api/question/get/vo?id=$QUESTION_ID"
    curl -s -o /dev/null -H "Cookie: satoken=$SATOKEN" \
        "http://$QUESTION_SERVICE_HOST/api/question/get/vo?id=$QUESTION_ID"
    curl -s -o /dev/null -H "Cookie: satoken=$SATOKEN" \
        -H "Content-Type: application/json" \
        -d '{"current":1,"pageSize":20}' \
        "http://$GATEWAY_HOST/api/question/list/page/vo"
    curl -s -o /dev/null -H "Cookie: satoken=$SATOKEN" \
        -H "Content-Type: application/json" \
        -d '{"current":1,"pageSize":20}' \
        "http://$QUESTION_SERVICE_HOST/api/question/list/page/vo"
    sleep $WARMUP_SLEEP
done
echo "预热完成"

# 开始记录
{
    echo "=========================================="
    echo " Question Service 压力测试报告（带 Token）"
    echo "=========================================="
    echo "测试时间: $(date)"
    echo "配置: $THREADS 线程, $CONNECTIONS 连接, $DURATION 持续"
    echo ""

    # ========== 测试 1: GET /get/vo 通过网关 ==========
    echo "[Test 1] GET /api/question/get/vo (通过网关)"
    echo "---"
    wrk -t$THREADS -c$CONNECTIONS -d$DURATION \
        -H "Cookie: satoken=$SATOKEN" \
        "http://$GATEWAY_HOST/api/question/get/vo?id=$QUESTION_ID"
    echo ""

    # ========== 测试 2: POST /list/page/vo 通过网关 ==========
    echo "[Test 2] POST /api/question/list/page/vo (通过网关)"
    echo "---"
    wrk -t$THREADS -c$CONNECTIONS -d$DURATION \
        -s "$(pwd)/list_page_vo_with_token.lua" \
        "http://$GATEWAY_HOST/api/question/list/page/vo"
    echo ""

    # ========== 测试 3: GET /get/vo 直连服务 ==========
    echo "[Test 3] GET /api/question/get/vo (直连服务)"
    echo "---"
    wrk -t$THREADS -c$CONNECTIONS -d$DURATION \
        -H "Cookie: satoken=$SATOKEN" \
        "http://$QUESTION_SERVICE_HOST/api/question/get/vo?id=$QUESTION_ID"
    echo ""

    # ========== 测试 4: POST /list/page/vo 直连服务 ==========
    echo "[Test 4] POST /api/question/list/page/vo (直连服务)"
    echo "---"
    wrk -t$THREADS -c$CONNECTIONS -d$DURATION \
        -s "$(pwd)/list_page_vo_with_token.lua" \
        "http://$QUESTION_SERVICE_HOST/api/question/list/page/vo"
    echo ""

    echo "=========================================="
    echo " 测试完成"
    echo "=========================================="

} 2>&1 | tee "$RESULT_FILE"

echo ""
echo "结果已保存到: $RESULT_FILE"
