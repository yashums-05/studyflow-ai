import 'package:dio/dio.dart';
import '../../core/constants/api_constants.dart';
import '../../core/services/api_client.dart';
import '../models/user_model.dart';
import '../models/subject_model.dart';
import '../models/study_session_model.dart';
import '../models/exam_model.dart';
import '../models/analytics_model.dart';
import '../models/quiz_model.dart';

class ApiRepository {
  final Dio _dio = ApiClient().dio;

  // User Profile
  Future<UserModel> getUserProfile() async {
    final res = await _dio.get(ApiConstants.userProfile);
    return UserModel.fromJson(res.data);
  }

  Future<UserModel> updateUserProfile(Map<String, dynamic> data) async {
    final res = await _dio.put(ApiConstants.userProfile, data: data);
    return UserModel.fromJson(res.data);
  }

  // Subjects
  Future<List<SubjectModel>> getSubjects() async {
    final res = await _dio.get(ApiConstants.subjects);
    return (res.data as List).map((e) => SubjectModel.fromJson(e)).toList();
  }

  Future<SubjectModel> createSubject(Map<String, dynamic> data) async {
    final res = await _dio.post(ApiConstants.subjects, data: data);
    return SubjectModel.fromJson(res.data);
  }

  Future<void> deleteSubject(String subjectId) async {
    await _dio.delete("${ApiConstants.subjects}/$subjectId");
  }

  // Units & Topics
  Future<UnitModel> createUnit(Map<String, dynamic> data) async {
    final res = await _dio.post(ApiConstants.units, data: data);
    return UnitModel.fromJson(res.data);
  }

  Future<TopicModel> createTopic(Map<String, dynamic> data) async {
    final res = await _dio.post(ApiConstants.topics, data: data);
    return TopicModel.fromJson(res.data);
  }

  Future<TopicModel> updateTopic(String topicId, Map<String, dynamic> data) async {
    final res = await _dio.put("${ApiConstants.topics}/$topicId", data: data);
    return TopicModel.fromJson(res.data);
  }

  // Timetable
  Future<Map<String, dynamic>> generateTimetable(Map<String, dynamic> req) async {
    final res = await _dio.post(ApiConstants.timetableGenerate, data: req);
    return res.data;
  }

  Future<List<StudySessionModel>> getDailyTimetable({String? date}) async {
    final res = await _dio.get(
      ApiConstants.timetableDaily,
      queryParameters: date != null ? {'date': date} : null,
    );
    return (res.data as List).map((e) => StudySessionModel.fromJson(e)).toList();
  }

  Future<List<StudySessionModel>> getWeeklyTimetable({String? startDate}) async {
    final res = await _dio.get(
      ApiConstants.timetableWeekly,
      queryParameters: startDate != null ? {'start_date': startDate} : null,
    );
    return (res.data as List).map((e) => StudySessionModel.fromJson(e)).toList();
  }

  Future<Map<String, dynamic>> rescheduleMissed({String? sessionId, bool all = true}) async {
    final res = await _dio.post(ApiConstants.timetableReschedule, data: {
      'sessionId': sessionId,
      'rescheduleAllMissed': all,
    });
    return res.data;
  }

  // Study Sessions
  Future<void> startSession(String sessionId) async {
    await _dio.post(ApiConstants.studySessionStart, data: {'sessionId': sessionId});
  }

  Future<void> completeSession(String sessionId, int actualMinutes, int rating, String notes) async {
    await _dio.post(
      "/study-sessions/$sessionId/complete",
      data: {
        'actualMinutes': actualMinutes,
        'understandingRating': rating,
        'notes': notes,
        'topicStatus': 'completed',
      },
    );
  }

  Future<List<StudySessionModel>> getSessionHistory({int limit = 50}) async {
    try {
      final res = await _dio.get(
        '/study-sessions/history',
        queryParameters: {'limit': limit},
      );
      return (res.data as List).map((e) => StudySessionModel.fromJson(e)).toList();
    } catch (_) {
      return [];
    }
  }

  // Exams
  Future<List<ExamModel>> getExams() async {
    final res = await _dio.get(ApiConstants.exams);
    return (res.data as List).map((e) => ExamModel.fromJson(e)).toList();
  }

  Future<ExamModel> createExam(Map<String, dynamic> data) async {
    final res = await _dio.post(ApiConstants.exams, data: data);
    return ExamModel.fromJson(res.data);
  }

  Future<void> deleteExam(String examId) async {
    await _dio.delete("${ApiConstants.exams}/$examId");
  }

  // Analytics
  Future<DashboardAnalyticsModel> getDashboardAnalytics() async {
    final res = await _dio.get(ApiConstants.analyticsDashboard);
    return DashboardAnalyticsModel.fromJson(res.data);
  }

  Future<WeeklyAnalyticsModel> getWeeklyAnalytics() async {
    final res = await _dio.get(ApiConstants.analyticsWeekly);
    return WeeklyAnalyticsModel.fromJson(res.data);
  }

  // AI Features
  Future<Map<String, dynamic>> aiChat(String message, {String? conversationId, String? subject, String? topic}) async {
    final res = await _dio.post(ApiConstants.aiChat, data: {
      'message': message,
      'conversationId': conversationId,
      'subjectContext': subject,
      'topicContext': topic,
    });
    return res.data;
  }

  Future<Map<String, dynamic>> aiExplain(String subject, String topic) async {
    final res = await _dio.post(ApiConstants.aiExplain, data: {
      'subject': subject,
      'topic': topic,
    });
    return res.data;
  }

  Future<QuizResponseModel> aiGenerateQuiz(String subject, String topic, {int numQuestions = 3}) async {
    final res = await _dio.post(ApiConstants.aiQuiz, data: {
      'subject': subject,
      'topic': topic,
      'numQuestions': numQuestions,
    });
    return QuizResponseModel.fromJson(res.data);
  }
}
