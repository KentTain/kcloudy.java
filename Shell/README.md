# Java项目构建和部署脚本
## 1. 脚本目录结构
```bash
kcloudy.java
|-- Shell
|   |-- update-application-config.ps1       # 统一管理项目application.yml配置文件
|   |-- update-db-config.ps1       # 更新application.yml中数据库配置文件
|   |-- update-files.ps1       # 统一管理项目标准配置文件，包括：Dockerfile、logback.xml
|   |-- build-all-webs.ps1     # 构建所有 Java Web 应用并部署到 Docker 容器
|   |-- build-java-web.ps1     # 构建 Java Web 应用并部署到 Docker 容器
|   |-- clear-lastupdated.bat  # 清理 Maven 本地仓库中的临时文件
|   |-- jenkins-build.sh       # Jenkins 持续集成/持续部署脚本
```
## 2. 脚本说明
### 2.1 update-application-config.ps1
#### 功能说明
统一管理项目中的 application.yml 配置文件，自动生成或更新标准化的 Spring Boot 应用配置。

#### 功能特点
- 自动生成唯一的 ApplicationId（如果不存在）
- 统一配置服务器端口、Spring 相关设置、Swagger 文档配置等
- 支持自定义应用名称、API 名称和端口号
- 自动处理多租户数据库配置

#### 使用说明
```powershell
# 直接运行脚本，会自动处理所有 Web 和 WebApi 目录下的 application.yml 文件
.\update-application-config.ps1
```

### 2.2 update-db-config.ps1
#### 功能说明
批量更新指定环境(dev/prod)下的数据库连接配置，包括数据库URL、用户名和密码。

#### 参数说明
* `-Environment`：指定要更新的环境，有效值为 'dev' 或 'prod'（必填）

#### 使用示例
```powershell
# 更新开发环境数据库配置
.\update-db-config.ps1 -Environment dev

# 更新生产环境数据库配置
.\update-db-config.ps1 -Environment prod
```

### 2.3 build-all-webs.ps1
#### 功能说明
批量构建并部署所有启用的 Java Web 应用到 Docker 容器。该脚本会自动遍历预定义的项目列表，并为每个启用的项目调用 build-java-web.ps1 脚本进行构建和部署。

#### 功能特点
- 支持配置多个项目的构建参数（项目名称、HTTP/HTTPS 端口等）
- 可以单独控制每个项目的启用/禁用状态
- 自动处理构建过程中的异常，确保一个项目构建失败不会影响其他项目
- 提供清晰的构建状态反馈（成功/失败）

#### 预定义项目列表
脚本中预定义了以下项目及其默认端口：
- kc.web.account (2001)
- kc.web.app (1105)
- kc.web.codegenerate (1007, 默认禁用)
- kc.web.config (1101)
- kc.web.dict (1103)
- kc.web.offering (3005)
- kc.web.portal (2007, 默认禁用)
- kc.web.training (6001, 默认禁用)

#### 使用说明
```powershell
# 直接运行脚本，将构建所有启用的项目
.\build-all-webs.ps1
```

### 2.4 update-files.ps1
#### 功能说明
将标准文件（如 Dockerfile、logback.xml 等）从模板项目（Web目录下的 kc.web.account 项目）复制到其他项目

#### 参数说明
* `solutionType`：解决方案类型，默认为 "Web"
* `solutionName`：需要更新文件的目标解决方案名称
* `fileFullPath`：需要复制的标准文件路径，例如 "Dockerfile" 或 "src\main\resources\logback.xml"
* `isLowercase`：是否将项目名称转换为小写，默认为 false

### 使用示例
```powershell
# 使用函数Update-Files，更新 Dockerfile
Update-Files -solutionType "Web" -solutionName "kc.web.account" -fileFullPath "Dockerfile" -isLowercase $true

# 使用函数Update-Files，更新 logback.xml
Update-Files -solutionType "Web" -solutionName "kc.web.account" -fileFullPath "src\main\resources\logback.xml" -isLowercase $true

# 使用Main方法，更新所有 Dockerfile、logback.xml
.\update-files.ps1
```

### 2.5 build-java-web.ps1
#### 功能说明
构建和部署 Java Web 应用到 Docker 容器

#### 参数说明
* `solutionType`：解决方案类型，默认为 "Web"
* `solutionName`：需要构建的解决方案名称
* `versionNum`：版本号
* `httpPort`：容器运行的 HTTP 端口
* `httpsPort`：容器运行的 HTTPS 端口，默认为 0（不启用）
* `env`：部署环境，默认为 "prod"

#### 使用示例
```powershell
# 使用脚本build-java-web.ps1，构建及部署应用
build-java-web.ps1 -solutionType "Web" -solutionName "kc.web.account" -versionNum 1 -httpPort 2001 -httpsPort 0 -env "prod"
```

### 2.6 clear-lastupdated.bat
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

### 2.7 jenkins-build.sh

#### 功能说明
Jenkins 构建脚本，用于自动化构建和部署项目

#### 功能概述
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
