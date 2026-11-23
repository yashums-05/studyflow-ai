import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import '../../../shared/models/study_session_model.dart';
import '../../../shared/repositories/api_repository.dart';

final sessionHistoryProvider = FutureProvider<List<StudySessionModel>>((ref) async {
  final repo = ref.watch(apiRepositoryProvider);
  return await repo.getSessionHistory();
});

class StudySessionHistoryScreen extends ConsumerWidget {
  const StudySessionHistoryScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final historyAsync = ref.watch(sessionHistoryProvider);

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Session History',
            style: TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
        backgroundColor: Colors.transparent,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh_rounded),
            onPressed: () => ref.invalidate(sessionHistoryProvider),
          ),
        ],
      ),
      body: historyAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (err, _) => _buildError(context, ref, err.toString()),
        data: (sessions) {
          if (sessions.isEmpty) return _buildEmpty();

          // Group sessions by date
          final Map<String, List<StudySessionModel>> grouped = {};
          for (final s in sessions) {
            final key = DateFormat('yyyy-MM-dd').format(s.startTime);
            grouped.putIfAbsent(key, () => []).add(s);
          }

          final sortedKeys = grouped.keys.toList()..sort((a, b) => b.compareTo(a));

          return RefreshIndicator(
            onRefresh: () async => ref.invalidate(sessionHistoryProvider),
            child: ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: sortedKeys.length,
              itemBuilder: (ctx, i) {
                final dateKey = sortedKeys[i];
                final daySessions = grouped[dateKey]!;
                final date = DateTime.parse(dateKey);
                final totalMins = daySessions.fold(0, (sum, s) => sum + (s.actualDurationMinutes ?? s.estimatedDurationMinutes ?? 0));

                return Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Date Header
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 10),
                      child: Row(
                        children: [
                          Expanded(
                            child: Text(
                              _formatDateHeader(date),
                              style: const TextStyle(
                                fontSize: 14, fontWeight: FontWeight.bold, color: AppColors.textSecondary,
                              ),
                            ),
                          ),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                            decoration: BoxDecoration(
                              color: AppColors.primary.withOpacity(0.1),
                              borderRadius: BorderRadius.circular(20),
                            ),
                            child: Text(
                              '${(totalMins / 60).toStringAsFixed(1)}h total',
                              style: const TextStyle(fontSize: 12, color: AppColors.primary, fontWeight: FontWeight.w600),
                            ),
                          ),
                        ],
                      ),
                    ),
                    ...daySessions.map((s) => _SessionHistoryCard(session: s)),
                    const SizedBox(height: 4),
                  ],
                );
              },
            ),
          );
        },
      ),
    );
  }

  String _formatDateHeader(DateTime date) {
    final today = DateTime.now();
    final yesterday = today.subtract(const Duration(days: 1));
    if (_sameDay(date, today)) return 'Today — ${DateFormat('EEEE, d MMM').format(date)}';
    if (_sameDay(date, yesterday)) return 'Yesterday — ${DateFormat('EEEE, d MMM').format(date)}';
    return DateFormat('EEEE, d MMMM yyyy').format(date);
  }

  bool _sameDay(DateTime a, DateTime b) =>
      a.year == b.year && a.month == b.month && a.day == b.day;

  Widget _buildEmpty() {
    return const Center(
      child: Padding(
        padding: EdgeInsets.all(32),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.history_toggle_off_rounded, size: 80, color: AppColors.textMuted),
            SizedBox(height: 20),
            Text('No Study Sessions Yet',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            SizedBox(height: 10),
            Text(
              'Your completed and missed sessions will appear here.',
              textAlign: TextAlign.center,
              style: TextStyle(color: AppColors.textSecondary, height: 1.4),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildError(BuildContext context, WidgetRef ref, String error) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(Icons.error_outline, size: 64, color: AppColors.error),
          const SizedBox(height: 16),
          const Text('Could not load session history'),
          const SizedBox(height: 8),
          ElevatedButton(
            onPressed: () => ref.invalidate(sessionHistoryProvider),
            child: const Text('Retry'),
          ),
        ],
      ),
    );
  }
}

class _SessionHistoryCard extends StatelessWidget {
  final StudySessionModel session;

  const _SessionHistoryCard({required this.session});

  Color _statusColor() {
    switch (session.status.toLowerCase()) {
      case 'completed': return AppColors.success;
      case 'missed': return AppColors.error;
      case 'in_progress': return AppColors.warning;
      default: return AppColors.textSecondary;
    }
  }

  IconData _statusIcon() {
    switch (session.status.toLowerCase()) {
      case 'completed': return Icons.check_circle_rounded;
      case 'missed': return Icons.cancel_rounded;
      case 'in_progress': return Icons.play_circle_rounded;
      default: return Icons.circle_outlined;
    }
  }

  @override
  Widget build(BuildContext context) {
    final statusColor = _statusColor();
    final duration = session.actualDurationMinutes ?? session.estimatedDurationMinutes ?? 0;

    return Card(
      margin: const EdgeInsets.only(bottom: 8),
      child: Padding(
        padding: const EdgeInsets.all(14),
        child: Row(
          children: [
            Icon(_statusIcon(), color: statusColor, size: 28),
            const SizedBox(width: 14),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    session.topicName,
                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14, color: AppColors.textPrimary),
                  ),
                  const SizedBox(height: 2),
                  Text(
                    '${session.subjectName} • ${session.unitName ?? ""}',
                    style: const TextStyle(fontSize: 12, color: AppColors.textSecondary),
                  ),
                  const SizedBox(height: 4),
                  Row(
                    children: [
                      const Icon(Icons.access_time, size: 13, color: AppColors.textMuted),
                      const SizedBox(width: 4),
                      Text(
                        '${DateFormat('h:mm a').format(session.startTime)} — $duration min',
                        style: const TextStyle(fontSize: 12, color: AppColors.textMuted),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                  decoration: BoxDecoration(
                    color: statusColor.withOpacity(0.1),
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: Text(
                    session.status.replaceAll('_', ' ').toUpperCase(),
                    style: TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: statusColor),
                  ),
                ),
                if (session.understandingRating != null) ...[
                  const SizedBox(height: 6),
                  Row(
                    children: List.generate(5, (i) => Icon(
                      i < (session.understandingRating ?? 0) ? Icons.star_rounded : Icons.star_outline_rounded,
                      color: Colors.amber,
                      size: 14,
                    )),
                  ),
                ],
              ],
            ),
          ],
        ),
      ),
    );
  }
}
