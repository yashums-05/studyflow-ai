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
            "https://raw.githubusercontent.com/yashums-05/studyflow-ai/main/apk/StudyFlowAI.apk";

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
        return PORTAL_HTML;
    }

    private static final String PORTAL_HTML = """
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0">
  <title>StudyFlow AI &bull; Smart Study & Academic Schedule Planner</title>
  <meta name="description" content="Download StudyFlow AI for Android - Turn your class timetable, syllabus, and exams into an effortless daily study schedule with Gemini AI.">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=JetBrains+Mono:wght@500;700&display=swap" rel="stylesheet">
  <style>
    :root {
      --primary: #6366F1;
      --primary-hover: #4F46E5;
      --primary-glow: rgba(99, 102, 241, 0.4);
      --accent: #EC4899;
      --cyan: #06B6D4;
      --emerald: #10B981;
      --bg-dark: #070913;
      --bg-card: rgba(15, 23, 42, 0.75);
      --border-card: rgba(255, 255, 255, 0.08);
      --border-card-hover: rgba(99, 102, 241, 0.4);
      --text-title: #FFFFFF;
      --text-sub: #94A3B8;
      --text-muted: #64748B;
    }

    *, *::before, *::after {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
      -webkit-tap-highlight-color: transparent;
    }

    html, body {
      width: 100%;
      max-width: 100%;
      overflow-x: hidden;
      background-color: var(--bg-dark);
      color: #F8FAFC;
      font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      line-height: 1.5;
    }

    /* Ambient Background Pattern & Lighting */
    .bg-canvas {
      position: fixed;
      inset: 0;
      pointer-events: none;
      z-index: 0;
      overflow: hidden;
    }
    .bg-grid {
      position: absolute;
      inset: 0;
      background-image: radial-gradient(rgba(255, 255, 255, 0.07) 1px, transparent 1px);
      background-size: 32px 32px;
      opacity: 0.6;
    }
    .glow-orb-top {
      position: absolute;
      top: -160px;
      left: 50%;
      transform: translateX(-50%);
      width: min(800px, 100vw);
      height: 450px;
      background: radial-gradient(circle, rgba(99, 102, 241, 0.25) 0%, rgba(236, 72, 153, 0.12) 40%, transparent 70%);
      filter: blur(80px);
    }
    .glow-orb-bottom {
      position: absolute;
      bottom: -100px;
      right: -50px;
      width: 500px;
      height: 500px;
      background: radial-gradient(circle, rgba(6, 182, 212, 0.12) 0%, transparent 60%);
      filter: blur(90px);
    }

    /* Header Navigation */
    header {
      width: 100%;
      position: sticky;
      top: 0;
      backdrop-filter: blur(20px);
      -webkit-backdrop-filter: blur(20px);
      background: rgba(7, 9, 19, 0.85);
      border-bottom: 1px solid var(--border-card);
      z-index: 50;
      padding: 14px 20px;
    }
    .nav-inner {
      max-width: 1180px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
    }
    .brand-link {
      display: flex;
      align-items: center;
      gap: 10px;
      text-decoration: none;
      color: #fff;
    }
    .brand-logo-icon {
      width: 38px;
      height: 38px;
      background: linear-gradient(135deg, #6366F1, #EC4899);
      border-radius: 11px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 16px var(--primary-glow);
      flex-shrink: 0;
    }
    .brand-text {
      font-size: 19px;
      font-weight: 800;
      letter-spacing: -0.5px;
      white-space: nowrap;
    }
    .brand-text span {
      background: linear-gradient(135deg, #A5B4FC, #EC4899);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
    .nav-badge-pill {
      display: flex;
      align-items: center;
      gap: 7px;
      background: rgba(16, 185, 129, 0.12);
      border: 1px solid rgba(16, 185, 129, 0.3);
      padding: 5px 12px;
      border-radius: 999px;
      font-size: 12px;
      font-weight: 600;
      color: #34D399;
    }
    .pulse-dot {
      width: 7px;
      height: 7px;
      background: #10B981;
      border-radius: 50%;
      box-shadow: 0 0 10px #10B981;
      animation: pulseAnim 2s infinite ease-in-out;
    }
    @keyframes pulseAnim {
      0%, 100% { opacity: 0.8; transform: scale(0.95); }
      50% { opacity: 1; transform: scale(1.25); }
    }
    .nav-right-actions {
      display: flex;
      align-items: center;
      gap: 12px;
    }
    .btn-nav-download {
      background: linear-gradient(135deg, #6366F1, #4F46E5);
      color: #FFFFFF;
      padding: 9px 18px;
      border-radius: 10px;
      font-size: 13px;
      font-weight: 700;
      text-decoration: none;
      border: none;
      cursor: pointer;
      display: inline-flex;
      align-items: center;
      gap: 6px;
      transition: all 0.2s ease;
      box-shadow: 0 4px 14px rgba(99, 102, 241, 0.35);
    }
    .btn-nav-download:hover {
      box-shadow: 0 6px 20px rgba(99, 102, 241, 0.55);
      transform: translateY(-1px);
    }

    /* Main Container */
    main {
      width: 100%;
      max-width: 1180px;
      margin: 0 auto;
      padding: 44px 20px 72px 20px;
      position: relative;
      z-index: 10;
      flex: 1;
    }

    /* Hero Section */
    .hero-container {
      display: grid;
      grid-template-columns: 1.15fr 0.85fr;
      gap: 48px;
      align-items: center;
      margin-bottom: 72px;
    }
    .hero-badge-pill {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      background: rgba(99, 102, 241, 0.12);
      border: 1px solid rgba(99, 102, 241, 0.3);
      color: #A5B4FC;
      font-size: 12px;
      font-weight: 700;
      padding: 7px 16px;
      border-radius: 999px;
      margin-bottom: 20px;
      letter-spacing: 0.2px;
    }
    .sparkle-icon {
      color: #EC4899;
    }
    h1.hero-title {
      font-size: clamp(32px, 4.2vw, 54px);
      font-weight: 800;
      line-height: 1.14;
      letter-spacing: -0.03em;
      margin-bottom: 18px;
      color: #FFFFFF;
    }
    .hero-gradient-text {
      background: linear-gradient(135deg, #FFFFFF 15%, #A5B4FC 55%, #EC4899 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      display: inline-block;
    }
    p.hero-description {
      color: var(--text-sub);
      font-size: clamp(15px, 1.8vw, 17px);
      line-height: 1.65;
      margin-bottom: 30px;
      max-width: 560px;
    }

    /* Hero Buttons */
    .hero-actions-row {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 14px;
      margin-bottom: 26px;
    }
    .btn-main-install {
      background: linear-gradient(135deg, #6366F1 0%, #4F46E5 100%);
      color: #FFFFFF;
      padding: 16px 32px;
      border-radius: 14px;
      font-size: 16px;
      font-weight: 800;
      border: none;
      cursor: pointer;
      display: inline-flex;
      align-items: center;
      gap: 12px;
      box-shadow: 0 10px 30px var(--primary-glow);
      transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
      text-decoration: none;
    }
    .btn-main-install:hover {
      transform: translateY(-2px);
      box-shadow: 0 16px 40px rgba(99, 102, 241, 0.6);
    }
    .btn-main-install:active {
      transform: translateY(0);
    }
    .btn-secondary-link {
      background: rgba(255, 255, 255, 0.05);
      border: 1px solid var(--border-card);
      color: #CBD5E1;
      padding: 16px 22px;
      border-radius: 14px;
      font-size: 14px;
      font-weight: 700;
      cursor: pointer;
      display: inline-flex;
      align-items: center;
      gap: 8px;
      text-decoration: none;
      transition: all 0.2s ease;
    }
    .btn-secondary-link:hover {
      background: rgba(255, 255, 255, 0.09);
      border-color: rgba(255, 255, 255, 0.2);
      color: #FFFFFF;
    }

    /* Trust & Spec Chips */
    .trust-specs-row {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 16px;
      color: var(--text-muted);
      font-size: 12px;
      font-weight: 600;
    }
    .spec-item {
      display: flex;
      align-items: center;
      gap: 6px;
    }
    .check-green {
      color: #10B981;
      font-weight: 800;
    }

    /* Desktop Phone Mockup Showcase */
    .mockup-column {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      width: 100%;
      position: relative;
    }
    .mockup-aura {
      position: absolute;
      width: 320px;
      height: 480px;
      background: radial-gradient(circle, rgba(99, 102, 241, 0.3) 0%, rgba(236, 72, 153, 0.15) 50%, transparent 70%);
      filter: blur(60px);
      pointer-events: none;
      z-index: 1;
    }
    .phone-chassis {
      width: 285px;
      height: 560px;
      background: #02040A;
      border-radius: 44px;
      padding: 9px;
      box-shadow:
        0 0 0 3px #1E293B,
        0 25px 65px rgba(0, 0, 0, 0.85),
        0 0 35px rgba(99, 102, 241, 0.25);
      position: relative;
      overflow: hidden;
      border: 3px solid #334155;
      z-index: 2;
      transition: transform 0.3s ease;
    }
    .phone-chassis:hover {
      transform: translateY(-6px);
    }
    .phone-notch-bar {
      width: 96px;
      height: 18px;
      background: #000000;
      border-radius: 12px;
      position: absolute;
      top: 14px;
      left: 50%;
      transform: translateX(-50%);
      z-index: 30;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .camera-lens {
      width: 7px;
      height: 7px;
      background: #0F172A;
      border: 1px solid #334155;
      border-radius: 50%;
    }
    .phone-inner-screen {
      width: 100%;
      height: 100%;
      background: #090D18;
      border-radius: 36px;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      padding-top: 26px;
    }
    .screen-status-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 4px 18px;
      font-size: 10px;
      font-weight: 700;
      color: #94A3B8;
    }
    .screen-app-bar {
      padding: 10px 16px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .user-greeting {
      font-size: 10px;
      color: #94A3B8;
      font-weight: 500;
    }
    .user-name-title {
      font-size: 13px;
      font-weight: 800;
      color: #FFFFFF;
    }
    .avatar-glow-chip {
      width: 30px;
      height: 30px;
      border-radius: 50%;
      background: linear-gradient(135deg, #6366F1, #EC4899);
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 800;
      font-size: 11px;
      color: #fff;
      box-shadow: 0 2px 10px rgba(99, 102, 241, 0.5);
    }
    .screen-feed {
      padding: 6px 14px;
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 9px;
    }
    .feed-card {
      background: rgba(255, 255, 255, 0.05);
      border: 1px solid rgba(255, 255, 255, 0.08);
      border-radius: 14px;
      padding: 10px 12px;
    }
    .feed-card.highlight {
      background: linear-gradient(135deg, rgba(99, 102, 241, 0.22), rgba(236, 72, 153, 0.16));
      border-color: rgba(99, 102, 241, 0.35);
    }
    .card-top-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 3px;
    }
    .card-tag {
      font-size: 11px;
      font-weight: 700;
      color: #F1F5F9;
    }
    .status-badge-chip {
      background: rgba(99, 102, 241, 0.25);
      color: #A5B4FC;
      font-size: 9px;
      font-weight: 800;
      padding: 2px 6px;
      border-radius: 4px;
    }
    .feed-card p {
      font-size: 10.5px;
      color: #94A3B8;
      line-height: 1.35;
    }
    .progress-track {
      width: 100%;
      height: 5px;
      background: rgba(255, 255, 255, 0.08);
      border-radius: 999px;
      margin-top: 6px;
      overflow: hidden;
    }
    .progress-fill-accent {
      width: 75%;
      height: 100%;
      background: linear-gradient(90deg, #6366F1, #EC4899);
      border-radius: 999px;
    }
    .screen-bottom-navigation {
      margin-top: auto;
      background: #050811;
      border-top: 1px solid rgba(255, 255, 255, 0.06);
      display: flex;
      justify-content: space-around;
      padding: 10px 6px 14px 6px;
      font-size: 9.5px;
      color: #64748B;
    }
    .nav-item-active {
      color: #818CF8;
      font-weight: 700;
    }

    /* Desktop QR Code Card */
    .qr-companion-card {
      margin-top: 18px;
      background: var(--bg-card);
      border: 1px solid var(--border-card);
      border-radius: 18px;
      padding: 14px 18px;
      display: flex;
      align-items: center;
      gap: 16px;
      width: 285px;
      z-index: 2;
      backdrop-filter: blur(12px);
    }
    .qr-code-img {
      width: 68px;
      height: 68px;
      border-radius: 8px;
      background: #FFFFFF;
      padding: 4px;
      flex-shrink: 0;
    }
    .qr-caption h6 {
      font-size: 12px;
      font-weight: 700;
      color: #FFFFFF;
      margin-bottom: 2px;
    }
    .qr-caption p {
      font-size: 11px;
      color: var(--text-sub);
      line-height: 1.35;
    }

    /* Bento Grid Feature Section */
    .bento-section {
      margin-top: 24px;
      margin-bottom: 64px;
    }
    .section-headline {
      text-align: center;
      margin-bottom: 40px;
    }
    .section-eyebrow {
      font-size: 12px;
      font-weight: 800;
      text-transform: uppercase;
      letter-spacing: 1px;
      color: #818CF8;
      margin-bottom: 8px;
    }
    .section-headline h2 {
      font-size: clamp(24px, 3.5vw, 36px);
      font-weight: 800;
      letter-spacing: -0.02em;
      color: #FFFFFF;
      margin-bottom: 10px;
    }
    .section-headline p {
      color: var(--text-sub);
      font-size: 15px;
      max-width: 520px;
      margin: 0 auto;
    }
    .bento-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
      gap: 20px;
    }
    .bento-card {
      background: var(--bg-card);
      border: 1px solid var(--border-card);
      border-radius: 20px;
      padding: 24px;
      transition: all 0.25s ease;
      position: relative;
      overflow: hidden;
    }
    .bento-card::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 1px;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.15), transparent);
    }
    .bento-card:hover {
      border-color: var(--border-card-hover);
      transform: translateY(-3px);
      box-shadow: 0 16px 36px rgba(0, 0, 0, 0.4);
    }
    .bento-icon-wrapper {
      width: 44px;
      height: 44px;
      border-radius: 12px;
      background: rgba(99, 102, 241, 0.14);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      margin-bottom: 16px;
      border: 1px solid rgba(99, 102, 241, 0.25);
    }
    .bento-card h3 {
      font-size: 17px;
      font-weight: 700;
      margin-bottom: 8px;
      color: #FFFFFF;
    }
    .bento-card p {
      color: var(--text-sub);
      font-size: 13.5px;
      line-height: 1.55;
    }

    /* 3-Step Walkthrough Cards */
    .steps-section {
      background: linear-gradient(180deg, rgba(15, 23, 42, 0.5) 0%, rgba(7, 9, 19, 0.8) 100%);
      border: 1px solid var(--border-card);
      border-radius: 24px;
      padding: 36px 28px;
      margin-bottom: 64px;
    }
    .steps-title {
      text-align: center;
      margin-bottom: 28px;
    }
    .steps-title h3 {
      font-size: 22px;
      font-weight: 800;
      color: #FFFFFF;
      margin-bottom: 6px;
    }
    .steps-title p {
      color: var(--text-sub);
      font-size: 14px;
    }
    .steps-row {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 18px;
    }
    .step-box {
      background: rgba(255, 255, 255, 0.03);
      border: 1px solid rgba(255, 255, 255, 0.06);
      border-radius: 16px;
      padding: 20px;
    }
    .step-num-pill {
      display: inline-block;
      background: rgba(99, 102, 241, 0.25);
      color: #A5B4FC;
      font-size: 11px;
      font-weight: 800;
      padding: 3px 9px;
      border-radius: 6px;
      margin-bottom: 12px;
    }
    .step-box h4 {
      font-size: 15px;
      font-weight: 700;
      color: #FFFFFF;
      margin-bottom: 4px;
    }
    .step-box p {
      font-size: 12.5px;
      color: var(--text-sub);
      line-height: 1.45;
    }

    /* Modal Dialog */
    .modal-overlay {
      position: fixed;
      inset: 0;
      background: rgba(0, 0, 0, 0.86);
      backdrop-filter: blur(14px);
      -webkit-backdrop-filter: blur(14px);
      z-index: 100;
      display: none;
      align-items: center;
      justify-content: center;
      padding: 18px;
    }
    .modal-dialog {
      background: #0E1322;
      border: 1px solid rgba(99, 102, 241, 0.35);
      border-radius: 24px;
      max-width: 480px;
      width: 100%;
      max-height: 90vh;
      overflow-y: auto;
      padding: 32px 26px;
      box-shadow: 0 30px 80px rgba(0, 0, 0, 0.9), 0 0 40px rgba(99, 102, 241, 0.25);
      position: relative;
      text-align: center;
      animation: modalFadeIn 0.25s cubic-bezier(0.16, 1, 0.3, 1) forwards;
    }
    @keyframes modalFadeIn {
      from { transform: translateY(24px) scale(0.96); opacity: 0; }
      to { transform: translateY(0) scale(1); opacity: 1; }
    }
    .btn-modal-close {
      position: absolute;
      top: 16px;
      right: 16px;
      background: rgba(255, 255, 255, 0.08);
      border: none;
      color: var(--text-sub);
      width: 32px;
      height: 32px;
      border-radius: 50%;
      cursor: pointer;
      font-size: 18px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s;
    }
    .btn-modal-close:hover {
      background: rgba(255, 255, 255, 0.16);
      color: #fff;
    }
    .download-anim-badge {
      width: 58px;
      height: 58px;
      background: linear-gradient(135deg, #10B981, #059669);
      border-radius: 18px;
      margin: 0 auto 16px auto;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 8px 24px rgba(16, 185, 129, 0.45);
      animation: bouncePulse 1.4s infinite alternate ease-in-out;
    }
    @keyframes bouncePulse {
      from { transform: translateY(0); }
      to { transform: translateY(-6px); }
    }
    .modal-dialog h3 {
      font-size: 21px;
      font-weight: 800;
      color: #FFFFFF;
      margin-bottom: 4px;
    }
    .modal-dialog p.modal-subtext {
      color: var(--text-sub);
      font-size: 13.5px;
      margin-bottom: 14px;
    }
    .progress-rail {
      width: 100%;
      height: 7px;
      background: rgba(255, 255, 255, 0.08);
      border-radius: 999px;
      overflow: hidden;
      margin: 18px 0;
    }
    .progress-bar-glow {
      height: 100%;
      width: 0%;
      background: linear-gradient(90deg, #6366F1, #10B981);
      border-radius: 999px;
      transition: width 0.3s ease;
    }
    .modal-instructions-box {
      text-align: left;
      background: rgba(255, 255, 255, 0.03);
      border: 1px solid rgba(255, 255, 255, 0.07);
      border-radius: 16px;
      padding: 16px;
      margin-bottom: 18px;
    }
    .modal-step-item {
      display: flex;
      align-items: flex-start;
      gap: 12px;
      margin-bottom: 12px;
    }
    .modal-step-item:last-child {
      margin-bottom: 0;
    }
    .modal-step-num {
      background: rgba(99, 102, 241, 0.25);
      color: #A5B4FC;
      font-weight: 800;
      font-size: 11px;
      width: 22px;
      height: 22px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      margin-top: 2px;
    }
    .modal-step-text h5 {
      font-size: 13px;
      font-weight: 700;
      color: #FFFFFF;
      margin-bottom: 2px;
    }
    .modal-step-text p {
      font-size: 11.5px;
      color: var(--text-sub);
      line-height: 1.4;
    }
    .modal-fallback-line {
      font-size: 12.5px;
      color: var(--text-muted);
    }
    .modal-fallback-line a {
      color: #818CF8;
      font-weight: 700;
      text-decoration: underline;
      cursor: pointer;
    }

    /* Toast Notification */
    .toast-pill {
      position: fixed;
      bottom: 28px;
      left: 50%;
      transform: translateX(-50%) translateY(40px);
      background: #1E293B;
      color: #FFFFFF;
      border: 1px solid rgba(255, 255, 255, 0.15);
      padding: 10px 20px;
      border-radius: 999px;
      font-size: 13px;
      font-weight: 600;
      box-shadow: 0 10px 28px rgba(0, 0, 0, 0.6);
      opacity: 0;
      transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
      z-index: 200;
      pointer-events: none;
    }
    .toast-pill.show {
      transform: translateX(-50%) translateY(0);
      opacity: 1;
    }

    /* Footer */
    footer {
      border-top: 1px solid var(--border-card);
      padding: 24px 20px;
      text-align: center;
      color: var(--text-muted);
      font-size: 12.5px;
      background: rgba(7, 9, 19, 0.9);
      position: relative;
      z-index: 10;
    }
    footer a {
      color: #818CF8;
      text-decoration: none;
    }

    /* Mobile Responsive Optimizations (< 860px) */
    @media (max-width: 860px) {
      header {
        padding: 12px 16px;
      }
      .nav-badge-pill {
        display: none;
      }
      main {
        padding: 28px 16px 56px 16px;
      }
      .hero-container {
        grid-template-columns: 1fr;
        gap: 32px;
        text-align: center;
        margin-bottom: 48px;
      }
      .hero-badge-pill {
        margin: 0 auto 16px auto;
      }
      p.hero-description {
        margin: 0 auto 24px auto;
      }
      .hero-actions-row {
        flex-direction: column;
        width: 100%;
        gap: 12px;
        margin: 0 auto 24px auto;
      }
      .btn-main-install, .btn-secondary-link {
        width: 100%;
        max-width: 360px;
        justify-content: center;
      }
      .trust-specs-row {
        justify-content: center;
        gap: 12px;
      }
      .mockup-column {
        display: none;
      }
      .steps-section {
        padding: 28px 18px;
      }
    }
  </style>
</head>
<body>
  <!-- Ambient Lighting & Canvas -->
  <div class="bg-canvas">
    <div class="bg-grid"></div>
    <div class="glow-orb-top"></div>
    <div class="glow-orb-bottom"></div>
  </div>

  <!-- Navigation Bar -->
  <header>
    <div class="nav-inner">
      <a href="/" class="brand-link">
        <div class="brand-logo-icon">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path></svg>
        </div>
        <div class="brand-text">StudyFlow<span>AI</span></div>
      </a>
      <div class="nav-right-actions">
        <div class="nav-badge-pill">
          <div class="pulse-dot"></div>
          <span>Cloud Live</span>
        </div>
        <button class="btn-nav-download" onclick="startInstallation()">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
          Install APK
        </button>
      </div>
    </div>
  </header>

  <!-- Main Content -->
  <main>
    <!-- Hero Section -->
    <div class="hero-container">
      <div>
        <div class="hero-badge-pill">
          <span class="sparkle-icon">✦</span> Official Release v1.0.0 &bull; Android APK
        </div>
        <h1 class="hero-title">
          Supercharge Your College Studies with <br>
          <span class="hero-gradient-text">StudyFlow AI</span>
        </h1>
        <p class="hero-description">
          The intelligent academic planner that analyzes your class timetables, syllabus, exams, and holidays &mdash; crafting the perfect personalized daily study routine with an on-demand AI tutor.
        </p>

        <div class="hero-actions-row">
          <button class="btn-main-install" id="heroInstallBtn" onclick="startInstallation()">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
            Download Android APK (59MB)
          </button>
          <button class="btn-secondary-link" onclick="copyDownloadLink()">
            <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path></svg>
            Copy Direct Link
          </button>
        </div>

        <div class="trust-specs-row">
          <div class="spec-item"><span class="check-green">✓</span> Android 8.0+</div>
          <div class="spec-item"><span class="check-green">✓</span> 58.9 MB Lightweight</div>
          <div class="spec-item"><span class="check-green">✓</span> Real-Time Cloud Sync</div>
          <div class="spec-item"><span class="check-green">✓</span> 100% Free &amp; Ad-Free</div>
        </div>
      </div>

      <!-- Desktop Phone Mockup & QR Code -->
      <div class="mockup-column">
        <div class="mockup-aura"></div>
        <div class="phone-chassis">
          <div class="phone-notch-bar">
            <div class="camera-lens"></div>
          </div>
          <div class="phone-inner-screen">
            <div class="screen-status-row">
              <span>9:41</span>
              <span>5G &bull; 100%</span>
            </div>
            <div class="screen-app-bar">
              <div>
                <div class="user-greeting">Personal Dashboard</div>
                <div class="user-name-title">StudyFlow AI</div>
              </div>
              <div class="avatar-glow-chip">AI</div>
            </div>
            <div class="screen-feed">
              <div class="feed-card highlight">
                <div class="card-top-row">
                  <span class="card-tag">📚 Academic Hub</span>
                  <span class="status-badge-chip">SYNCED</span>
                </div>
                <p>Classes, Exam Timetable &amp; Events Active</p>
              </div>

              <div class="feed-card">
                <div class="card-top-row">
                  <span class="card-tag">⏰ Upcoming Class</span>
                  <span style="color: #818CF8; font-size: 10px; font-weight: 700;">10:00 AM</span>
                </div>
                <p>Computer Networks &bull; Room 302</p>
              </div>

              <div class="feed-card">
                <div class="card-top-row">
                  <span class="card-tag">🤖 AI Study Gap</span>
                  <span style="color: #10B981; font-size: 10px; font-weight: 700;">2h Free</span>
                </div>
                <p>Focus on Graph Algorithms (Exam in 4 days)</p>
              </div>

              <div class="feed-card">
                <div class="card-top-row">
                  <span class="card-tag">🎯 Daily Goal</span>
                  <span style="color: #F43F5E; font-size: 10px; font-weight: 700;">4.5 / 6h</span>
                </div>
                <div class="progress-track">
                  <div class="progress-fill-accent"></div>
                </div>
              </div>
            </div>

            <div class="screen-bottom-navigation">
              <div class="nav-item-active">🏠 Home</div>
              <div>📖 Subjects</div>
              <div>📅 Hub</div>
              <div>🤖 AI Tutor</div>
              <div>👤 Profile</div>
            </div>
          </div>
        </div>

        <div class="qr-companion-card">
          <img id="qrCodeImg" class="qr-code-img" src="https://api.qrserver.com/v1/create-qr-code/?size=160x160&data=https://studyflow-ai-app.onrender.com/download/app.apk" alt="QR code" />
          <div class="qr-caption">
            <h6>Scan on Mobile</h6>
            <p>Point phone camera to install directly onto your device.</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Bento Grid Feature Highlights -->
    <section class="bento-section">
      <div class="section-headline">
        <div class="section-eyebrow">Engineered for Students</div>
        <h2>Everything You Need to Ace College</h2>
        <p>Built exclusively for students who want stress-free academic success.</p>
      </div>

      <div class="bento-grid">
        <div class="bento-card">
          <div class="bento-icon-wrapper">📅</div>
          <h3>Academic Hub &amp; Schedule</h3>
          <p>Upload your weekly class slots, college holidays, and exam dates. The engine identifies optimal study gaps between classes automatically.</p>
        </div>

        <div class="bento-card">
          <div class="bento-icon-wrapper">🤖</div>
          <h3>AI Study Tutor &amp; Quizzer</h3>
          <p>Stuck on a complex topic? Ask your AI tutor for clear analogies, step-by-step breakdowns, and instant practice quizzes tailored to your syllabus.</p>
        </div>

        <div class="bento-card">
          <div class="bento-icon-wrapper">🎯</div>
          <h3>Focus &amp; Pomodoro Timer</h3>
          <p>Custom focus intervals with break reminders and session tracking to keep your retention high without burnout.</p>
        </div>

        <div class="bento-card">
          <div class="bento-icon-wrapper">📊</div>
          <h3>Mastery &amp; Readiness Analytics</h3>
          <p>Visual progress tracking highlighting your strongest and weakest subjects, weekly study targets, and exam readiness scores.</p>
        </div>
      </div>
    </section>

    <!-- 3-Step Walkthrough -->
    <section class="steps-section">
      <div class="steps-title">
        <h3>Quick 3-Step Installation</h3>
        <p>No Google Play account needed &bull; Direct install on any modern Android device</p>
      </div>
      <div class="steps-row">
        <div class="step-box">
          <div class="step-num-pill">Step 1</div>
          <h4>Download File</h4>
          <p>Tap <b>Download APK</b> or scan the QR code to get <code>StudyFlowAI.apk</code>.</p>
        </div>
        <div class="step-box">
          <div class="step-num-pill">Step 2</div>
          <h4>Allow Unknown Apps</h4>
          <p>Tap the downloaded file. If prompted, toggle <i>Allow from this source</i> in Settings.</p>
        </div>
        <div class="step-box">
          <div class="step-num-pill">Step 3</div>
          <h4>Launch &amp; Excel</h4>
          <p>Create your personal account, set up your subjects, and let AI organize your study life.</p>
        </div>
      </div>
    </section>
  </main>

  <!-- Download In-Progress Modal -->
  <div class="modal-overlay" id="downloadModal">
    <div class="modal-dialog">
      <button class="btn-modal-close" onclick="closeDownloadModal()">&times;</button>
      <div class="download-anim-badge">
        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
      </div>
      <h3>Starting Your Download...</h3>
      <p class="modal-subtext">Downloading <b>StudyFlowAI.apk</b> (58.9 MB)</p>
      
      <div class="progress-rail">
        <div class="progress-bar-glow" id="modalProgressBar"></div>
      </div>

      <div class="modal-instructions-box">
        <div class="modal-step-item">
          <div class="modal-step-num">1</div>
          <div class="modal-step-text">
            <h5>Open Downloaded File</h5>
            <p>Tap <b>StudyFlowAI.apk</b> in your notification shade or browser downloads.</p>
          </div>
        </div>
        <div class="modal-step-item">
          <div class="modal-step-num">2</div>
          <div class="modal-step-text">
            <h5>Allow Installation</h5>
            <p>If prompted by Android, tap <b>Settings</b> &rarr; enable <b>Allow from this source</b>.</p>
          </div>
        </div>
        <div class="modal-step-item">
          <div class="modal-step-num">3</div>
          <div class="modal-step-text">
            <h5>Start Studying</h5>
            <p>Launch StudyFlow AI and create your personal student profile.</p>
          </div>
        </div>
      </div>

      <div class="modal-fallback-line">
        Didn't start automatically? <a href="/download/app.apk" onclick="triggerDirectDownload()">Click here to download directly</a>
      </div>
    </div>
  </div>

  <!-- Toast Notification -->
  <div class="toast-pill" id="toastMessage">Direct APK download link copied!</div>

  <!-- Footer -->
  <footer>
    StudyFlow AI &bull; Spring Boot 3.2 Java 17 Cloud Backend on Render &bull; 2026
  </footer>

  <script>
    function startInstallation() {
      const modal = document.getElementById('downloadModal');
      modal.style.display = 'flex';

      const bar = document.getElementById('modalProgressBar');
      bar.style.width = '0%';

      setTimeout(() => { bar.style.width = '40%'; }, 150);
      setTimeout(() => { bar.style.width = '80%'; }, 550);
      setTimeout(() => { bar.style.width = '100%'; }, 950);

      setTimeout(() => {
        const link = document.createElement('a');
        link.href = '/download/app.apk';
        link.setAttribute('download', 'StudyFlowAI.apk');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }, 700);
    }

    function closeDownloadModal() {
      document.getElementById('downloadModal').style.display = 'none';
    }

    function triggerDirectDownload() {
      console.log('Direct download initiated.');
    }

    function copyDownloadLink() {
      const downloadUrl = window.location.origin + '/download/app.apk';
      navigator.clipboard.writeText(downloadUrl).then(() => {
        showToast('Download link copied to clipboard!');
      }).catch(() => {
        showToast('Direct URL: ' + downloadUrl);
      });
    }

    function showToast(msg) {
      const toast = document.getElementById('toastMessage');
      toast.textContent = msg;
      toast.classList.add('show');
      setTimeout(() => {
        toast.classList.remove('show');
      }, 3000);
    }

    window.addEventListener('DOMContentLoaded', () => {
      const qr = document.getElementById('qrCodeImg');
      if (qr) {
        qr.src = 'https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=' + encodeURIComponent(window.location.origin + '/download/app.apk');
      }

      const urlParams = new URLSearchParams(window.location.search);
      if (urlParams.get('auto') === 'true' || urlParams.get('install') === 'true' || urlParams.get('download') === 'true') {
        startInstallation();
      }
    });
  </script>
</body>
</html>
""";
}
