FROM maven:3.9.4-eclipse-temurin-23 AS build
WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:23-jre-jammy
WORKDIR /app


ARG JAR_FILE=target/APICitaMed-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]