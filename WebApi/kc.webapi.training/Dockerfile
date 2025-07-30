FROM openjdk:8-jdk-slim-bullseye

# 设置默认端口、环境及版本号
ARG port=80
ARG env=production
ARG version=1.0.0.1

# 设置时区、语言环境和编码
ENV LANG=zh_CN.UTF-8 \
    LC_ALL=zh_CN.UTF-8 \
    TZ=Asia/Shanghai \
    SPRING_PROFILES_ACTIVE=${env} \
    PORT=${port}

# 配置APT源为阿里云镜像并安装必要的依赖
RUN set -eux; \
    # 替换源地址
    sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list && \
    sed -i 's/security.debian.org\/debian-security/mirrors.aliyun.com\/debian-security/g' /etc/apt/sources.list && \
    sed -i 's/updates.debian.org\/debian/mirrors.aliyun.com\/debian/g' /etc/apt/sources.list && \
    # 更新包索引并安装依赖
    apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
        locales \
        fonts-wqy-zenhei \
        fontconfig && \
    # 生成中文语言环境
    echo 'zh_CN.UTF-8 UTF-8' > /etc/locale.gen && \
    echo 'en_US.UTF-8 UTF-8' >> /etc/locale.gen && \
    locale-gen && \
    update-locale LANG=zh_CN.UTF-8 && \
    # 清理缓存
    rm -rf /var/lib/apt/lists/*

# 创建字体目录并复制字体
RUN mkdir -p /usr/share/fonts/truetype/deng/
COPY ./Fonts/* /usr/share/fonts/truetype/deng/
RUN fc-cache -fv

# 设置工作目录
WORKDIR /app

# 复制发布文件到容器
COPY ./kc.webapi.training-${version}.jar app.jar

# 暴露端口
EXPOSE ${PORT}

# 健康检查（可选启用）
# HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
#     CMD curl -sf http://localhost:${PORT}/actuator/health | grep -q '"status":"UP"' || exit 1

# 启动命令
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "./app.jar"]