<#
.SYNOPSIS
    更新Spring Boot应用的数据库配置文件

.DESCRIPTION
    此脚本用于批量更新指定环境(dev/prod)下的数据库连接配置，包括URL、用户名和密码。
    适用于更新多个微服务项目的application-{env}.yml文件。

.PARAMETER Environment
    指定要更新的环境，有效值为 'dev' 或 'prod'
    必填参数

.EXAMPLE
    # 更新开发环境配置
    .\update-db-config.ps1 -Environment dev

.EXAMPLE
    # 更新生产环境配置
    .\update-db-config.ps1 -Environment prod

.NOTES
    作者: 系统管理员
    创建日期: 2025-08-14
    版本: 1.0
#>

param(
    [Parameter(Mandatory=$true, HelpMessage="指定要更新的环境(dev/prod)")]
    [ValidateSet('dev', 'prod', IgnoreCase=$false)]
    [string]$Environment
)

# 设置要搜索的根目录
$rootDirs = @(
    "d:\Project\kcloudy\java\Web",
    "d:\Project\kcloudy\java\WebApi"
)

# 数据库配置 (根据实际需求修改以下配置)
# 开发环境配置
$dbConfigs = @{
    dev = @{
        url = "jdbc:sqlserver://127.0.0.1:1433;databaseName=MSSqlKCContext"
        username = "sa"
        password = "0QVw0yFoX2GuwkMSQyz1tg=="
    }
    # 生产环境配置
    prod = @{
        url = "jdbc:sqlserver://121.89.220.143:1433;databaseName=MSSqlKCContext"
        username = "sa"
        password = "Hcqqkeum+lPvQlPHyHOhM33xffnXWK2P"
    }
}

# 获取指定环境的配置
$config = $dbConfigs[$Environment]
$configFile = "application-${Environment}.yml"

Write-Host "Updating database configuration for $Environment environment..." -ForegroundColor Cyan
Write-Host "URL: $($config.url)"
Write-Host "Username: $($config.username)"
Write-Host "Password: **********"

# 查找所有匹配的配置文件
$files = Get-ChildItem -Path $rootDirs -Recurse -Filter $configFile -File -ErrorAction SilentlyContinue

if ($files.Count -eq 0) {
    Write-Host "No $configFile files found in the specified directories." -ForegroundColor Yellow
    exit 1
}

foreach ($file in $files) {
    Write-Host "`nProcessing file: $($file.FullName)" -ForegroundColor Green
    
    try {
        # 读取文件内容
        $content = Get-Content -Path $file.FullName -Raw -ErrorAction Stop
        
        # 更新数据库配置
        $updatedContent = $content -replace '(?<=spring:\s*\r?\n(?:\s{2,}.*\r?\n)*\s{2,}datasource:\s*\r?\n(?:\s{4,}.*\r?\n)*\s{4,}url:\s*).*', " $($config.url)"
        $updatedContent = $updatedContent -replace '(?<=spring:\s*\r?\n(?:\s{2,}.*\r?\n)*\s{2,}datasource:\s*\r?\n(?:\s{4,}.*\r?\n)*\s{4,}username:\s*).*', " $($config.username)"
        $updatedContent = $updatedContent -replace '(?<=spring:\s*\r?\n(?:\s{2,}.*\r?\n)*\s{2,}datasource:\s*\r?\n(?:\s{4,}.*\r?\n)*\s{4,}password:\s*).*', " $($config.password)"
        
        # 检查是否有变化
        if ($updatedContent -ne $content) {            # 写入更新后的内容
            $updatedContent | Set-Content -Path $file.FullName -Encoding UTF8 -NoNewline -Force
            Write-Host "  Updated database configuration in: $($file.Name)" -ForegroundColor Green
        } else {
            Write-Host "  No database configuration found or already up to date in: $($file.Name)" -ForegroundColor Yellow
        }
    }
    catch {
        Write-Host "  Error processing file: $($file.FullName)" -ForegroundColor Red
        Write-Host "  $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n更新完成! 共处理了 $($files.Count) 个配置文件。" -ForegroundColor Cyan
Write-Host "`n使用说明:"
Write-Host "1. 开发环境: .\update-db-config.ps1 -Environment dev"
Write-Host "2. 生产环境: .\update-db-config.ps1 -Environment prod"
Write-Host "`n注意: 请确保有文件写入权限，并检查备份文件(.bak)确认更改是否符合预期。" -ForegroundColor Yellow
