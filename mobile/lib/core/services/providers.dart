import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../shared/models/user_model.dart';
import '../../shared/models/subject_model.dart';
import '../../shared/models/study_session_model.dart';
import '../../shared/models/exam_model.dart';
import '../../shared/models/analytics_model.dart';
import '../../shared/models/class_slot_model.dart';
import '../../shared/models/academic_event_model.dart';
import '../../shared/models/schedule_suggestion_model.dart';
import '../../shared/repositories/api_repository.dart';

final apiRepositoryProvider = Provider<ApiRepository>((ref) {
  return ApiRepository();
});

// 1. Auth Provider
final authProvider = StateNotifierProvider<AuthNotifier, AsyncValue<UserModel?>>((ref) {
  return AuthNotifier(ref.watch(apiRepositoryProvider));
});

class AuthNotifier extends StateNotifier<AsyncValue<UserModel?>> {
  final ApiRepository _repo;
  AuthNotifier(this._repo) : super(const AsyncValue.loading()) {
    checkAuth();
  }

  Future<void> checkAuth() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final token = prefs.getString('auth_token');
      if (token == null || token.trim().isEmpty) {
        state = const AsyncValue.data(null);
        return;
      }
      final user = await _repo.getUserProfile();
      state = AsyncValue.data(user);
    } catch (e, st) {
      state = AsyncValue.error(e, st);
    }
  }

  Future<void> login(String email, String password) async {
    state = const AsyncValue.loading();
    try {
      final res = await _repo.login(email.trim(), password);
      final token = res['token']?.toString() ?? 'user_${email.split('@').first}';
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString('auth_token', token);

      final userData = res['user'];
      UserModel user;
      if (userData is Map<String, dynamic>) {
        user = UserModel.fromJson(userData);
      } else {
        user = await _repo.getUserProfile();
      }
      state = AsyncValue.data(user);
    } catch (e, st) {
      state = AsyncValue.error(e, st);
      rethrow;
    }
  }

  Future<void> register({
    required String email,
    required String password,
    required String fullName,
    String? college,
    String? degree,
    String? department,
    int? semester,
  }) async {
    state = const AsyncValue.loading();
    try {
      final res = await _repo.register({
        'email': email.trim(),
        'password': password,
        'fullName': fullName.trim(),
        if (college != null) 'collegeName': college.trim(),
        if (degree != null) 'degree': degree.trim(),
        if (department != null) 'department': department.trim(),
        if (semester != null) 'semester': semester,
      });

      final token = res['token']?.toString() ?? 'user_${email.split('@').first}';
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString('auth_token', token);

      final userData = res['user'];
      UserModel user;
      if (userData is Map<String, dynamic>) {
        user = UserModel.fromJson(userData);
      } else {
        user = await _repo.getUserProfile();
      }
      state = AsyncValue.data(user);
    } catch (e, st) {
      state = AsyncValue.error(e, st);
      rethrow;
    }
  }

  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('auth_token');
    state = const AsyncValue.data(null);
  }
}

// 2. Dashboard Analytics Provider
final dashboardProvider = FutureProvider<DashboardAnalyticsModel>((ref) async {
  final repo = ref.watch(apiRepositoryProvider);
  return await repo.getDashboardAnalytics();
});

// 3. Subjects Provider
final subjectsProvider = AsyncNotifierProvider<SubjectsNotifier, List<SubjectModel>>(() {
  return SubjectsNotifier();
});

class SubjectsNotifier extends AsyncNotifier<List<SubjectModel>> {
  @override
  Future<List<SubjectModel>> build() async {
    final repo = ref.watch(apiRepositoryProvider);
    return await repo.getSubjects();
  }

  Future<void> refresh() async {
    state = const AsyncValue.loading();
    state = await AsyncValue.guard(() => ref.read(apiRepositoryProvider).getSubjects());
  }

  Future<void> addSubject(Map<String, dynamic> data) async {
    await ref.read(apiRepositoryProvider).createSubject(data);
    await refresh();
  }

  Future<void> deleteSubject(String subjectId) async {
    await ref.read(apiRepositoryProvider).deleteSubject(subjectId);
    await refresh();
  }
}

// 4. Timetable Provider
final timetableProvider = AsyncNotifierProvider<TimetableNotifier, List<StudySessionModel>>(() {
  return TimetableNotifier();
});

class TimetableNotifier extends AsyncNotifier<List<StudySessionModel>> {
  @override
  Future<List<StudySessionModel>> build() async {
    final repo = ref.watch(apiRepositoryProvider);
    return await repo.getDailyTimetable();
  }

  Future<void> fetchDaily({String? date}) async {
    state = const AsyncValue.loading();
    state = await AsyncValue.guard(() => ref.read(apiRepositoryProvider).getDailyTimetable(date: date));
  }

