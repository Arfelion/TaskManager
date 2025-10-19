FROM maven:3.9.11-eclipse-temurin-25-alpine AS builder

COPY . /app

WORKDIR /app

RUN mvn clean package

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar taskmanager_v1.jar

CMD ["java", "-jar", "taskmanager_v1.jar"]