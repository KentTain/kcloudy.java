# 生成应用的整个流程
## 1. 部署目录结构
```bash
kcloudy.business
|-- 1.Common
|   |-- kc.component
|   |-- kc.database
|   |-- kc.framework
|   |-- kc.service
|   |-- kc.storage
|   |-- kc.thirdparty
|   |-- kc.web
|-- 2.Domain
|   |-- kc.dataaccess.account
|   |-- kc.dataaccess.app
|   |-- kc.dataaccess.codegenerate
|   |-- kc.dataaccess.config
|   |-- kc.dataaccess.dict
|   |-- kc.dataaccess.offering
|   |-- kc.dataaccess.portal
|   |-- kc.dataaccess.training
|-- 3.Service
|   |-- kc.service.account
|   |-- kc.service.app
|   |-- kc.service.codegenerate
|   |-- kc.service.config
|   |-- kc.service.dict
|   |-- kc.service.offering
|   |-- kc.service.portal
|   |-- kc.service.training
|-- 4.Web
|   |-- kc.web.account
|   |-- kc.web.app
|   |-- kc.web.codegenerate
|   |-- kc.web.config
|   |-- kc.web.dict
|   |-- kc.web.offering
|   |-- kc.web.portal
|   |-- kc.web.training
|-- 5.WebApi
|   |-- kc.webapi.account
|   |-- kc.webapi.app
|   |-- kc.webapi.codegenerate
|   |-- kc.webapi.config
|   |-- kc.webapi.dict
|   |-- kc.webapi.offering
|   |-- kc.webapi.portal
|   |-- kc.webapi.training
```
## 2. 部署说明
有三台阿里云的服务器且都有公网IP，使用docker部署相关应用，按以下需求对所有应用进行打包、推送至阿里云仓库，生成一键打包发布shell脚本。
### 2.1 相关服务器的名称、IP及配置目录如下：
| 主机名      | 公网IP       | 内网IP        | 配置文件目录                      | 日志文件目录                      | 证书文件目录                      |
|-------------|--------------|---------------|----------------------------------|----------------------------------|----------------------------------|
| k8s-master  | 121.89.220.143 | 172.23.160.12 | /share/k8s-master/nginx/conf    | /share/k8s-master/nginx/logs    | /share/k8s-master/nginx/certs    |
| k8s-worker01| 47.92.221.208 | 172.23.160.15 | /share/k8s-worker01/nginx/conf  | /share/k8s-worker01/nginx/logs  | /share/k8s-worker01/nginx/certs  |
| k8s-worker02| 47.92.192.48  | 172.23.160.17 | /share/k8s-worker02/nginx/conf  | /share/k8s-worker02/nginx/logs  | /share/k8s-worker02/nginx/certs  |

###	 2.2 相关应用的部署如下：
| 部署服务器   | 应用编码   |应用名称   | 域名/泛域名              | 80端口   | 443端口   | 
|----------|----------|-----------|---------------------------|---------|---------|
| k8s-master  | kc.web.codegenerate | 代码生成应用 | code.kcloudy.com        | 1007  | 1017  | 
| k8s-worker01  | kc.web.config | 配置中心 | *.cfg.kcloudy.com        | 1101  | 1111  | 
| k8s-worker01  | kc.web.dict | 字典管理 | *.dic.kcloudy.com        | 1103  | 1113  | 
| k8s-worker01  | kc.web.app | 应用管理 | *.app.kcloudy.com        | 1105  | 1115  | 
| k8s-worker02  | kc.web.offering | 商品管理 | *.off.kcloudy.com        | 3005  | 3015  | 
| k8s-worker02  | kc.web.account | 用户权限应用 | *.acc.kcloudy.com      | 2001  | 2011  | 
| k8s-worker02  | kc.web.portal | 网站应用 | *.portal.kcloudy.com      | 2007  | 2017  | 
| k8s-worker02  | kc.web.training | 培训应用 | *.training.kcloudy.com      | 6001  | 6011  | 

###	 2.3 相关Api应用的部署如下：
| 部署服务器   | 应用编码   |应用名称   | 域名/泛域名              | 80端口   | 443端口   | 
|----------|----------|-----------|---------------------------|---------|---------|
| k8s-master  | kc.webapi.config | 配置中心Api | *.cfgapi.kcloudy.com        | 1102  | 1112  | 
| k8s-master  | kc.webapi.dict | 字典管理Api | *.dicapi.kcloudy.com        | 1104  | 1114  | 
| k8s-master  | kc.webapi.app | 应用管理Api | *.appapi.kcloudy.com        | 1106  | 1116  | 
| k8s-worker02  | kc.webapi.offering | 商品管理Api | *.offapi.kcloudy.com        | 3006  | 3016  | 
| k8s-worker02  | kc.webapi.account | 用户权限Api | *.accapi.kcloudy.com      | 2002  | 2012  | 
| k8s-worker02  | kc.webapi.portal | 网站管理Api | *.portalapi.kcloudy.com      | 2007  | 2017  | 
| k8s-worker02  | kc.webapi.training | 培训管理Api | *.trainingapi.kcloudy.com      | 6002  | 6012  | 

### 2.4 生成package并推送至阿里云仓库
将Java项目（项目编码）以版本号（版本号）打包至发布目录（D:\Publish\Java\{项目编码}\v-{版本号}），生成Docker镜像并推送到阿里云仓库，具体步骤如下：

#### 2.4.1 设置版本号并打包
* 替换版本号, 会修改pom.xml中的版本号  
mvn clean versions:set -DnewVersion={版本号}

