# 设置要处理的根目录
$rootDir = "d:\Project\kcloudy\java"

# 定义要处理的文件模式
$filePattern = "application.yml"

# 定义要处理的目录
$directories = @("Web", "WebApi")

# 函数：获取应用名称
function Get-ApplicationId($content) {
    try {
        if ($content -match 'GlobalConfig:[\s\S]*?ApplicationId:\s*([^\r\n]+)') {
            return $matches[1].Trim()
        }
    } catch {
        Write-Host "Warning: Error parsing application id: $_"
    }
    return $null
}

# 函数：获取应用名称
function Get-ApplicationName($content) {
    try {
        if ($content -match 'spring:[\s\S]*?application:[\s\S]*?name:\s*([^\r\n]+)') {
            return $matches[1].Trim()
        }
    } catch {
        Write-Host "Warning: Error parsing application name: $_"
    }
    return $null
}

# 函数：获取应用名称
function Get-ApplicationPort($content) {
    try {
        if ($content -match 'server:[\s\S]*?port:\s*([^\r\n]+)') {
            return $matches[1].Trim()
        }
    } catch {
        Write-Host "Warning: Error parsing application port: $_"
    }
    return $null
}

# 定义标准的配置模板
function Get-StandardConfig($appId, $appName, $appPort) {
    if ([string]::IsNullOrEmpty($appId)) {
        $appId = ""
    }

    $apiName = "$appName" + "接口"
    if ([string]::IsNullOrEmpty($appName)) {
        $appName = "默认应用"
        $apiName = "默认应用接口"
    }
    if ([string]::IsNullOrEmpty($appPort)) {
        $appPort = "80"
    }
    return @"
server:
  port: $appPort
  tomcat:
    uri-encoding: UTF-8
    max-connections: 3000
    max-threads: 1000
  max-http-header-size: 1024000

spring:
  profiles:
    active: dev
  application:
    name: $appName
  main:
    allow-bean-definition-overriding: true
  servlet:
    multipart:
      enabled: true
      max-file-size: 1024000
  devtools:
    restart:
      enabled: true
  jackson:
    default-property-inclusion: non_null
    time-zone: GMT+8
    date-format: yyyy-MM-dd HH:mm:ss
  liquibase:
    enabled: false
  jpa:
    show-sql: true
    hibernate:
      naming:
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    properties:
      hibernate:
        dialect: kc.database.core.CustomSqlServer2012Dialect
        format_sql: true
        hbm2ddl:
          auto: none
        multiTenancy: SCHEMA
        multi_tenant_connection_provider: kc.database.multitenancy.MsSqlMultiTenantConnectionProviderImpl
        tenant_identifier_resolver: kc.database.multitenancy.MultiTenantIdentifierResolver
        globally_quoted_identifiers: true
        enable_lazy_load_no_trans: true
        event:
          merge:
            entity_copy_observer: allow
  output:
    ansi:
      enabled: DETECT
  thymeleaf:
    cache: false
    enabled: true
    prefix: classpath:/templates/
    suffix: .html

logging:
  charset:
    console: UTF-8
  file:
    charset: UTF-8
  level:
    org.springframework.boot.autoconfigure: ERROR

GlobalConfig:
  ApplicationId: $appId
  EncryptKey: KCloudy-Microsoft-EncryptKey

  SSOWebDomain: https://sso.kcloudy.com/
  ResWebDomain: https://resource.kcloudy.com/
  AccWebDomain: https://subdomain.acc.kcloudy.com/

  ClientId: CFWinAPP
  ClientSecret: app_password
  TempFilePath: /tmp,
  UploadConfig:
    ImageMaxSize: 20
    ImageExt: jpg,jpeg,png,gif,bmp
    FileMaxSize: 20
    FileExt: txt,doc,docx,xls,xlsx,ppt,pptx,pdf

swagger:
  clientId: Y0RiYQ==
  clientSecret: MmJmNWIyM2Q5ZjY4OWU5YzFmYWVkZTUwNzY2ZWJkNTg=
  title: $apiName
  description: $apiName

"@
}

# 处理每个目录
foreach ($dir in $directories) {
    $searchPath = Join-Path -Path $rootDir -ChildPath $dir
    
    # 查找所有匹配的文件
    Write-Host "Searching for YAML files in: $searchPath"
    
    # 先查找所有可能的目录
    $searchDirs = @(
        "src\\main\\resources"
    )
    
    $files = @()
    foreach ($dir in $searchDirs) {
        $searchPattern = "*$filePattern"
        $foundFiles = Get-ChildItem -Path $searchPath -Recurse -Filter $searchPattern -File | 
                     Where-Object { $_.FullName -match "[\\/]src[\\/]main[\\/]resources[\\/]" -or $_.FullName -match "[\\/]target[\\/]classes[\\/]" }
        
        if ($foundFiles) {
            $files += $foundFiles
        }
    }
    
    # 去重
    $files = $files | Sort-Object FullName -Unique
    
    if ($files.Count -eq 0) {
        Write-Host "No YAML files found in: $searchPath" -ForegroundColor Yellow
        continue
    }
    
    Write-Host "Found $($files.Count) YAML files to process" -ForegroundColor Cyan
    
    foreach ($file in $files) {
        Write-Host "Processing file: $($file.FullName)"
        
        try {
            Write-Host "`nProcessing: $($file.FullName)"
            
            # 读取文件内容
            $content = Get-Content -Path $file.FullName -Raw -ErrorAction Stop
            
            # 提取应用ID  
            $appId = Get-ApplicationId $content
            if (-not $appId) {
                $appId = [guid]::NewGuid().ToString().ToUpper()
                Write-Host "Generated new app id: $appId"
            } else {
                Write-Host "Found app id: $appId"
            }

            # 提取应用名称
            $appName = Get-ApplicationName $content
            if (-not $appName) {
                $appName = $file.Directory.Parent.Name -replace '^kc\.(web|webapi)\.', ''
                Write-Host "Using default app name: $appName"
            } else {
                Write-Host "Found app name: $appName"
            }

            # 提取应用端口
            $appPort = Get-ApplicationPort $content
            if (-not $appPort) {
                Write-Host "Using default app port: $appPort"
            } else {
                Write-Host "Found app port: $appPort"
            }
            
            Write-Host "Replacing with standard configuration..."
            
            # 生成新的标准配置
            Write-Host "Generating standard configuration..."
            $newContent = Get-StandardConfig $appId $appName $appPort
            
            # 如果是Web模块，删除swagger配置
            if ($file.FullName -match '\\Web\\') {
                Write-Host "Removing swagger configuration for Web module..."
                $newContent = [regex]::Replace($newContent, '(?m)^swagger:(?:\r?\n(?:\s{2,}.*|\s*#.*|\s*))*(?=\S|$)', [String]::Empty, [System.Text.RegularExpressions.RegexOptions]::Singleline)
                # 清理多余的空行
                $newContent = [regex]::Replace($newContent, '(\r?\n){3,}', "`r`n`r`n").Trim()
            }
            
            # 写入新内容
            Write-Host "Writing changes to file..."
            $newContent | Out-File -FilePath $file.FullName -Encoding utf8 -NoNewline -Force
            
            Write-Host "Successfully updated: $($file.Name)" -ForegroundColor Green
        } catch {
            Write-Host "Error processing file $($file.FullName): $_"
        }
    }
}

Write-Host "All files have been processed."
