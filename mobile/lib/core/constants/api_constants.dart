class ApiConstants {
  // Configured default backend URL (Live Cloud Render Endpoint)
  static const String defaultBaseUrl = "https://studyflow-ai-app.onrender.com/api/v1";
  
  // Auth Endpoints
  static const String authRegister = "/auth/register";
  static const String authLogin = "/auth/login";
  static const String authLogout = "/auth/logout";

  // Endpoints
  static const String userProfile = "/users/profile";
  static const String subjects = "/subjects";
  static const String units = "/units";
  static const String topics = "/topics";
  static const String timetableGenerate = "/timetable/generate";
  static const String timetableDaily = "/timetable/daily";
  static const String timetableWeekly = "/timetable/weekly";
  static const String timetableReschedule = "/timetable/reschedule";
  static const String studySessionStart = "/study-sessions/start";
  static const String studySessionComplete = "/study-sessions/{id}/complete";
  static const String studySessionPause = "/study-sessions/{id}/pause";
  static const String studySessionHistory = "/study-sessions/history";
  static const String exams = "/exams";
  static const String aiChat = "/ai/chat";
  static const String aiExplain = "/ai/explain";
  static const String aiQuiz = "/ai/generate-quiz";
  static const String aiRecommendations = "/ai/recommendations";
  static const String aiAnalyzeSyllabus = "/ai/analyze-syllabus";
  static const String analyticsDashboard = "/analytics/dashboard";
  static const String analyticsWeekly = "/analytics/weekly";
  static const String analyticsSubjects = "/analytics/subjects";
  static const String documentsUpload = "/documents/upload";
  static const String documents = "/documents";
  
  // Academic Schedule & Upload Hub Endpoints
  static const String academicClassTimetable = "/academic/class-timetable";
  static const String academicCalendarEvents = "/academic/calendar-events";
  static const String academicSuggestions = "/academic/suggestions";
  static const String academicUploadParse = "/academic/upload-and-parse";
}
