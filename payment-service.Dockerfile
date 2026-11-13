# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY common/pom.xml ./common/pom.xml
COPY common/src ./common/src

COPY payment-service/pom.xml ./payment-service/pom.xml
COPY payment-service/src ./payment-service/src

COPY notification-service/pom.xml ./notification-service/pom.xml

RUN mvn -B -DskipTests -pl payment-service -am package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/payment-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
