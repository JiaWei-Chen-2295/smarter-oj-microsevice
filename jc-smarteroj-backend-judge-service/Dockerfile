ARG DOCKER_REGISTRY=docker.m.daocloud.io
FROM ${DOCKER_REGISTRY}/library/maven:3.8.5-openjdk-8 AS build

WORKDIR /app

# 复制父 pom 和依赖模块
COPY pom.xml .
COPY jc-smarteroj-backend-common ./jc-smarteroj-backend-common
COPY jc-smarteroj-backend-model ./jc-smarteroj-backend-model
COPY jc-smarter-oj-server-client ./jc-smarter-oj-server-client
COPY jc-smarteroj-backend-gateway ./jc-smarteroj-backend-gateway
COPY jc-smarteroj-backend-user-service ./jc-smarteroj-backend-user-service
COPY jc-smarteroj-backend-question-service ./jc-smarteroj-backend-question-service
COPY jc-smarteroj-backend-post-service ./jc-smarteroj-backend-post-service
COPY jc-smarteroj-backend-room-service ./jc-smarteroj-backend-room-service

# 复制当前服务代码
COPY jc-smarteroj-backend-judge-service ./jc-smarteroj-backend-judge-service

# 构建项目
RUN mvn clean package -pl jc-smarteroj-backend-judge-service -am -Dmaven.test.skip=true

# 运行阶段
FROM ${DOCKER_REGISTRY}/library/eclipse-temurin:8-jre-jammy

WORKDIR /app

# 复制 jar 文件
COPY --from=build /app/jc-smarteroj-backend-judge-service/target/*.jar app.jar

# 暴露端口
EXPOSE 8203

# 启动命令
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
