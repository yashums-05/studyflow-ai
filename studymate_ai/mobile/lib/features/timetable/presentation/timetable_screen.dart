import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import '../../../core/widgets/status_badge.dart';

class TimetableScreen extends ConsumerStatefulWidget {
  const TimetableScreen({super.key});

  @override
  ConsumerState<TimetableScreen> createState() => _TimetableScreenState();
}

class _TimetableScreenState extends ConsumerState<TimetableScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  DateTime _selectedDate = DateTime.now();

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  void _showGenerateModal() {
    int days = 7;
    double maxHours = 5.0;
    bool revisions = true;

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(24))),
      builder: (ctx) => StatefulBuilder(
        builder: (context, setModalState) => Padding(
          padding: EdgeInsets.only(
            left: 20,
            right: 20,
            top: 20,
            bottom: MediaQuery.of(context).viewInsets.bottom + 20,
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                'Generate AI Study Timetable ⚡',
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
              ),
              const SizedBox(height: 6),
              const Text(
                'Deterministic priority scheduling with exam urgency, topic difficulty, and Pomodoro breaks.',
                style: TextStyle(fontSize: 13, color: AppColors.textSecondary),
              ),
              const SizedBox(height: 18),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text('Scheduling Horizon', style: TextStyle(fontWeight: FontWeight.w600)),
                  Text('$days Days', style: const TextStyle(fontWeight: FontWeight.bold, color: AppColors.primary)),
                ],
              ),
              Slider(
                value: days.toDouble(),
                min: 3,
                max: 14,
                divisions: 11,
                activeColor: AppColors.primary,
                onChanged: (v) => setModalState(() => days = v.toInt()),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text('Max Daily Study Time', style: TextStyle(fontWeight: FontWeight.w600)),
                  Text('${maxHours.toStringAsFixed(1)} hrs', style: const TextStyle(fontWeight: FontWeight.bold, color: AppColors.primary)),
                ],
              ),
              Slider(
                value: maxHours,
                min: 2,
                max: 8,
                divisions: 12,
                activeColor: AppColors.primary,
                onChanged: (v) => setModalState(() => maxHours = v),
              ),
              SwitchListTile(
                title: const Text('Include Revision Sessions', style: TextStyle(fontSize: 14, fontWeight: FontWeight.w600)),
                value: revisions,
                activeColor: AppColors.primary,
                contentPadding: EdgeInsets.zero,
                onChanged: (v) => setModalState(() => revisions = v),
              ),
              const SizedBox(height: 16),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton.icon(
                  onPressed: () async {
                    Navigator.pop(ctx);
                    final todayStr = DateFormat('yyyy-MM-dd').format(DateTime.now());
                    await ref.read(timetableProvider.notifier).generateTimetable({
                      'startDate': todayStr,
                      'daysToSchedule': days,
                      'dailyAvailableHours': maxHours,
                      'includeRevisions': revisions,
                    });
                    if (mounted) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('AI Timetable successfully generated!'), backgroundColor: AppColors.success),
                      );
                    }
                  },
                  icon: const Icon(Icons.auto_awesome),
                  label: const Text('Generate Timetable Now'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final timetableAsync = ref.watch(timetableProvider);

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Study Timetable', style: TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
        backgroundColor: Colors.transparent,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.healing_rounded, color: AppColors.warning),
            tooltip: 'Reschedule Missed Sessions',
            onPressed: () async {
              await ref.read(timetableProvider.notifier).rescheduleMissed();
              if (mounted) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Missed sessions recovered and rescheduled!'), backgroundColor: AppColors.success),
                );
              }
            },
          ),
          IconButton(
            icon: const Icon(Icons.auto_awesome_rounded, color: AppColors.primary),
            tooltip: 'Generate Timetable',
            onPressed: _showGenerateModal,
          ),
        ],
        bottom: TabBar(
          controller: _tabController,
          labelColor: AppColors.primary,
          unselectedLabelColor: AppColors.textSecondary,
          indicatorColor: AppColors.primary,
          tabs: const [
            Tab(text: "Today's Schedule"),
            Tab(text: "Weekly Overview"),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          // 1. Daily Schedule Tab
          timetableAsync.when(
            loading: () => const Center(child: CircularProgressIndicator()),
            error: (err, _) => Center(child: Text('Error loading schedule: $err')),
            data: (sessions) {
              if (sessions.isEmpty) {
                return Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(Icons.event_note_rounded, size: 64, color: AppColors.textMuted),
                      const SizedBox(height: 16),
                      const Text(
                        'No study sessions scheduled for today',
                        style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                      ),
                      const SizedBox(height: 8),
                      const Text('Generate an intelligent timetable based on your syllabus.'),
                      const SizedBox(height: 16),
                      ElevatedButton.icon(
                        onPressed: _showGenerateModal,
                        icon: const Icon(Icons.auto_awesome),
                        label: const Text('Generate Timetable'),
                      ),
                    ],
                  ),
                );
              }

              return ListView.builder(
                padding: const EdgeInsets.all(16),
                itemCount: sessions.length,
                itemBuilder: (ctx, idx) {
                  final s = sessions[idx];
                  return Card(
                    child: Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Row(
                                children: [
                                  const Icon(Icons.access_time_rounded, size: 16, color: AppColors.primary),
                                  const SizedBox(width: 6),
                                  Text(
                                    "${s.startTimeStr} - ${s.endTimeStr} (${s.estimatedDurationMinutes}m)",
                                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                                  ),
                                ],
                              ),
                              StatusBadge(status: s.priority),
                            ],
                          ),
                          const SizedBox(height: 8),
                          Text(
                            s.topicName,
                            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                          ),
                          const SizedBox(height: 4),
                          Text(
                            s.subjectName,
                            style: const TextStyle(fontSize: 13, color: AppColors.textSecondary),
                          ),
                          const SizedBox(height: 12),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              StatusBadge(status: s.status),
                              ElevatedButton.icon(
                                onPressed: () {
                                  ref.read(focusTimerProvider.notifier).setSession(
                                        sessionId: s.sessionId,
                                        topicName: s.topicName,
                                        focusMinutes: s.estimatedDurationMinutes,
                                      );
                                  context.push('/focus-timer');
                                },
                                icon: const Icon(Icons.play_arrow, size: 18),
                                label: const Text('Start Focus'),
                                style: ElevatedButton.styleFrom(
                                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  );
                },
              );
            },
          ),

          // 2. Weekly Overview Tab
          Consumer(
            builder: (context, ref, _) {
              return timetableAsync.when(
                loading: () => const Center(child: CircularProgressIndicator()),
                error: (err, _) => Center(child: Text('Error: $err')),
                data: (sessions) {
                  return ListView(
                    padding: const EdgeInsets.all(16),
                    children: [
                      Card(
                        color: AppColors.primary.withOpacity(0.08),
                        child: Padding(
                          padding: const EdgeInsets.all(16.0),
                          child: Row(
                            children: [
                              const Icon(Icons.info_outline, color: AppColors.primary),
                              const SizedBox(width: 12),
                              Expanded(
                                child: Text(
                                  'Showing scheduled sessions. Adaptive rescheduling automatically reallocates missed topics.',
                                  style: TextStyle(fontSize: 13, color: AppColors.primaryDark),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      const SizedBox(height: 12),
                      ...sessions.map((s) => Card(
                            child: ListTile(
                              leading: Container(
                                padding: const EdgeInsets.all(8),
                                decoration: BoxDecoration(
                                  color: AppColors.primary.withOpacity(0.12),
                                  borderRadius: BorderRadius.circular(8),
                                ),
                                child: const Icon(Icons.calendar_month, color: AppColors.primary),
                              ),
                              title: Text(s.topicName, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                              subtitle: Text('${s.subjectName} • ${s.date} at ${s.startTimeStr}'),
                              trailing: StatusBadge(status: s.status),
                            ),
                          )),
                    ],
                  );
                },
              );
            },
          ),
        ],
      ),
    );
  }
}
