FROM eclipse-temurin:24.0.1_9-jdk-alpine AS builder
WORKDIR /application
COPY . .
RUN --mount=type=cache,target=/root/.gradle  chmod +x gradlew && ./gradlew clean build -x test

FROM eclipse-temurin:24.0.1_9-jdk-alpine AS layers
WORKDIR /application
COPY --from=builder /application/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:24.0.1_9-jdk-alpine
VOLUME /tmp
RUN adduser -S spring-user
USER spring-user

COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./

ENV JAVA_OPTS="-XX:+UseG1GC -XX:+TieredCompilation -XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
