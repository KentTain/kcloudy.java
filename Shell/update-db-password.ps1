# 设置要搜索的根目录
$rootDirs = @(
    "d:\Project\kcloudy\java\Web",
    "d:\Project\kcloudy\java\WebApi"
)

# 数据库配置
$dbConfig = @{
    url = "jdbc:sqlserver://121.89.220.143,1433;databaseName=MSSqlKCContext"
    username = "sa"
    password = "Hcqqkeum+lPvQlPHyHOhM33xffnXWK2P"
}

# 查找所有 application-prod.yml 文件
$files = Get-ChildItem -Path $rootDirs -Recurse -Filter "application-prod.yml" -File

foreach ($file in $files) {
    Write-Host "Processing file: $($file.FullName)"
    
    # 读取文件内容
    $content = Get-Content -Path $file.FullName -Raw
    
    # 更新数据库配置
    $updatedContent = $content -replace '(?<=spring:\s*\r?\n(?:\s{2,}.*\r?\n)*\s{2,}datasource:\s*\r?\n(?:\s{4,}.*\r?\n)*\s{4,}url:\s*).*', " $($dbConfig.url)"
    $updatedContent = $updatedContent -replace '(?<=spring:\s*\r?\n(?:\s{2,}.*\r?\n)*\s{2,}datasource:\s*\r?\n(?:\s{4,}.*\r?\n)*\s{4,}username:\s*).*', " $($dbConfig.username)"
    $updatedContent = $updatedContent -replace '(?<=spring:\s*\r?\n(?:\s{2,}.*\r?\n)*\s{2,}datasource:\s*\r?\n(?:\s{4,}.*\r?\n)*\s{4,}password:\s*).*', " $($dbConfig.password)"
    
    # 检查是否有变化
    if ($updatedContent -ne $content) {
        # 写入更新后的内容
        $updatedContent | Set-Content -Path $file.FullName -Encoding UTF8 -NoNewline
        Write-Host "  Updated database configuration in: $($file.Name)"
    } else {
        Write-Host "  No database configuration found or already updated in: $($file.Name)" -ForegroundColor Yellow
    }
}

Write-Host "\nUpdate completed!" -ForegroundColor Green
