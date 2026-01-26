# SmarterOJ 微服务部署脚本
# Windows PowerShell 版本

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
Write-ColorOutput Green "  SmarterOJ 微服务部署脚本 v2.0"
Write-ColorOutput Green "========================================"
Write-Output ""
Write-ColorOutput Yellow "本脚本支持分阶段部署："
Write-ColorOutput Yellow "  1. 基础依赖服务（MySQL、Redis、Nacos）"
Write-ColorOutput Yellow "  2. 业务微服务（Gateway、User、Question等）"
Write-Output ""

# 检查 Docker
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-ColorOutput Red "错误: Docker 未安装，请先安装 Docker Desktop"
    exit 1
}

# 检查 Docker Compose
$composeCmd = ""
if (Get-Command docker-compose -ErrorAction SilentlyContinue) {
    $composeCmd = "docker-compose"
} elseif (docker compose version 2>$null) {
    $composeCmd = "docker"
    $composeArgs = "compose"
} else {
    Write-ColorOutput Red "错误: Docker Compose 未安装"
    exit 1
}

$envFile = "env.prod"
if (-not (Test-Path $envFile)) {
    Write-ColorOutput Yellow "未找到 $envFile 文件，从 env.prod.example 复制..."
    Copy-Item "env.prod.example" $envFile
    Write-ColorOutput Yellow "请编辑 $envFile 文件，配置必要的环境变量"
    pause
}

Write-Output ""
Write-ColorOutput Yellow "=== 选择部署模式 ==="
Write-Output "1. 全部部署（基础服务 + 业务服务）"
Write-Output "2. 仅部署基础服务（MySQL + Redis + Nacos）"
Write-Output "3. 仅部署业务服务（需先启动基础服务）"
Write-Output "4. 停止所有服务"
Write-Output "5. 查看服务状态"
Write-Output "6. 退出"
Write-Output ""
$choice = Read-Host "请输入选择 (1-6)"

