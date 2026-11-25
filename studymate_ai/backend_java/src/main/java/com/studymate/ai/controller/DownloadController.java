package com.studymate.ai.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Map;

@RestController
public class DownloadController {

    private final String[] APK_PATHS = {
            "D:\\AI app\\studymate_ai\\mobile\\build\\app\\outputs\\flutter-apk\\app-debug.apk",
            "D:\\AI app\\studymate_ai\\mobile\\android\\app\\build\\outputs\\flutter-apk\\app-debug.apk",
            "D:\\AI app\\studymate_ai\\mobile\\android\\app\\build\\outputs\\apk\\debug\\app-debug.apk"
    };

    @GetMapping("/download/app.apk")
    public ResponseEntity<?> downloadApk() {
        for (String p : APK_PATHS) {
            File f = new File(p);
            if (f.exists() && f.length() > 0) {
                Resource resource = new FileSystemResource(f);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"StudyFlowAI.apk\"")
                        .contentType(MediaType.parseMediaType("application/vnd.android.package-archive"))
                        .contentLength(f.length())
                        .body(resource);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "APK file is currently being prepared. Please check back shortly."));
    }
}
