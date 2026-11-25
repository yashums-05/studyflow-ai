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
                "  <link href=\"https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;700&display=swap\" rel=\"stylesheet\">\n" +
                "  <style>\n" +
                "    :root {\n" +
                "      --primary: #6366F1;\n" +
                "      --primary-hover: #4F46E5;\n" +
                "      --primary-glow: rgba(99, 102, 241, 0.45);\n" +
                "      --accent: #EC4899;\n" +
                "      --cyan: #06B6D4;\n" +
                "      --bg-dark: #070913;\n" +
                "      --card-bg: rgba(18, 24, 38, 0.75);\n" +
                "      --card-border: rgba(255, 255, 255, 0.08);\n" +
                "      --text-main: #F8FAFC;\n" +
                "      --text-muted: #94A3B8;\n" +
                "      --text-dim: #64748B;\n" +
                "      --green: #10B981;\n" +
                "    }\n" +
                "    * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Plus Jakarta Sans', sans-serif; }\n" +
                "    body {\n" +
                "      background-color: var(--bg-dark);\n" +
                "      color: var(--text-main);\n" +
                "      min-height: 100vh;\n" +
                "      position: relative;\n" +
                "      overflow-x: hidden;\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "    }\n" +
                "    /* Glow orbs */\n" +
                "    .glow-orb-1 {\n" +
                "      position: absolute;\n" +
                "      top: -150px;\n" +
                "      left: 50%;\n" +
                "      transform: translateX(-50%);\n" +
                "      width: 700px;\n" +
                "      height: 400px;\n" +
                "      background: radial-gradient(circle, rgba(99, 102, 241, 0.22) 0%, rgba(236, 72, 153, 0.12) 40%, transparent 70%);\n" +
                "      filter: blur(80px);\n" +
                "      pointer-events: none;\n" +
                "      z-index: 0;\n" +
                "    }\n" +
                "    .glow-orb-2 {\n" +
                "      position: absolute;\n" +
                "      bottom: 10%;\n" +
                "      right: -100px;\n" +
                "      width: 500px;\n" +
                "      height: 400px;\n" +
                "      background: radial-gradient(circle, rgba(6, 182, 212, 0.15) 0%, transparent 60%);\n" +
                "      filter: blur(90px);\n" +
                "      pointer-events: none;\n" +
                "      z-index: 0;\n" +
                "    }\n" +
                "    /* Navbar */\n" +
                "    header {\n" +
                "      position: sticky;\n" +
                "      top: 0;\n" +
                "      backdrop-filter: blur(20px);\n" +
                "      background: rgba(7, 9, 19, 0.8);\n" +
                "      border-bottom: 1px solid var(--card-border);\n" +
                "      z-index: 50;\n" +
                "      padding: 16px 28px;\n" +
                "    }\n" +
                "    .nav-container {\n" +
                "      max-width: 1180px;\n" +
                "      margin: 0 auto;\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "      align-items: center;\n" +
                "    }\n" +
                "    .brand {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 12px;\n" +
                "      text-decoration: none;\n" +
                "      color: #fff;\n" +
                "    }\n" +
                "    .brand-icon {\n" +
                "      width: 38px;\n" +
                "      height: 38px;\n" +
                "      background: linear-gradient(135deg, #6366F1, #EC4899);\n" +
                "      border-radius: 10px;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      box-shadow: 0 4px 12px var(--primary-glow);\n" +
                "    }\n" +
                "    .brand-title {\n" +
                "      font-size: 20px;\n" +
                "      font-weight: 800;\n" +
                "      letter-spacing: -0.5px;\n" +
                "    }\n" +
                "    .brand-title span { color: #818CF8; }\n" +
                "    .nav-actions {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 16px;\n" +
                "    }\n" +
                "    .status-pill {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 8px;\n" +
                "      background: rgba(16, 185, 129, 0.12);\n" +
                "      border: 1px solid rgba(16, 185, 129, 0.3);\n" +
                "      padding: 6px 14px;\n" +
                "      border-radius: 999px;\n" +
                "      font-size: 13px;\n" +
                "      font-weight: 600;\n" +
                "      color: #34D399;\n" +
                "    }\n" +
                "    .pulse-dot {\n" +
                "      width: 8px;\n" +
                "      height: 8px;\n" +
                "      background: #10B981;\n" +
                "      border-radius: 50%;\n" +
                "      box-shadow: 0 0 10px #10B981;\n" +
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
                "      padding: 9px 18px;\n" +
                "      border-radius: 10px;\n" +
                "      text-decoration: none;\n" +
                "      font-weight: 700;\n" +
                "      font-size: 14px;\n" +
                "      border: none;\n" +
                "      cursor: pointer;\n" +
                "      transition: all 0.2s;\n" +
                "    }\n" +
                "    .btn-nav-install:hover {\n" +
                "      box-shadow: 0 6px 20px var(--primary-glow);\n" +
                "      transform: translateY(-1px);\n" +
                "    }\n" +
                "    /* Hero Container */\n" +
                "    .main-wrap {\n" +
                "      max-width: 1140px;\n" +
                "      margin: 0 auto;\n" +
                "      padding: 56px 24px 72px 24px;\n" +
                "      position: relative;\n" +
                "      z-index: 10;\n" +
                "      flex: 1;\n" +
                "    }\n" +
                "    .hero-grid {\n" +
                "      display: grid;\n" +
                "      grid-template-columns: 1.15fr 0.85fr;\n" +
                "      gap: 48px;\n" +
                "      align-items: center;\n" +
                "    }\n" +
                "    @media (max-width: 900px) {\n" +
                "      .hero-grid { grid-template-columns: 1fr; text-align: center; }\n" +
                "      .cta-row { justify-content: center; }\n" +
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
                "      padding: 7px 16px;\n" +
                "      border-radius: 999px;\n" +
                "      margin-bottom: 24px;\n" +
                "      letter-spacing: 0.3px;\n" +
                "    }\n" +
                "    h1.hero-title {\n" +
                "      font-size: 44px;\n" +
                "      font-weight: 800;\n" +
                "      line-height: 1.18;\n" +
                "      margin-bottom: 20px;\n" +
                "      letter-spacing: -1.2px;\n" +
                "    }\n" +
                "    .gradient-text {\n" +
                "      background: linear-gradient(135deg, #FFFFFF 20%, #A5B4FC 60%, #EC4899 100%);\n" +
                "      -webkit-background-clip: text;\n" +
                "      -webkit-text-fill-color: transparent;\n" +
                "    }\n" +
                "    p.hero-desc {\n" +
                "      color: var(--text-muted);\n" +
                "      font-size: 18px;\n" +
                "      line-height: 1.6;\n" +
                "      margin-bottom: 34px;\n" +
                "    }\n" +
                "    .cta-row {\n" +
                "      display: flex;\n" +
                "      flex-wrap: wrap;\n" +
                "      align-items: center;\n" +
                "      gap: 16px;\n" +
                "      margin-bottom: 32px;\n" +
                "    }\n" +
                "    .btn-hero-install {\n" +
                "      background: linear-gradient(135deg, #6366F1 0%, #4F46E5 100%);\n" +
                "      color: #FFFFFF;\n" +
                "      padding: 18px 36px;\n" +
                "      border-radius: 16px;\n" +
                "      font-size: 17px;\n" +
                "      font-weight: 800;\n" +
                "      border: none;\n" +
                "      cursor: pointer;\n" +
                "      display: inline-flex;\n" +
                "      align-items: center;\n" +
                "      gap: 12px;\n" +
                "      box-shadow: 0 10px 30px var(--primary-glow);\n" +
                "      transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);\n" +
                "      position: relative;\n" +
                "      overflow: hidden;\n" +
                "    }\n" +
                "    .btn-hero-install:hover {\n" +
                "      transform: translateY(-3px) scale(1.02);\n" +
                "      box-shadow: 0 16px 40px rgba(99, 102, 241, 0.6);\n" +
                "    }\n" +
                "    .btn-hero-install:active {\n" +
                "      transform: translateY(-1px) scale(0.99);\n" +
                "    }\n" +
                "    .btn-secondary {\n" +
                "      background: rgba(255, 255, 255, 0.05);\n" +
                "      color: #CBD5E1;\n" +
                "      border: 1px solid var(--card-border);\n" +
                "      padding: 18px 26px;\n" +
                "      border-radius: 16px;\n" +
                "      font-size: 15px;\n" +
                "      font-weight: 700;\n" +
                "      cursor: pointer;\n" +
                "      text-decoration: none;\n" +
                "      display: inline-flex;\n" +
                "      align-items: center;\n" +
                "      gap: 8px;\n" +
                "      transition: all 0.2s;\n" +
                "    }\n" +
                "    .btn-secondary:hover {\n" +
                "      background: rgba(255, 255, 255, 0.09);\n" +
                "      border-color: rgba(255, 255, 255, 0.2);\n" +
                "      color: #fff;\n" +
                "    }\n" +
                "    .meta-pills {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 20px;\n" +
                "      color: var(--text-dim);\n" +
                "      font-size: 13px;\n" +
                "      font-weight: 600;\n" +
                "    }\n" +
                "    .meta-pills div {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 6px;\n" +
                "    }\n" +
                "    /* Interactive Install & QR Card */\n" +
                "    .installer-box {\n" +
                "      background: var(--card-bg);\n" +
                "      border: 1px solid var(--card-border);\n" +
                "      border-radius: 28px;\n" +
                "      padding: 32px;\n" +
                "      box-shadow: 0 24px 60px rgba(0,0,0,0.5);\n" +
                "      backdrop-filter: blur(24px);\n" +
                "      text-align: center;\n" +
                "      position: relative;\n" +
                "    }\n" +
                "    .box-badge {\n" +
                "      position: absolute;\n" +
                "      top: -12px;\n" +
                "      right: 32px;\n" +
                "      background: linear-gradient(135deg, #10B981, #059669);\n" +
                "      color: #fff;\n" +
                "      font-size: 11px;\n" +
                "      font-weight: 800;\n" +
                "      text-transform: uppercase;\n" +
                "      letter-spacing: 0.8px;\n" +
                "      padding: 4px 12px;\n" +
                "      border-radius: 999px;\n" +
                "      box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);\n" +
                "    }\n" +
                "    .qr-frame {\n" +
                "      background: #FFFFFF;\n" +
                "      padding: 16px;\n" +
                "      border-radius: 20px;\n" +
                "      display: inline-block;\n" +
                "      margin: 16px 0;\n" +
                "      box-shadow: 0 10px 30px rgba(0,0,0,0.3);\n" +
                "      transition: transform 0.25s;\n" +
                "    }\n" +
                "    .qr-frame:hover {\n" +
                "      transform: scale(1.03);\n" +
                "    }\n" +
                "    .qr-frame img {\n" +
                "      display: block;\n" +
                "      width: 170px;\n" +
                "      height: 170px;\n" +
                "      border-radius: 8px;\n" +
                "    }\n" +
                "    .box-title {\n" +
                "      font-size: 20px;\n" +
                "      font-weight: 700;\n" +
                "      margin-bottom: 4px;\n" +
                "    }\n" +
                "    .box-subtitle {\n" +
                "      color: var(--text-muted);\n" +
                "      font-size: 13px;\n" +
                "      margin-bottom: 12px;\n" +
                "    }\n" +
                "    .direct-link-btn {\n" +
                "      display: block;\n" +
                "      width: 100%;\n" +
                "      background: rgba(99, 102, 241, 0.12);\n" +
                "      border: 1px solid rgba(99, 102, 241, 0.3);\n" +
                "      color: #A5B4FC;\n" +
                "      text-decoration: none;\n" +
                "      padding: 14px;\n" +
                "      border-radius: 14px;\n" +
                "      font-weight: 700;\n" +
                "      font-size: 14px;\n" +
                "      transition: all 0.2s;\n" +
                "      margin-top: 12px;\n" +
                "    }\n" +
                "    .direct-link-btn:hover {\n" +
                "      background: rgba(99, 102, 241, 0.25);\n" +
                "      color: #fff;\n" +
                "    }\n" +
                "    /* Features Grid */\n" +
                "    .features-section {\n" +
                "      margin-top: 72px;\n" +
                "    }\n" +
                "    .section-title {\n" +
                "      text-align: center;\n" +
                "      margin-bottom: 40px;\n" +
                "    }\n" +
                "    .section-title h2 {\n" +
                "      font-size: 28px;\n" +
                "      font-weight: 800;\n" +
                "      margin-bottom: 8px;\n" +
                "      letter-spacing: -0.5px;\n" +
                "    }\n" +
                "    .section-title p {\n" +
                "      color: var(--text-muted);\n" +
                "      font-size: 15px;\n" +
                "    }\n" +
                "    .cards-grid {\n" +
                "      display: grid;\n" +
                "      grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));\n" +
                "      gap: 24px;\n" +
                "    }\n" +
                "    .feat-card {\n" +
                "      background: var(--card-bg);\n" +
                "      border: 1px solid var(--card-border);\n" +
                "      border-radius: 20px;\n" +
                "      padding: 26px;\n" +
                "      transition: all 0.25s;\n" +
                "    }\n" +
                "    .feat-card:hover {\n" +
                "      transform: translateY(-4px);\n" +
                "      border-color: rgba(99, 102, 241, 0.3);\n" +
                "      box-shadow: 0 16px 36px rgba(0,0,0,0.3);\n" +
                "    }\n" +
                "    .feat-icon {\n" +
                "      width: 44px;\n" +
                "      height: 44px;\n" +
                "      border-radius: 12px;\n" +
                "      background: rgba(99, 102, 241, 0.15);\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      font-size: 22px;\n" +
                "      margin-bottom: 18px;\n" +
                "    }\n" +
                "    .feat-card h3 {\n" +
                "      font-size: 18px;\n" +
                "      font-weight: 700;\n" +
                "      margin-bottom: 8px;\n" +
                "    }\n" +
                "    .feat-card p {\n" +
                "      color: var(--text-muted);\n" +
                "      font-size: 14px;\n" +
                "      line-height: 1.5;\n" +
                "    }\n" +
                "    /* Modal Dialog */\n" +
                "    .modal-backdrop {\n" +
                "      position: fixed;\n" +
                "      inset: 0;\n" +
                "      background: rgba(0, 0, 0, 0.8);\n" +
                "      backdrop-filter: blur(12px);\n" +
                "      z-index: 100;\n" +
                "      display: none;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      padding: 20px;\n" +
                "      animation: fadeIn 0.25s ease-out forwards;\n" +
                "    }\n" +
                "    @keyframes fadeIn {\n" +
                "      from { opacity: 0; }\n" +
                "      to { opacity: 1; }\n" +
                "    }\n" +
                "    .modal-content {\n" +
                "      background: #0E1322;\n" +
                "      border: 1px solid rgba(99, 102, 241, 0.35);\n" +
                "      border-radius: 28px;\n" +
                "      max-width: 520px;\n" +
                "      width: 100%;\n" +
                "      padding: 36px 30px;\n" +
                "      box-shadow: 0 30px 80px rgba(0,0,0,0.8), 0 0 40px rgba(99, 102, 241, 0.2);\n" +
                "      position: relative;\n" +
                "      text-align: center;\n" +
                "      animation: slideUp 0.3s cubic-bezier(0.16, 1, 0.3, 1) forwards;\n" +
                "    }\n" +
                "    @keyframes slideUp {\n" +
                "      from { transform: translateY(30px) scale(0.96); opacity: 0; }\n" +
                "      to { transform: translateY(0) scale(1); opacity: 1; }\n" +
                "    }\n" +
                "    .modal-close {\n" +
                "      position: absolute;\n" +
                "      top: 20px;\n" +
                "      right: 20px;\n" +
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
                "      width: 64px;\n" +
                "      height: 64px;\n" +
                "      background: linear-gradient(135deg, #10B981, #059669);\n" +
                "      border-radius: 20px;\n" +
                "      margin: 0 auto 18px auto;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      box-shadow: 0 10px 24px rgba(16, 185, 129, 0.4);\n" +
                "      animation: bounce 1.2s infinite alternate ease-in-out;\n" +
                "    }\n" +
                "    @keyframes bounce {\n" +
                "      from { transform: translateY(0); }\n" +
                "      to { transform: translateY(-8px); }\n" +
                "    }\n" +
                "    .progress-bar-container {\n" +
                "      width: 100%;\n" +
                "      height: 8px;\n" +
                "      background: rgba(255, 255, 255, 0.08);\n" +
                "      border-radius: 999px;\n" +
                "      overflow: hidden;\n" +
                "      margin: 20px 0 24px 0;\n" +
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
                "      border-radius: 18px;\n" +
                "      padding: 18px;\n" +
                "      margin-bottom: 24px;\n" +
                "    }\n" +
                "    .step-item {\n" +
                "      display: flex;\n" +
                "      align-items: flex-start;\n" +
                "      gap: 12px;\n" +
                "      margin-bottom: 14px;\n" +
                "    }\n" +
                "    .step-item:last-child { margin-bottom: 0; }\n" +
                "    .step-num {\n" +
                "      background: rgba(99, 102, 241, 0.25);\n" +
                "      color: #A5B4FC;\n" +
                "      font-weight: 800;\n" +
                "      font-size: 12px;\n" +
                "      width: 24px;\n" +
                "      height: 24px;\n" +
                "      border-radius: 50%;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      flex-shrink: 0;\n" +
                "      margin-top: 1px;\n" +
                "    }\n" +
                "    .step-text h4 { font-size: 14px; font-weight: 700; color: #F1F5F9; margin-bottom: 2px; }\n" +
                "    .step-text p { font-size: 12px; color: var(--text-muted); line-height: 1.4; }\n" +
                "    .modal-footer-note {\n" +
                "      font-size: 13px;\n" +
                "      color: var(--text-dim);\n" +
                "    }\n" +
                "    .modal-footer-note a {\n" +
                "      color: #818CF8;\n" +
                "      font-weight: 600;\n" +
                "      text-decoration: underline;\n" +
                "      cursor: pointer;\n" +
                "    }\n" +
                "    /* Footer */\n" +
                "    footer {\n" +
                "      border-top: 1px solid var(--card-border);\n" +
                "      padding: 24px;\n" +
                "      text-align: center;\n" +
                "      color: var(--text-dim);\n" +
                "      font-size: 13px;\n" +
                "      background: rgba(7, 9, 19, 0.6);\n" +
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
                "          <svg width=\"22\" height=\"22\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"white\" stroke-width=\"2.4\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M4 19.5A2.5 2.5 0 0 1 6.5 17H20\"></path><path d=\"M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z\"></path></svg>\n" +
                "        </div>\n" +
                "        <div class=\"brand-title\">StudyFlow<span>AI</span></div>\n" +
                "      </a>\n" +
                "      <div class=\"nav-actions\">\n" +
                "        <div class=\"status-pill\">\n" +
                "          <div class=\"pulse-dot\"></div>\n" +
                "          Cloud Connected\n" +
                "        </div>\n" +
                "        <button class=\"btn-nav-install\" onclick=\"startInstallation()\">Install App ⬇</button>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </header>\n" +
                "\n" +
                "  <main class=\"main-wrap\">\n" +
                "    <div class=\"hero-grid\">\n" +
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
                "            <svg width=\"22\" height=\"22\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2.6\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4\"></path><polyline points=\"7 10 12 15 17 10\"></polyline><line x1=\"12\" y1=\"15\" x2=\"12\" y2=\"3\"></line></svg>\n" +
                "            Install StudyFlow AI\n" +
                "          </button>\n" +
                "          <a href=\"#qr-section\" class=\"btn-secondary\">\n" +
                "            <svg width=\"18\" height=\"18\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><rect x=\"3\" y=\"3\" width=\"7\" height=\"7\"></rect><rect x=\"14\" y=\"3\" width=\"7\" height=\"7\"></rect><rect x=\"14\" y=\"14\" width=\"7\" height=\"7\"></rect><rect x=\"3\" y=\"14\" width=\"7\" height=\"7\"></rect></svg>\n" +
                "            Scan QR Code\n" +
                "          </a>\n" +
                "        </div>\n" +
                "        <div class=\"meta-pills\">\n" +
                "          <div>✓ Android 8.0+</div>\n" +
                "          <div>✓ 58.9 MB Lightweight</div>\n" +
                "          <div>✓ Cloud Synced</div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "\n" +
                "      <div id=\"qr-section\">\n" +
                "        <div class=\"installer-box\">\n" +
                "          <div class=\"box-badge\">Direct Android APK</div>\n" +
                "          <div class=\"box-title\">Scan with Phone</div>\n" +
                "          <div class=\"box-subtitle\">Point camera to start download immediately</div>\n" +
                "          <div class=\"qr-frame\">\n" +
                "            <img src=\"https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=https://studymate-ai-uuvd.onrender.com/download/app.apk\" alt=\"QR Code to Download StudyFlow AI\" />\n" +
                "          </div>\n" +
                "          <a href=\"/download/app.apk\" class=\"direct-link-btn\" onclick=\"triggerDownloadAnalytics()\">\n" +
                "            Direct APK Download Link (59MB)\n" +
                "          </a>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <section class=\"features-section\">\n" +
                "      <div class=\"section-title\">\n" +
                "        <h2>Everything You Need to Ace College</h2>\n" +
                "        <p>Built exclusively for students who want stress-free academic excellence</p>\n" +
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
                "        <svg width=\"32\" height=\"32\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"white\" stroke-width=\"2.6\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4\"></path><polyline points=\"7 10 12 15 17 10\"></polyline><line x1=\"12\" y1=\"15\" x2=\"12\" y2=\"3\"></line></svg>\n" +
                "      </div>\n" +
                "      <h3 style=\"font-size: 22px; font-weight: 800; margin-bottom: 6px;\">Starting Download...</h3>\n" +
                "      <p style=\"color: var(--text-muted); font-size: 14px; margin-bottom: 12px;\">Your Android APK file is downloading now.</p>\n" +
                "      <div class=\"progress-bar-container\">\n" +
                "        <div class=\"progress-bar-fill\" id=\"progressBar\"></div>\n" +
                "      </div>\n" +
                "      <div class=\"step-list\">\n" +
                "        <div class=\"step-item\">\n" +
                "          <div class=\"step-num\">1</div>\n" +
                "          <div class=\"step-text\">\n" +
                "            <h4>Open Downloaded File</h4>\n" +
                "            <p>Tap <b>StudyFlowAI.apk</b> from your phone notification bar or Chrome downloads.</p>\n" +
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