switch ($choice) {
    "1" {
        Write-ColorOutput Green "`n=== 开始全部部署 ==="
        
        # 部署基础服务
        Write-ColorOutput Yellow "`n[1/2] 正在启动基础依赖服务..."
        if ($composeCmd -eq "docker") {
            docker compose --env-file $envFile up -d
        } else {
            docker-compose --env-file $envFile up -d
        }
        
        if ($LASTEXITCODE -ne 0) {
            Write-ColorOutput Red "基础服务启动失败！"
            exit 1
        }
        
        Write-ColorOutput Green "基础服务启动成功！等待 60 秒供服务初始化..."
        Start-Sleep -Seconds 60
        
        # 检查 Nacos 健康状态
        Write-ColorOutput Yellow "检查 Nacos 健康状态..."
        for ($i = 1; $i -le 10; $i++) {
            try {
                $response = Invoke-WebRequest -Uri "http://localhost:8848/nacos/v1/console/health/readiness" -UseBasicParsing -TimeoutSec 5
                if ($response.StatusCode -eq 200) {
                    Write-ColorOutput Green "✓ Nacos 已就绪"
                    break
                }
            } catch {
                Write-ColorOutput Yellow "等待 Nacos 就绪... ($i/10)"
                Start-Sleep -Seconds 10
            }
        }
        
        Write-ColorOutput Yellow "`n[2/3] 正在构建 JAR..."
        .\build-jars.ps1

        # 部署业务服务
        Write-ColorOutput Yellow "`n[3/3] 正在启动业务微服务..."
        if ($composeCmd -eq "docker") {
            docker compose --env-file $envFile -f docker-compose-services.yml up -d
        } else {
            docker-compose --env-file $envFile -f docker-compose-services.yml up -d
        }
        
        if ($LASTEXITCODE -ne 0) {
            Write-ColorOutput Red "业务服务启动失败！"
            exit 1
        }
        
        Write-ColorOutput Green "`n✅ 所有服务部署完成！"
        
        Write-Output ""
        Write-ColorOutput Green "========================================"
        Write-ColorOutput Green "  服务访问地址"
        Write-ColorOutput Green "========================================"
        Write-Output "  网关: http://localhost:8101"
        Write-Output "  Nacos 控制台: http://localhost:8848/nacos"
        Write-Output "  默认账号: nacos / nacos"
    }
    
    "2" {
        Write-ColorOutput Green "`n=== 部署基础服务 ==="
        Write-ColorOutput Yellow "正在启动 MySQL、Redis、Nacos..."
        
        if ($composeCmd -eq "docker") {
            docker compose --env-file $envFile up -d
        } else {
            docker-compose --env-file $envFile up -d
        }
        
        if ($LASTEXITCODE -ne 0) {
            Write-ColorOutput Red "基础服务启动失败！"
            exit 1
        }
        
        Write-ColorOutput Green "`n✅ 基础服务部署完成！"
        Write-ColorOutput Yellow "`n提示：请等待 30-60 秒让服务完全初始化后，再部署业务服务"
        Write-ColorOutput Yellow "运行命令: .\deploy.ps1 并选择选项3"
    }
    
    "3" {
        Write-ColorOutput Green "`n=== 部署业务微服务 ==="
        
        # 检查基础服务是否运行
        Write-ColorOutput Yellow "正在检查基础服务状态..."
        $mysqlRunning = docker ps --filter "name=smarteroj-mysql" --filter "status=running" --format "{{.Names}}"
        $redisRunning = docker ps --filter "name=smarteroj-redis" --filter "status=running" --format "{{.Names}}"
        $nacosRunning = docker ps --filter "name=smarteroj-nacos" --filter "status=running" --format "{{.Names}}"
        
        if (-not $mysqlRunning -or -not $redisRunning -or -not $nacosRunning) {
            Write-ColorOutput Red "错误：基础服务未完全运行！"
            Write-Output "MySQL: $(if ($mysqlRunning) {'✓ 运行中'} else {'✗ 未运行'})"
            Write-Output "Redis: $(if ($redisRunning) {'✓ 运行中'} else {'✗ 未运行'})"
            Write-Output "Nacos: $(if ($nacosRunning) {'✓ 运行中'} else {'✗ 未运行'})"
            Write-ColorOutput Yellow "`n请先执行: .\deploy.ps1 并选择选项1或选项2来启动基础服务"
            exit 1
        }
        
        Write-ColorOutput Green "✓ 基础服务检查通过！"
        Write-Output "  MySQL: ✓ 运行中"
        Write-Output "  Redis: ✓ 运行中"
        Write-Output "  Nacos: ✓ 运行中"
        
        Write-ColorOutput Yellow "`n正在构建 JAR..."
        .\build-jars.ps1

        Write-ColorOutput Yellow "`n正在启动业务微服务..."
        if ($composeCmd -eq "docker") {
            docker compose --env-file $envFile -f docker-compose-services.yml up -d
        } else {
            docker-compose --env-file $envFile -f docker-compose-services.yml up -d
        }
        
        if ($LASTEXITCODE -ne 0) {
            Write-ColorOutput Red "业务服务启动失败！"
            exit 1
        }
        
        Write-ColorOutput Green "`n✅ 业务服务部署完成！"
        Write-Output ""
        Write-Output "服务访问地址："
        Write-Output "  网关: http://localhost:8101"
    }
    
    "4" {
        Write-ColorOutput Yellow "`n=== 停止所有服务 ==="
        
        Write-ColorOutput Yellow "正在停止业务服务..."
        if ($composeCmd -eq "docker") {
            docker compose --env-file $envFile -f docker-compose-services.yml down
        } else {
            docker-compose --env-file $envFile -f docker-compose-services.yml down
        }
        
        Write-ColorOutput Yellow "正在停止基础服务..."
        if ($composeCmd -eq "docker") {
            docker compose --env-file $envFile down
        } else {
            docker-compose --env-file $envFile down
        }
        
        Write-ColorOutput Green "`n✅ 所有服务已停止"
    }
    
    "5" {
        Write-ColorOutput Green "`n=== 服务状态 ==="
        
        Write-ColorOutput Yellow "基础服务:"
        if ($composeCmd -eq "docker") {
            docker compose --env-file $envFile ps
        } else {
            docker-compose --env-file $envFile ps
        }
        
        Write-Output ""
        Write-ColorOutput Yellow "业务服务:"
        if ($composeCmd -eq "docker") {
            docker compose --env-file $envFile -f docker-compose-services.yml ps
        } else {
            docker-compose --env-file $envFile -f docker-compose-services.yml ps
        }
    }
    
    "6" {
        Write-ColorOutput Yellow "退出部署脚本"
        exit 0
    }
    
    default {
        Write-ColorOutput Red "无效的选择！"
        exit 1
    }
}
