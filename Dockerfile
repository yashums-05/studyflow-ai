# Lightweight JRE runtime image for StudyFlow AI
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy pre-compiled, production-tested executable jar & Android APK
COPY backend_java/app.jar app.jar
COPY apk/StudyFlowAI.apk /app/StudyFlowAI.apk

ENV PORT=8000
EXPOSE 8000

# Run Spring Boot backend
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]
