# ============================================================
# StudyMate AI - Flutter Setup & Launch Script
# Run this script AFTER installing Flutter SDK
# ============================================================

param(
    [switch]$SkipFlutterCheck,
    [switch]$RunEmulator,
    [switch]$BuildAPK,
    [switch]$ReleaseAPK
)

$ErrorActionPreference = "Stop"
$ProjectRoot = "D:\AI app\studymate_ai"
$MobileDir   = "$ProjectRoot\mobile"
$BackendDir  = "$ProjectRoot\backend"

function Write-Header($msg) {
    Write-Host ""
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host "  $msg" -ForegroundColor Cyan
    Write-Host "================================================" -ForegroundColor Cyan
}

function Write-Step($msg) { Write-Host "[*] $msg" -ForegroundColor Yellow }
function Write-OK($msg)   { Write-Host "[OK] $msg" -ForegroundColor Green }
function Write-Err($msg)  { Write-Host "[ERR] $msg" -ForegroundColor Red }

# ---- 1. Check Prerequisites ----
Write-Header "Checking Prerequisites"

# Flutter
try {
    $flVer = (flutter --version 2>&1 | Select-String "Flutter") -replace "Flutter ", ""
    Write-OK "Flutter found: $flVer"
} catch {
    Write-Err "Flutter not found in PATH!"
    Write-Host ""
    Write-Host "To install Flutter:" -ForegroundColor White
    Write-Host "  1. Download from https://flutter.dev/docs/get-started/install/windows"
    Write-Host "  2. Extract to C:\flutter"
    Write-Host "  3. Add C:\flutter\bin to your PATH"
    Write-Host "  4. Run: flutter doctor"
    Write-Host ""
    if (-not $SkipFlutterCheck) { exit 1 }
}

# Java
try {
    $jVer = (java -version 2>&1)[0]
    Write-OK "Java found: $jVer"
} catch {
    Write-Err "Java not found. Install JDK 17 from https://adoptium.net/"
    exit 1
}

# Android SDK
$AndroidSdk = "$env:LOCALAPPDATA\Android\Sdk"
if (Test-Path $AndroidSdk) {
    Write-OK "Android SDK found at $AndroidSdk"
} else {
    Write-Err "Android SDK not found at $AndroidSdk"
    Write-Host "Install Android Studio from https://developer.android.com/studio"
    Write-Host "Or set ANDROID_HOME environment variable."
}

# ---- 2. Backend Server Check ----
Write-Header "Backend Server"
try {
    $resp = Invoke-RestMethod -Uri "http://localhost:8000/health" -TimeoutSec 3
    Write-OK "Backend is running: $($resp | ConvertTo-Json -Compress)"
} catch {
    Write-Step "Starting backend server..."
    Start-Process powershell -ArgumentList @(
        "-NoExit",
        "-Command",
        "`$env:PYTHONPATH='$BackendDir'; & '$BackendDir\venv\Scripts\python.exe' -m uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload"
    ) -WorkingDirectory $BackendDir
    Start-Sleep -Seconds 4
    try {
        $resp = Invoke-RestMethod -Uri "http://localhost:8000/health" -TimeoutSec 5
        Write-OK "Backend started: $($resp | ConvertTo-Json -Compress)"
    } catch {
        Write-Err "Backend failed to start. Check $BackendDir"
    }
}

# ---- 3. Flutter pub get ----
Write-Header "Installing Flutter Dependencies"
Set-Location $MobileDir
Write-Step "Running flutter pub get..."
flutter pub get
Write-OK "Dependencies installed."

# ---- 4. Flutter analyze ----
Write-Header "Static Analysis"
Write-Step "Running flutter analyze..."
flutter analyze --no-fatal-infos
Write-OK "Analysis complete."

# ---- 5. Flutter test ----
Write-Header "Running Tests"
Write-Step "Running flutter test..."
flutter test test/model_test.dart
Write-OK "Tests complete."

# ---- 6. Run / Build ----
if ($RunEmulator) {
    Write-Header "Launching on Emulator"
    Write-Step "Starting app on Android emulator..."
    Write-Host "Make sure an emulator is running: flutter emulators --launch <emulator_id>"
    flutter run -d android
}

if ($BuildAPK) {
    Write-Header "Building Debug APK"
    Write-Step "Building debug APK..."
    flutter build apk --debug
    $apkPath = "$MobileDir\build\app\outputs\flutter-apk\app-debug.apk"
    if (Test-Path $apkPath) {
        Write-OK "Debug APK built: $apkPath"
        $sizeMB = [math]::Round((Get-Item $apkPath).Length / 1MB, 1)
        Write-Host "   Size: ${sizeMB} MB" -ForegroundColor White
    }
}

if ($ReleaseAPK) {
    Write-Header "Building Release APK"
    Write-Step "Building release APK..."
    flutter build apk --release
    $apkPath = "$MobileDir\build\app\outputs\flutter-apk\app-release.apk"
    if (Test-Path $apkPath) {
        Write-OK "Release APK built: $apkPath"
        $sizeMB = [math]::Round((Get-Item $apkPath).Length / 1MB, 1)
        Write-Host "   Size: ${sizeMB} MB" -ForegroundColor White
    }
}

Write-Header "Setup Complete!"
Write-Host ""
Write-Host "Next steps:" -ForegroundColor White
Write-Host "  flutter emulators                       # List available emulators"
Write-Host "  flutter emulators --launch <id>         # Start an emulator"
Write-Host "  flutter devices                         # List connected devices"
Write-Host "  flutter run                             # Run app on connected device"
Write-Host "  flutter build apk --debug               # Build debug APK"
Write-Host "  flutter build apk --release             # Build release APK"
Write-Host ""
Write-Host "API Docs: http://localhost:8000/docs" -ForegroundColor Cyan
Write-Host ""
