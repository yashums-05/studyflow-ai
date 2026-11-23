import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:percent_indicator/percent_indicator.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';

class FocusTimerScreen extends ConsumerWidget {
  const FocusTimerScreen({super.key});

  String _formatTime(int seconds) {
    final m = seconds ~/ 60;
    final s = seconds % 60;
    return '${m.toString().padLeft(2, '0')}:${s.toString().padLeft(2, '0')}';
  }

  void _showCompletionDialog(BuildContext context, WidgetRef ref, FocusTimerState state) {
    int rating = 4;
    final notesCtrl = TextEditingController();

    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) => StatefulBuilder(
        builder: (context, setDlgState) => AlertDialog(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
          title: const Text('Session Complete! 🎉', style: TextStyle(fontWeight: FontWeight.bold)),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Topic: ${state.activeTopicName ?? "Study Topic"}',
                style: const TextStyle(fontWeight: FontWeight.w600, color: AppColors.primary),
              ),
              const SizedBox(height: 16),
              const Text('How well did you understand this topic?', style: TextStyle(fontWeight: FontWeight.w600)),
              const SizedBox(height: 8),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [1, 2, 3, 4, 5].map((star) {
                  return IconButton(
                    icon: Icon(
                      star <= rating ? Icons.star_rounded : Icons.star_outline_rounded,
                      color: Colors.amber,
                      size: 32,
                    ),
                    onPressed: () => setDlgState(() => rating = star),
                  );
                }).toList(),
              ),
              const SizedBox(height: 12),
              const Text('Session Notes (Optional)', style: TextStyle(fontWeight: FontWeight.w600)),
              const SizedBox(height: 6),
              TextField(
                controller: notesCtrl,
                maxLines: 2,
                decoration: const InputDecoration(hintText: 'Key insights, tricky formulas, etc.'),
              ),
            ],
          ),
          actions: [
            ElevatedButton(
              onPressed: () async {
                Navigator.pop(ctx);
                if (state.activeSessionId != null) {
                  final actualMins = (state.totalSeconds - state.remainingSeconds) ~/ 60;
                  final repo = ref.read(apiRepositoryProvider);
                  await repo.completeSession(
                    state.activeSessionId!,
                    actualMins > 0 ? actualMins : 25,
                    rating,
                    notesCtrl.text.trim(),
                  );
                  ref.invalidate(dashboardProvider);
                  ref.invalidate(timetableProvider);
                  ref.invalidate(subjectsProvider);
                }
                context.pop();
              },
              child: const Text('Save & Update Progress'),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final timerState = ref.watch(focusTimerProvider);
    final timerNotifier = ref.read(focusTimerProvider.notifier);

    final percent = (timerState.remainingSeconds / timerState.totalSeconds).clamp(0.0, 1.0);

    return Scaffold(
      backgroundColor: timerState.isBreak ? const Color(0xFFF0FDF4) : AppColors.background,
      appBar: AppBar(
        title: Text(
          timerState.isBreak ? 'Short Break ☕' : 'Focus Mode 🧠',
          style: const TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary),
        ),
        backgroundColor: Colors.transparent,
        elevation: 0,
      ),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 20),
          child: Column(
            children: [
              // Topic Header
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Row(
                    children: [
                      Container(
                        padding: const EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: AppColors.primary.withOpacity(0.12),
                          shape: BoxShape.circle,
                        ),
                        child: const Icon(Icons.bookmark_border_rounded, color: AppColors.primary),
                      ),
                      const SizedBox(width: 14),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              'Current Focus Topic',
                              style: TextStyle(fontSize: 12, color: AppColors.textSecondary),
                            ),
                            Text(
                              timerState.activeTopicName ?? 'General Study Session',
                              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: AppColors.textPrimary),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              const Spacer(),

              // Circular Countdown Timer
              CircularPercentIndicator(
                radius: 120.0,
                lineWidth: 16.0,
                animation: false,
                percent: percent,
                center: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      _formatTime(timerState.remainingSeconds),
                      style: TextStyle(
                        fontSize: 48,
                        fontWeight: FontWeight.bold,
                        color: timerState.isBreak ? AppColors.success : AppColors.textPrimary,
                        letterSpacing: -1,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      timerState.isBreak ? 'RELAX' : 'FOCUS',
                      style: TextStyle(
                        fontSize: 14,
                        letterSpacing: 2,
                        fontWeight: FontWeight.w600,
                        color: timerState.isBreak ? AppColors.success : AppColors.primary,
                      ),
                    ),
                  ],
                ),
                circularStrokeCap: CircularStrokeCap.round,
                progressColor: timerState.isBreak ? AppColors.success : AppColors.primary,
                backgroundColor: (timerState.isBreak ? AppColors.success : AppColors.primary).withOpacity(0.12),
              ),

              const SizedBox(height: 24),
              Text(
                'Completed Today: ${timerState.completedSessions} Pomodoros',
                style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500, color: AppColors.textSecondary),
              ),
              const Spacer(),

              // Timer Controls
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  IconButton.filledTonal(
                    iconSize: 28,
                    onPressed: timerNotifier.reset,
                    icon: const Icon(Icons.replay_rounded),
                  ),
                  const SizedBox(width: 24),
                  ElevatedButton(
                    onPressed: () {
                      if (timerState.isRunning) {
                        timerNotifier.pause();
                      } else {
                        timerNotifier.start();
                      }
                    },
                    style: ElevatedButton.styleFrom(
                      shape: const CircleBorder(),
                      padding: const EdgeInsets.all(24),
                      backgroundColor: timerState.isBreak ? AppColors.success : AppColors.primary,
                    ),
                    child: Icon(
                      timerState.isRunning ? Icons.pause_rounded : Icons.play_arrow_rounded,
                      size: 36,
                      color: Colors.white,
                    ),
                  ),
                  const SizedBox(width: 24),
                  IconButton.filledTonal(
                    iconSize: 28,
                    onPressed: () => _showCompletionDialog(context, ref, timerState),
                    icon: const Icon(Icons.check_rounded, color: AppColors.success),
                  ),
                ],
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }
}
