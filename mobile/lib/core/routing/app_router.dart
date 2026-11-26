import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../core/constants/app_colors.dart';
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
      path: '/',
      redirect: (context, state) => '/home',
    ),
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
      builder: (context, state) => const MainShell(initialIndex: 0),
    ),
    GoRoute(
      path: '/subjects',
      builder: (context, state) => const MainShell(initialIndex: 1),
    ),
    GoRoute(
      path: '/timetable',
      builder: (context, state) => const MainShell(initialIndex: 2),
    ),
    GoRoute(
      path: '/tutor',
      builder: (context, state) => const MainShell(initialIndex: 3),
    ),
    GoRoute(
      path: '/profile',
      builder: (context, state) => const MainShell(initialIndex: 4),
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
  errorBuilder: (context, state) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: AppColors.primary.withOpacity(0.1),
                  shape: BoxShape.circle,
                ),
                child: const Icon(Icons.explore_off_rounded, size: 56, color: AppColors.primary),
              ),
              const SizedBox(height: 20),
              const Text(
                'Page Not Found',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
              ),
              const SizedBox(height: 8),
              Text(
                'Route "${state.uri}" does not exist.',
                textAlign: TextAlign.center,
                style: const TextStyle(fontSize: 14, color: AppColors.textSecondary),
              ),
              const SizedBox(height: 24),
              ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.primary,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                onPressed: () => context.go('/home'),
                icon: const Icon(Icons.home_rounded),
                label: const Text('Back to Home', style: TextStyle(fontWeight: FontWeight.bold)),
              ),
            ],
          ),
        ),
      ),
    );
  },
);
