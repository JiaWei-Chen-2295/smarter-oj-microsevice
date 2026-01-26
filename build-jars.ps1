$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

$outDir = Join-Path $root "docker\\jars"
New-Item -ItemType Directory -Force -Path $outDir | Out-Null

function Test-CommandExists {
    param([string]$Name)
    return [bool](Get-Command $Name -ErrorAction SilentlyContinue)
}

# 设置 JDK 8 环境
$jdk8Path = "C:\\Users\\16010\\.jdks\\corretto-1.8.0_432"
if (Test-Path $jdk8Path) {
    Write-Host "使用 JDK 8: $jdk8Path" -ForegroundColor Cyan
    $env:JAVA_HOME = $jdk8Path
    $env:PATH = "$jdk8Path\\bin;$env:PATH"
    java -version
} else {
    Write-Host "警告: 未找到 JDK 8，使用系统默认 JDK" -ForegroundColor Yellow
}

# 使用 Maven 构建
if (Test-CommandExists "mvn") {
    Write-Host "开始构建..." -ForegroundColor Green
    mvn "-Dmaven.test.skip=true" "clean" "package"
} elseif (Test-CommandExists "docker") {
    Write-Host "使用 Docker Maven 构建 (JDK 8)..." -ForegroundColor Cyan
    $mavenSettings = (Resolve-Path (Join-Path $root "maven\\settings.xml")).Path.Replace("\\", "/")
    $workDir = $root.Replace("\\", "/")
    docker run --rm -v "${workDir}:/app" -v "${mavenSettings}:/root/.m2/settings.xml:ro" -w /app docker.m.daocloud.io/library/maven:3.8.5-openjdk-8 mvn "-Dmaven.test.skip=true" "clean" "package"
} else {
    throw "未找到 mvn 或 docker"
}

function Copy-LatestJar {
    param(
        [Parameter(Mandatory = $true)][string]$ModulePath,
        [Parameter(Mandatory = $true)][string]$DestName
    )

    $targetDir = Join-Path $root (Join-Path $ModulePath "target")
    if (!(Test-Path $targetDir)) {
        throw "target 目录不存在: $targetDir"
    }

    $jar = Get-ChildItem $targetDir -Filter "*.jar" -File |
        Where-Object { $_.Name -notlike "*.original" -and $_.Name -notlike "*-sources.jar" -and $_.Name -notlike "*-javadoc.jar" } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    if ($null -eq $jar) {
        throw "未找到可用 jar: $targetDir"
    }

    Copy-Item -Force $jar.FullName (Join-Path $outDir $DestName)
}

Copy-LatestJar "jc-smarteroj-backend-gateway" "gateway.jar"
Copy-LatestJar "jc-smarteroj-backend-user-service" "user-service.jar"
Copy-LatestJar "jc-smarteroj-backend-question-service" "question-service.jar"
Copy-LatestJar "jc-smarteroj-backend-judge-service" "judge-service.jar"
Copy-LatestJar "jc-smarteroj-backend-post-service" "post-service.jar"
Copy-LatestJar "jc-smarteroj-backend-room-service" "room-service.jar"

Write-Output "JAR 已输出到: $outDir"
