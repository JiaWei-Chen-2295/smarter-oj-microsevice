# ===============================================
# SmarterOJ 数据库数据导出脚本 (Windows PowerShell)
# 用于导出当前数据库中的数据到 SQL 文件
# ===============================================

# 数据库配置
$DB_HOST = "localhost"
$DB_PORT = "3306"
$DB_NAME = "smarter_oj_db"
$DB_USER = "root"
$DB_PASS = "cjw2295cjw"

# 输出文件
$OUTPUT_FILE = "docker\mysql\init\03-data.sql"

# 颜色输出函数
function Write-ColorOutput($ForegroundColor) {
    $fc = $host.UI.RawUI.ForegroundColor
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    if ($args) {
        Write-Output $args
    }
    $host.UI.RawUI.ForegroundColor = $fc
}

Write-ColorOutput Green "========================================"
Write-ColorOutput Green "  SmarterOJ 数据导出工具"
Write-ColorOutput Green "========================================"
Write-Output ""

# 检查 mysqldump 是否存在
if (-not (Get-Command mysqldump -ErrorAction SilentlyContinue)) {
    Write-ColorOutput Red "错误: mysqldump 命令未找到，请确保已安装 MySQL 客户端"
    Write-Output ""
    Write-Output "MySQL 客户端下载: https://dev.mysql.com/downloads/mysql/"
    exit 1
}

Write-ColorOutput Yellow "正在导出数据..."
Write-Output ""

# 构建 mysqldump 命令
$tables = "user question question_set question_set_item post room"
$cmd = "mysqldump -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASS --no-create-info --skip-add-drop-table --complete-insert --single-transaction $DB_NAME $tables"

try {
    # 执行导出
    Invoke-Expression "$cmd > $OUTPUT_FILE 2>&1"
    
    if (Test-Path $OUTPUT_FILE) {
        Write-ColorOutput Green "✓ 数据导出成功！"
        Write-Output "  输出文件: $OUTPUT_FILE"
        Write-Output ""
        
        # 显示文件大小
        $fileInfo = Get-Item $OUTPUT_FILE
        $fileSize = "{0:N2} KB" -f ($fileInfo.Length / 1KB)
        Write-Output "  文件大小: $fileSize"
        
        # 统计记录数
        $content = Get-Content $OUTPUT_FILE
        $recordCount = ($content | Select-String -Pattern "^INSERT INTO").Count
        Write-Output "  INSERT 语句数: $recordCount"
        Write-Output ""
        
        Write-ColorOutput Green "提示："
        Write-Output "  1. 该文件已保存到 docker\mysql\init\ 目录"
        Write-Output "  2. Docker 容器首次启动时会自动执行该脚本"
        Write-Output "  3. 如果不需要导入历史数据，可以删除此文件"
        Write-Output ""
        
        Write-ColorOutput Yellow "说明："
        Write-Output "  - 已导出的表: user, question, question_set, question_set_item, post, room"
        Write-Output "  - 未导出的表: question_submit, post_favour, post_thumb, user_room, ws_message"
        Write-Output "  - 原因: 这些表包含用户行为数据，通常不需要在新环境中导入"
    } else {
        throw "导出文件未生成"
    }
} catch {
    Write-ColorOutput Red "✗ 数据导出失败！"
    Write-Output ""
    Write-Output "可能的原因："
    Write-Output "  1. 数据库连接失败，请检查数据库是否启动"
    Write-Output "  2. 用户名或密码错误"
    Write-Output "  3. 数据库不存在"
    Write-Output ""
    Write-Output "错误信息: $_"
    exit 1
}

Write-ColorOutput Green "========================================"
