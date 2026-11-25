package com.studymate.ai.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @Value("${app.gemini.api-key:}")
    private String geminiApiKey;

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
                "app", "StudyFlow AI Java Backend",
                "status", "online",
                "framework", "Spring Boot 3.2 (Java 17)",
                "version", "1.0.0",
                "downloadApk", "https://studymate-ai-uuvd.onrender.com/download/app.apk",
                "downloadPage", "https://studymate-ai-uuvd.onrender.com/download"
        );
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "healthy",
                "api", "v1",
                "runtime", "Java 17 OpenJDK",
                "geminiConfigured", geminiApiKey != null && !geminiApiKey.trim().isEmpty(),
                "downloadApk", "https://studymate-ai-uuvd.onrender.com/download/app.apk"
        );
    }
}
