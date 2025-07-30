#!/bin/sh -l
export LANG=zh_CN.utf8

#---1.生成kcloudy.demo.core项目并发布至Nexus-->2.生成发布kcloudy.demo.web项目并发布至本地路径-->3.生成docker镜像

# 发布环境
env=$1
# 制定run的端口
port=$2
# jenkins的工作目录
workspaceDir=$JENKINS_HOME/workspace/$JOB_NAME

# 本地Maven服务：Maven Server in Nexus
hostedMaven="http://nexus.kcloudy.com/repository/maven-snapshots/";
groupMaven="http://nexus.kcloudy.com/repository/maven-public/";
publicMaven="http://nexus.kcloudy.com/repository/maven-aliyun/";

# Nexus本地仓地址
dockerHubUrl=dockerhub.kcloudy.com

# 项目发布的版本
#newVersion=1.0.0.$BUILD_NUMBER
newVersion=1.0.0.1
# 上一个版本
lastNum=$(($BUILD_NUMBER-1))
lastVersion=1.0.0.${lastNum}


#---------------1. deploy and publish发布项目【kcloudy.demo.core】到Maven的本地仓及远程仓---------------
# 定义好存放发布好的项目代码的目录和备份发布内容的目录
# 要构建的项目名称
projectName=kcloudy.demo.core
# 项目文件全路径
csprojDir=${workspaceDir}/${projectName}
# 项目发布的目录
publishDir=$JENKINS_HOME/publish/${projectName}
webDir=${publishDir}/v-${newVersion}
oldwebDir=${publishDir}/v-${lastVersion}

echo "1.1----kcloudy: start to clear dictionary: "${webDir}
# 清空文件夹
if [ ! -d "${publishDir}" ]; then
  mkdir  ${publishDir}
  setfacl -Rm u:jenkins:rwx ${publishDir}
fi


echo "1.2----kcloudy: deploy and publish project："${projectName}" into Maven server: "${hostedMaven}
echo "1.2----maven: mvn deploy -pl ${projectName} -am -DskipTests --no-transfer-progress source:jar"

# 替换版本号, 会修改pom.xml中的版本号
#mvn clean versions:set -DnewVersion=${newVersion}
# maven cli 相关参数：https://maven.apache.org/ref/3.6.1/maven-embedder/cli.html
# 针对指定项目（-pl）并构建列表所需的项目（-am），采用跳过测试（-DskipTests）并包含源码（source:jar）方式进行发布
# 不显示下载进度（--no-transfer-progress）
#mvn deploy -pl ${projectName} -am -DskipTests --no-transfer-progress source:jar
# 回滚pom.xml中的版本号
#mvn versions:revert

#---------------2. 还原并发布spring项目【kcloudy.demo.web】至本地文件夹---------------
# 要构建的项目名称
projectName=kcloudy.demo.web
# 项目的全路径
csprojDir=${workspaceDir}/${projectName}
# 容器名
containerName=$(echo "$projectName" | tr '[:upper:]' '[:lower:]');#镜像名
imageName="kcloudy/"$containerName:${newVersion}
imageOldName="kcloudy/"$containerName:${lastVersion}
hubImageName=${dockerHubUrl}/${imageName}
hubImageOldName=${dockerHubUrl}/${imageOldName}

# 项目发布的目录
publishDir=$JENKINS_HOME/publish/${projectName}
webDir=${publishDir}/v-${newVersion}
oldwebDir=${publishDir}/v-${lastVersion}

echo "2.1----kcloudy: start to clear dictionary: "${webDir}
# 清空文件夹
if [ ! -d "${webDir}" ]; then
  mkdir -p ${webDir}
  setfacl -Rm u:jenkins:rwx ${webDir}
