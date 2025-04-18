FROM maven:3.9-eclipse-temurin-23 AS builder
WORKDIR /app

COPY pom.xml .
COPY src/ ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:23-jdk
WORKDIR /app

COPY --from=builder /app/target/APIBiblioteca2-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
