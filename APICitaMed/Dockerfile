FROM maven:3.9.4-eclipse-temurin-23 AS build
WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:23-jre-jammy
WORKDIR /app

COPY --from=build /app/target/APICitaMed-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]