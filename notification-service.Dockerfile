# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY common/pom.xml ./common/pom.xml
COPY common/src ./common/src

COPY payment-service/pom.xml ./payment-service/pom.xml

COPY notification-service/pom.xml ./notification-service/pom.xml
COPY notification-service/src ./notification-service/src

RUN mvn -B -DskipTests -pl notification-service -am package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/notification-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]