else
  rm -rf ${webDir}/*
fi

echo "2.2----kcloudy: update project: "${csprojDir}" from lastVersion: "${lastVersion}" to newVersion: "${newVersion}
if [ -n "$(docker ps -aq -f name=${containerName})" ]; then
    # 停止docker容器
    echo "----kcloudy: stop docker container--"${containerName};
    docker stop ${containerName};
    if [ -n "$(docker ps -aq -f status=exited -f name=${containerName})" ]; then
        # 删除容器
        echo "----kcloudy: delete container--"${containerName};
        docker rm -f ${containerName};
    fi
fi

if [ -n "$(docker images -aq ${imageOldName})" ]; then
    # 删除镜像
    echo "----kcloudy: delete image--"${imageOldName};
    docker rmi -f ${imageOldName};
fi

echo "2.3----kcloudy: package and publish project：kcloudy.demo.web into dictionary: "${webDir}
echo "2.3----maven: mvn package -pl ${projectName} -am -Ddir=${webDir}/ -DskipTests --no-transfer-progress source:jar"
# 替换版本号, 会修改pom.xml中的版本号
#mvn clean versions:set -DnewVersion=${newVersion}
# maven cli 相关参数：https://maven.apache.org/ref/3.6.1/maven-embedder/cli.html
# 针对指定项目（-pl）并构建所需的项目（-am），采用跳过测试（-DskipTests）并包含源码（source:jar）方式进行发布
# 不显示下载进度（--no-transfer-progress）
mvn clean package -pl ${projectName} -am -DskipTests --no-transfer-progress source:jar
# 回滚pom.xml中的版本号
#mvn versions:revert

echo "2.4----kcloudy: tar all publish file into tar.gz: "${publishDir}/${projectName}-${newVersion}.tar.gz
echo "2.4----copy: cp -rf ./fonts/ ${webDir}/fonts/"
echo "2.4----copy: cp -rf ${projectName}/Dockerfile ${webDir}/"
echo "2.4----copy: cp -rf ${projectName}/target/${projectName}-${newVersion}.jar  ${webDir}/"
echo "2.4----tar: tar -zcf ${publishDir}/${projectName}-${newVersion}.tar.gz ."
cp -rf ./fonts/ ${webDir}/fonts/
cp -rf ${projectName}/Dockerfile ${webDir}/
cp -rf ${projectName}/target/${projectName}-${newVersion}.jar  ${webDir}/
cd ${webDir}
tar -zcf ${publishDir}/${projectName}-${newVersion}.tar.gz .


#---------------3.生成docker镜像：kcloudy.demo.web:vxxx，并推送至Nexus的本地docker镜像仓---------------
echo "3.1----kcloudy: build image with Dockerfile--"${imageName}"--build-arg env="${env}" publishdir="${webDir}
echo "3.1----docker: docker build -t ${imageName} --build-arg env=${env} --build-arg port=${port} --build-arg publishdir=${webDir} ."

# 通过Dockerfile重新构建镜像
docker build -t ${imageName} --build-arg env=${env} --build-arg port=${port} --build-arg publishdir=${webDir} .;
docker images;

echo "3.2----kcloudy: push docker ["${imageName}"] to private docekr hub: "${dockerHubUrl};
# 登陆Nexus中的Docker本地仓，并将本地镜像推送至Nexus本地仓
docker login ${dockerHubUrl} -u docker-admin -p dockeradmin123
docker tag ${imageName} ${hubImageName}
docker push ${hubImageName}

echo "3.3----kcloudy: generate the shell [runRemoteDemoJavaDocker.sh] to send the remote host to run: "${port};
# 在工作目录下生成shell脚本：runRemoteDemoJavaDocker.sh
cd ${workspaceDir}
cat << EOF > runRemoteDemoJavaDocker.sh
#!/bin/bash
docker login ${dockerHubUrl} -u admin -p chjtian1981;
docker pull ${hubImageName};
if [ -n "\$(docker ps -aq -f name=${containerName})" ]; then
    # 停止docker容器
    echo "----kcloudy: stop docker container--${containerName}";
    docker stop ${containerName};
    if [ -n "\$(docker ps -aq -f status=exited -f name=${containerName})" ]; then
        # 删除容器
        echo "----kcloudy: delete container--${containerName}";
        docker rm -f ${containerName};
    fi
fi

if [ -n "\$(docker images -aq ${hubImageOldName})" ]; then
    # 删除镜像
    echo "----kcloudy: delete image--${hubImageOldName}";
    docker rmi -f ${hubImageOldName};
fi
docker run -d -p ${port}:${port} --name ${containerName} ${hubImageName};
EOF
# 在工作目录下查看生成好的shell脚本：runRemoteDemoJavaDocker.sh
cat runRemoteDemoJavaDocker.sh

# 清空发布的老版本文件夹
echo "3.3----kcloudy: end to clear the old dictionary--"${oldwebDir};
if [ -d "${oldwebDir}" ]; then
  rm -rf ${oldwebDir}
fi

echo "----kcloudy: success!";
