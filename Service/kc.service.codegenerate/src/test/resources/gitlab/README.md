# lowcode-backend-generate

## 工程简介

代码生成基于lowcode-backend-generate项目实现。

## 功能

## 项目结构
 D:.
├─.idea
│  └─shelf
│      ├─Uncommitted_changes_before_Checkout_at_2022_4_19_13_03_[Changes]
│      ├─Uncommitted_changes_before_Update_at_2022_4_19_13_08_[Changes]
│      └─Uncommitted_changes_before_Update_at_2022_4_20_17_18_[Changes]
├─bin
├─doc
├─lowcode-generate-adapter
│  ├─lowcode-generate-adapter-api
│  │  ├─src
│  │  │  └─main
│  │  │      └─java
│  │  │          └─com
│  │  │              └─oppo
│  │  │                  └─gcommon
│  │  │                      └─service
│  │  └─target
│  │      ├─classes
│  │      │  └─com
│  │      │      └─oppo
│  │      │          └─gcommon
│  │      │              └─service
│  │      ├─generated-sources
│  │      │  └─annotations
│  │      └─maven-status
│  │          └─maven-compiler-plugin
│  │              └─compile
│  │                  └─default-compile
│  ├─lowcode-generate-adapter-default
│  │  ├─src
│  │  │  └─main
│  │  │      └─java
│  │  │          └─com
│  │  │              └─flydiy
│  │  │                  └─gcommon
│  │  │                      └─service
│  │  │                          └─impl
│  │  └─target
│  │      ├─classes
│  │      │  └─com
│  │      │      └─flydiy
│  │      │          └─gcommon
│  │      │              └─service
│  │      │                  └─impl
│  │      ├─generated-sources
│  │      │  └─annotations
│  │      └─maven-status
│  │          └─maven-compiler-plugin
│  │              └─compile
│  │                  └─default-compile
│  ├─lowcode-generate-adapter-oppo
│  │  └─src
│  │      └─main
│  │          └─java
│  │              └─com
│  │                  └─oppo
│  │                      └─gcommon
│  │                          ├─config
│  │                          └─service
│  │                              └─impl
│  └─target
│      └─generated-sources
│          └─annotations
├─lowcode-generate-core
│  ├─src
│  │  └─main
│  │      ├─java
│  │      │  ├─cn
│  │      │  │  └─flydiy
│  │      │  │      ├─authmanager
│  │      │  │      │  ├─controller
│  │      │  │      │  ├─dto
│  │      │  │      │  ├─entity
│  │      │  │      │  ├─repository
│  │      │  │      │  │  └─impl
│  │      │  │      │  ├─runner
│  │      │  │      │  ├─service
│  │      │  │      │  ├─util
│  │      │  │      │  └─vo
│  │      │  │      ├─cloud
│  │      │  │      │  └─git
│  │      │  │      │      ├─config
│  │      │  │      │      ├─database
│  │      │  │      │      ├─dto
│  │      │  │      │      ├─harbor
│  │      │  │      │      │  ├─client
│  │      │  │      │      │  ├─dto
│  │      │  │      │      │  └─service
│  │      │  │      │      ├─rancher
│  │      │  │      │      │  ├─client
│  │      │  │      │      │  ├─dto
│  │      │  │      │      │  │  └─enums
│  │      │  │      │      │  └─websocket
│  │      │  │      │      ├─service
│  │      │  │      │      └─util
│  │      │  │      ├─codegenerate
│  │      │  │      │  ├─constant
│  │      │  │      │  ├─controller
│  │      │  │      │  ├─dto
│  │      │  │      │  │  ├─front
│  │      │  │      │  │  └─server
│  │      │  │      │  │      └─metadatavo
│  │      │  │      │  ├─handle
│  │      │  │      │  │  ├─front
│  │      │  │      │  │  ├─metadata
│  │      │  │      │  │  └─server
│  │      │  │      │  ├─service
│  │      │  │      │  │  ├─impl
│  │      │  │      │  │  └─metadata
│  │      │  │      │  └─utils
│  │      │  │      ├─config
│  │      │  │      ├─core
│  │      │  │      │  └─entity
│  │      │  │      ├─exception
│  │      │  │      │  └─errors
│  │      │  │      │      ├─e400
│  │      │  │      │      └─e500
│  │      │  │      ├─fdp
│  │      │  │      │  ├─cache
│  │      │  │      │  ├─code
│  │      │  │      │  │  ├─data
│  │      │  │      │  │  ├─enumeration
│  │      │  │      │  │  ├─model
│  │      │  │      │  │  │  └─plugin
│  │      │  │      │  │  └─produce
│  │      │  │      │  │      └─java
│  │      │  │      │  ├─controller
│  │      │  │      │  ├─dto
│  │      │  │      │  ├─entity
│  │      │  │      │  │  ├─base
│  │      │  │      │  │  ├─enumeration
│  │      │  │      │  │  └─listener
│  │      │  │      │  ├─listener
│  │      │  │      │  ├─meta
│  │      │  │      │  ├─param
│  │      │  │      │  │  ├─enumeration
│  │      │  │      │  │  └─handler
│  │      │  │      │  ├─repository
│  │      │  │      │  ├─service
│  │      │  │      │  └─util
│  │      │  │      ├─fdpbackend
│  │      │  │      │  ├─config
│  │      │  │      │  ├─controller
│  │      │  │      │  ├─dto
│  │      │  │      │  ├─entity
│  │      │  │      │  │  └─enums
│  │      │  │      │  ├─listener
│  │      │  │      │  ├─proxy
│  │      │  │      │  ├─repository
│  │      │  │      │  ├─service
│  │      │  │      │  ├─target
│  │      │  │      │  ├─test
│  │      │  │      │  │  └─target
│  │      │  │      │  └─util
│  │      │  │      ├─fontmicroflow
│  │      │  │      │  ├─cache
│  │      │  │      │  ├─controller
│  │      │  │      │  ├─entity
│  │      │  │      │  ├─repository
│  │      │  │      │  └─service
│  │      │  │      ├─frdp
│  │      │  │      │  ├─aop
│  │      │  │      │  │  └─annotation
│  │      │  │      │  ├─cache
│  │      │  │      │  ├─client
│  │      │  │      │  ├─common
│  │      │  │      │  ├─controller
│  │      │  │      │  ├─entity
│  │      │  │      │  │  ├─enumerate
│  │      │  │      │  │  └─primarykey
│  │      │  │      │  ├─listener
│  │      │  │      │  ├─repository
│  │      │  │      │  ├─schedule
│  │      │  │      │  ├─service
│  │      │  │      │  │  └─impl
│  │      │  │      │  └─util
│  │      │  │      ├─generate
│  │      │  │      │  ├─aop
│  │      │  │      │  ├─cache
│  │      │  │      │  ├─code
│  │      │  │      │  │  ├─data
│  │      │  │      │  │  ├─plugin
│  │      │  │      │  │  │  └─model
│  │      │  │      │  │  └─produce
│  │      │  │      │  │      ├─java
│  │      │  │      │  │      │  └─extend
│  │      │  │      │  │      └─xml
│  │      │  │      │  ├─constant
│  │      │  │      │  ├─controller
│  │      │  │      │  ├─dialect
│  │      │  │      │  │  ├─highgo
│  │      │  │      │  │  ├─oralce
│  │      │  │      │  │  ├─postgresql
│  │      │  │      │  │  └─sqlserver
│  │      │  │      │  ├─dto
│  │      │  │      │  ├─entity
│  │      │  │      │  │  └─enums
│  │      │  │      │  ├─enumtype
│  │      │  │      │  ├─git
│  │      │  │      │  │  └─handler
│  │      │  │      │  │      ├─all
│  │      │  │      │  │      ├─front
│  │      │  │      │  │      └─server
│  │      │  │      │  │          └─module
│  │      │  │      │  ├─listener
│  │      │  │      │  ├─param
│  │      │  │      │  ├─repository
│  │      │  │      │  ├─service
│  │      │  │      │  │  └─projectClone
│  │      │  │      │  ├─task
│  │      │  │      │  └─util
│  │      │  │      ├─interceptor
│  │      │  │      ├─microflows
│  │      │  │      │  ├─client
│  │      │  │      │  ├─constant
│  │      │  │      │  │  ├─method
│  │      │  │      │  │  └─utilClass
│  │      │  │      │  ├─controller
│  │      │  │      │  ├─dto
│  │      │  │      │  │  └─component
│  │      │  │      │  ├─model
│  │      │  │      │  │  └─job
│  │      │  │      │  │      ├─entity
│  │      │  │      │  │      ├─repository
│  │      │  │      │  │      │  └─clouddata
│  │      │  │      │  │      └─view
│  │      │  │      │  ├─service
│  │      │  │      │  ├─util
│  │      │  │      │  └─warn
│  │      │  │      ├─microservice
│  │      │  │      │  ├─constant
│  │      │  │      │  ├─controller
│  │      │  │      │  ├─dto
│  │      │  │      │  ├─entity
│  │      │  │      │  ├─mapping
│  │      │  │      │  ├─repository
│  │      │  │      │  ├─service
│  │      │  │      │  │  ├─handle
│  │      │  │      │  │  │  └─nodetype
│  │      │  │      │  │  └─impl
│  │      │  │      │  └─util
│  │      │  │      └─ws2
│  │      │  │          ├─impl
│  │      │  │          └─vo
│  │      │  ├─com
│  │      │  │  └─oppo
│  │      │  │      └─gcommon
│  │      │  │          ├─config
│  │      │  │          ├─convert
│  │      │  │          ├─dao
│  │      │  │          ├─enums
│  │      │  │          ├─exception
│  │      │  │          ├─interceptor
│  │      │  │          ├─pojo
│  │      │  │          │  ├─dto
│  │      │  │          │  ├─form
│  │      │  │          │  ├─po
│  │      │  │          │  ├─query
│  │      │  │          │  └─vo
│  │      │  │          ├─repository
│  │      │  │          ├─service
│  │      │  │          │  └─impl
│  │      │  │          ├─starter
│  │      │  │          │  └─base
│  │      │  │          │      ├─errorcode
│  │      │  │          │      └─exception
│  │      │  │          ├─util
│  │      │  │          │  ├─bean
│  │      │  │          │  └─ExcelUtil
│  │      │  │          └─web
│  │      │  └─org
│  │      │      └─gitlab
│  │      │          └─api
│  │      ├─resources
│  │      │  ├─config
│  │      │  │  └─i18n
│  │      │  ├─excel
│  │      │  ├─liquibase
│  │      │  ├─logback
│  │      │  └─mapper
│  │      └─webapp
│  │          ├─public
│  │          └─test
│  └─target
│      ├─classes
│      │  ├─cn
│      │  │  └─flydiy
│      │  │      ├─authmanager
│      │  │      │  ├─controller
│      │  │      │  ├─dto
│      │  │      │  ├─entity
│      │  │      │  ├─repository
│      │  │      │  │  └─impl
│      │  │      │  ├─runner
│      │  │      │  ├─service
│      │  │      │  ├─util
│      │  │      │  └─vo
│      │  │      ├─cloud
│      │  │      │  └─git
│      │  │      │      ├─config
│      │  │      │      ├─database
│      │  │      │      ├─dto
│      │  │      │      ├─harbor
│      │  │      │      │  ├─client
│      │  │      │      │  ├─dto
│      │  │      │      │  └─service
│      │  │      │      ├─rancher
│      │  │      │      │  ├─client
│      │  │      │      │  ├─dto
│      │  │      │      │  │  └─enums
│      │  │      │      │  └─websocket
│      │  │      │      ├─service
│      │  │      │      └─util
│      │  │      ├─codegenerate
│      │  │      │  ├─constant
│      │  │      │  ├─controller
│      │  │      │  ├─dto
│      │  │      │  │  ├─front
│      │  │      │  │  └─server
│      │  │      │  │      └─metadatavo
│      │  │      │  ├─handle
│      │  │      │  │  ├─front
│      │  │      │  │  ├─metadata
│      │  │      │  │  └─server
│      │  │      │  ├─service
│      │  │      │  │  ├─impl
│      │  │      │  │  └─metadata
│      │  │      │  └─utils
│      │  │      ├─config
│      │  │      ├─core
│      │  │      │  └─entity
│      │  │      ├─exception
│      │  │      │  └─errors
│      │  │      │      ├─e400
│      │  │      │      └─e500
│      │  │      ├─fdp
│      │  │      │  ├─cache
│      │  │      │  ├─code
│      │  │      │  │  ├─data
│      │  │      │  │  ├─enumeration
│      │  │      │  │  ├─model
│      │  │      │  │  │  └─plugin
│      │  │      │  │  └─produce
│      │  │      │  │      └─java
│      │  │      │  ├─controller
│      │  │      │  ├─dto
│      │  │      │  ├─entity
│      │  │      │  │  ├─base
│      │  │      │  │  ├─enumeration
│      │  │      │  │  └─listener
│      │  │      │  ├─listener
│      │  │      │  ├─meta
│      │  │      │  ├─param
│      │  │      │  │  ├─enumeration
│      │  │      │  │  └─handler
│      │  │      │  ├─repository
│      │  │      │  ├─service
│      │  │      │  └─util
│      │  │      ├─fdpbackend
│      │  │      │  ├─controller
│      │  │      │  ├─dto
│      │  │      │  ├─entity
│      │  │      │  │  └─enums
│      │  │      │  ├─listener
│      │  │      │  ├─proxy
│      │  │      │  ├─repository
│      │  │      │  ├─service
│      │  │      │  ├─target
│      │  │      │  ├─test
│      │  │      │  │  └─target
│      │  │      │  └─util
│      │  │      ├─fontmicroflow
│      │  │      │  ├─cache
│      │  │      │  ├─controller
│      │  │      │  ├─entity
│      │  │      │  ├─repository
│      │  │      │  └─service
│      │  │      ├─frdp
│      │  │      │  ├─aop
│      │  │      │  │  └─annotation
│      │  │      │  ├─cache
│      │  │      │  ├─client
│      │  │      │  ├─common
│      │  │      │  ├─controller
│      │  │      │  ├─entity
│      │  │      │  │  ├─enumerate
│      │  │      │  │  └─primarykey
│      │  │      │  ├─listener
│      │  │      │  ├─repository
│      │  │      │  ├─schedule
│      │  │      │  ├─service
│      │  │      │  │  └─impl
│      │  │      │  └─util
│      │  │      ├─generate
│      │  │      │  ├─aop
│      │  │      │  ├─cache
│      │  │      │  ├─code
│      │  │      │  │  ├─data
│      │  │      │  │  ├─plugin
│      │  │      │  │  │  └─model
│      │  │      │  │  └─produce
│      │  │      │  │      ├─java
│      │  │      │  │      └─xml
│      │  │      │  ├─constant
│      │  │      │  ├─controller
│      │  │      │  ├─dialect
│      │  │      │  │  ├─highgo
│      │  │      │  │  ├─oralce
│      │  │      │  │  ├─postgresql
│      │  │      │  │  └─sqlserver
│      │  │      │  ├─dto
│      │  │      │  ├─entity
│      │  │      │  │  └─enums
│      │  │      │  ├─enumtype
│      │  │      │  ├─git
│      │  │      │  │  └─handler
│      │  │      │  │      ├─all
│      │  │      │  │      ├─front
│      │  │      │  │      └─server
│      │  │      │  │          └─module
│      │  │      │  ├─listener
│      │  │      │  ├─param
│      │  │      │  ├─repository
│      │  │      │  ├─service
│      │  │      │  │  └─projectClone
│      │  │      │  ├─task
│      │  │      │  └─util
│      │  │      ├─interceptor
│      │  │      ├─microflows
│      │  │      │  ├─client
│      │  │      │  ├─constant
│      │  │      │  │  ├─method
│      │  │      │  │  └─utilClass
│      │  │      │  ├─controller
│      │  │      │  ├─dto
│      │  │      │  │  └─component
│      │  │      │  ├─model
│      │  │      │  │  └─job
│      │  │      │  │      ├─entity
│      │  │      │  │      ├─repository
│      │  │      │  │      │  └─clouddata
│      │  │      │  │      └─view
│      │  │      │  ├─service
│      │  │      │  ├─util
│      │  │      │  └─warn
│      │  │      ├─microservice
│      │  │      │  ├─constant
│      │  │      │  ├─controller
│      │  │      │  ├─dto
│      │  │      │  ├─entity
│      │  │      │  ├─mapping
│      │  │      │  ├─repository
│      │  │      │  ├─service
│      │  │      │  │  ├─handle
│      │  │      │  │  │  └─nodetype
│      │  │      │  │  └─impl
│      │  │      │  └─util
│      │  │      └─ws2
│      │  │          ├─impl
│      │  │          └─vo
│      │  ├─com
│      │  │  └─oppo
│      │  │      └─gcommon
│      │  │          ├─convert
│      │  │          ├─dao
│      │  │          ├─enums
│      │  │          ├─exception
│      │  │          ├─pojo
│      │  │          │  ├─dto
│      │  │          │  ├─form
│      │  │          │  ├─po
│      │  │          │  ├─query
│      │  │          │  └─vo
│      │  │          ├─repository
│      │  │          ├─service
│      │  │          │  └─impl
│      │  │          ├─starter
│      │  │          │  └─base
│      │  │          │      ├─errorcode
│      │  │          │      └─exception
│      │  │          ├─util
│      │  │          │  ├─bean
│      │  │          │  └─ExcelUtil
│      │  │          └─web
│      │  ├─config
│      │  │  └─i18n
│      │  ├─excel
│      │  ├─liquibase
│      │  ├─logback
│      │  ├─mapper
│      │  ├─META-INF
│      │  └─org
│      │      └─gitlab
│      │          └─api
│      ├─generated-sources
│      │  └─annotations
│      │      └─cn
│      │          └─flydiy
│      │              ├─authmanager
│      │              │  └─entity
│      │              ├─core
│      │              │  └─entity
│      │              ├─fdp
│      │              │  └─entity
│      │              │      └─base
│      │              ├─fdpbackend
│      │              │  └─entity
│      │              ├─fontmicroflow
│      │              │  └─entity
│      │              ├─frdp
│      │              │  └─entity
│      │              ├─generate
│      │              │  └─entity
│      │              ├─microflows
│      │              │  └─model
│      │              │      └─job
│      │              │          └─entity
│      │              └─microservice
│      │                  ├─entity
│      │                  └─mapping
│      └─maven-status
│          └─maven-compiler-plugin
│              └─compile
│                  └─default-compile
├─lowcode-generate-start
│  ├─src
│  │  ├─main
│  │  │  └─java
│  │  │      └─cn
│  │  │          └─flydiy
│  │  └─test
│  │      ├─java
│  │      │  ├─cn
│  │      │  │  └─flydiy
│  │      │  │      ├─fdp
│  │      │  │      │  ├─service
│  │      │  │      │  └─util
│  │      │  │      ├─fdpbackend
│  │      │  │      │  └─service
│  │      │  │      ├─generate
│  │      │  │      │  └─util
│  │      │  │      ├─microflows
│  │      │  │      │  └─service
│  │      │  │      └─microservice
│  │      │  │          ├─mapping
│  │      │  │          ├─service
│  │      │  │          │  └─impl
│  │      │  │          └─util
│  │      │  └─com
│  │      │      └─oppo
│  │      │          └─gcommon
│  │      │              ├─enums
│  │      │              ├─service
│  │      │              └─util
│  │      └─resources
│  │          └─Excel
│  └─target
│      ├─classes
│      │  └─cn
│      │      └─flydiy
│      ├─generated-sources
│      │  └─annotations
│      ├─generated-test-sources
│      │  └─test-annotations
│      ├─maven-status
│      │  └─maven-compiler-plugin
│      │      └─compile
│      │          └─default-compile
│      └─test-classes
│          ├─cn
│          │  └─flydiy
│          │      ├─fdp
│          │      │  ├─service
│          │      │  └─util
│          │      ├─fdpbackend
│          │      │  └─service
│          │      ├─generate
│          │      │  └─util
│          │      ├─microflows
│          │      │  └─service
│          │      └─microservice
│          │          ├─mapping
│          │          ├─service
│          │          │  └─impl
│          │          └─util
│          ├─com
│          │  └─oppo
│          │      └─gcommon
│          │          ├─enums
│          │          ├─service
│          │          └─util
│          └─Excel
├─sql
└─target
    └─generated-sources
        └─annotations

## 安装依赖
```bash
$ cd lowcode-backend-generate
$ mvn -f pom.xml dependency:copy-dependencies
```

### 运行
```bash
$ mvn compile spring-boot:run 
```

### 打包
```bash
$ mvn clean compile -Dmaven.test.skip=true package
```