  Future<void> generateTimetable(Map<String, dynamic> req) async {
    state = const AsyncValue.loading();
    await ref.read(apiRepositoryProvider).generateTimetable(req);
    await fetchDaily();
    ref.invalidate(dashboardProvider);
  }

  Future<void> rescheduleMissed() async {
    state = const AsyncValue.loading();
    await ref.read(apiRepositoryProvider).rescheduleMissed();
    await fetchDaily();
    ref.invalidate(dashboardProvider);
  }
}

// 5. Exams Provider
final examsProvider = AsyncNotifierProvider<ExamsNotifier, List<ExamModel>>(() {
  return ExamsNotifier();
});

class ExamsNotifier extends AsyncNotifier<List<ExamModel>> {
  @override
  Future<List<ExamModel>> build() async {
    final repo = ref.watch(apiRepositoryProvider);
    return await repo.getExams();
  }

  Future<void> refresh() async {
    state = const AsyncValue.loading();
    state = await AsyncValue.guard(() => ref.read(apiRepositoryProvider).getExams());
  }

  Future<void> addExam(Map<String, dynamic> data) async {
    await ref.read(apiRepositoryProvider).createExam(data);
    await refresh();
  }

  Future<void> deleteExam(String examId) async {
    await ref.read(apiRepositoryProvider).deleteExam(examId);
    await refresh();
  }
}

// 6. Pomodoro Focus Timer Provider
class FocusTimerState {
  final int totalSeconds;
  final int remainingSeconds;
  final bool isRunning;
  final bool isBreak;
  final int completedSessions;
  final String? activeSessionId;
  final String? activeTopicName;

  FocusTimerState({
    this.totalSeconds = 25 * 60,
    this.remainingSeconds = 25 * 60,
    this.isRunning = false,
    this.isBreak = false,
    this.completedSessions = 0,
    this.activeSessionId,
    this.activeTopicName,
  });

  FocusTimerState copyWith({
    int? totalSeconds,
    int? remainingSeconds,
    bool? isRunning,
    bool? isBreak,
    int? completedSessions,
    String? activeSessionId,
    String? activeTopicName,
  }) {
    return FocusTimerState(
      totalSeconds: totalSeconds ?? this.totalSeconds,
      remainingSeconds: remainingSeconds ?? this.remainingSeconds,
      isRunning: isRunning ?? this.isRunning,
      isBreak: isBreak ?? this.isBreak,
      completedSessions: completedSessions ?? this.completedSessions,
      activeSessionId: activeSessionId ?? this.activeSessionId,
      activeTopicName: activeTopicName ?? this.activeTopicName,
    );
  }
}

final focusTimerProvider = StateNotifierProvider<FocusTimerNotifier, FocusTimerState>((ref) {
  return FocusTimerNotifier();
});

class FocusTimerNotifier extends StateNotifier<FocusTimerState> {
  Timer? _timer;

  FocusTimerNotifier() : super(FocusTimerState());

  void setSession({required String sessionId, required String topicName, int focusMinutes = 25}) {
    _timer?.cancel();
    state = FocusTimerState(
      activeSessionId: sessionId,
      activeTopicName: topicName,
      totalSeconds: focusMinutes * 60,
      remainingSeconds: focusMinutes * 60,
    );
  }

  void start() {
    if (state.isRunning) return;
    state = state.copyWith(isRunning: true);
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (state.remainingSeconds > 0) {
        state = state.copyWith(remainingSeconds: state.remainingSeconds - 1);
      } else {
        _timer?.cancel();
        // Switch between focus and break
        if (!state.isBreak) {
          final isLongBreak = (state.completedSessions + 1) % 4 == 0;
          final breakSeconds = (isLongBreak ? 15 : 5) * 60;
          state = state.copyWith(
            isRunning: false,
            isBreak: true,
            completedSessions: state.completedSessions + 1,
            totalSeconds: breakSeconds,
            remainingSeconds: breakSeconds,
          );
        } else {
          state = state.copyWith(
            isRunning: false,
            isBreak: false,
            totalSeconds: 25 * 60,
            remainingSeconds: 25 * 60,
          );
        }
      }
    });
  }

  void pause() {
    _timer?.cancel();
    state = state.copyWith(isRunning: false);
  }

  void reset() {
    _timer?.cancel();
    state = state.copyWith(
      isRunning: false,
      remainingSeconds: state.totalSeconds,
    );
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }
}

// 7. Academic Hub Providers
final classTimetableProvider = FutureProvider<List<ClassSlotModel>>((ref) async {
  return ref.watch(apiRepositoryProvider).getClassTimetable();
});

final calendarEventsProvider = FutureProvider<List<AcademicEventModel>>((ref) async {
  return ref.watch(apiRepositoryProvider).getCalendarEvents();
});

final academicSuggestionsProvider = FutureProvider<List<ScheduleSuggestionModel>>((ref) async {
  return ref.watch(apiRepositoryProvider).getAcademicSuggestions();
});