* maven cli 相关参数：https://maven.apache.org/ref/3.8.1/maven-embedder/cli.html  
mvn clean package -pl {项目编码} -am -DskipTests --no-transfer-progress source:jar
  - -pl：针对指定项目
  - -am：并构建所需的项目
  - -DskipTests：采用跳过测试
  - --no-transfer-progress：不显示下载进度
  - source:jar：并包含源码方式进行发布

* 复制Jar包到发布目录  
cp -rf {项目编码}/target/{项目编码}-{版本号}.jar  D:\Publish\Java\{项目编码}\v-{版本号}
* 复制Dockerfile到发布目录  
cp -rf {项目编码}/Dockerfile  D:\Publish\Java\{项目编码}\v-{版本号}
* 复制字体文件到发布目录  
cp -rf Fonts/*  D:\Publish\Java\{项目编码}\v-{版本号}\Fonts

#### 2.4.2 构建Docker镜像
* 进入项目目录  
cd D:\Publish\Java\{项目编码}\v-{版本号}
* 构建Docker镜像  
docker build -t {项目编码}:{版本号} --build-arg env=prod .
* 运行容器  
docker run -d -p 9999:8080 --name {项目编码} {项目编码}:{版本号}

#### 2.4.3 推送Docker镜像到阿里云仓库
* 登录Docker仓库  
docker login -u 用户名 -p 密码 registry.zhangjiakou.aliyuncs.com

* 标记镜像  
docker tag {项目编码}:{版本号} registry.zhangjiakou.aliyuncs.com/kcloudy-java/{项目编码}:{版本号}

* 推送镜像  
docker push registry.zhangjiakou.aliyuncs.com/kcloudy-java/{项目编码}:{版本号}

#### 2.4.4 具体示例
```bash
# 1. 设置版本号并打包
# 1.1 替换版本号, 会修改pom.xml中的版本号，版本号格式为1.0.0.1，备注：执行后所有模块都会修改，以便统一版本号
mvn clean versions:set -DnewVersion=1.0.0.1

# 1.2 清理并打包项目，包含源码，maven cli 相关参数：https://maven.apache.org/ref/3.6.1/maven-embedder/cli.html，-pl：针对指定项目，-am：并构建所需的项目，-DskipTests：采用跳过测试，--no-transfer-progress：不显示下载进度，source:jar：并包含源码方式进行发布
mvn clean package -pl :kc.web.demo -am -DskipTests --no-transfer-progress source:jar

# 1.3 复制Jar包到发布目录
cp ./4.Web/kc.web.demo/target/kc.web.demo-1.0.0.1.jar  D:\Publish\Java\kc.web.demo\v-1.0.0.1
cp ./4.Web/kc.web.demo/Dockerfile  D:\Publish\Java\kc.web.demo\v-1.0.0.1
cp ./Fonts/*  D:\Publish\Java\kc.web.demo\v-1.0.0.1\Fonts

# 2.构建docker镜像
cd D:\Publish\Java\kc.web.demo\v-1.0.0.1
docker build -t kc.web.demo:1.0.0.1  --build-arg env=prod .
docker run -d -p 9999:9999 -p 10000:10000 --name kc.web.demo kc.web.demo:1.0.0.1

# 3.推送docker镜像
docker tag kc.web.demo:1.0.0.1 registry.cn-zhangjiakou.aliyuncs.com/kcloudy-java/kc.web.demo:1.0.0.1
docker push registry.cn-zhangjiakou.aliyuncs.com/kcloudy-java/kc.web.demo:1.0.0.1

```


# maven清除lastUpdated文件
```bash
find /mnt/maven/repository/ -name "*.lastUpdated" -exec grep -q "Could not transfer" {} \; -print -exec rm {} \;

```

# java代码格式设置规则
## 1.方法调用参数换行设置：
* 每个参数单独占一行
* 参数对齐方式设置为16个空格
* 在第一个参数前换行
## 2.注解换行设置：
* 每个注解单独占一行
* 注解参数过多时会自动换行
* 注解参数对齐方式设置为16个空格
## 3.Lambda 表达式格式设置：
* Lambda 表达式的大括号不换行
* Lambda 参数和箭头在同一行
* Lambda 主体如果是单行则保持单行
## 4.大括号不换行设置：
* 匿名类、代码块、构造函数、枚举常量、方法声明、switch语句和类型声明的大括号都不换行
## 5.逗号后换行：
* 在逗号后换行
* 保持参数对齐
## 6.其他格式设置：
* 在逗号后添加空格
* 保持代码整洁和一致性
* 超长注释不自动换行
## 7.案例
```java
result = WebSendGet(
		new TypeReference<ServiceResult<Boolean>>() {}, 
		ServiceName + ".ChangePasswordAsync", 
		AccountApiUrl() + "AccountApi/ChangePasswordAsync?userId=" + userId + "&currentPassword=" + currentPassword + "&newPassword=" + newPassword, ApplicationConstant.AccScope,
		callback -> {
			return callback;
		}, 
		failCallback -> {
			log.error(ServiceName + " throw error: " + failCallback.toString());
		}, 
		true);

@lombok.extern.slf4j.Slf4j
@RequestMapping("/ConfigManager")
@MenuAnnotation(ParentMenuName = "配置管理", MenuName = "配置管理", Url = "/ConfigManager/Index",
		Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
		SmallIcon = "fa fa-file-code-o", AuthorityId = "7D931A51-18DF-439D-BBE9-173576711980",
		DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 2)

		//        if (!ModelState.IsValid)
		//            return new ServiceResult<Boolean>(ServiceResultType.ParamError, "数据有误,请重新输入." );

```