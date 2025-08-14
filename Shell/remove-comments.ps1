# 设置要处理的根目录
$rootDir = "d:\Project\kcloudy\java"

# 定义要处理的文件模式
$filePatterns = @("application.yml", "application-dev.yml", "application-prod.yml")

# 定义要处理的目录
$directories = @("Web", "WebApi")

# 处理每个目录
foreach ($dir in $directories) {
    $searchPath = Join-Path -Path $rootDir -ChildPath $dir
    
    # 查找所有匹配的文件
    $files = Get-ChildItem -Path $searchPath -Recurse -Include $filePatterns -File
    
    foreach ($file in $files) {
        Write-Host "Processing file: $($file.FullName)"
        
        # 读取文件内容
        $content = Get-Content -Path $file.FullName -Raw
        
        # 删除注释行（以#开头的行）和内联注释
        $lines = $content -split "`r`n"
        $newContent = @()
        foreach ($line in $lines) {
            # 删除整行注释
            if ($line -match '^\s*#') {
                continue
            }
            # 删除行尾注释
            $line = $line -replace '\s*#.*$', ''
            $newContent += $line
        }
        $newContent = $newContent -join "`r`n"
        
        # 写入新内容
        Set-Content -Path $file.FullName -Value $newContent.Trim()
        
        Write-Host "Removed comments from: $($file.Name)"
    }
}

Write-Host "All files have been processed."
