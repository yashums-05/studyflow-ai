import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import 'package:percent_indicator/percent_indicator.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import '../../../core/widgets/stat_card.dart';

class HomeDashboardScreen extends ConsumerWidget {
  const HomeDashboardScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final userAsync = ref.watch(authProvider);
    final dashAsync = ref.watch(dashboardProvider);
    final examsAsync = ref.watch(examsProvider);
    final todayStr = DateFormat('EEEE, MMM d, yyyy').format(DateTime.now());

    final userName = userAsync.value?.fullName ?? 'Student';

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Hello, $userName 👋',
              style: const TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                color: AppColors.textPrimary,
              ),
            ),
            Text(
              todayStr,
              style: const TextStyle(fontSize: 12, color: AppColors.textSecondary),
            ),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.notifications_none_rounded, color: AppColors.textPrimary),
            onPressed: () {},
          ),
          IconButton(
            icon: const Icon(Icons.refresh_rounded, color: AppColors.textPrimary),
            onPressed: () {
              ref.invalidate(dashboardProvider);
              ref.invalidate(examsProvider);
            },
          ),
        ],
      ),
      body: dashAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (err, _) => Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(Icons.cloud_off_rounded, size: 48, color: AppColors.textMuted),
              const SizedBox(height: 12),
              const Text('Could not load dashboard data.'),
              const SizedBox(height: 12),
              ElevatedButton(
                onPressed: () => ref.invalidate(dashboardProvider),
                child: const Text('Retry'),
              ),
            ],
          ),
        ),
        data: (dash) {
          return SingleChildScrollView(
            padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // 1. Overall Syllabus Completion Banner
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(18.0),
                    child: Row(
                      children: [
                        CircularPercentIndicator(
                          radius: 44.0,
                          lineWidth: 9.0,
                          percent: (dash.overallCompletionPercentage / 100.0).clamp(0.0, 1.0),
                          center: Text(
                            "${dash.overallCompletionPercentage.toStringAsFixed(0)}%",
                            style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                          ),
                          progressColor: AppColors.primary,
                          backgroundColor: AppColors.primary.withOpacity(0.12),
                          circularStrokeCap: CircularStrokeCap.round,
                        ),
                        const SizedBox(width: 18),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                "Academic Progress",
                                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                "${dash.completedTopics} of ${dash.totalTopics} topics completed across ${dash.totalSubjects} subjects.",
                                style: TextStyle(fontSize: 13, color: AppColors.textSecondary),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 16),

                // 2. Today's Study Summary Row
                Row(
                  children: [
                    Expanded(
                      child: StatCard(
                        title: "Today's Study",
                        value: "${dash.completedStudyHoursToday}/${dash.plannedStudyHoursToday}h",
                        subtitle: "${dash.remainingSessionsToday} sessions pending",
                        icon: Icons.timer_outlined,
                        iconColor: AppColors.primary,
                      ),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: StatCard(
                        title: "Study Streak",
                        value: "${dash.studyStreakDays} Days",
                        subtitle: "Consistent progress",
                        icon: Icons.local_fire_department_rounded,
                        iconColor: AppColors.warning,
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 16),

                // 3. Quick Actions
                const Text(
                  'Quick Actions',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                ),
                const SizedBox(height: 10),
                SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: Row(
                    children: [
                      _buildQuickAction(
                        icon: Icons.play_arrow_rounded,
                        label: 'Focus Timer',
                        color: AppColors.primary,
                        onTap: () => context.push('/focus-timer'),
                      ),
                      _buildQuickAction(
                        icon: Icons.auto_awesome_rounded,
                        label: 'AI Tutor',
                        color: AppColors.secondary,
                        onTap: () => context.push('/ai-tutor'),
                      ),
                      _buildQuickAction(
                        icon: Icons.upload_file_rounded,
                        label: 'PDF Syllabus',
                        color: AppColors.info,
                        onTap: () => context.push('/syllabus-upload'),
                      ),
                      _buildQuickAction(
                        icon: Icons.insights_rounded,
                        label: 'Analytics',
                        color: AppColors.success,
                        onTap: () => context.push('/analytics'),
                      ),
                      _buildQuickAction(
                        icon: Icons.event_note_rounded,
                        label: 'My Exams',
                        color: AppColors.warning,
                        onTap: () => context.push('/exams'),
                      ),
                      _buildQuickAction(
                        icon: Icons.history_rounded,
                        label: 'History',
                        color: AppColors.textSecondary,
                        onTap: () => context.push('/session-history'),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 20),

                // 4. AI Study Recommendations
                const Text(
                  'AI Study Recommendations ✨',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                ),
                const SizedBox(height: 8),
                if (dash.aiRecommendations.isEmpty)
                  const Card(
                    child: Padding(
                      padding: EdgeInsets.all(16.0),
                      child: Text('Add subjects and generate a timetable to unlock personalized AI insights.'),
                    ),
                  )
                else
                  ...dash.aiRecommendations.map((rec) => Card(
                        margin: const EdgeInsets.only(bottom: 8),
                        child: Padding(
                          padding: const EdgeInsets.all(14.0),
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Container(
                                padding: const EdgeInsets.all(6),
                                decoration: BoxDecoration(
                                  color: AppColors.primary.withOpacity(0.1),
                                  shape: BoxShape.circle,
                                ),
                                child: const Icon(Icons.lightbulb_outline_rounded, color: AppColors.primary, size: 20),
                              ),
                              const SizedBox(width: 12),
                              Expanded(
                                child: Text(
                                  rec,
                                  style: const TextStyle(fontSize: 13, height: 1.4, color: AppColors.textPrimary),
                                ),
                              ),
                            ],
                          ),
                        ),
                      )),
                const SizedBox(height: 20),

                // 5. Upcoming Examinations
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      'Upcoming Exams 📝',
                      style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                    ),
                    TextButton(
                      onPressed: () => context.push('/exams'),
                      child: const Text('View All'),
                    ),
                  ],
                ),
                examsAsync.when(
                  loading: () => const LinearProgressIndicator(),
                  error: (_, __) => const Text('Could not load exams'),
                  data: (exams) {
                    if (exams.isEmpty) {
                      return Card(
                        child: Padding(
                          padding: const EdgeInsets.all(16.0),
                          child: Row(
                            children: const [
                              Icon(Icons.event_available_rounded, color: AppColors.success),
                              SizedBox(width: 12),
                              Text('No upcoming examinations scheduled!'),
                            ],
                          ),
                        ),
                      );
                    }
                    return Column(
                      children: exams.map((e) {
                        return Card(
                          child: ListTile(
                            leading: Container(
                              padding: const EdgeInsets.all(10),
                              decoration: BoxDecoration(
                                color: AppColors.primary.withOpacity(0.12),
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: const Icon(Icons.school_rounded, color: AppColors.primary),
                            ),
                            title: Text(e.examName, style: const TextStyle(fontWeight: FontWeight.bold)),
                            subtitle: Text('${e.subjectName} • ${DateFormat('d MMM yyyy').format(e.examDate)}'),
                            trailing: Container(
                              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                              decoration: BoxDecoration(
                                color: e.examDate.difference(DateTime.now()).inDays <= 7 ? AppColors.error.withOpacity(0.12) : AppColors.primary.withOpacity(0.12),
                                borderRadius: BorderRadius.circular(20),
                              ),
                              child: Text(
                                '${e.examDate.difference(DateTime.now()).inDays} days left',
                                style: TextStyle(
                                  color: e.examDate.difference(DateTime.now()).inDays <= 7 ? AppColors.error : AppColors.primary,
                                  fontWeight: FontWeight.bold,
                                  fontSize: 12,
                                ),
                              ),
                            ),
                          ),
                        );
                      }).toList(),
                    );
                  },
                ),
                const SizedBox(height: 24),
              ],
            ),
          );
        },
      ),
    );
  }

  Widget _buildQuickAction({
    required IconData icon,
    required String label,
    required Color color,
    required VoidCallback onTap,
  }) {
    return Padding(
      padding: const EdgeInsets.only(right: 12.0),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(16),
        child: Container(
          width: 105,
          padding: const EdgeInsets.symmetric(vertical: 14, horizontal: 8),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(16),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(0.04),
                blurRadius: 8,
                offset: const Offset(0, 2),
              ),
            ],
          ),
          child: Column(
            children: [
              Container(
                padding: const EdgeInsets.all(10),
                decoration: BoxDecoration(
                  color: color.withOpacity(0.12),
                  shape: BoxShape.circle,
                ),
                child: Icon(icon, color: color, size: 24),
              ),
              const SizedBox(height: 8),
              Text(
                label,
                textAlign: TextAlign.center,
                style: const TextStyle(fontSize: 12, fontWeight: FontWeight.w600, color: AppColors.textPrimary),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

