# Multi-stage build for StudyFlow AI Java Backend
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy gradle wrapper and configuration files
COPY studymate_ai/backend_java/gradlew studymate_ai/backend_java/build.gradle studymate_ai/backend_java/settings.gradle ./
COPY studymate_ai/backend_java/gradle ./gradle
COPY studymate_ai/backend_java/src ./src

# Build executable jar
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

# Lightweight JRE runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENV PORT=8000
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]
