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
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=yes">
  <title>StudyFlow AI &bull; The Intelligent Academic OS for Students</title>
  <meta name="description" content="Download StudyFlow AI APK for Android. Smart timetables, AI study mentor powered by Gemini, attendance analytics, and exam mastery.">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=JetBrains+Mono:wght@500;700&display=swap" rel="stylesheet">
  <style>
    *, *::before, *::after {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
    }

    :root {
      --bg-dark: #07090E;
      --bg-card: rgba(18, 22, 34, 0.7);
      --bg-card-hover: rgba(26, 32, 50, 0.85);
      --border-card: rgba(255, 255, 255, 0.08);
      --border-glow: rgba(99, 102, 241, 0.4);
      --primary: #6366F1;
      --primary-hover: #4F46E5;
      --primary-glow: rgba(99, 102, 241, 0.35);
      --accent: #8B5CF6;
      --accent-cyan: #06B6D4;
      --accent-rose: #F43F5E;
      --text-main: #F8FAFC;
      --text-sub: #94A3B8;
      --text-muted: #64748B;
      --font-sans: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      --font-mono: 'JetBrains Mono', monospace;
    }

    html {
      width: 100%;
      max-width: 100vw;
      overflow-x: hidden;
      background-color: var(--bg-dark);
      color: var(--text-main);
      font-family: var(--font-sans);
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
      scroll-behavior: smooth;
    }

    body {
      width: 100%;
      max-width: 100vw;
      overflow-x: hidden;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      position: relative;
      line-height: 1.5;
    }

    /* Ambient Background - Contained strictly to avoid horizontal overflow */
    .ambient-canvas {
      position: fixed;
      inset: 0;
      pointer-events: none;
      z-index: 0;
      overflow: hidden;
    }

    .ambient-grid {
      position: absolute;
      inset: 0;
      background-image: 
        radial-gradient(rgba(255, 255, 255, 0.06) 1px, transparent 1px);
      background-size: 28px 28px;
      opacity: 0.5;
    }

    .aurora-top {
      position: absolute;
      top: -180px;
      left: 50%;
      transform: translateX(-50%);
      width: min(850px, 95vw);
      height: 480px;
      background: radial-gradient(circle, rgba(99, 102, 241, 0.28) 0%, rgba(139, 92, 246, 0.16) 35%, rgba(6, 182, 212, 0.08) 60%, transparent 80%);
      filter: blur(85px);
    }

    .aurora-bottom {
      position: absolute;
      bottom: -150px;
      right: -100px;
      width: 550px;
      height: 550px;
      background: radial-gradient(circle, rgba(244, 63, 94, 0.12) 0%, rgba(99, 102, 241, 0.08) 45%, transparent 70%);
      filter: blur(95px);
    }

    /* Page Content Structure */
    .page-wrap {
      position: relative;
      z-index: 1;
      width: 100%;
      max-width: 100%;
      display: flex;
      flex-direction: column;
      min-height: 100vh;
    }

    .container {
      width: 100%;
      max-width: 1180px;
      margin: 0 auto;
      padding: 0 24px;
    }

    @media (max-width: 640px) {
      .container {
        padding: 0 16px;
      }
    }

    /* Floating Capsule Navbar */
    header.site-header {
      position: sticky;
      top: 14px;
      z-index: 50;
      width: 100%;
      padding: 0 16px;
      margin-bottom: 24px;
    }

    .nav-pill {
      max-width: 1140px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: rgba(11, 15, 25, 0.75);
      backdrop-filter: blur(20px);
      -webkit-backdrop-filter: blur(20px);
      border: 1px solid var(--border-card);
      border-radius: 999px;
      padding: 10px 18px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4);
    }

    .brand-group {
      display: flex;
      align-items: center;
      gap: 12px;
      text-decoration: none;
      color: #FFFFFF;
    }

    .brand-icon {
      width: 36px;
      height: 36px;
      border-radius: 10px;
      background: linear-gradient(135deg, #6366F1 0%, #8B5CF6 50%, #EC4899 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 14px var(--primary-glow);
      flex-shrink: 0;
    }

    .brand-title {
      font-size: 18px;
      font-weight: 800;
      letter-spacing: -0.4px;
      color: #FFFFFF;
      white-space: nowrap;
    }

    .brand-title span {
      background: linear-gradient(135deg, #A5B4FC, #EC4899);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    .nav-status-badge {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      background: rgba(16, 185, 129, 0.12);
      border: 1px solid rgba(16, 185, 129, 0.28);
      padding: 4px 12px;
      border-radius: 999px;
      font-size: 12px;
      font-weight: 600;
      color: #34D399;
    }

    .pulse-beacon {
      width: 7px;
      height: 7px;
      border-radius: 50%;
      background: #10B981;
      box-shadow: 0 0 10px #10B981;
      animation: beaconPulse 2s infinite ease-in-out;
    }

    @keyframes beaconPulse {
      0%, 100% { opacity: 1; transform: scale(1); }
      50% { opacity: 0.4; transform: scale(0.85); }
    }

    .nav-cta-btn {
      background: linear-gradient(135deg, var(--primary), var(--accent));
      color: #FFFFFF;
      text-decoration: none;
      padding: 8px 18px;
      border-radius: 999px;
      font-size: 13px;
      font-weight: 700;
      display: inline-flex;
      align-items: center;
      gap: 6px;
      box-shadow: 0 4px 14px var(--primary-glow);
      transition: all 0.2s ease;
      white-space: nowrap;
    }

    .nav-cta-btn:hover {
      transform: translateY(-1px);
      box-shadow: 0 6px 20px rgba(99, 102, 241, 0.5);
    }

    /* Main Content */
    main {
      flex: 1;
      width: 100%;
    }

    /* Hero Section */
    .hero-section {
      padding: 32px 0 64px 0;
    }

    .hero-grid {
      display: grid;
      grid-template-columns: 1.1fr 0.9fr;
      gap: 40px;
      align-items: center;
    }

    .hero-content {
      width: 100%;
      min-width: 0;
    }

    .release-tag {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      background: rgba(99, 102, 241, 0.12);
      border: 1px solid rgba(99, 102, 241, 0.32);
      color: #C7D2FE;
      font-size: 12px;
      font-weight: 700;
      padding: 6px 14px;
      border-radius: 999px;
      margin-bottom: 20px;
      letter-spacing: 0.2px;
    }

    .release-tag svg {
      color: #A5B4FC;
    }

    .hero-heading {
      font-size: clamp(30px, 4.4vw, 52px);
      font-weight: 800;
      line-height: 1.15;
      letter-spacing: -0.035em;
      color: #FFFFFF;
      margin-bottom: 18px;
    }

    .hero-gradient-text {
      background: linear-gradient(135deg, #FFFFFF 20%, #A5B4FC 60%, #F43F5E 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      display: inline-block;
    }

    .hero-desc {
      color: var(--text-sub);
      font-size: clamp(15px, 1.8vw, 17px);
      line-height: 1.65;
      margin-bottom: 32px;
      max-width: 540px;
    }

    /* Main Download Card */
    .download-card {
      background: rgba(18, 22, 34, 0.8);
      backdrop-filter: blur(24px);
      -webkit-backdrop-filter: blur(24px);
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 20px;
      padding: 24px;
      margin-bottom: 28px;
      box-shadow: 0 16px 40px rgba(0, 0, 0, 0.35);
      position: relative;
      overflow: hidden;
    }

    .download-card::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 2px;
      background: linear-gradient(90deg, transparent, rgba(99, 102, 241, 0.8), rgba(244, 63, 94, 0.6), transparent);
    }

    .cta-button-group {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    /* Giant Tactile Download CTA */
    .btn-download-primary {
      position: relative;
      background: linear-gradient(135deg, #6366F1 0%, #4F46E5 60%, #4338CA 100%);
      color: #FFFFFF;
      padding: 16px 24px;
      border-radius: 14px;
      text-decoration: none;
      font-weight: 800;
      display: flex;
      align-items: center;
      justify-content: space-between;
      box-shadow: 0 12px 32px var(--primary-glow);
      transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
      border: 1px solid rgba(255, 255, 255, 0.18);
      cursor: pointer;
      overflow: hidden;
    }

    .btn-download-primary:hover {
      transform: translateY(-2px);
      box-shadow: 0 16px 40px rgba(99, 102, 241, 0.55);
      background: linear-gradient(135deg, #6E72FF 0%, #574DFF 60%, #4F46E5 100%);
    }

    .btn-download-primary:active {
      transform: translateY(1px);
    }

    .btn-download-left {
      display: flex;
      align-items: center;
      gap: 14px;
    }

    .btn-icon-wrap {
      width: 42px;
      height: 42px;
      border-radius: 10px;
      background: rgba(255, 255, 255, 0.18);
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .btn-text-wrap {
      display: flex;
      flex-direction: column;
      text-align: left;
    }

    .btn-text-main {
      font-size: 17px;
      font-weight: 800;
      line-height: 1.2;
    }

    .btn-text-sub {
      font-size: 12px;
      font-weight: 600;
      color: #C7D2FE;
    }

    .btn-badge-size {
      background: rgba(255, 255, 255, 0.18);
      border: 1px solid rgba(255, 255, 255, 0.25);
      padding: 5px 12px;
      border-radius: 999px;
      font-size: 12px;
      font-weight: 700;
      font-family: var(--font-mono);
      white-space: nowrap;
    }

    /* Secondary Action Bar */
    .secondary-actions-bar {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 10px;
      margin-top: 4px;
    }

    .btn-card-sub {
      background: rgba(255, 255, 255, 0.05);
      border: 1px solid var(--border-card);
      color: #CBD5E1;
      padding: 12px 14px;
      border-radius: 12px;
      font-size: 13px;
      font-weight: 700;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      text-decoration: none;
      cursor: pointer;
      transition: all 0.2s ease;
      white-space: nowrap;
    }

    .btn-card-sub:hover {
      background: rgba(255, 255, 255, 0.09);
      border-color: rgba(255, 255, 255, 0.18);
      color: #FFFFFF;
    }

    /* Trust & Specs Indicators */
    .trust-meta-row {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 16px;
      font-size: 12px;
      font-weight: 600;
      color: var(--text-muted);
    }

    .trust-item {
      display: flex;
      align-items: center;
      gap: 6px;
    }

    .trust-item svg {
      color: #10B981;
    }

    /* Right Column: Interactive Phone Mockup */
    .mockup-column {
      display: flex;
      justify-content: center;
      align-items: center;
      position: relative;
      width: 100%;
    }

    .phone-outer-frame {
      width: 100%;
      max-width: 340px;
      height: 640px;
      background: #0B0F19;
      border-radius: 44px;
      border: 8px solid #1E293B;
      box-shadow: 
        0 25px 60px -15px rgba(0, 0, 0, 0.8),
        0 0 0 1px rgba(255, 255, 255, 0.1),
        0 0 50px rgba(99, 102, 241, 0.2);
      display: flex;
      flex-direction: column;
      overflow: hidden;
      position: relative;
    }

    /* Dynamic Island / Notch */
    .phone-island {
      position: absolute;
      top: 10px;
      left: 50%;
      transform: translateX(-50%);
      width: 96px;
      height: 22px;
      background: #000000;
      border-radius: 20px;
      z-index: 20;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      padding-right: 8px;
    }

    .island-lens {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: #0F172A;
      border: 1px solid rgba(255, 255, 255, 0.15);
    }

    /* Phone Status Bar */
    .phone-status-bar {
      height: 40px;
      padding: 10px 20px 0 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 11px;
      font-weight: 700;
      color: #94A3B8;
      z-index: 10;
    }

    .phone-screen-content {
      flex: 1;
      padding: 14px 16px;
      display: flex;
      flex-direction: column;
      overflow-y: auto;
      gap: 14px;
    }

    /* Inside Mockup UI */
    .mockup-app-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .mockup-greeting {
      font-size: 12px;
      color: var(--text-muted);
      font-weight: 600;
    }

    .mockup-app-title {
      font-size: 17px;
      font-weight: 800;
      color: #FFFFFF;
    }

    .mockup-avatar {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      background: linear-gradient(135deg, #EC4899, #8B5CF6);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 11px;
      font-weight: 800;
      color: #FFFFFF;
    }

    /* Mockup Tabs */
    .mockup-tabs {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      background: rgba(255, 255, 255, 0.05);
      border-radius: 12px;
      padding: 4px;
      gap: 4px;
    }

    .mock-tab-btn {
      background: none;
      border: none;
      color: var(--text-sub);
      font-size: 10px;
      font-weight: 700;
      padding: 6px 2px;
      border-radius: 8px;
      cursor: pointer;
      text-align: center;
      transition: all 0.2s ease;
      white-space: nowrap;
    }

    .mock-tab-btn.active {
      background: var(--primary);
      color: #FFFFFF;
      box-shadow: 0 2px 8px var(--primary-glow);
    }

    /* Tab Panels */
    .mock-tab-pane {
      display: none;
      flex-direction: column;
      gap: 12px;
    }

    .mock-tab-pane.active {
      display: flex;
    }

    /* Card within Mockup */
    .mock-card {
      background: rgba(30, 41, 59, 0.7);
      border: 1px solid rgba(255, 255, 255, 0.07);
      border-radius: 14px;
      padding: 12px 14px;
    }

    .mock-card-label {
      font-size: 10px;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      font-weight: 700;
      color: #A5B4FC;
      margin-bottom: 4px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .mock-card-title {
      font-size: 13px;
      font-weight: 700;
      color: #F8FAFC;
    }

    .mock-card-sub {
      font-size: 11px;
      color: #94A3B8;
      margin-top: 2px;
    }

    .mock-progress-bg {
      height: 6px;
      background: rgba(255, 255, 255, 0.08);
      border-radius: 999px;
      margin-top: 8px;
      overflow: hidden;
    }

    .mock-progress-fill {
      height: 100%;
      border-radius: 999px;
      background: linear-gradient(90deg, #6366F1, #EC4899);
    }

    /* AI Chat Bubble Mockup */
    .mock-chat-bubble {
      background: rgba(99, 102, 241, 0.12);
      border: 1px solid rgba(99, 102, 241, 0.25);
      border-radius: 12px;
      padding: 10px 12px;
      font-size: 11px;
      line-height: 1.5;
      color: #E2E8F0;
    }

    .mock-chat-bubble strong {
      color: #A5B4FC;
      display: block;
      margin-bottom: 2px;
    }

    /* Phone Home Bar */
    .phone-home-indicator {
      width: 120px;
      height: 4px;
      background: rgba(255, 255, 255, 0.3);
      border-radius: 999px;
      margin: 8px auto;
    }

    /* Features Bento Grid */
    .features-section {
      padding: 48px 0 64px 0;
    }

    .section-header {
      text-align: center;
      margin-bottom: 40px;
    }

    .section-badge {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      background: rgba(139, 92, 246, 0.12);
      border: 1px solid rgba(139, 92, 246, 0.3);
      color: #DDD6FE;
      font-size: 12px;
      font-weight: 700;
      padding: 5px 14px;
      border-radius: 999px;
      margin-bottom: 12px;
    }

    .section-title {
      font-size: clamp(24px, 3.2vw, 36px);
      font-weight: 800;
      letter-spacing: -0.025em;
      color: #FFFFFF;
      margin-bottom: 10px;
    }

    .section-subtitle {
      color: var(--text-sub);
      font-size: 15px;
      max-width: 580px;
      margin: 0 auto;
    }

    .bento-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 20px;
    }

    .bento-card {
      background: var(--bg-card);
      border: 1px solid var(--border-card);
      border-radius: 20px;
      padding: 26px;
      transition: all 0.3s ease;
      display: flex;
      flex-direction: column;
      backdrop-filter: blur(16px);
      -webkit-backdrop-filter: blur(16px);
      position: relative;
    }

    .bento-card:hover {
      background: var(--bg-card-hover);
      border-color: rgba(255, 255, 255, 0.16);
      transform: translateY(-3px);
      box-shadow: 0 16px 36px rgba(0, 0, 0, 0.3);
    }

    .bento-span-2 {
      grid-column: span 2;
    }

    .bento-icon-box {
      width: 46px;
      height: 46px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 18px;
    }

    .bento-card h3 {
      font-size: 18px;
      font-weight: 800;
      color: #FFFFFF;
      margin-bottom: 8px;
    }

    .bento-card p {
      font-size: 14px;
      color: var(--text-sub);
      line-height: 1.6;
    }

    /* 3-Step Installation Guide */
    .steps-section {
      padding: 32px 0 64px 0;
    }

    .steps-container {
      background: rgba(18, 22, 34, 0.6);
      border: 1px solid var(--border-card);
      border-radius: 24px;
      padding: 36px 30px;
    }

    .steps-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 24px;
      margin-top: 32px;
    }

    .step-box {
      background: rgba(255, 255, 255, 0.02);
      border: 1px solid rgba(255, 255, 255, 0.06);
      border-radius: 16px;
      padding: 22px;
      position: relative;
    }

    .step-number {
      width: 32px;
      height: 32px;
      border-radius: 8px;
      background: var(--primary);
      color: #FFFFFF;
      font-weight: 800;
      font-size: 14px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 14px;
    }

    .step-box h4 {
      font-size: 16px;
      font-weight: 700;
      color: #FFFFFF;
      margin-bottom: 8px;
    }

    .step-box p {
      font-size: 13px;
      color: var(--text-sub);
      line-height: 1.5;
    }

    /* Specifications Matrix */
    .specs-section {
      padding: 0 0 64px 0;
    }

    .specs-card {
      background: rgba(18, 22, 34, 0.5);
      border: 1px solid var(--border-card);
      border-radius: 20px;
      padding: 28px;
    }

    .specs-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
    }

    .spec-cell {
      background: rgba(255, 255, 255, 0.02);
      border: 1px solid rgba(255, 255, 255, 0.05);
      border-radius: 12px;
      padding: 14px 16px;
    }

    .spec-label {
      font-size: 11px;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      color: var(--text-muted);
      margin-bottom: 4px;
    }

    .spec-value {
      font-size: 14px;
      font-weight: 700;
      color: #FFFFFF;
      font-family: var(--font-mono);
    }

    /* Footer */
    footer.site-footer {
      border-top: 1px solid var(--border-card);
      padding: 36px 0;
      margin-top: auto;
      background: rgba(7, 9, 14, 0.9);
    }

    .footer-inner {
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
      gap: 16px;
    }

    .footer-copy {
      font-size: 13px;
      color: var(--text-muted);
    }

    .footer-links {
      display: flex;
      gap: 20px;
    }

    .footer-link {
      color: var(--text-sub);
      font-size: 13px;
      text-decoration: none;
      transition: color 0.2s;
    }

    .footer-link:hover {
      color: #FFFFFF;
    }

    /* Download Trigger Modal / Toast */
    .download-modal-overlay {
      position: fixed;
      inset: 0;
      background: rgba(0, 0, 0, 0.7);
      backdrop-filter: blur(10px);
      -webkit-backdrop-filter: blur(10px);
      z-index: 100;
      display: none;
      align-items: center;
      justify-content: center;
      padding: 16px;
    }

    .download-modal-overlay.active {
      display: flex;
      animation: fadeIn 0.25s ease-out;
    }

    .download-modal {
      background: #0F1422;
      border: 1px solid rgba(255, 255, 255, 0.12);
      border-radius: 24px;
      padding: 32px 28px;
      max-width: 440px;
      width: 100%;
      text-align: center;
      box-shadow: 0 25px 60px rgba(0, 0, 0, 0.6);
      position: relative;
    }

    .modal-close-btn {
      position: absolute;
      top: 16px;
      right: 16px;
      background: rgba(255, 255, 255, 0.08);
      border: none;
      color: #CBD5E1;
      width: 30px;
      height: 30px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
    }

    .modal-icon-glow {
      width: 64px;
      height: 64px;
      border-radius: 18px;
      background: linear-gradient(135deg, #6366F1, #8B5CF6);
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 18px auto;
      box-shadow: 0 10px 25px var(--primary-glow);
    }

    .modal-title {
      font-size: 20px;
      font-weight: 800;
      color: #FFFFFF;
      margin-bottom: 8px;
    }

    .modal-desc {
      font-size: 14px;
      color: var(--text-sub);
      line-height: 1.5;
      margin-bottom: 22px;
    }

    .modal-progress-bar {
      height: 6px;
      background: rgba(255, 255, 255, 0.08);
      border-radius: 999px;
      overflow: hidden;
      margin-bottom: 20px;
    }

    .modal-progress-active {
      height: 100%;
      background: linear-gradient(90deg, #6366F1, #EC4899);
      border-radius: 999px;
      width: 0%;
      animation: progressLoad 2.2s ease-in-out forwards;
    }

    @keyframes progressLoad {
      0% { width: 0%; }
      40% { width: 65%; }
      80% { width: 90%; }
      100% { width: 100%; }
    }

    .modal-fallback-link {
      font-size: 13px;
      color: #A5B4FC;
      text-decoration: underline;
      cursor: pointer;
    }

    /* Toast Notification */
    .toast-pill {
      position: fixed;
      bottom: 24px;
      left: 50%;
      transform: translateX(-50%) translateY(100px);
      background: #1E293B;
      color: #FFFFFF;
      border: 1px solid rgba(255, 255, 255, 0.15);
      padding: 12px 24px;
      border-radius: 999px;
      font-size: 13px;
      font-weight: 700;
      display: flex;
      align-items: center;
      gap: 10px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
      z-index: 200;
      transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
      pointer-events: none;
    }

    .toast-pill.show {
      transform: translateX(-50%) translateY(0);
    }

    /* Animations */
    @keyframes fadeIn {
      from { opacity: 0; transform: scale(0.96); }
      to { opacity: 1; transform: scale(1); }
    }

    /* RESPONSIVE BREAKPOINTS: Mobile First Alignment */
    @media (max-width: 960px) {
      .hero-grid {
        grid-template-columns: 1fr;
        gap: 36px;
        text-align: center;
      }
      .hero-content {
        display: flex;
        flex-direction: column;
        align-items: center;
      }
      .hero-desc {
        margin: 0 auto 28px auto;
      }
      .download-card {
        width: 100%;
        max-width: 480px;
      }
      .trust-meta-row {
        justify-content: center;
      }
      .bento-grid {
        grid-template-columns: repeat(2, 1fr);
      }
      .bento-span-2 {
        grid-column: span 2;
      }
      .steps-grid {
        grid-template-columns: 1fr;
        gap: 16px;
      }
      .specs-grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    @media (max-width: 640px) {
      header.site-header {
        top: 8px;
        padding: 0 10px;
      }
      .nav-pill {
        padding: 8px 14px;
      }
      .nav-status-badge {
        display: none;
      }
      .hero-section {
        padding: 16px 0 40px 0;
      }
      .download-card {
        padding: 18px 16px;
      }
      .secondary-actions-bar {
        grid-template-columns: 1fr;
      }
      .bento-grid {
        grid-template-columns: 1fr;
      }
      .bento-span-2 {
        grid-column: span 1;
      }
      .specs-grid {
        grid-template-columns: 1fr;
      }
      .footer-inner {
        flex-direction: column;
        text-align: center;
      }
      .phone-outer-frame {
        max-width: 100%;
        height: 560px;
      }
    }
  </style>
</head>
<body>

  <!-- Ambient Backdrop -->
  <div class="ambient-canvas">
    <div class="ambient-grid"></div>
    <div class="aurora-top"></div>
    <div class="aurora-bottom"></div>
  </div>

  <div class="page-wrap">
    <!-- Floating Capsule Navbar -->
    <header class="site-header">
      <nav class="nav-pill">
        <a href="/" class="brand-group">
          <div class="brand-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round">
              <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path>
              <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path>
              <path d="M8 7h8"></path>
              <path d="M8 11h8"></path>
            </svg>
          </div>
          <span class="brand-title">StudyFlow <span>AI</span></span>
        </a>

        <div class="nav-status-badge">
          <span class="pulse-beacon"></span>
          <span>v1.0.0 Live</span>
        </div>

        <button onclick="triggerDownload()" class="nav-cta-btn">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
            <polyline points="7 10 12 15 17 10"></polyline>
            <line x1="12" y1="15" x2="12" y2="3"></line>
          </svg>
          <span>Install App</span>
        </button>
      </nav>
    </header>

    <!-- Main Content -->
    <main>
      <div class="container">
        
        <!-- Hero Section -->
        <section class="hero-section">
          <div class="hero-grid">
            
            <!-- Left Column: Copy & Download Actions -->
            <div class="hero-content">
              <div class="release-tag">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                  <path d="m12 3-1.9 5.8a2 2 0 0 1-1.3 1.3L3 12l5.8 1.9a2 2 0 0 1 1.3 1.3L12 21l1.9-5.8a2 2 0 0 1 1.3-1.3L21 12l-5.8-1.9a2 2 0 0 1-1.3-1.3Z"/>
                </svg>
                <span>Production Release &bull; Powered by Gemini 2.5</span>
              </div>

              <h1 class="hero-heading">
                The Smart Academic OS for <span class="hero-gradient-text">Top Students.</span>
              </h1>

              <p class="hero-desc">
                Stop juggling messy timetables and exam anxiety. StudyFlow AI synchronizes your lectures, tracks syllabus deadlines, and generates dynamic daily study routines tailored to your cognitive energy.
              </p>

              <!-- Central Download Card -->
              <div class="download-card">
                <div class="cta-button-group">
                  
                  <!-- Primary Action: Direct Download Trigger -->
                  <button onclick="triggerDownload()" class="btn-download-primary" id="mainDownloadBtn">
                    <div class="btn-download-left">
                      <div class="btn-icon-wrap">
                        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                          <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                          <polyline points="7 10 12 15 17 10"></polyline>
                          <line x1="12" y1="15" x2="12" y2="3"></line>
                        </svg>
                      </div>
                      <div class="btn-text-wrap">
                        <span class="btn-text-main">Download for Android</span>
                        <span class="btn-text-sub">Instant APK Install &bull; Free</span>
                      </div>
                    </div>
                    <span class="btn-badge-size">58.9 MB</span>
                  </button>

                  <!-- Secondary Actions -->
                  <div class="secondary-actions-bar">
                    <a href="/download/app.apk" class="btn-card-sub" download="StudyFlowAI.apk">
                      <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"></path>
                        <path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"></path>
                      </svg>
                      <span>Direct APK File</span>
                    </a>

                    <button onclick="copyDownloadUrl()" class="btn-card-sub" type="button">
                      <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect width="14" height="14" x="8" y="8" rx="2" ry="2"></rect>
                        <path d="M4 16c-1.1 0-2-.9-2-2V4c0-1.1.9-2 2-2h10c1.1 0 2 .9 2 2"></path>
                      </svg>
                      <span id="copyBtnText">Copy APK Link</span>
                    </button>
                  </div>

                </div>
              </div>

              <!-- Specs Row -->
              <div class="trust-meta-row">
                <div class="trust-item">
                  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                  </svg>
                  <span>Android 8.0+ Ready</span>
                </div>
                <div class="trust-item">
                  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                  </svg>
                  <span>Zero Ads &bull; 100% Free</span>
                </div>
                <div class="trust-item">
                  <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                  </svg>
                  <span>Cloud Synced</span>
                </div>
              </div>

            </div>

            <!-- Right Column: Interactive Phone Mockup -->
            <div class="mockup-column">
              <div class="phone-outer-frame">
                
                <!-- Notch -->
                <div class="phone-island">
                  <div class="island-lens"></div>
                </div>

                <!-- Status Bar -->
                <div class="phone-status-bar">
                  <span>9:41</span>
                  <div style="display: flex; gap: 4px; align-items: center;">
                    <span>5G</span>
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor">
                      <rect x="2" y="7" width="18" height="10" rx="2" fill="none" stroke="currentColor" stroke-width="2"/>
                      <path d="M22 11v2"/>
                      <rect x="4" y="9" width="13" height="6" fill="currentColor"/>
                    </svg>
                  </div>
                </div>

                <!-- App Screen Content -->
                <div class="phone-screen-content">
                  
                  <!-- Top greeting -->
                  <div class="mockup-app-header">
                    <div>
                      <div class="mockup-greeting">Welcome Back</div>
                      <div class="mockup-app-title">StudyFlow AI</div>
                    </div>
                    <div class="mockup-avatar">AI</div>
                  </div>

                  <!-- Interactive Screen Tabs -->
                  <div class="mockup-tabs">
                    <button class="mock-tab-btn active" onclick="switchMockTab('tab-schedule', this)">Plan</button>
                    <button class="mock-tab-btn" onclick="switchMockTab('tab-tutor', this)">Tutor</button>
                    <button class="mock-tab-btn" onclick="switchMockTab('tab-academic', this)">Hub</button>
                    <button class="mock-tab-btn" onclick="switchMockTab('tab-analytics', this)">Stats</button>
                  </div>

                  <!-- Tab 1: Schedule -->
                  <div id="tab-schedule" class="mock-tab-pane active">
                    <div class="mock-card" style="border-left: 3px solid #6366F1;">
                      <div class="mock-card-label">
                        <span>Upcoming Lecture</span>
                        <span style="color: #34D399;">10:00 AM</span>
                      </div>
                      <div class="mock-card-title">Data Structures &amp; Algorithms</div>
                      <div class="mock-card-sub">Hall 304 &bull; Unit 3 Trees &amp; Graphs</div>
                    </div>

                    <div class="mock-card" style="border-left: 3px solid #EC4899;">
                      <div class="mock-card-label">
                        <span>AI Recommended Session</span>
                        <span style="color: #A5B4FC;">45 min</span>
                      </div>
                      <div class="mock-card-title">Operating Systems Memory Mgmt</div>
                      <div class="mock-card-sub">Calculated for peak focus window</div>
                      <div class="mock-progress-bg">
                        <div class="mock-progress-fill" style="width: 72%;"></div>
                      </div>
                    </div>

                    <div class="mock-card" style="border-left: 3px solid #10B981;">
                      <div class="mock-card-label">
                        <span>Daily Streak</span>
                        <span style="color: #FBBF24;">🔥 14 Days</span>
                      </div>
                      <div class="mock-card-title">Study Target on Track</div>
                      <div class="mock-card-sub">3.5 hrs completed / 4.0 hrs planned</div>
                    </div>
                  </div>

                  <!-- Tab 2: Gemini Tutor -->
                  <div id="tab-tutor" class="mock-tab-pane">
                    <div class="mock-chat-bubble" style="background: rgba(255,255,255,0.06); border-color: rgba(255,255,255,0.1);">
                      <strong>You asked:</strong>
                      Can you explain Dijkstra's algorithm intuitively with a real example?
                    </div>
                    <div class="mock-chat-bubble">
                      <strong>Gemini 2.5 Flash Tutor:</strong>
                      Think of Dijkstra's algorithm like GPS navigation finding the shortest travel time between road intersections. It greedily expands the lowest cost neighbor first!
                    </div>
                    <div class="mock-card" style="background: rgba(99,102,241,0.15);">
                      <div class="mock-card-label" style="color: #DDD6FE;">Generated Practice Flashcard</div>
                      <div class="mock-card-title" style="font-size: 12px;">What is the time complexity with min-heap?</div>
                      <div class="mock-card-sub" style="color: #C7D2FE;">Tap to reveal O((V + E) log V)</div>
                    </div>
                  </div>

                  <!-- Tab 3: Academic Hub -->
                  <div id="tab-academic" class="mock-tab-pane">
                    <div class="mock-card">
                      <div class="mock-card-label">Attendance Safeguard</div>
                      <div class="mock-card-title">Computer Networks: 86.5%</div>
                      <div class="mock-card-sub" style="color: #34D399;">Safe! You can miss 2 more classes.</div>
                    </div>
                    <div class="mock-card">
                      <div class="mock-card-label">Exam Countdown</div>
                      <div class="mock-card-title">Software Engineering Mid-Term</div>
                      <div class="mock-card-sub" style="color: #F87171;">In 4 days &bull; 8 topics remaining</div>
                      <div class="mock-progress-bg">
                        <div class="mock-progress-fill" style="width: 60%; background: #F87171;"></div>
                      </div>
                    </div>
                  </div>

                  <!-- Tab 4: Analytics -->
                  <div id="tab-analytics" class="mock-tab-pane">
                    <div class="mock-card">
                      <div class="mock-card-label">Weekly Mastery Score</div>
                      <div class="mock-card-title">91.4% Overall Readiness</div>
                      <div class="mock-progress-bg">
                        <div class="mock-progress-fill" style="width: 91%;"></div>
                      </div>
                    </div>
                    <div class="mock-card">
                      <div class="mock-card-label">Deep Work Distribution</div>
                      <div class="mock-card-title">28.5 Hours Logged This Week</div>
                      <div class="mock-card-sub">+18% compared to last week</div>
                    </div>
                  </div>

                </div>

                <!-- Bottom Bar -->
                <div class="phone-home-indicator"></div>

              </div>
            </div>

          </div>
        </section>

        <!-- Bento Grid: Core Capabilities -->
        <section class="features-section">
          <div class="section-header">
            <div class="section-badge">Engineering &bull; Capabilities</div>
            <h2 class="section-title">Designed for Serious Academic Success</h2>
            <p class="section-subtitle">Everything a student needs to stay organized, ace semester exams, and build sustainable study habits.</p>
          </div>

          <div class="bento-grid">
            
            <!-- Bento 1 -->
            <div class="bento-card bento-span-2">
              <div class="bento-icon-box" style="background: rgba(99, 102, 241, 0.15); color: #818CF8;">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                  <line x1="16" y1="2" x2="16" y2="6"></line>
                  <line x1="8" y1="2" x2="8" y2="6"></line>
                  <line x1="3" y1="10" x2="21" y2="10"></line>
                </svg>
              </div>
              <h3>Automated Smart Timetable Generator</h3>
              <p>Upload your college timetable and syllabus. StudyFlow AI maps out classes, balances study blocks, and accounts for your sleep and break intervals automatically.</p>
            </div>

            <!-- Bento 2 -->
            <div class="bento-card">
              <div class="bento-icon-box" style="background: rgba(236, 72, 153, 0.15); color: #F472B6;">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="m12 3-1.9 5.8a2 2 0 0 1-1.3 1.3L3 12l5.8 1.9a2 2 0 0 1 1.3 1.3L12 21l1.9-5.8a2 2 0 0 1 1.3-1.3L21 12l-5.8-1.9a2 2 0 0 1-1.3-1.3Z"/>
                </svg>
              </div>
              <h3>Gemini 2.5 Study Mentor</h3>
              <p>Break down complex formulas, generate quizzes, and clarify difficult concepts with an AI mentor available 24/7 on your phone.</p>
            </div>

            <!-- Bento 3 -->
            <div class="bento-card">
              <div class="bento-icon-box" style="background: rgba(16, 185, 129, 0.15); color: #34D399;">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                  <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
              </div>
              <h3>Attendance Safe-Zone</h3>
              <p>Real-time class attendance calculator keeps you safely above mandatory thresholds and calculates exact bunks allowed without penalty.</p>
            </div>

            <!-- Bento 4 -->
            <div class="bento-card bento-span-2">
              <div class="bento-icon-box" style="background: rgba(6, 182, 212, 0.15); color: #22D3EE;">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                  <line x1="18" y1="20" x2="18" y2="10"></line>
                  <line x1="12" y1="20" x2="12" y2="4"></line>
                  <line x1="6" y1="20" x2="6" y2="14"></line>
                </svg>
              </div>
              <h3>Mastery &amp; Retention Analytics</h3>
              <p>Visual retention curves, active recall streaks, and chapter readiness metrics prove you are genuinely prepared before you step into the exam hall.</p>
            </div>

          </div>
        </section>

        <!-- 3-Step Install Flow -->
        <section class="steps-section">
          <div class="steps-container">
            <div class="section-header" style="margin-bottom: 20px;">
              <h2 class="section-title">Quick 30-Second Android Setup</h2>
              <p class="section-subtitle">Since StudyFlow AI is distributed directly as an APK, follow these quick steps:</p>
            </div>

            <div class="steps-grid">
              
              <div class="step-box">
                <div class="step-number">1</div>
                <h4>Download APK</h4>
                <p>Tap the <strong>Download for Android</strong> button above. The 58.9 MB signed APK begins downloading instantly.</p>
              </div>

              <div class="step-box">
                <div class="step-number">2</div>
                <h4>Tap "Download Anyway"</h4>
                <p>Android displays a standard safety prompt for direct APKs. Tap <strong>Download anyway</strong> to proceed.</p>
              </div>

              <div class="step-box">
                <div class="step-number">3</div>
                <h4>Install &amp; Launch</h4>
                <p>Open the completed download from your notification drawer, tap <strong>Install</strong>, and begin planning your semester!</p>
              </div>

            </div>
          </div>
        </section>

        <!-- Technical Specs -->
        <section class="specs-section">
          <div class="specs-card">
            <div class="specs-grid">
              
              <div class="spec-cell">
                <div class="spec-label">Package Name</div>
                <div class="spec-value">com.studymate.ai</div>
              </div>

              <div class="spec-cell">
                <div class="spec-label">Version</div>
                <div class="spec-value">1.0.0 (Release)</div>
              </div>

              <div class="spec-cell">
                <div class="spec-label">File Size</div>
                <div class="spec-value">58.9 MB</div>
              </div>

              <div class="spec-cell">
                <div class="spec-label">OS Minimum</div>
                <div class="spec-value">Android 8.0+</div>
              </div>

            </div>
          </div>
        </section>

      </div>
    </main>

    <!-- Footer -->
    <footer class="site-footer">
      <div class="container">
        <div class="footer-inner">
          <div class="footer-copy">
            &copy; 2026 StudyFlow AI &bull; Built with Spring Boot 3, Flutter 3.29 &amp; Gemini Flash
          </div>
          <div class="footer-links">
            <a href="/download/app.apk" class="footer-link">Download APK</a>
            <a href="/health" class="footer-link">System Health</a>
          </div>
        </div>
      </div>
    </footer>
  </div>

  <!-- Download Started Modal -->
  <div id="downloadModal" class="download-modal-overlay">
    <div class="download-modal">
      <button onclick="closeDownloadModal()" class="modal-close-btn" aria-label="Close">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
          <line x1="18" y1="6" x2="6" y2="18"></line>
          <line x1="6" y1="6" x2="18" y2="18"></line>
        </svg>
      </button>

      <div class="modal-icon-glow">
        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
          <polyline points="7 10 12 15 17 10"></polyline>
          <line x1="12" y1="15" x2="12" y2="3"></line>
        </svg>
      </div>

      <h3 class="modal-title">Downloading StudyFlow AI</h3>
      <p class="modal-desc">Your download has started automatically. Check your notification panel or browser downloads.</p>

      <div class="modal-progress-bar">
        <div class="modal-progress-active"></div>
      </div>

      <p style="font-size: 13px; color: var(--text-muted);">
        Didn't start? <a href="/download/app.apk" class="modal-fallback-link">Click here to retry</a>
      </p>
    </div>
  </div>

  <!-- Toast Notification -->
  <div id="toastNotification" class="toast-pill">
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#10B981" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
      <polyline points="20 6 9 17 4 12"></polyline>
    </svg>
    <span id="toastMessage">Download link copied to clipboard!</span>
  </div>

  <!-- Interactive JavaScript -->
  <script>
    function triggerDownload() {
      // Show smooth progress modal
      const modal = document.getElementById('downloadModal');
      modal.classList.add('active');

      // Trigger actual download redirect immediately
      setTimeout(() => {
        window.location.href = '/download/app.apk';
      }, 300);
    }

    function closeDownloadModal() {
      document.getElementById('downloadModal').classList.remove('active');
    }

    function copyDownloadUrl() {
      const url = window.location.origin + '/download/app.apk';
      if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(url).then(() => {
          showToast('Direct APK link copied to clipboard!');
        }).catch(() => {
          fallbackCopy(url);
        });
      } else {
        fallbackCopy(url);
      }
    }

    function fallbackCopy(text) {
      const input = document.createElement('input');
      input.value = text;
      document.body.appendChild(input);
      input.select();
      document.execCommand('copy');
      document.body.removeChild(input);
      showToast('Direct APK link copied to clipboard!');
    }

    function showToast(msg) {
      const toast = document.getElementById('toastNotification');
      const text = document.getElementById('toastMessage');
      text.innerText = msg;
      toast.classList.add('show');
      setTimeout(() => {
        toast.classList.remove('show');
      }, 3500);
    }

    function switchMockTab(tabId, btn) {
      document.querySelectorAll('.mock-tab-pane').forEach(el => el.classList.remove('active'));
      document.querySelectorAll('.mock-tab-btn').forEach(el => el.classList.remove('active'));
      
      const target = document.getElementById(tabId);
      if (target) {
        target.classList.add('active');
      }
      btn.classList.add('active');
    }

    // Close modal on escape key or clicking backdrop
    window.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') closeDownloadModal();
    });

    document.getElementById('downloadModal').addEventListener('click', (e) => {
      if (e.target.id === 'downloadModal') closeDownloadModal();
    });
  </script>
</body>
</html>
""";
}
