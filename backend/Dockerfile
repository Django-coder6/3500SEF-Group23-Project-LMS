# Builds the Spring Boot service from backend/. It needs backend/pom.xml, the
# Maven wrapper (.mvn/, mvnw) and backend/src, all delivered by [T-30-02]
# (issue #137, merged as #192). The service is behind the "full" profile in
# docker-compose.yml because the image takes a few minutes to build, so
# `docker compose up -d` leaves it out unless you ask for it.

# Stage 1: Build stage (Use JDK and Maven wrapper to build the project)
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Copy maven wrapper related files and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Grant execution permissions to mvnw and download dependencies (utilizing caching mechanism)
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copy source code and package it (skip tests to accelerate)
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run stage (Use lightweight JRE to run the project)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the packaged jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Declare the exposed port (Spring Boot default is 8080)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]