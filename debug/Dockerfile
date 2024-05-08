# Spring project is prepared to run on https://render.com/ webservice
#
# Build stage
FROM gradle:8.7-jdk21-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

LABEL org.name="malex"
# Package stage
FROM eclipse-temurin:21-jdk-jammy
EXPOSE 8080 8983
COPY --from=build /home/gradle/src/build/libs/app-telegram-rss-service-1.0.5.jar app.jar
ENTRYPOINT ["java","-Xdebug", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8983","-jar","/app.jar"]