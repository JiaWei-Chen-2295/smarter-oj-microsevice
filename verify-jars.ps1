$ErrorActionPreference = "Stop"

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "验证 JAR 包中是否包含 application-prod.yml" -ForegroundColor Cyan
Write-Host "============================================`n" -ForegroundColor Cyan

$jarsDir = Join-Path $PSScriptRoot "docker\jars"
$services = @(
    "gateway.jar",
    "user-service.jar",
    "question-service.jar",
    "judge-service.jar",
    "post-service.jar",
    "room-service.jar"
)

foreach ($jar in $services) {
    $jarPath = Join-Path $jarsDir $jar
    
    if (!(Test-Path $jarPath)) {
        Write-Host "❌ $jar 不存在" -ForegroundColor Red
        continue
    }
    
    Write-Host "检查 $jar ..." -ForegroundColor Yellow
    
    # 使用 jar 命令查看内容
    if (Get-Command jar -ErrorAction SilentlyContinue) {
        $result = jar -tf $jarPath | Select-String "application-prod.yml"
        if ($result) {
            Write-Host "  ✅ 包含 application-prod.yml: $result" -ForegroundColor Green
        } else {
            Write-Host "  ❌ 不包含 application-prod.yml" -ForegroundColor Red
        }
    } else {
        # 使用 7z 或 PowerShell 的 Expand-Archive
        Write-Host "  使用 unzip 检查..." -ForegroundColor Gray
        $tempDir = Join-Path $env:TEMP "jar-verify-$(Get-Random)"
        New-Item -ItemType Directory -Force -Path $tempDir | Out-Null
        
        try {
            Expand-Archive -Path $jarPath -DestinationPath $tempDir -Force
            $prodYml = Get-ChildItem -Path $tempDir -Recurse -Filter "application-prod.yml"
            
            if ($prodYml) {
                Write-Host "  ✅ 包含 application-prod.yml" -ForegroundColor Green
                Write-Host "     路径: $($prodYml.FullName.Replace($tempDir, ''))" -ForegroundColor Gray
            } else {
                Write-Host "  ❌ 不包含 application-prod.yml" -ForegroundColor Red
            }
        } finally {
            Remove-Item -Recurse -Force $tempDir -ErrorAction SilentlyContinue
        }
    }
    
    Write-Host ""
}

Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "验证完成" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
