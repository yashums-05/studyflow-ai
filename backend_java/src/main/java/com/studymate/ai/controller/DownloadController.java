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

    @GetMapping(value = {"/download", "/install", "/portal"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String downloadPage() {
        return getPortalHtml();
    }

    public static String getPortalHtml() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>StudyFlow AI &bull; Intelligent Academic Portal & App</title>\n" +
                "  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" +
                "  <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n" +
                "  <link href=\"https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap\" rel=\"stylesheet\">\n" +
                "  <style>\n" +
                "    *, *::before, *::after {\n" +
                "      box-sizing: border-box;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      -webkit-tap-highlight-color: transparent;\n" +
                "    }\n" +
                "    html, body {\n" +
                "      width: 100%;\n" +
                "      max-width: 100%;\n" +
                "      overflow-x: hidden;\n" +
                "      background-color: #080C17;\n" +
                "      color: #F8FAFC;\n" +
                "      font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;\n" +
                "      min-height: 100vh;\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "    }\n" +
                "    /* Navbar */\n" +
                "    header {\n" +
                "      width: 100%;\n" +
                "      position: sticky;\n" +
                "      top: 0;\n" +
                "      backdrop-filter: blur(20px);\n" +
                "      -webkit-backdrop-filter: blur(20px);\n" +
                "      background: rgba(8, 12, 23, 0.9);\n" +
                "      border-bottom: 1px solid rgba(255, 255, 255, 0.08);\n" +
                "      z-index: 50;\n" +
                "      padding: 12px 16px;\n" +
                "    }\n" +
                "    .nav-container {\n" +
                "      max-width: 1140px;\n" +
                "      margin: 0 auto;\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "      align-items: center;\n" +
                "      width: 100%;\n" +
                "    }\n" +
                "    .brand {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 10px;\n" +
                "      text-decoration: none;\n" +
                "      color: #fff;\n" +
                "    }\n" +
                "    .brand-icon {\n" +
                "      width: 34px;\n" +
                "      height: 34px;\n" +
                "      background: linear-gradient(135deg, #6366F1, #EC4899);\n" +
                "      border-radius: 9px;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      box-shadow: 0 4px 12px rgba(99, 102, 241, 0.4);\n" +
                "      flex-shrink: 0;\n" +
                "    }\n" +
                "    .brand-title {\n" +
                "      font-size: 18px;\n" +
                "      font-weight: 800;\n" +
                "      letter-spacing: -0.4px;\n" +
                "      white-space: nowrap;\n" +
                "    }\n" +
                "    .brand-title span { color: #818CF8; }\n" +
                "    .nav-actions {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 12px;\n" +
                "    }\n" +
                "    .status-pill {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 7px;\n" +
                "      background: rgba(16, 185, 129, 0.12);\n" +
                "      border: 1px solid rgba(16, 185, 129, 0.3);\n" +
                "      padding: 5px 12px;\n" +
                "      border-radius: 999px;\n" +
                "      font-size: 12px;\n" +
                "      font-weight: 600;\n" +
                "      color: #34D399;\n" +
                "      white-space: nowrap;\n" +
                "    }\n" +
                "    .pulse-dot {\n" +
                "      width: 7px;\n" +
                "      height: 7px;\n" +
                "      background: #10B981;\n" +
                "      border-radius: 50%;\n" +
                "      box-shadow: 0 0 8px #10B981;\n" +
                "    }\n" +
                "    .btn-nav-install {\n" +
                "      background: linear-gradient(135deg, #6366F1, #4F46E5);\n" +
                "      color: #fff;\n" +
                "      padding: 8px 16px;\n" +
                "      border-radius: 10px;\n" +
                "      text-decoration: none;\n" +
                "      font-weight: 700;\n" +
                "      font-size: 13px;\n" +
                "      border: none;\n" +
                "      cursor: pointer;\n" +
                "      white-space: nowrap;\n" +
                "      transition: all 0.2s;\n" +
                "    }\n" +
                "    .btn-nav-install:hover {\n" +
                "      box-shadow: 0 4px 16px rgba(99, 102, 241, 0.5);\n" +
                "    }\n" +
                "    /* Main Container */\n" +
                "    .main-wrap {\n" +
                "      width: 100%;\n" +
                "      max-width: 1140px;\n" +
                "      margin: 0 auto;\n" +
                "      padding: 32px 16px 56px 16px;\n" +
                "      flex: 1;\n" +
                "      position: relative;\n" +
                "      z-index: 10;\n" +
                "    }\n" +
                "    /* Responsive Hero Grid */\n" +
                "    .hero-grid {\n" +
                "      display: grid;\n" +
                "      grid-template-columns: 1.15fr 0.85fr;\n" +
                "      gap: 36px;\n" +
                "      align-items: center;\n" +
                "      width: 100%;\n" +
                "    }\n" +
                "    .hero-badge {\n" +
                "      display: inline-flex;\n" +
                "      align-items: center;\n" +
                "      gap: 6px;\n" +
                "      background: rgba(99, 102, 241, 0.15);\n" +
                "      border: 1px solid rgba(99, 102, 241, 0.35);\n" +
                "      color: #A5B4FC;\n" +
                "      font-size: 12px;\n" +
                "      font-weight: 700;\n" +
                "      padding: 6px 14px;\n" +
                "      border-radius: 999px;\n" +
                "      margin-bottom: 16px;\n" +
                "    }\n" +
                "    h1.hero-title {\n" +
                "      font-size: clamp(26px, 5.5vw, 46px);\n" +
                "      font-weight: 800;\n" +
                "      line-height: 1.18;\n" +
                "      margin-bottom: 14px;\n" +
                "      letter-spacing: -0.6px;\n" +
                "      word-break: break-word;\n" +
                "    }\n" +
                "    .gradient-text {\n" +
                "      background: linear-gradient(135deg, #FFFFFF 15%, #A5B4FC 60%, #EC4899 100%);\n" +
                "      -webkit-background-clip: text;\n" +
                "      -webkit-text-fill-color: transparent;\n" +
                "    }\n" +
                "    p.hero-desc {\n" +
                "      color: #94A3B8;\n" +
                "      font-size: clamp(14px, 2.2vw, 16px);\n" +
                "      line-height: 1.6;\n" +
                "      margin-bottom: 24px;\n" +
                "    }\n" +
                "    .cta-row {\n" +
                "      display: flex;\n" +
                "      flex-wrap: wrap;\n" +
                "      align-items: center;\n" +
                "      gap: 12px;\n" +
                "      margin-bottom: 22px;\n" +
                "    }\n" +
                "    .btn-hero-install {\n" +
                "      background: linear-gradient(135deg, #6366F1 0%, #4F46E5 100%);\n" +
                "      color: #FFFFFF;\n" +
                "      padding: 15px 28px;\n" +
                "      border-radius: 14px;\n" +
                "      font-size: 15px;\n" +
                "      font-weight: 800;\n" +
                "      border: none;\n" +
                "      cursor: pointer;\n" +
                "      display: inline-flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      gap: 10px;\n" +
                "      box-shadow: 0 8px 24px rgba(99, 102, 241, 0.45);\n" +
                "      transition: all 0.2s;\n" +
                "      text-decoration: none;\n" +
                "    }\n" +
                "    .btn-hero-install:hover {\n" +
                "      transform: translateY(-2px);\n" +
                "      box-shadow: 0 12px 30px rgba(99, 102, 241, 0.6);\n" +
                "    }\n" +
                "    .btn-secondary {\n" +
                "      background: rgba(255, 255, 255, 0.05);\n" +
                "      color: #CBD5E1;\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.1);\n" +
                "      padding: 15px 22px;\n" +
                "      border-radius: 14px;\n" +
                "      font-size: 14px;\n" +
                "      font-weight: 700;\n" +
                "      cursor: pointer;\n" +
                "      text-decoration: none;\n" +
                "      display: inline-flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      gap: 8px;\n" +
                "      transition: all 0.2s;\n" +
                "    }\n" +
                "    .btn-secondary:hover {\n" +
                "      background: rgba(255, 255, 255, 0.09);\n" +
                "      color: #fff;\n" +
                "    }\n" +
                "    .meta-pills {\n" +
                "      display: flex;\n" +
                "      flex-wrap: wrap;\n" +
                "      align-items: center;\n" +
                "      gap: 14px;\n" +
                "      color: #64748B;\n" +
                "      font-size: 12px;\n" +
                "      font-weight: 600;\n" +
                "    }\n" +
                "    .meta-pills div {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 4px;\n" +
                "    }\n" +
                "    /* Desktop Phone Mockup */\n" +
                "    .phone-mockup-wrapper {\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      width: 100%;\n" +
                "    }\n" +
                "    .phone-frame {\n" +
                "      width: 270px;\n" +
                "      height: 540px;\n" +
                "      background: #000000;\n" +
                "      border-radius: 40px;\n" +
                "      padding: 9px;\n" +
                "      box-shadow:\n" +
                "        0 0 0 3px #1F2937,\n" +
                "        0 20px 50px rgba(0, 0, 0, 0.75),\n" +
                "        0 0 30px rgba(99, 102, 241, 0.2);\n" +
                "      position: relative;\n" +
                "      overflow: hidden;\n" +
                "      border: 3px solid #374151;\n" +
                "    }\n" +
                "    .phone-notch {\n" +
                "      width: 90px;\n" +
                "      height: 16px;\n" +
                "      background: #000000;\n" +
                "      border-radius: 10px;\n" +
                "      position: absolute;\n" +
                "      top: 12px;\n" +
                "      left: 50%;\n" +
                "      transform: translateX(-50%);\n" +
                "      z-index: 30;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "    }\n" +
                "    .camera-dot {\n" +
                "      width: 6px;\n" +
                "      height: 6px;\n" +
                "      background: #111827;\n" +
                "      border: 1px solid #374151;\n" +
                "      border-radius: 50%;\n" +
                "    }\n" +
                "    .phone-screen {\n" +
                "      width: 100%;\n" +
                "      height: 100%;\n" +
                "      background: #0B0F19;\n" +
                "      border-radius: 32px;\n" +
                "      overflow: hidden;\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "      padding-top: 24px;\n" +
                "    }\n" +
                "    .screen-status-bar {\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "      align-items: center;\n" +
                "      padding: 4px 16px;\n" +
                "      font-size: 10px;\n" +
                "      font-weight: 700;\n" +
                "      color: #94A3B8;\n" +
                "    }\n" +
                "    .screen-header {\n" +
                "      padding: 8px 14px;\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "      align-items: center;\n" +
                "    }\n" +
                "    .screen-avatar {\n" +
                "      width: 28px;\n" +
                "      height: 28px;\n" +
                "      border-radius: 50%;\n" +
                "      background: linear-gradient(135deg, #6366F1, #EC4899);\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      font-weight: 800;\n" +
                "      font-size: 11px;\n" +
                "    }\n" +
                "    .screen-content {\n" +
                "      padding: 6px 12px;\n" +
                "      flex: 1;\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "      gap: 8px;\n" +
                "    }\n" +
                "    .app-card-mock {\n" +
                "      background: rgba(255, 255, 255, 0.05);\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.08);\n" +
                "      border-radius: 12px;\n" +
                "      padding: 9px 10px;\n" +
                "    }\n" +
                "    .app-card-mock h5 {\n" +
                "      font-size: 11px;\n" +
                "      font-weight: 700;\n" +
                "      color: #F1F5F9;\n" +
                "      margin-bottom: 2px;\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "    }\n" +
                "    .app-card-mock p {\n" +
                "      font-size: 10px;\n" +
                "      color: #94A3B8;\n" +
                "      line-height: 1.3;\n" +
                "    }\n" +
                "    .app-tag {\n" +
                "      background: rgba(99, 102, 241, 0.25);\n" +
                "      color: #A5B4FC;\n" +
                "      font-size: 9px;\n" +
                "      font-weight: 800;\n" +
                "      padding: 1px 5px;\n" +
                "      border-radius: 4px;\n" +
                "    }\n" +
                "    .screen-bottom-nav {\n" +
                "      margin-top: auto;\n" +
                "      background: #070913;\n" +
                "      border-top: 1px solid rgba(255, 255, 255, 0.06);\n" +
                "      display: flex;\n" +
                "      justify-content: space-around;\n" +
                "      padding: 8px 4px 12px 4px;\n" +
                "      font-size: 9px;\n" +
                "      color: #64748B;\n" +
                "    }\n" +
                "    .nav-item-active {\n" +
                "      color: #818CF8;\n" +
                "      font-weight: 700;\n" +
                "    }\n" +
                "    .qr-card-desktop {\n" +
                "      margin-top: 16px;\n" +
                "      background: rgba(18, 24, 40, 0.75);\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.08);\n" +
                "      border-radius: 14px;\n" +
                "      padding: 12px;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 12px;\n" +
                "      width: 270px;\n" +
                "    }\n" +
                "    .qr-card-desktop img {\n" +
                "      width: 60px;\n" +
                "      height: 60px;\n" +
                "      border-radius: 6px;\n" +
                "      background: #fff;\n" +
                "      padding: 3px;\n" +
                "      flex-shrink: 0;\n" +
                "    }\n" +
                "    .qr-card-info h6 {\n" +
                "      font-size: 11px;\n" +
                "      font-weight: 700;\n" +
                "      color: #F1F5F9;\n" +
                "      margin-bottom: 2px;\n" +
                "    }\n" +
                "    .qr-card-info p {\n" +
                "      font-size: 10px;\n" +
                "      color: #64748B;\n" +
                "      line-height: 1.3;\n" +
                "    }\n" +
                "    /* Features Section */\n" +
                "    .features-section {\n" +
                "      margin-top: 48px;\n" +
                "      width: 100%;\n" +
                "    }\n" +
                "    .section-title {\n" +
                "      text-align: center;\n" +
                "      margin-bottom: 28px;\n" +
                "    }\n" +
                "    .section-title h2 {\n" +
                "      font-size: clamp(22px, 3.5vw, 30px);\n" +
                "      font-weight: 800;\n" +
                "      margin-bottom: 6px;\n" +
                "      letter-spacing: -0.4px;\n" +
                "    }\n" +
                "    .section-title p {\n" +
                "      color: #94A3B8;\n" +
                "      font-size: 14px;\n" +
                "    }\n" +
                "    .cards-grid {\n" +
                "      display: grid;\n" +
                "      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));\n" +
                "      gap: 16px;\n" +
                "      width: 100%;\n" +
                "    }\n" +
                "    .feat-card {\n" +
                "      background: rgba(18, 24, 40, 0.75);\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.08);\n" +
                "      border-radius: 16px;\n" +
                "      padding: 20px;\n" +
                "      transition: all 0.2s;\n" +
                "    }\n" +
                "    .feat-card:hover {\n" +
                "      border-color: rgba(99, 102, 241, 0.35);\n" +
                "      transform: translateY(-2px);\n" +
                "    }\n" +
                "    .feat-icon {\n" +
                "      width: 38px;\n" +
                "      height: 38px;\n" +
                "      border-radius: 10px;\n" +
                "      background: rgba(99, 102, 241, 0.15);\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      font-size: 18px;\n" +
                "      margin-bottom: 12px;\n" +
                "    }\n" +
                "    .feat-card h3 {\n" +
                "      font-size: 15px;\n" +
                "      font-weight: 700;\n" +
                "      margin-bottom: 4px;\n" +
                "    }\n" +
                "    .feat-card p {\n" +
                "      color: #94A3B8;\n" +
                "      font-size: 13px;\n" +
                "      line-height: 1.5;\n" +
                "    }\n" +
                "    /* Responsive Modal Dialog */\n" +
                "    .modal-backdrop {\n" +
                "      position: fixed;\n" +
                "      inset: 0;\n" +
                "      background: rgba(0, 0, 0, 0.85);\n" +
                "      backdrop-filter: blur(12px);\n" +
                "      -webkit-backdrop-filter: blur(12px);\n" +
                "      z-index: 100;\n" +
                "      display: none;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      padding: 16px;\n" +
                "    }\n" +
                "    .modal-content {\n" +
                "      background: #0E1322;\n" +
                "      border: 1px solid rgba(99, 102, 241, 0.35);\n" +
                "      border-radius: 20px;\n" +
                "      max-width: 440px;\n" +
                "      width: 100%;\n" +
                "      max-height: 90vh;\n" +
                "      overflow-y: auto;\n" +
                "      padding: 24px 20px;\n" +
                "      box-shadow: 0 20px 50px rgba(0,0,0,0.8);\n" +
                "      position: relative;\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "    .modal-close {\n" +
                "      position: absolute;\n" +
                "      top: 14px;\n" +
                "      right: 14px;\n" +
                "      background: rgba(255, 255, 255, 0.08);\n" +
                "      border: none;\n" +
                "      color: #94A3B8;\n" +
                "      width: 28px;\n" +
                "      height: 28px;\n" +
                "      border-radius: 50%;\n" +
                "      cursor: pointer;\n" +
                "      font-size: 16px;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "    }\n" +
                "    .download-anim-icon {\n" +
                "      width: 50px;\n" +
                "      height: 50px;\n" +
                "      background: linear-gradient(135deg, #10B981, #059669);\n" +
                "      border-radius: 14px;\n" +
                "      margin: 0 auto 12px auto;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "    }\n" +
                "    .progress-bar-container {\n" +
                "      width: 100%;\n" +
                "      height: 6px;\n" +
                "      background: rgba(255, 255, 255, 0.08);\n" +
                "      border-radius: 999px;\n" +
                "      overflow: hidden;\n" +
                "      margin: 16px 0;\n" +
                "    }\n" +
                "    .progress-bar-fill {\n" +
                "      height: 100%;\n" +
                "      width: 0%;\n" +
                "      background: linear-gradient(90deg, #6366F1, #10B981);\n" +
                "      border-radius: 999px;\n" +
                "      transition: width 0.3s ease;\n" +
                "    }\n" +
                "    .step-list {\n" +
                "      text-align: left;\n" +
                "      background: rgba(255, 255, 255, 0.03);\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.07);\n" +
                "      border-radius: 14px;\n" +
                "      padding: 14px;\n" +
                "      margin-bottom: 16px;\n" +
                "    }\n" +
                "    .step-item {\n" +
                "      display: flex;\n" +
                "      align-items: flex-start;\n" +
                "      gap: 10px;\n" +
                "      margin-bottom: 10px;\n" +
                "    }\n" +
                "    .step-item:last-child { margin-bottom: 0; }\n" +
                "    .step-num {\n" +
                "      background: rgba(99, 102, 241, 0.25);\n" +
                "      color: #A5B4FC;\n" +
                "      font-weight: 800;\n" +
                "      font-size: 11px;\n" +
                "      width: 20px;\n" +
                "      height: 20px;\n" +
                "      border-radius: 50%;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      flex-shrink: 0;\n" +
                "      margin-top: 2px;\n" +
                "    }\n" +
                "    .step-text h4 { font-size: 13px; font-weight: 700; color: #F1F5F9; margin-bottom: 2px; }\n" +
                "    .step-text p { font-size: 11px; color: #94A3B8; line-height: 1.35; }\n" +
                "    .modal-footer-note {\n" +
                "      font-size: 12px;\n" +
                "      color: #64748B;\n" +
                "    }\n" +
                "    .modal-footer-note a {\n" +
                "      color: #818CF8;\n" +
                "      font-weight: 600;\n" +
                "      text-decoration: underline;\n" +
                "      cursor: pointer;\n" +
                "    }\n" +
                "    /* MOBILE PHONE VIEWPORT ALIGNMENTS (<820px) */\n" +
                "    @media (max-width: 820px) {\n" +
                "      header { padding: 10px 14px; }\n" +
                "      .status-pill { display: none; }\n" +
                "      .main-wrap {\n" +
                "        padding: 24px 14px 48px 14px;\n" +
                "      }\n" +
                "      .hero-grid {\n" +
                "        grid-template-columns: 1fr;\n" +
                "        gap: 24px;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "      .hero-badge {\n" +
                "        margin: 0 auto 14px auto;\n" +
                "      }\n" +
                "      p.hero-desc {\n" +
                "        margin: 0 auto 20px auto;\n" +
                "        max-width: 100%;\n" +
                "      }\n" +
                "      .cta-row {\n" +
                "        flex-direction: column;\n" +
                "        align-items: center;\n" +
                "        width: 100%;\n" +
                "        gap: 10px;\n" +
                "        margin: 0 auto 20px auto;\n" +
                "      }\n" +
                "      .btn-hero-install, .btn-secondary {\n" +
                "        width: 100%;\n" +
                "        max-width: 340px;\n" +
                "        padding: 15px 16px;\n" +
                "      }\n" +
                "      .meta-pills {\n" +
                "        justify-content: center;\n" +
                "        gap: 10px;\n" +
                "        font-size: 11px;\n" +
                "      }\n" +
                "      /* Hide duplicate phone frame on actual mobile phone screen */\n" +
                "      .phone-frame {\n" +
                "        display: none;\n" +
                "      }\n" +
                "      .qr-card-desktop {\n" +
                "        display: none;\n" +
                "      }\n" +
                "      .phone-mockup-wrapper {\n" +
                "        display: none;\n" +
                "      }\n" +
                "    }\n" +
                "    footer {\n" +
                "      border-top: 1px solid rgba(255, 255, 255, 0.08);\n" +
                "      padding: 16px;\n" +
                "      text-align: center;\n" +
                "      color: #64748B;\n" +
                "      font-size: 11px;\n" +
                "      background: rgba(8, 12, 23, 0.8);\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <!-- Constrained background glow with zero overflow -->\n" +
                "  <div style=\"position: fixed; inset: 0; overflow: hidden; pointer-events: none; z-index: 0;\">\n" +
                "    <div style=\"position: absolute; top: -100px; left: 50%; transform: translateX(-50%); width: min(500px, 100vw); height: 350px; background: radial-gradient(circle, rgba(99, 102, 241, 0.22) 0%, rgba(236, 72, 153, 0.12) 40%, transparent 70%); filter: blur(50px);\"></div>\n" +
                "  </div>\n" +
                "\n" +
                "  <header>\n" +
                "    <div class=\"nav-container\">\n" +
                "      <a href=\"/\" class=\"brand\">\n" +
                "        <div class=\"brand-icon\">\n" +
                "          <svg width=\"18\" height=\"18\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"white\" stroke-width=\"2.5\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M4 19.5A2.5 2.5 0 0 1 6.5 17H20\"></path><path d=\"M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z\"></path></svg>\n" +
                "        </div>\n" +
                "        <div class=\"brand-title\">StudyFlow<span>AI</span></div>\n" +
                "      </a>\n" +
                "      <div class=\"nav-actions\">\n" +
                "        <div class=\"status-pill\">\n" +
                "          <div class=\"pulse-dot\"></div>\n" +
                "          <span>Cloud Active</span>\n" +
                "        </div>\n" +
                "        <button class=\"btn-nav-install\" onclick=\"startInstallation()\">Install App ⬇</button>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </header>\n" +
                "\n" +
                "  <main class=\"main-wrap\">\n" +
                "    <div class=\"hero-grid\">\n" +
                "      <!-- Left Column: Copy & Actions -->\n" +
                "      <div>\n" +
                "        <div class=\"hero-badge\">\n" +
                "          <span>✨</span> Official Release v1.0.0 &bull; Android APK\n" +
                "        </div>\n" +
                "        <h1 class=\"hero-title\">\n" +
                "          Your Personal AI <br>\n" +
                "          <span class=\"gradient-text\">Academic Companion</span>\n" +
                "        </h1>\n" +
                "        <p class=\"hero-desc\">\n" +
                "          Upload class timetables, syllabus, calendar of events, and exam schedules. StudyFlow AI crafts balanced study routines, highlights weak areas, and mentors you through each exam.\n" +
                "        </p>\n" +
                "        <div class=\"cta-row\">\n" +
                "          <button class=\"btn-hero-install\" id=\"mainInstallBtn\" onclick=\"startInstallation()\">\n" +
                "            <svg width=\"18\" height=\"18\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2.6\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4\"></path><polyline points=\"7 10 12 15 17 10\"></polyline><line x1=\"12\" y1=\"15\" x2=\"12\" y2=\"3\"></line></svg>\n" +
                "            Install StudyFlow AI\n" +
                "          </button>\n" +
                "          <a href=\"/download/app.apk\" class=\"btn-secondary\">\n" +
                "            <svg width=\"16\" height=\"16\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M3 15v4c0 1.1.9 2 2 2h14a2 2 0 0 0 2-2v-4M17 9l-5 5-5-5M12 12.8V2.5\"/></svg>\n" +
                "            Direct APK File (59MB)\n" +
                "          </a>\n" +
                "        </div>\n" +
                "        <div class=\"meta-pills\">\n" +
                "          <div>✓ Android 8.0+</div>\n" +
                "          <div>✓ 58.9 MB Size</div>\n" +
                "          <div>✓ Cloud Connected</div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "\n" +
                "      <!-- Right Column: Realistic Phone Mockup & Quick QR (Desktop Viewports) -->\n" +
                "      <div class=\"phone-mockup-wrapper\">\n" +
                "        <div class=\"phone-frame\">\n" +
                "          <div class=\"phone-notch\">\n" +
                "            <div class=\"camera-dot\"></div>\n" +
                "          </div>\n" +
                "          <div class=\"phone-screen\">\n" +
                "            <div class=\"screen-status-bar\">\n" +
                "              <span>9:41</span>\n" +
                "              <span>5G &bull; 100%</span>\n" +
                "            </div>\n" +
                "            <div class=\"screen-header\">\n" +
                "              <div>\n" +
                "                <div style=\"font-size: 10px; color: #94A3B8;\">Welcome Back</div>\n" +
                "                <div style=\"font-size: 12px; font-weight: 800; color: #F1F5F9;\">StudyFlow AI</div>\n" +
                "              </div>\n" +
                "              <div class=\"screen-avatar\">AI</div>\n" +
                "            </div>\n" +
                "            <div class=\"screen-content\">\n" +
                "              <div class=\"app-card-mock\" style=\"background: linear-gradient(135deg, rgba(99, 102, 241, 0.2), rgba(236, 72, 153, 0.15)); border-color: rgba(99, 102, 241, 0.3);\">\n" +
                "                <h5><span>📚 Academic Hub</span> <span class=\"app-tag\">SYNCED</span></h5>\n" +
                "                <p>Timetable &bull; Events &bull; Exams Active</p>\n" +
                "              </div>\n" +
                "              <div class=\"app-card-mock\">\n" +
                "                <h5><span>⏰ Next Class</span> <span style=\"color: #818CF8; font-size: 10px;\">10:00 AM</span></h5>\n" +
                "                <p>Computer Networks &bull; Room 302</p>\n" +
                "              </div>\n" +
                "              <div class=\"app-card-mock\">\n" +
                "                <h5><span>🤖 AI Study Gap</span> <span style=\"color: #10B981; font-size: 10px;\">Free Slot</span></h5>\n" +
                "                <p>Revise Graph Algorithms (Exam in 4 days)</p>\n" +
                "              </div>\n" +
                "              <div class=\"app-card-mock\">\n" +
                "                <h5><span>🎯 Today's Target</span> <span style=\"color: #F43F5E; font-size: 10px;\">4.5 / 6h</span></h5>\n" +
                "                <div style=\"width: 100%; height: 4px; background: rgba(255,255,255,0.1); border-radius: 4px; margin-top: 4px; overflow: hidden;\">\n" +
                "                  <div style=\"width: 75%; height: 100%; background: #6366F1;\"></div>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "            <div class=\"screen-bottom-nav\">\n" +
                "              <div class=\"nav-item-active\">🏠 Home</div>\n" +
                "              <div>📖 Subjects</div>\n" +
                "              <div>📅 Hub</div>\n" +
                "              <div>🤖 AI Tutor</div>\n" +
                "              <div>👤 Profile</div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <div class=\"qr-card-desktop\">\n" +
                "          <img src=\"https://api.qrserver.com/v1/create-qr-code/?size=160x160&data=https://studymate-ai-uuvd.onrender.com/download/app.apk\" alt=\"QR code\" />\n" +
                "          <div class=\"qr-card-info\">\n" +
                "            <h6>Scan on Phone</h6>\n" +
                "            <p>Point phone camera to download directly to your mobile device.</p>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Features Section -->\n" +
                "    <section class=\"features-section\">\n" +
                "      <div class=\"section-title\">\n" +
                "        <h2>Engineered for College Students</h2>\n" +
                "        <p>A smart, seamless system balancing college life, classes, and exams</p>\n" +
                "      </div>\n" +
                "      <div class=\"cards-grid\">\n" +
                "        <div class=\"feat-card\">\n" +
                "          <div class=\"feat-icon\">📅</div>\n" +
                "          <h3>Academic Hub & Timetable</h3>\n" +
                "          <p>Upload class slots, college holidays, and exam dates. The engine identifies optimal study gaps automatically.</p>\n" +
                "        </div>\n" +
                "        <div class=\"feat-card\">\n" +
                "          <div class=\"feat-icon\">🤖</div>\n" +
                "          <h3>AI Tutor & Quiz Engine</h3>\n" +
                "          <p>Stuck on a tricky concept? Ask your AI tutor for analogies, step-by-step breakdowns, and instant practice quizzes.</p>\n" +
                "        </div>\n" +
                "        <div class=\"feat-card\">\n" +
                "          <div class=\"feat-icon\">🎯</div>\n" +
                "          <h3>Focus & Pomodoro Timer</h3>\n" +
                "          <p>Custom focus intervals with break reminders and session tracking to keep your retention high.</p>\n" +
                "        </div>\n" +
                "        <div class=\"feat-card\">\n" +
                "          <div class=\"feat-icon\">📊</div>\n" +
                "          <h3>Mastery Analytics</h3>\n" +
                "          <p>Real-time visual reports tracking your weekly target hours, weak subjects, and readiness scores.</p>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </section>\n" +
                "  </main>\n" +
                "\n" +
                "  <!-- Installation & Redirect Modal -->\n" +
                "  <div class=\"modal-backdrop\" id=\"installModal\">\n" +
                "    <div class=\"modal-content\">\n" +
                "      <button class=\"modal-close\" onclick=\"closeInstallModal()\">&times;</button>\n" +
                "      <div class=\"download-anim-icon\">\n" +
                "        <svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"white\" stroke-width=\"2.6\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4\"></path><polyline points=\"7 10 12 15 17 10\"></polyline><line x1=\"12\" y1=\"15\" x2=\"12\" y2=\"3\"></line></svg>\n" +
                "      </div>\n" +
                "      <h3 style=\"font-size: 19px; font-weight: 800; margin-bottom: 4px;\">Starting Download...</h3>\n" +
                "      <p style=\"color: #94A3B8; font-size: 13px; margin-bottom: 12px;\">Your Android APK file is downloading now.</p>\n" +
                "      <div class=\"progress-bar-container\">\n" +
                "        <div class=\"progress-bar-fill\" id=\"progressBar\"></div>\n" +
                "      </div>\n" +
                "      <div class=\"step-list\">\n" +
                "        <div class=\"step-item\">\n" +
                "          <div class=\"step-num\">1</div>\n" +
                "          <div class=\"step-text\">\n" +
                "            <h4>Open Downloaded File</h4>\n" +
                "            <p>Tap <b>StudyFlowAI.apk</b> from your phone notification bar or browser downloads.</p>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <div class=\"step-item\">\n" +
                "          <div class=\"step-num\">2</div>\n" +
                "          <div class=\"step-text\">\n" +
                "            <h4>Allow Installation</h4>\n" +
                "            <p>If prompted by Android, tap <b>Settings</b> &rarr; enable <b>Allow from this source</b>.</p>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <div class=\"step-item\">\n" +
                "          <div class=\"step-num\">3</div>\n" +
                "          <div class=\"step-text\">\n" +
                "            <h4>Launch &amp; Excel</h4>\n" +
                "            <p>Open the app, sign up for your account, and enjoy AI study power!</p>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <div class=\"modal-footer-note\">\n" +
                "        Didn't start? <a href=\"/download/app.apk\" onclick=\"triggerDownloadAnalytics()\">Click here to download directly</a>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "\n" +
                "  <footer>\n" +
                "    StudyFlow AI &bull; Live Cloud Backend on Render (Java 17 Spring Boot 3.2) &bull; 2026\n" +
                "  </footer>\n" +
                "\n" +
                "  <script>\n" +
                "    function startInstallation() {\n" +
                "      const modal = document.getElementById('installModal');\n" +
                "      modal.style.display = 'flex';\n" +
                "      \n" +
                "      const bar = document.getElementById('progressBar');\n" +
                "      bar.style.width = '0%';\n" +
                "      \n" +
                "      // Animate progress bar\n" +
                "      setTimeout(() => { bar.style.width = '45%'; }, 100);\n" +
                "      setTimeout(() => { bar.style.width = '85%'; }, 600);\n" +
                "      setTimeout(() => { bar.style.width = '100%'; }, 1100);\n" +
                "      \n" +
                "      // Trigger download redirect\n" +
                "      setTimeout(() => {\n" +
                "        const link = document.createElement('a');\n" +
                "        link.href = '/download/app.apk';\n" +
                "        link.setAttribute('download', 'StudyFlowAI.apk');\n" +
                "        document.body.appendChild(link);\n" +
                "        link.click();\n" +
                "        document.body.removeChild(link);\n" +
                "      }, 800);\n" +
                "    }\n" +
                "    \n" +
                "    function closeInstallModal() {\n" +
                "      document.getElementById('installModal').style.display = 'none';\n" +
                "    }\n" +
                "    \n" +
                "    function triggerDownloadAnalytics() {\n" +
                "      console.log('Downloading StudyFlow AI APK...');\n" +
                "    }\n" +
                "    \n" +
                "    // If URL has ?auto=true or ?install=true, auto-trigger\n" +
                "    window.addEventListener('DOMContentLoaded', () => {\n" +
                "      const urlParams = new URLSearchParams(window.location.search);\n" +
                "      if (urlParams.get('auto') === 'true' || urlParams.get('install') === 'true') {\n" +
                "        startInstallation();\n" +
                "      }\n" +
                "    });\n" +
                "  </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
