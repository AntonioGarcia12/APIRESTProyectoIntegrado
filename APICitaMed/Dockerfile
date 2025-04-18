# Etapa de build con Maven + JDK 23
FROM maven:3.9.4-eclipse-temurin-23-jdk AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de runtime (JRE 23)
FROM eclipse-temurin:23-jre-jammy
WORKDIR /app
COPY --from=build /app/target/APICitaMed-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]