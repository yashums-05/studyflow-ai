package com.studymate.ai.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @Value("${app.gemini.api-key:}")
    private String geminiApiKey;

    private String getBaseUrl(HttpServletRequest request) {
        String proto = request.getHeader("X-Forwarded-Proto");
        if (proto == null || proto.isEmpty()) {
            proto = request.getScheme();
        }
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null || host.isEmpty()) {
            host = request.getHeader("Host");
        }
        if (host == null || host.isEmpty()) {
            host = "studyflow-ai.onrender.com";
        }
        return proto + "://" + host;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String rootHtml() {
        return DownloadController.getPortalHtml();
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> rootJson(HttpServletRequest request) {
        String base = getBaseUrl(request);
        return Map.of(
                "app", "StudyFlow AI Java Backend",
                "status", "online",
                "framework", "Spring Boot 3.2 (Java 17)",
                "version", "1.0.0",
                "downloadApk", base + "/download/app.apk",
                "downloadPage", base + "/download",
                "installPortal", base + "/install"
        );
    }

    @GetMapping("/health")
    public Map<String, Object> health(HttpServletRequest request) {
        String base = getBaseUrl(request);
        return Map.of(
                "status", "healthy",
                "api", "v1",
                "runtime", "Java 17 OpenJDK",
                "geminiConfigured", geminiApiKey != null && !geminiApiKey.trim().isEmpty(),
                "downloadApk", base + "/download/app.apk"
        );
    }
}
