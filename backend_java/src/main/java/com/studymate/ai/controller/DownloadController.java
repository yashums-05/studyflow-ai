package com.studymate.ai.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URI;
import java.util.Map;

@RestController
public class DownloadController {

    private static final String GITHUB_BACKUP_URL =
            "https://raw.githubusercontent.com/yashums-05/studymate-ai/main/apk/StudyFlowAI.apk";

    private final String[] CANDIDATE_PATHS = {
            "/app/StudyFlowAI.apk",
            "/app/apk/StudyFlowAI.apk",
            "./apk/StudyFlowAI.apk",
            "apk/StudyFlowAI.apk",
            "StudyFlowAI.apk",
            "D:\\AI app\\apk\\StudyFlowAI.apk",
            "D:\\AI app\\mobile\\android\\app\\build\\outputs\\apk\\release\\app-release.apk",
            "D:\\AI app\\mobile\\build\\app\\outputs\\flutter-apk\\StudyFlowAI.apk"
    };

    @GetMapping("/download/app.apk")
    public ResponseEntity<?> downloadApk() {
        for (String p : CANDIDATE_PATHS) {
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

        // If local binary is not present in container, redirect directly to GitHub raw storage
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(GITHUB_BACKUP_URL))
                .build();
    }

    @GetMapping(value = "/download", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String downloadPage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Download StudyFlow AI (Android)</title>\n" +
                "  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" +
                "  <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n" +
                "  <link href=\"https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700;800&display=swap\" rel=\"stylesheet\">\n" +
                "  <style>\n" +
                "    * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Plus Jakarta Sans', sans-serif; }\n" +
                "    body { background: #0A0E1A; color: #FFFFFF; min-height: 100vh; display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 24px; }\n" +
                "    .card { background: rgba(255, 255, 255, 0.05); border: 1px solid rgba(255, 255, 255, 0.12); border-radius: 24px; max-width: 480px; width: 100%; padding: 36px 28px; text-align: center; box-shadow: 0 20px 40px rgba(0,0,0,0.4); backdrop-filter: blur(16px); }\n" +
                "    .badge { display: inline-block; background: rgba(99, 102, 241, 0.2); color: #818CF8; border: 1px solid rgba(99, 102, 241, 0.4); font-size: 12px; font-weight: 700; padding: 6px 14px; border-radius: 999px; text-transform: uppercase; margin-bottom: 18px; letter-spacing: 0.5px; }\n" +
                "    h1 { font-size: 28px; font-weight: 800; margin-bottom: 8px; background: linear-gradient(135deg, #A5B4FC 0%, #6366F1 50%, #EC4899 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }\n" +
                "    p.sub { color: #94A3B8; font-size: 14px; line-height: 1.5; margin-bottom: 24px; }\n" +
                "    .btn-download { display: inline-flex; align-items: center; justify-content: center; gap: 10px; width: 100%; background: linear-gradient(135deg, #6366F1 0%, #4F46E5 100%); color: #FFFFFF; padding: 16px 24px; border-radius: 14px; text-decoration: none; font-weight: 700; font-size: 16px; transition: transform 0.2s, box-shadow 0.2s; box-shadow: 0 8px 24px rgba(99, 102, 241, 0.35); }\n" +
                "    .btn-download:hover { transform: translateY(-2px); box-shadow: 0 12px 30px rgba(99, 102, 241, 0.5); }\n" +
                "    .qr-container { margin: 28px 0 20px 0; padding: 16px; background: #FFFFFF; border-radius: 16px; display: inline-block; box-shadow: 0 8px 20px rgba(0,0,0,0.3); }\n" +
                "    .qr-container img { display: block; width: 150px; height: 150px; }\n" +
                "    .qr-hint { font-size: 12px; color: #64748B; margin-top: 6px; }\n" +
                "    .instructions { text-align: left; background: rgba(255, 255, 255, 0.03); border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 14px; padding: 16px; margin-top: 24px; font-size: 13px; color: #CBD5E1; }\n" +
                "    .instructions h4 { color: #F1F5F9; margin-bottom: 8px; font-size: 14px; }\n" +
                "    .instructions ol { padding-left: 20px; }\n" +
                "    .instructions li { margin-bottom: 6px; line-height: 1.4; }\n" +
                "    .footer { margin-top: 20px; font-size: 12px; color: #64748B; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"card\">\n" +
                "    <div class=\"badge\">Android Release v1.0.0</div>\n" +
                "    <h1>StudyFlow AI</h1>\n" +
                "    <p class=\"sub\">Intelligent Study Management, Timetable Generator & AI Academic Hub</p>\n" +
                "    <a href=\"/download/app.apk\" class=\"btn-download\">\n" +
                "      <svg width=\"20\" height=\"20\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2.5\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4\"></path><polyline points=\"7 10 12 15 17 10\"></polyline><line x1=\"12\" y1=\"15\" x2=\"12\" y2=\"3\"></line></svg>\n" +
                "      Download APK (Android)\n" +
                "    </a>\n" +
                "    <div class=\"qr-container\">\n" +
                "      <img src=\"https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=https://studymate-ai-uuvd.onrender.com/download/app.apk\" alt=\"Scan to Download APK\" />\n" +
                "      <div class=\"qr-hint\">Scan with phone to download</div>\n" +
                "    </div>\n" +
                "    <div class=\"instructions\">\n" +
                "      <h4>Installation Steps:</h4>\n" +
                "      <ol>\n" +
                "        <li>Tap <b>Download APK</b> or scan the QR code on your Android device.</li>\n" +
                "        <li>Open the downloaded file and tap <b>Install</b>.</li>\n" +
                "        <li>If prompted, allow <i>Install unknown apps</i> in your browser settings.</li>\n" +
                "        <li>Launch StudyFlow AI and create your personal account!</li>\n" +
                "      </ol>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "      Connected to Cloud Backend &bull; Render Live\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
