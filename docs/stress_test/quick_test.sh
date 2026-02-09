#!/bin/bash
# 快速压测脚本 - 用于快速验证接口性能
# 使用较短的测试时间（10s）

set -e

# =============== 配置区域 ===============
# 直连 Question Service
HOST="127.0.0.1:8202"

# 测试用的题目 ID
QUESTION_ID=1907

# Cookie（请替换为有效的登录 Cookie）
SATOKEN="a95a5e0f-f1cb-40d9-bda9-9bc597057352"

# 快速测试参数
THREADS=2
CONNECTIONS=20
DURATION="10s"
# =======================================

echo "===== 快速压测: GET /api/question/get/vo ====="
echo "配置: $THREADS 线程, $CONNECTIONS 连接, $DURATION"
echo ""

wrk -t$THREADS -c$CONNECTIONS -d$DURATION \
    -H "Cookie: satoken=$SATOKEN" \
    "http://$HOST/api/question/get/vo?id=$QUESTION_ID"

echo ""
echo "===== 快速压测: POST /api/question/list/page/vo ====="
echo ""

wrk -t$THREADS -c$CONNECTIONS -d$DURATION \
    -H "Cookie: satoken=$SATOKEN" \
    -H "Content-Type: application/json" \
    -s list_page_vo.lua \
    "http://$HOST/api/question/list/page/vo"

echo ""
echo "===== 测试完成 ====="
