import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:fl_chart/fl_chart.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import '../../../core/widgets/stat_card.dart';
import '../../../shared/models/analytics_model.dart';
import '../../../shared/repositories/api_repository.dart';

// Real weekly analytics provider
final weeklyAnalyticsProvider = FutureProvider<WeeklyAnalyticsModel>((ref) async {
  final repo = ref.watch(apiRepositoryProvider);
  return await repo.getWeeklyAnalytics();
});

class AnalyticsScreen extends ConsumerWidget {
  const AnalyticsScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final dashAsync = ref.watch(dashboardProvider);
    final weeklyAsync = ref.watch(weeklyAnalyticsProvider);

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Study Analytics',
            style: TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
        backgroundColor: Colors.transparent,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh_rounded),
            onPressed: () {
              ref.invalidate(dashboardProvider);
              ref.invalidate(weeklyAnalyticsProvider);
            },
          ),
        ],
      ),
      body: dashAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (err, _) => _buildErrorView(context, ref, err.toString()),
        data: (dash) => weeklyAsync.when(
          loading: () => _buildBody(context, ref, dash, null),
          error: (_, __) => _buildBody(context, ref, dash, null),
          data: (weekly) => _buildBody(context, ref, dash, weekly),
        ),
      ),
    );
  }

  Widget _buildErrorView(BuildContext context, WidgetRef ref, String error) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.error_outline, size: 64, color: AppColors.error),
            const SizedBox(height: 16),
            const Text('Could not load analytics', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
            const SizedBox(height: 8),
            Text(error, style: const TextStyle(color: AppColors.textSecondary), textAlign: TextAlign.center),
            const SizedBox(height: 24),
            ElevatedButton.icon(
              onPressed: () => ref.invalidate(dashboardProvider),
              icon: const Icon(Icons.refresh),
              label: const Text('Retry'),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildBody(BuildContext context, WidgetRef ref, DashboardAnalyticsModel dash, WeeklyAnalyticsModel? weekly) {
    return RefreshIndicator(
      onRefresh: () async {
        ref.invalidate(dashboardProvider);
        ref.invalidate(weeklyAnalyticsProvider);
      },
      child: SingleChildScrollView(
        physics: const AlwaysScrollableScrollPhysics(),
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Summary Stat Cards
            Row(
              children: [
                Expanded(
                  child: StatCard(
                    title: 'Weekly Study Time',
                    value: '${dash.weeklyTotalStudyHours.toStringAsFixed(1)}h',
                    subtitle: 'This week',
                    icon: Icons.hourglass_bottom_rounded,
                    iconColor: AppColors.primary,
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: StatCard(
                    title: 'Active Streak',
                    value: '${dash.studyStreakDays} Days',
                    subtitle: 'Consistency',
                    icon: Icons.local_fire_department_rounded,
                    iconColor: AppColors.warning,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                Expanded(
                  child: StatCard(
                    title: 'Syllabus Done',
                    value: '${dash.overallCompletionPercentage.toStringAsFixed(0)}%',
                    subtitle: '${dash.completedTopics}/${dash.totalTopics} topics',
                    icon: Icons.check_circle_outline_rounded,
                    iconColor: AppColors.success,
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: StatCard(
                    title: 'Subjects',
                    value: '${dash.totalSubjects}',
                    subtitle: '${dash.upcomingExamsCount} exams ahead',
                    icon: Icons.menu_book_rounded,
                    iconColor: AppColors.secondary,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 20),

            // Weekly Bar Chart (Real Data)
            const Text(
              'Daily Study Hours (This Week)',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
            ),
            const SizedBox(height: 8),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: SizedBox(
                  height: 200,
                  child: weekly != null && weekly.dailyBreakdown.isNotEmpty
                      ? _buildRealBarChart(weekly)
                      : _buildEmptyChart(),
                ),
              ),
            ),
            const SizedBox(height: 8),

            // Legend
            if (weekly != null && weekly.dailyBreakdown.isNotEmpty)
              Row(
                children: [
                  Container(width: 12, height: 12, decoration: BoxDecoration(
                    color: AppColors.primary, borderRadius: BorderRadius.circular(2),
                  )),
                  const SizedBox(width: 6),
                  const Text('Actual', style: TextStyle(fontSize: 12, color: AppColors.textSecondary)),
                  const SizedBox(width: 16),
                  Container(width: 12, height: 12, decoration: BoxDecoration(
                    color: AppColors.secondary.withOpacity(0.5), borderRadius: BorderRadius.circular(2),
                  )),
                  const SizedBox(width: 6),
                  const Text('Planned', style: TextStyle(fontSize: 12, color: AppColors.textSecondary)),
                ],
              ),
            const SizedBox(height: 20),

            // Completion Breakdown
            const Text(
              'Completion Breakdown',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
            ),
            const SizedBox(height: 8),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  children: [
                    _buildProgressRow('Completed Topics', dash.completedTopics, dash.totalTopics, AppColors.success),
                    const SizedBox(height: 12),
                    _buildProgressRow('Pending Topics', dash.pendingTopics, dash.totalTopics, AppColors.warning),
                    const SizedBox(height: 12),
                    _buildProgressRow(
                      'Today: Completed Hours',
                      dash.completedStudyHoursToday.toInt(),
                      (dash.plannedStudyHoursToday > 0 ? dash.plannedStudyHoursToday : 1).toInt(),
                      AppColors.primary,
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 20),

            // AI Recommendations Panel
            if (dash.aiRecommendations.isNotEmpty) ...[
              const Text(
                'AI Recommendations 🤖',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
              ),
              const SizedBox(height: 8),
              ...dash.aiRecommendations.map((rec) => Card(
                color: AppColors.primary.withOpacity(0.06),
                margin: const EdgeInsets.only(bottom: 8),
                child: Padding(
                  padding: const EdgeInsets.all(14),
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Icon(Icons.lightbulb_outline_rounded, color: AppColors.primary, size: 20),
                      const SizedBox(width: 10),
                      Expanded(
                        child: Text(rec, style: const TextStyle(fontSize: 13, height: 1.4)),
                      ),
                    ],
                  ),
                ),
              )),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildRealBarChart(WeeklyAnalyticsModel weekly) {
    final days = weekly.dailyBreakdown;
    final maxY = days.fold(0.0, (m, d) => m > d.plannedHours ? m : (m > d.actualHours ? m : d.plannedHours)) + 1;

    return BarChart(
      BarChartData(
        alignment: BarChartAlignment.spaceAround,
        maxY: maxY < 2 ? 6 : maxY,
        barTouchData: BarTouchData(
          enabled: true,
          touchTooltipData: BarTouchTooltipData(
            getTooltipItem: (group, _, rod, rodIndex) {
              if (group.x < days.length) {
                final d = days[group.x];
                final label = rodIndex == 0 ? 'Actual: ${d.actualHours}h' : 'Planned: ${d.plannedHours}h';
                return BarTooltipItem(label, const TextStyle(fontSize: 12, color: Colors.white));
              }
              return null;
            },
          ),
        ),
        titlesData: FlTitlesData(
          leftTitles: const AxisTitles(
            sideTitles: SideTitles(showTitles: true, reservedSize: 28),
          ),
          bottomTitles: AxisTitles(
            sideTitles: SideTitles(
              showTitles: true,
              getTitlesWidget: (val, meta) {
                final idx = val.toInt();
                if (idx >= 0 && idx < days.length) {
                  return Padding(
                    padding: const EdgeInsets.only(top: 4),
                    child: Text(days[idx].dayName.substring(0, 3), style: const TextStyle(fontSize: 11)),
                  );
                }
                return const Text('');
              },
            ),
          ),
          topTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
          rightTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
        ),
        gridData: const FlGridData(show: true, drawVerticalLine: false),
        borderData: FlBorderData(show: false),
        barGroups: List.generate(days.length, (i) {
          return BarChartGroupData(
            x: i,
            barRods: [
              BarChartRodData(
                toY: days[i].actualHours,
                color: AppColors.primary,
                width: 10,
                borderRadius: BorderRadius.circular(4),
              ),
              BarChartRodData(
                toY: days[i].plannedHours,
                color: AppColors.secondary.withOpacity(0.5),
                width: 10,
                borderRadius: BorderRadius.circular(4),
              ),
            ],
          );
        }),
      ),
    );
  }

  Widget _buildEmptyChart() {
    return const Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.bar_chart_rounded, size: 48, color: AppColors.textMuted),
          SizedBox(height: 8),
          Text('No study data yet this week',
              style: TextStyle(color: AppColors.textSecondary)),
        ],
      ),
    );
  }

  Widget _buildProgressRow(String label, int value, int total, Color color) {
    final ratio = total > 0 ? (value / total).clamp(0.0, 1.0) : 0.0;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(label, style: const TextStyle(fontSize: 13, color: AppColors.textSecondary)),
            Text('$value / $total', style: TextStyle(fontSize: 13, fontWeight: FontWeight.bold, color: color)),
          ],
        ),
        const SizedBox(height: 6),
        ClipRRect(
          borderRadius: BorderRadius.circular(8),
          child: LinearProgressIndicator(
            value: ratio,
            minHeight: 8,
            backgroundColor: color.withOpacity(0.12),
            valueColor: AlwaysStoppedAnimation<Color>(color),
          ),
        ),
      ],
    );
  }
}
