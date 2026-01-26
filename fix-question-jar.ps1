$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$jarPath = Join-Path $root "docker\\jars\\question-service.jar"

if (!(Test-Path $jarPath)) {
    throw "未找到 jar: $jarPath"
}

$tmpDir = Join-Path $env:TEMP ("qs-fix-" + [guid]::NewGuid().ToString())
New-Item -ItemType Directory -Force -Path $tmpDir | Out-Null

$tmpJarPath = Join-Path $tmpDir "question-service.fixed.jar"
Copy-Item -Force $jarPath $tmpJarPath

Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

$zip = [System.IO.Compression.ZipFile]::Open($tmpJarPath, [System.IO.Compression.ZipArchiveMode]::Update)
try {
    $entry = $zip.GetEntry("META-INF/MANIFEST.MF")
    if ($null -eq $entry) {
        throw "jar 中未找到 META-INF/MANIFEST.MF"
    }

    $reader = New-Object System.IO.StreamReader($entry.Open(), [System.Text.Encoding]::ASCII)
    $manifest = $reader.ReadToEnd()
    $reader.Dispose()

    $manifest = $manifest -replace "(\r?\n) ", ""
    $manifest = $manifest -replace "fun\.javierchen\.jcsmarterojbackendquestionservice", "fun.javierchen.jcojbackendquestionservice"
    if ($manifest -notmatch "fun\.javierchen\.jcojbackendquestionservice") {
        throw "Start-Class 替换失败"
    }

    $lines = $manifest -split "\r?\n"
    $newLines = New-Object System.Collections.Generic.List[string]
    foreach ($line in $lines) {
        if ($line -like "Start-Class:*") {
            $prefix = "Start-Class: "
            $value = $line.Substring($prefix.Length)
            $maxFirst = 70 - $prefix.Length
            if ($value.Length -le $maxFirst) {
                $newLines.Add($line)
            } else {
                $newLines.Add($prefix + $value.Substring(0, $maxFirst))
                $rest = $value.Substring($maxFirst)
                while ($rest.Length -gt 0) {
                    $take = [Math]::Min(69, $rest.Length)
                    $newLines.Add(" " + $rest.Substring(0, $take))
                    $rest = $rest.Substring($take)
                }
            }
        } else {
            $newLines.Add($line)
        }
    }

    $manifest = ($newLines -join "`r`n").TrimEnd() + "`r`n`r`n"

    $entry.Delete()
    $newEntry = $zip.CreateEntry("META-INF/MANIFEST.MF", [System.IO.Compression.CompressionLevel]::Optimal)
    $writer = New-Object System.IO.StreamWriter($newEntry.Open(), [System.Text.Encoding]::ASCII)
    $writer.Write($manifest)
    $writer.Dispose()
} finally {
    $zip.Dispose()
}

Move-Item -Force $tmpJarPath $jarPath

Write-Output "已修复 question-service.jar 的 Start-Class"
