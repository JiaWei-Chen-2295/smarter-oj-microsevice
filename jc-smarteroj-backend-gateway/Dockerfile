ARG DOCKER_REGISTRY=docker.m.daocloud.io
FROM ${DOCKER_REGISTRY}/library/maven:3.8.5-openjdk-8 AS build

WORKDIR /app

# 复制父 pom 和依赖模块
COPY pom.xml .
COPY jc-smarteroj-backend-common ./jc-smarteroj-backend-common
COPY jc-smarteroj-backend-model ./jc-smarteroj-backend-model
COPY jc-smarter-oj-server-client ./jc-smarter-oj-server-client
COPY jc-smarteroj-backend-user-service ./jc-smarteroj-backend-user-service
COPY jc-smarteroj-backend-judge-service ./jc-smarteroj-backend-judge-service
COPY jc-smarteroj-backend-question-service ./jc-smarteroj-backend-question-service
COPY jc-smarteroj-backend-post-service ./jc-smarteroj-backend-post-service
COPY jc-smarteroj-backend-room-service ./jc-smarteroj-backend-room-service

# 复制当前服务代码
COPY jc-smarteroj-backend-gateway ./jc-smarteroj-backend-gateway

# 构建项目
RUN mvn clean package -pl jc-smarteroj-backend-gateway -am -Dmaven.test.skip=true

# 运行阶段 - 使用 AdoptOpenJDK OpenJ9（内存优化更好）
FROM ${DOCKER_REGISTRY}/library/adoptopenjdk:8-jre-openj9

# 设置 UTF-8 locale（解决 Docker 容器中文乱码）
ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    LANGUAGE=C.UTF-8

WORKDIR /app

# 复制 jar 文件
COPY --from=build /app/jc-smarteroj-backend-gateway/target/*.jar app.jar

# 暴露端口
EXPOSE 8101

# 启动命令 - IBM OpenJ9 优化参数（极限内存环境）
# -Xms: 初始堆内存 128MB（极限优化）
# -Xmx: 最大堆内存 192MB（适合 1.87GB 总内存环境）
# -Xquickstart: 快速启动模式（适合微服务）
# -Xshareclasses:none: 禁用共享类缓存（容器环境推荐）
# -XX:+IdleTuningGcOnIdle: 空闲时压缩堆内存
# -XX:+UseContainerSupport: 容器感知
# -Dfile.encoding=UTF-8: 解决中文乱码
ENTRYPOINT ["java", \
    "-Xms128m", \
    "-Xmx192m", \
    "-Xquickstart", \
    "-Xshareclasses:none", \
    "-XX:+IdleTuningGcOnIdle", \
    "-XX:+UseContainerSupport", \
    "-Dfile.encoding=UTF-8", \
    "-Dsun.jnu.encoding=UTF-8", \
    "-jar", \
    "-Dspring.profiles.active=prod", \
    "app.jar"]
