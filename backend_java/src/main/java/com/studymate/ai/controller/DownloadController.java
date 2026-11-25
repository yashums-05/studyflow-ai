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
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover\">\n" +
                "  <title>StudyFlow AI &bull; Intelligent Academic Portal & App</title>\n" +
                "  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" +
                "  <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n" +
                "  <link href=\"https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;700&display=swap\" rel=\"stylesheet\">\n" +
                "  <style>\n" +
                "    :root {\n" +
                "      --primary: #6366F1;\n" +
                "      --primary-hover: #4F46E5;\n" +
                "      --primary-glow: rgba(99, 102, 241, 0.45);\n" +
                "      --accent: #EC4899;\n" +
                "      --cyan: #06B6D4;\n" +
                "      --bg-dark: #080C17;\n" +
                "      --bg-surface: #0E1424;\n" +
                "      --card-bg: rgba(18, 24, 40, 0.75);\n" +
                "      --card-border: rgba(255, 255, 255, 0.08);\n" +
                "      --text-main: #F8FAFC;\n" +
                "      --text-muted: #94A3B8;\n" +
                "      --text-dim: #64748B;\n" +
                "      --green: #10B981;\n" +
                "    }\n" +
                "    * { box-sizing: border-box; margin: 0; padding: 0; -webkit-tap-highlight-color: transparent; }\n" +
                "    html, body {\n" +
                "      background-color: var(--bg-dark);\n" +
                "      color: var(--text-main);\n" +
                "      font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;\n" +
                "      min-height: 100vh;\n" +
                "      width: 100%;\n" +
                "      overflow-x: hidden;\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "    }\n" +
                "    /* Background glow orbs */\n" +
                "    .glow-orb-1 {\n" +
                "      position: absolute;\n" +
                "      top: -100px;\n" +
                "      left: 50%;\n" +
                "      transform: translateX(-50%);\n" +
                "      width: min(800px, 100vw);\n" +
                "      height: 450px;\n" +
                "      background: radial-gradient(circle, rgba(99, 102, 241, 0.22) 0%, rgba(236, 72, 153, 0.12) 40%, transparent 70%);\n" +
                "      filter: blur(80px);\n" +
                "      pointer-events: none;\n" +
                "      z-index: 0;\n" +
                "    }\n" +
                "    .glow-orb-2 {\n" +
                "      position: absolute;\n" +
                "      bottom: 5%;\n" +
                "      right: -100px;\n" +
                "      width: 450px;\n" +
                "      height: 450px;\n" +
                "      background: radial-gradient(circle, rgba(6, 182, 212, 0.15) 0%, transparent 60%);\n" +
                "      filter: blur(90px);\n" +
                "      pointer-events: none;\n" +
                "      z-index: 0;\n" +
                "    }\n" +
                "    /* Responsive Navbar */\n" +
                "    header {\n" +
                "      position: sticky;\n" +
                "      top: 0;\n" +
                "      backdrop-filter: blur(20px);\n" +
                "      -webkit-backdrop-filter: blur(20px);\n" +
                "      background: rgba(8, 12, 23, 0.85);\n" +
                "      border-bottom: 1px solid var(--card-border);\n" +
                "      z-index: 50;\n" +
                "      padding: 14px 20px;\n" +
                "    }\n" +
                "    .nav-container {\n" +
                "      max-width: 1200px;\n" +
                "      margin: 0 auto;\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "      align-items: center;\n" +
                "      gap: 12px;\n" +
                "    }\n" +
                "    .brand {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 10px;\n" +
                "      text-decoration: none;\n" +
                "      color: #fff;\n" +
                "      flex-shrink: 0;\n" +
                "    }\n" +
                "    .brand-icon {\n" +
                "      width: 36px;\n" +
                "      height: 36px;\n" +
                "      background: linear-gradient(135deg, #6366F1, #EC4899);\n" +
                "      border-radius: 10px;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      box-shadow: 0 4px 12px var(--primary-glow);\n" +
                "    }\n" +
                "    .brand-title {\n" +
                "      font-size: 19px;\n" +
                "      font-weight: 800;\n" +
                "      letter-spacing: -0.5px;\n" +
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
                "      padding: 6px 12px;\n" +
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
                "      animation: pulse 2s infinite;\n" +
                "    }\n" +
                "    @keyframes pulse {\n" +
                "      0% { transform: scale(0.95); opacity: 0.8; }\n" +
                "      50% { transform: scale(1.3); opacity: 1; }\n" +
                "      100% { transform: scale(0.95); opacity: 0.8; }\n" +
                "    }\n" +
                "    .btn-nav-install {\n" +
                "      background: linear-gradient(135deg, var(--primary), var(--primary-hover));\n" +
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
                "      box-shadow: 0 4px 16px var(--primary-glow);\n" +
                "      transform: translateY(-1px);\n" +
                "    }\n" +
                "    /* Responsive Main Wrapper */\n" +
                "    .main-wrap {\n" +
                "      max-width: 1200px;\n" +
                "      width: 100%;\n" +
                "      margin: 0 auto;\n" +
                "      padding: 36px 20px 64px 20px;\n" +
                "      position: relative;\n" +
                "      z-index: 10;\n" +
                "      flex: 1;\n" +
                "    }\n" +
                "    /* Desktop Hero Split (55% Copy, 45% Phone Mockup) */\n" +
                "    .hero-grid {\n" +
                "      display: grid;\n" +
                "      grid-template-columns: 1.1fr 0.9fr;\n" +
                "      gap: 40px;\n" +
                "      align-items: center;\n" +
                "    }\n" +
                "    .hero-badge {\n" +
                "      display: inline-flex;\n" +
                "      align-items: center;\n" +
                "      gap: 8px;\n" +
                "      background: rgba(99, 102, 241, 0.15);\n" +
                "      border: 1px solid rgba(99, 102, 241, 0.35);\n" +
                "      color: #A5B4FC;\n" +
                "      font-size: 13px;\n" +
                "      font-weight: 700;\n" +
                "      padding: 6px 14px;\n" +
                "      border-radius: 999px;\n" +
                "      margin-bottom: 20px;\n" +
                "    }\n" +
                "    h1.hero-title {\n" +
                "      font-size: clamp(30px, 4.4vw, 50px);\n" +
                "      font-weight: 800;\n" +
                "      line-height: 1.15;\n" +
                "      margin-bottom: 18px;\n" +
                "      letter-spacing: -0.8px;\n" +
                "    }\n" +
                "    .gradient-text {\n" +
                "      background: linear-gradient(135deg, #FFFFFF 15%, #A5B4FC 60%, #EC4899 100%);\n" +
                "      -webkit-background-clip: text;\n" +
                "      -webkit-text-fill-color: transparent;\n" +
                "    }\n" +
                "    p.hero-desc {\n" +
                "      color: var(--text-muted);\n" +
                "      font-size: clamp(15px, 1.8vw, 17px);\n" +
                "      line-height: 1.6;\n" +
                "      margin-bottom: 28px;\n" +
                "      max-width: 540px;\n" +
                "    }\n" +
                "    .cta-row {\n" +
                "      display: flex;\n" +
                "      flex-wrap: wrap;\n" +
                "      align-items: center;\n" +
                "      gap: 14px;\n" +
                "      margin-bottom: 26px;\n" +
                "    }\n" +
                "    .btn-hero-install {\n" +
                "      background: linear-gradient(135deg, #6366F1 0%, #4F46E5 100%);\n" +
                "      color: #FFFFFF;\n" +
                "      padding: 16px 32px;\n" +
                "      border-radius: 14px;\n" +
                "      font-size: 16px;\n" +
                "      font-weight: 800;\n" +
                "      border: none;\n" +
                "      cursor: pointer;\n" +
                "      display: inline-flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      gap: 10px;\n" +
                "      box-shadow: 0 8px 24px var(--primary-glow);\n" +
                "      transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);\n" +
                "      text-decoration: none;\n" +
                "    }\n" +
                "    .btn-hero-install:hover {\n" +
                "      transform: translateY(-2px);\n" +
                "      box-shadow: 0 14px 36px rgba(99, 102, 241, 0.55);\n" +
                "    }\n" +
                "    .btn-secondary {\n" +
                "      background: rgba(255, 255, 255, 0.05);\n" +
                "      color: #CBD5E1;\n" +
                "      border: 1px solid var(--card-border);\n" +
                "      padding: 16px 22px;\n" +
                "      border-radius: 14px;\n" +
                "      font-size: 15px;\n" +
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
                "      gap: 16px;\n" +
                "      color: var(--text-dim);\n" +
                "      font-size: 13px;\n" +
                "      font-weight: 600;\n" +
                "    }\n" +
                "    .meta-pills div {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 5px;\n" +
                "    }\n" +
                "    /* Realistic Smartphone Frame (Aligned for Desktop & Mobile) */\n" +
                "    .phone-mockup-wrapper {\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      position: relative;\n" +
                "    }\n" +
                "    .phone-frame {\n" +
                "      width: 290px;\n" +
                "      height: 580px;\n" +
                "      background: #000000;\n" +
                "      border-radius: 44px;\n" +
                "      padding: 10px;\n" +
                "      box-shadow:\n" +
                "        0 0 0 3px #1F2937,\n" +
                "        0 25px 60px rgba(0, 0, 0, 0.8),\n" +
                "        0 0 40px rgba(99, 102, 241, 0.25);\n" +
                "      position: relative;\n" +
                "      overflow: hidden;\n" +
                "      border: 4px solid #374151;\n" +
                "      transition: transform 0.3s ease;\n" +
                "    }\n" +
                "    .phone-frame:hover {\n" +
                "      transform: translateY(-6px);\n" +
                "    }\n" +
                "    .phone-notch {\n" +
                "      width: 100px;\n" +
                "      height: 18px;\n" +
                "      background: #000000;\n" +
                "      border-radius: 12px;\n" +
                "      position: absolute;\n" +
                "      top: 14px;\n" +
                "      left: 50%;\n" +
                "      transform: translateX(-50%);\n" +
                "      z-index: 30;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      gap: 6px;\n" +
                "    }\n" +
                "    .camera-dot {\n" +
                "      width: 7px;\n" +
                "      height: 7px;\n" +
                "      background: #111827;\n" +
                "      border: 1px solid #374151;\n" +
                "      border-radius: 50%;\n" +
                "    }\n" +
                "    .phone-screen {\n" +
                "      width: 100%;\n" +
                "      height: 100%;\n" +
                "      background: #0B0F19;\n" +
                "      border-radius: 34px;\n" +
                "      overflow: hidden;\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "      position: relative;\n" +
                "      padding-top: 26px;\n" +
                "    }\n" +
                "    .screen-status-bar {\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "      align-items: center;\n" +
                "      padding: 4px 18px;\n" +
                "      font-size: 11px;\n" +
                "      font-weight: 700;\n" +
                "      color: #94A3B8;\n" +
                "    }\n" +
                "    .screen-header {\n" +
                "      padding: 10px 16px;\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "      align-items: center;\n" +
                "    }\n" +
                "    .screen-avatar {\n" +
                "      width: 32px;\n" +
                "      height: 32px;\n" +
                "      border-radius: 50%;\n" +
                "      background: linear-gradient(135deg, #6366F1, #EC4899);\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      font-weight: 800;\n" +
                "      font-size: 12px;\n" +
                "    }\n" +
                "    .screen-content {\n" +
                "      padding: 8px 14px;\n" +
                "      flex: 1;\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "      gap: 10px;\n" +
                "      overflow-y: hidden;\n" +
                "    }\n" +
                "    .app-card-mock {\n" +
                "      background: rgba(255, 255, 255, 0.05);\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.08);\n" +
                "      border-radius: 14px;\n" +
                "      padding: 10px 12px;\n" +
                "    }\n" +
                "    .app-card-mock h5 {\n" +
                "      font-size: 12px;\n" +
                "      font-weight: 700;\n" +
                "      color: #F1F5F9;\n" +
                "      margin-bottom: 3px;\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "    }\n" +
                "    .app-card-mock p {\n" +
                "      font-size: 11px;\n" +
                "      color: #94A3B8;\n" +
                "      line-height: 1.3;\n" +
                "    }\n" +
                "    .app-tag {\n" +
                "      display: inline-block;\n" +
                "      background: rgba(99, 102, 241, 0.25);\n" +
                "      color: #A5B4FC;\n" +
                "      font-size: 9px;\n" +
                "      font-weight: 800;\n" +
                "      padding: 2px 6px;\n" +
                "      border-radius: 4px;\n" +
                "    }\n" +
                "    .screen-bottom-nav {\n" +
                "      margin-top: auto;\n" +
                "      background: #070913;\n" +
                "      border-top: 1px solid rgba(255, 255, 255, 0.06);\n" +
                "      display: flex;\n" +
                "      justify-content: space-around;\n" +
                "      padding: 10px 4px 14px 4px;\n" +
                "      font-size: 10px;\n" +
                "      color: #64748B;\n" +
                "    }\n" +
                "    .nav-item-active {\n" +
                "      color: #818CF8;\n" +
                "      font-weight: 700;\n" +
                "    }\n" +
                "    /* QR Code Box (Desktop & Mobile Adaptive) */\n" +
                "    .qr-card-desktop {\n" +
                "      margin-top: 18px;\n" +
                "      background: var(--card-bg);\n" +
                "      border: 1px solid var(--card-border);\n" +
                "      border-radius: 16px;\n" +
                "      padding: 14px;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 14px;\n" +
                "      width: 290px;\n" +
                "    }\n" +
                "    .qr-card-desktop img {\n" +
                "      width: 68px;\n" +
                "      height: 68px;\n" +
                "      border-radius: 8px;\n" +
                "      background: #fff;\n" +
                "      padding: 4px;\n" +
                "      flex-shrink: 0;\n" +
                "    }\n" +
                "    .qr-card-info h6 {\n" +
                "      font-size: 12px;\n" +
                "      font-weight: 700;\n" +
                "      color: #F1F5F9;\n" +
                "      margin-bottom: 2px;\n" +
                "    }\n" +
                "    .qr-card-info p {\n" +
                "      font-size: 11px;\n" +
                "      color: var(--text-dim);\n" +
                "      line-height: 1.3;\n" +
                "    }\n" +
                "    /* Features Section */\n" +
                "    .features-section {\n" +
                "      margin-top: 60px;\n" +
                "    }\n" +
                "    .section-title {\n" +
                "      text-align: center;\n" +
                "      margin-bottom: 36px;\n" +
                "    }\n" +
                "    .section-title h2 {\n" +
                "      font-size: clamp(24px, 3.5vw, 32px);\n" +
                "      font-weight: 800;\n" +
                "      margin-bottom: 8px;\n" +
                "      letter-spacing: -0.5px;\n" +
                "    }\n" +
                "    .section-title p {\n" +
                "      color: var(--text-muted);\n" +
                "      font-size: 14px;\n" +
                "    }\n" +
                "    .cards-grid {\n" +
                "      display: grid;\n" +
                "      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));\n" +
                "      gap: 20px;\n" +
                "    }\n" +
                "    .feat-card {\n" +
                "      background: var(--card-bg);\n" +
                "      border: 1px solid var(--card-border);\n" +
                "      border-radius: 18px;\n" +
                "      padding: 22px;\n" +
                "      transition: all 0.25s;\n" +
                "    }\n" +
                "    .feat-card:hover {\n" +
                "      transform: translateY(-3px);\n" +
                "      border-color: rgba(99, 102, 241, 0.35);\n" +
                "      box-shadow: 0 12px 28px rgba(0,0,0,0.35);\n" +
                "    }\n" +
                "    .feat-icon {\n" +
                "      width: 40px;\n" +
                "      height: 40px;\n" +
                "      border-radius: 10px;\n" +
                "      background: rgba(99, 102, 241, 0.15);\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      font-size: 20px;\n" +
                "      margin-bottom: 14px;\n" +
                "    }\n" +
                "    .feat-card h3 {\n" +
                "      font-size: 16px;\n" +
                "      font-weight: 700;\n" +
                "      margin-bottom: 6px;\n" +
                "    }\n" +
                "    .feat-card p {\n" +
                "      color: var(--text-muted);\n" +
                "      font-size: 13px;\n" +
                "      line-height: 1.5;\n" +
                "    }\n" +
                "    /* Responsive Modal Dialog */\n" +
                "    .modal-backdrop {\n" +
                "      position: fixed;\n" +
                "      inset: 0;\n" +
                "      background: rgba(0, 0, 0, 0.82);\n" +
                "      backdrop-filter: blur(12px);\n" +
                "      -webkit-backdrop-filter: blur(12px);\n" +
                "      z-index: 100;\n" +
                "      display: none;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      padding: 16px;\n" +
                "      animation: fadeIn 0.2s ease-out forwards;\n" +
                "    }\n" +
                "    @keyframes fadeIn {\n" +
                "      from { opacity: 0; }\n" +
                "      to { opacity: 1; }\n" +
                "    }\n" +
                "    .modal-content {\n" +
                "      background: #0E1322;\n" +
                "      border: 1px solid rgba(99, 102, 241, 0.35);\n" +
                "      border-radius: 24px;\n" +
                "      max-width: 480px;\n" +
                "      width: 100%;\n" +
                "      max-height: 90vh;\n" +
                "      overflow-y: auto;\n" +
                "      padding: 30px 24px;\n" +
                "      box-shadow: 0 24px 60px rgba(0,0,0,0.8), 0 0 35px rgba(99, 102, 241, 0.2);\n" +
                "      position: relative;\n" +
                "      text-align: center;\n" +
                "      animation: slideUp 0.25s cubic-bezier(0.16, 1, 0.3, 1) forwards;\n" +
                "    }\n" +
                "    @keyframes slideUp {\n" +
                "      from { transform: translateY(24px) scale(0.96); opacity: 0; }\n" +
                "      to { transform: translateY(0) scale(1); opacity: 1; }\n" +
                "    }\n" +
                "    .modal-close {\n" +
                "      position: absolute;\n" +
                "      top: 16px;\n" +
                "      right: 16px;\n" +
                "      background: rgba(255, 255, 255, 0.08);\n" +
                "      border: none;\n" +
                "      color: var(--text-muted);\n" +
                "      width: 32px;\n" +
                "      height: 32px;\n" +
                "      border-radius: 50%;\n" +
                "      cursor: pointer;\n" +
                "      font-size: 18px;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      transition: all 0.2s;\n" +
                "    }\n" +
                "    .modal-close:hover {\n" +
                "      background: rgba(255, 255, 255, 0.18);\n" +
                "      color: #fff;\n" +
                "    }\n" +
                "    .download-anim-icon {\n" +
                "      width: 56px;\n" +
                "      height: 56px;\n" +
                "      background: linear-gradient(135deg, #10B981, #059669);\n" +
                "      border-radius: 16px;\n" +
                "      margin: 0 auto 14px auto;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      box-shadow: 0 8px 20px rgba(16, 185, 129, 0.4);\n" +
                "      animation: bounce 1.2s infinite alternate ease-in-out;\n" +
                "    }\n" +
                "    @keyframes bounce {\n" +
                "      from { transform: translateY(0); }\n" +
                "      to { transform: translateY(-6px); }\n" +
                "    }\n" +
                "    .progress-bar-container {\n" +
                "      width: 100%;\n" +
                "      height: 7px;\n" +
                "      background: rgba(255, 255, 255, 0.08);\n" +
                "      border-radius: 999px;\n" +
                "      overflow: hidden;\n" +
                "      margin: 18px 0 20px 0;\n" +
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
                "      border-radius: 16px;\n" +
                "      padding: 16px;\n" +
                "      margin-bottom: 20px;\n" +
                "    }\n" +
                "    .step-item {\n" +
                "      display: flex;\n" +
                "      align-items: flex-start;\n" +
                "      gap: 10px;\n" +
                "      margin-bottom: 12px;\n" +
                "    }\n" +
                "    .step-item:last-child { margin-bottom: 0; }\n" +
                "    .step-num {\n" +
                "      background: rgba(99, 102, 241, 0.25);\n" +
                "      color: #A5B4FC;\n" +
                "      font-weight: 800;\n" +
                "      font-size: 11px;\n" +
                "      width: 22px;\n" +
                "      height: 22px;\n" +
                "      border-radius: 50%;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      flex-shrink: 0;\n" +
                "      margin-top: 1px;\n" +
                "    }\n" +
                "    .step-text h4 { font-size: 13px; font-weight: 700; color: #F1F5F9; margin-bottom: 2px; }\n" +
                "    .step-text p { font-size: 12px; color: var(--text-muted); line-height: 1.35; }\n" +
                "    .modal-footer-note {\n" +
                "      font-size: 12px;\n" +
                "      color: var(--text-dim);\n" +
                "    }\n" +
                "    .modal-footer-note a {\n" +
                "      color: #818CF8;\n" +
                "      font-weight: 600;\n" +
                "      text-decoration: underline;\n" +
                "      cursor: pointer;\n" +
                "    }\n" +
                "    /* Mobile Viewport Alignments (<768px) */\n" +
                "    @media (max-width: 768px) {\n" +
                "      header { padding: 12px 16px; }\n" +
                "      .status-pill span.status-text { display: none; }\n" +
                "      .status-pill { padding: 6px 8px; }\n" +
                "      .main-wrap { padding: 24px 16px 48px 16px; }\n" +
                "      .hero-grid {\n" +
                "        grid-template-columns: 1fr;\n" +
                "        gap: 32px;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "      .hero-badge { margin: 0 auto 16px auto; }\n" +
                "      p.hero-desc { margin: 0 auto 24px auto; }\n" +
                "      .cta-row {\n" +
                "        flex-direction: column;\n" +
                "        width: 100%;\n" +
                "        gap: 10px;\n" +
                "      }\n" +
                "      .btn-hero-install, .btn-secondary {\n" +
                "        width: 100%;\n" +
                "        padding: 16px 20px;\n" +
                "        font-size: 16px;\n" +
                "      }\n" +
                "      .meta-pills {\n" +
                "        justify-content: center;\n" +
                "        gap: 12px;\n" +
                "        font-size: 12px;\n" +
                "      }\n" +
                "      .phone-frame {\n" +
                "        width: 260px;\n" +
                "        height: 520px;\n" +
                "        border-radius: 38px;\n" +
                "      }\n" +
                "      .qr-card-desktop {\n" +
                "        width: 100%;\n" +
                "        max-width: 300px;\n" +
                "      }\n" +
                "    }\n" +
                "    /* Extra Small Phone (<400px) */\n" +
                "    @media (max-width: 400px) {\n" +
                "      .brand-title { font-size: 17px; }\n" +
                "      .btn-nav-install { padding: 6px 12px; font-size: 12px; }\n" +
                "      .phone-frame { width: 230px; height: 460px; }\n" +
                "    }\n" +
                "    footer {\n" +
                "      border-top: 1px solid var(--card-border);\n" +
                "      padding: 20px 16px;\n" +
                "      text-align: center;\n" +
                "      color: var(--text-dim);\n" +
                "      font-size: 12px;\n" +
                "      background: rgba(8, 12, 23, 0.7);\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"glow-orb-1\"></div>\n" +
                "  <div class=\"glow-orb-2\"></div>\n" +
                "\n" +
                "  <header>\n" +
                "    <div class=\"nav-container\">\n" +
                "      <a href=\"/\" class=\"brand\">\n" +
                "        <div class=\"brand-icon\">\n" +
                "          <svg width=\"20\" height=\"20\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"white\" stroke-width=\"2.5\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M4 19.5A2.5 2.5 0 0 1 6.5 17H20\"></path><path d=\"M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z\"></path></svg>\n" +
                "        </div>\n" +
                "        <div class=\"brand-title\">StudyFlow<span>AI</span></div>\n" +
                "      </a>\n" +
                "      <div class=\"nav-actions\">\n" +
                "        <div class=\"status-pill\">\n" +
                "          <div class=\"pulse-dot\"></div>\n" +
                "          <span class=\"status-text\">Cloud Active</span>\n" +
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
                "          Upload your class timetables, syllabus, calendar of events, and exam schedules. StudyFlow AI crafts balanced study routines, highlights weak areas, and mentors you through each exam.\n" +
                "        </p>\n" +
                "        <div class=\"cta-row\">\n" +
                "          <button class=\"btn-hero-install\" id=\"mainInstallBtn\" onclick=\"startInstallation()\">\n" +
                "            <svg width=\"20\" height=\"20\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2.6\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4\"></path><polyline points=\"7 10 12 15 17 10\"></polyline><line x1=\"12\" y1=\"15\" x2=\"12\" y2=\"3\"></line></svg>\n" +
                "            Install StudyFlow AI\n" +
                "          </button>\n" +
                "          <a href=\"/download/app.apk\" class=\"btn-secondary\">\n" +
                "            <svg width=\"18\" height=\"18\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M3 15v4c0 1.1.9 2 2 2h14a2 2 0 0 0 2-2v-4M17 9l-5 5-5-5M12 12.8V2.5\"/></svg>\n" +
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
                "      <!-- Right Column: Realistic Phone Mockup & Quick QR -->\n" +
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
                "                <div style=\"font-size: 11px; color: #94A3B8;\">Welcome Back</div>\n" +
                "                <div style=\"font-size: 13px; font-weight: 800; color: #F1F5F9;\">StudyFlow AI</div>\n" +
                "              </div>\n" +
                "              <div class=\"screen-avatar\">AI</div>\n" +
                "            </div>\n" +
                "            <div class=\"screen-content\">\n" +
                "              <!-- Academic Hub Card -->\n" +
                "              <div class=\"app-card-mock\" style=\"background: linear-gradient(135deg, rgba(99, 102, 241, 0.2), rgba(236, 72, 153, 0.15)); border-color: rgba(99, 102, 241, 0.3);\">\n" +
                "                <h5><span>📚 Academic Hub</span> <span class=\"app-tag\">SYNCED</span></h5>\n" +
                "                <p>Timetable &bull; Events &bull; Exams Active</p>\n" +
                "              </div>\n" +
                "              <!-- Next Schedule Slot -->\n" +
                "              <div class=\"app-card-mock\">\n" +
                "                <h5><span>⏰ Next Class</span> <span style=\"color: #818CF8; font-size: 10px;\">10:00 AM</span></h5>\n" +
                "                <p>Computer Networks &bull; Room 302</p>\n" +
                "              </div>\n" +
                "              <!-- AI Insight Recommendation -->\n" +
                "              <div class=\"app-card-mock\">\n" +
                "                <h5><span>🤖 AI Study Gap</span> <span style=\"color: #10B981; font-size: 10px;\">Free Slot</span></h5>\n" +
                "                <p>Revise Graph Algorithms (Exam in 4 days)</p>\n" +
                "              </div>\n" +
                "              <!-- Focus Stats -->\n" +
                "              <div class=\"app-card-mock\">\n" +
                "                <h5><span>🎯 Today's Target</span> <span style=\"color: #F43F5E; font-size: 10px;\">4.5 / 6h</span></h5>\n" +
                "                <div style=\"width: 100%; height: 4px; background: rgba(255,255,255,0.1); border-radius: 4px; margin-top: 5px; overflow: hidden;\">\n" +
                "                  <div style=\"width: 75%; height: 100%; background: #6366F1;\"></div>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "            <!-- Mobile App Bottom Nav Mockup -->\n" +
                "            <div class=\"screen-bottom-nav\">\n" +
                "              <div class=\"nav-item-active\">🏠 Home</div>\n" +
                "              <div>📖 Subjects</div>\n" +
                "              <div>📅 Hub</div>\n" +
                "              <div>🤖 AI Tutor</div>\n" +
                "              <div>👤 Profile</div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <!-- QR Code for Phone Scan -->\n" +
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
                "        <svg width=\"28\" height=\"28\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"white\" stroke-width=\"2.6\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4\"></path><polyline points=\"7 10 12 15 17 10\"></polyline><line x1=\"12\" y1=\"15\" x2=\"12\" y2=\"3\"></line></svg>\n" +
                "      </div>\n" +
                "      <h3 style=\"font-size: 20px; font-weight: 800; margin-bottom: 4px;\">Starting Download...</h3>\n" +
                "      <p style=\"color: var(--text-muted); font-size: 13px; margin-bottom: 12px;\">Your Android APK file is downloading now.</p>\n" +
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
