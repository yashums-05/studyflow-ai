import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../features/authentication/presentation/splash_screen.dart';
import '../../features/authentication/presentation/welcome_screen.dart';
import '../../features/authentication/presentation/login_screen.dart';
import '../../features/authentication/presentation/register_screen.dart';
import '../../features/authentication/presentation/forgot_password_screen.dart';
import '../../features/profile/presentation/academic_profile_setup_screen.dart';
import '../../features/profile/presentation/study_preferences_screen.dart';
import '../../features/dashboard/presentation/main_shell.dart';
import '../../features/subjects/presentation/subject_detail_screen.dart';
import '../../features/syllabus/presentation/syllabus_upload_screen.dart';
import '../../features/focus_mode/presentation/focus_timer_screen.dart';
import '../../features/ai_tutor/presentation/quiz_screen.dart';
import '../../features/ai_tutor/presentation/ai_tutor_chat_screen.dart';
import '../../features/analytics/presentation/analytics_screen.dart';
import '../../features/exams/presentation/exams_screen.dart';
import '../../features/study_sessions/presentation/study_session_history_screen.dart';
import '../../features/academic_hub/presentation/academic_hub_screen.dart';

final appRouter = GoRouter(
  initialLocation: '/splash',
  routes: [
    GoRoute(
      path: '/splash',
      builder: (context, state) => const SplashScreen(),
    ),
    GoRoute(
      path: '/welcome',
      builder: (context, state) => const WelcomeScreen(),
    ),
    GoRoute(
      path: '/login',
      builder: (context, state) => const LoginScreen(),
    ),
    GoRoute(
      path: '/register',
      builder: (context, state) => const RegisterScreen(),
    ),
    GoRoute(
      path: '/forgot-password',
      builder: (context, state) => const ForgotPasswordScreen(),
    ),
    GoRoute(
      path: '/onboarding/academic',
      builder: (context, state) => const AcademicProfileSetupScreen(),
    ),
    GoRoute(
      path: '/onboarding/preferences',
      builder: (context, state) => const StudyPreferencesScreen(),
    ),
    GoRoute(
      path: '/home',
      builder: (context, state) => const MainShell(),
    ),
    GoRoute(
      path: '/subjects/:id',
      builder: (context, state) {
        final id = state.pathParameters['id'] ?? '';
        return SubjectDetailScreen(subjectId: id);
      },
    ),
    GoRoute(
      path: '/syllabus-upload',
      builder: (context, state) => const SyllabusUploadScreen(),
    ),
    GoRoute(
      path: '/focus-timer',
      builder: (context, state) => const FocusTimerScreen(),
    ),
    GoRoute(
      path: '/ai-tutor',
      builder: (context, state) => const AiTutorChatScreen(),
    ),
    GoRoute(
      path: '/quiz',
      builder: (context, state) => const QuizScreen(),
    ),
    GoRoute(
      path: '/analytics',
      builder: (context, state) => const AnalyticsScreen(),
    ),
    GoRoute(
      path: '/exams',
      builder: (context, state) => const ExamsScreen(),
    ),
    GoRoute(
      path: '/session-history',
      builder: (context, state) => const StudySessionHistoryScreen(),
    ),
    GoRoute(
      path: '/academic-hub',
      builder: (context, state) => const AcademicHubScreen(),
    ),
  ],
);
