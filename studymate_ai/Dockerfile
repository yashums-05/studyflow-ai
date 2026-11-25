# Multi-stage build for StudyFlow AI Java Backend
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy gradle wrapper and configuration files
COPY backend_java/gradlew backend_java/build.gradle backend_java/settings.gradle ./
COPY backend_java/gradle ./gradle
COPY backend_java/src ./src

# Build executable jar without running tests
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

# Lightweight JRE runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENV PORT=8000
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]
