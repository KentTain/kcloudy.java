# Java项目构建和部署脚本
## 1. 脚本目录结构
```bash
kcloudy.business
|-- Shell
|   |-- build-java-web.ps1     # 构建 Java Web 应用并部署到 Docker 容器
|   |-- update-files.ps1       # 统一管理项目标准配置文件，包括：Dockerfile、logback.xml
|   |-- clear-lastupdated.bat   # 清理 Maven 本地仓库中的临时文件
|   |-- jenkins-build.sh       # Jenkins 持续集成/持续部署脚本
```
## 2. 脚本说明
### 2.1 build-java-web.ps1
#### 功能说明
构建和部署 Java Web 应用到 Docker 容器

#### 参数说明
* `solutionType`：解决方案类型，默认为 "Web"
* `solutionName`：需要构建的解决方案名称
* `versionNum`：版本号
* `httpPort`：容器运行的 HTTP 端口
* `httpsPort`：容器运行的 HTTPS 端口，默认为 0（不启用）
* `env`：部署环境，默认为 "Production"

#### 使用示例
```powershell
.\build-java-web.ps1 -solutionType "Web" -solutionName "kc.web.account" -versionNum 1 -httpPort 2001 -httpsPort 0 -env "Production"
```

## 2. update-files.ps1
#### 功能说明
将标准文件（如 Dockerfile、logback.xml 等）从模板项目复制到其他项目

#### 参数说明
* `solutionType`：解决方案类型，默认为 "Web"
* `solutionName`：需要更新文件的目标解决方案名称
* `fileFullPath`：需要复制的标准文件路径，例如 "Dockerfile" 或 "src\main\resources\logback.xml"
* `isLowercase`：是否将项目名称转换为小写，默认为 false

### 使用示例
```powershell
# 更新 Dockerfile
.\update-files.ps1 -solutionType "Web" -solutionName "kc.web.account" -fileFullPath "Dockerfile" -isLowercase $true

# 更新 logback.xml
.\update-files.ps1 -solutionType "Web" -solutionName "kc.web.account" -fileFullPath "src\main\resources\logback.xml" -isLowercase $true
```

## 3. clearLastUpdated.bat
#### 功能说明
清除 Maven 本地仓库中的 .lastUpdated 文件，解决 Maven 依赖更新问题

#### 使用说明
1. 修改脚本中的 `REPOSITORY_PATH` 变量为您的 Maven 本地仓库路径
2. 直接运行脚本即可

#### 示例
```batch
set REPOSITORY_PATH=D:\Java\repository
rem 正在搜索...
for /f "delims=" %%i in ('dir /b /s "%REPOSITORY_PATH%\*lastUpdated*"') do (
    del /s /q %%i
)
rem 搜索完毕
pause
```

## 4. jenkins.sh

### 功能说明
Jenkins 构建脚本，用于自动化构建和部署项目

### 功能概述
1. 构建 kcloudy.demo.core 项目并发布到 Nexus 仓库
2. 构建 kcloudy.demo.web 项目并发布到本地路径
3. 生成 Docker 镜像

### 参数说明
* `env`：发布环境
* `port`：运行端口

### 环境变量
* `JENKINS_HOME`：Jenkins 主目录
* `JOB_NAME`：当前任务名称

### 版本控制
* 新版本号：1.0.0.$BUILD_NUMBER
* 旧版本号：1.0.0.$(($BUILD_NUMBER-1))
