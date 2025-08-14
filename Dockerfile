FROM gradle:8.5.0-jdk21 AS builder
WORKDIR /app
COPY build.gradle settings.gradle gradle.properties openapi.properties ./
COPY gradle ./gradle
COPY src ./src
RUN gradle clean shadowJar --no-daemon

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder app/build/libs/*-all.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]