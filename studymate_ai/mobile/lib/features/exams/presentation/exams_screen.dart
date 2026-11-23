import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:intl/intl.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import '../../../shared/models/exam_model.dart';

class ExamsScreen extends ConsumerStatefulWidget {
  const ExamsScreen({super.key});

  @override
  ConsumerState<ExamsScreen> createState() => _ExamsScreenState();
}

class _ExamsScreenState extends ConsumerState<ExamsScreen> {
  void _showAddExamDialog() {
    final subjectCtrl = TextEditingController();
    final codeCtrl = TextEditingController();
    final venueCtrl = TextEditingController();
    DateTime selectedDate = DateTime.now().add(const Duration(days: 14));
    TimeOfDay selectedTime = const TimeOfDay(hour: 10, minute: 0);
    int duration = 180;
    String examType = 'semester';

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (ctx) => StatefulBuilder(
        builder: (context, setModalState) => Padding(
          padding: EdgeInsets.only(
            left: 24, right: 24, top: 24,
            bottom: MediaQuery.of(context).viewInsets.bottom + 24,
          ),
          child: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text('Add Examination', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                    IconButton(onPressed: () => Navigator.pop(ctx), icon: const Icon(Icons.close)),
                  ],
                ),
                const SizedBox(height: 16),
                _dialogLabel('Subject Name *'),
                const SizedBox(height: 6),
                TextFormField(
                  controller: subjectCtrl,
                  decoration: const InputDecoration(
                    hintText: 'e.g. Operating Systems',
                    prefixIcon: Icon(Icons.menu_book_rounded),
                  ),
                ),
                const SizedBox(height: 14),
                _dialogLabel('Subject Code'),
                const SizedBox(height: 6),
                TextFormField(
                  controller: codeCtrl,
                  decoration: const InputDecoration(hintText: 'e.g. CS301'),
                ),
                const SizedBox(height: 14),
                _dialogLabel('Exam Type'),
                const SizedBox(height: 6),
                DropdownButtonFormField<String>(
                  value: examType,
                  items: const [
                    DropdownMenuItem(value: 'semester', child: Text('Semester Exam')),
                    DropdownMenuItem(value: 'midterm', child: Text('Midterm')),
                    DropdownMenuItem(value: 'unit_test', child: Text('Unit Test')),
                    DropdownMenuItem(value: 'competitive', child: Text('Competitive')),
                    DropdownMenuItem(value: 'other', child: Text('Other')),
                  ],
                  onChanged: (v) => setModalState(() => examType = v ?? examType),
                ),
                const SizedBox(height: 14),
                _dialogLabel('Exam Date *'),
                const SizedBox(height: 6),
                InkWell(
                  onTap: () async {
                    final d = await showDatePicker(
                      context: context,
                      initialDate: selectedDate,
                      firstDate: DateTime.now(),
                      lastDate: DateTime.now().add(const Duration(days: 365)),
                    );
                    if (d != null) setModalState(() => selectedDate = d);
                  },
                  child: Container(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.grey.shade300),
                      borderRadius: BorderRadius.circular(12),
                      color: Colors.white,
                    ),
                    child: Row(
                      children: [
                        const Icon(Icons.calendar_today_outlined, color: AppColors.primary),
                        const SizedBox(width: 10),
                        Text(DateFormat('EEEE, d MMMM yyyy').format(selectedDate),
                            style: const TextStyle(fontWeight: FontWeight.w500)),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 14),
                _dialogLabel('Exam Time'),
                const SizedBox(height: 6),
                InkWell(
                  onTap: () async {
                    final t = await showTimePicker(context: context, initialTime: selectedTime);
                    if (t != null) setModalState(() => selectedTime = t);
                  },
                  child: Container(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.grey.shade300),
                      borderRadius: BorderRadius.circular(12),
                      color: Colors.white,
                    ),
                    child: Row(
                      children: [
                        const Icon(Icons.access_time_rounded, color: AppColors.primary),
                        const SizedBox(width: 10),
                        Text(selectedTime.format(context),
                            style: const TextStyle(fontWeight: FontWeight.w500)),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 14),
                _dialogLabel('Venue / Hall'),
                const SizedBox(height: 6),
                TextFormField(
                  controller: venueCtrl,
                  decoration: const InputDecoration(hintText: 'e.g. Exam Hall B'),
                ),
                const SizedBox(height: 14),
                _dialogLabel('Duration (minutes)'),
                const SizedBox(height: 6),
                DropdownButtonFormField<int>(
                  value: duration,
                  items: [60, 90, 120, 150, 180, 210, 240]
                      .map((v) => DropdownMenuItem(value: v, child: Text('$v min')))
                      .toList(),
                  onChanged: (v) => setModalState(() => duration = v ?? 180),
                ),
                const SizedBox(height: 24),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: () async {
                      if (subjectCtrl.text.trim().isEmpty) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text('Subject name is required')),
                        );
                        return;
                      }
                      Navigator.pop(ctx);
                      final examDate = DateTime(
                        selectedDate.year, selectedDate.month, selectedDate.day,
                        selectedTime.hour, selectedTime.minute,
                      );
                      await ref.read(examsProvider.notifier).addExam({
                        'subjectName': subjectCtrl.text.trim(),
                        'subjectCode': codeCtrl.text.trim(),
                        'examType': examType,
                        'examDate': examDate.toIso8601String(),
                        'venue': venueCtrl.text.trim(),
                        'durationMinutes': duration,
                        'status': 'upcoming',
                      });
                      if (mounted) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                            content: Text('Exam added successfully!'),
                            backgroundColor: AppColors.success,
                          ),
                        );
                      }
                    },
                    child: const Text('Add Examination'),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _dialogLabel(String text) =>
      Text(text, style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 13));

  @override
  Widget build(BuildContext context) {
    final examsAsync = ref.watch(examsProvider);

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('My Examinations',
            style: TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
        backgroundColor: Colors.transparent,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.add_circle_outline_rounded, color: AppColors.primary),
            onPressed: _showAddExamDialog,
            tooltip: 'Add Exam',
          ),
        ],
      ),
      body: examsAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (err, _) => _buildError(err.toString()),
        data: (exams) {
          if (exams.isEmpty) return _buildEmpty();
          final upcoming = exams.where((e) => e.status == 'upcoming' || e.status == 'scheduled').toList()
            ..sort((a, b) => a.examDate.compareTo(b.examDate));
          final past = exams.where((e) => e.status == 'completed' || e.status == 'missed').toList()
            ..sort((a, b) => b.examDate.compareTo(a.examDate));
          return RefreshIndicator(
            onRefresh: () async => ref.invalidate(examsProvider),
            child: ListView(
              padding: const EdgeInsets.all(16),
              children: [
                if (upcoming.isNotEmpty) ...[
                  _sectionHeader('Upcoming Exams', Icons.event_available_rounded, AppColors.primary),
                  const SizedBox(height: 8),
                  ...upcoming.map((e) => _ExamCard(exam: e, onDelete: () async {
                    await ref.read(examsProvider.notifier).deleteExam(e.id);
                  })),
                  const SizedBox(height: 20),
                ],
                if (past.isNotEmpty) ...[
                  _sectionHeader('Past Exams', Icons.history_rounded, AppColors.textSecondary),
                  const SizedBox(height: 8),
                  ...past.map((e) => _ExamCard(exam: e, isPast: true, onDelete: () async {
                    await ref.read(examsProvider.notifier).deleteExam(e.id);
                  })),
                ],
              ],
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _showAddExamDialog,
        icon: const Icon(Icons.add),
        label: const Text('Add Exam'),
        backgroundColor: AppColors.primary,
        foregroundColor: Colors.white,
      ),
    );
  }

  Widget _sectionHeader(String title, IconData icon, Color color) {
    return Row(
      children: [
        Icon(icon, size: 20, color: color),
        const SizedBox(width: 8),
        Text(title, style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: color)),
      ],
    );
  }

  Widget _buildEmpty() {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(32),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.event_note_rounded, size: 80, color: AppColors.primary.withOpacity(0.3)),
            const SizedBox(height: 20),
            const Text('No Exams Yet', style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
            const SizedBox(height: 10),
            const Text(
              'Add your upcoming exams to track preparation and get a countdown.',
              textAlign: TextAlign.center,
              style: TextStyle(color: AppColors.textSecondary, height: 1.4),
            ),
            const SizedBox(height: 28),
            ElevatedButton.icon(
              onPressed: _showAddExamDialog,
              icon: const Icon(Icons.add),
              label: const Text('Add First Exam'),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildError(String error) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(Icons.error_outline, size: 64, color: AppColors.error),
          const SizedBox(height: 16),
          const Text('Failed to load exams'),
          const SizedBox(height: 8),
          ElevatedButton(
            onPressed: () => ref.invalidate(examsProvider),
            child: const Text('Retry'),
          ),
        ],
      ),
    );
  }
}

class _ExamCard extends StatelessWidget {
  final ExamModel exam;
  final bool isPast;
  final VoidCallback onDelete;

  const _ExamCard({required this.exam, this.isPast = false, required this.onDelete});

  int _daysUntil(DateTime date) => date.difference(DateTime.now()).inDays;

  Color _urgencyColor(int days) {
    if (days < 0) return AppColors.textMuted;
    if (days <= 3) return AppColors.error;
    if (days <= 7) return AppColors.warning;
    return AppColors.success;
  }

  String _countdownText(int days) {
    if (days < 0) return 'Completed ${-days}d ago';
    if (days == 0) return 'TODAY!';
    if (days == 1) return 'Tomorrow!';
    return '$days days left';
  }

  @override
  Widget build(BuildContext context) {
    final days = _daysUntil(exam.examDate);
    final urgency = _urgencyColor(days);
    final countdown = _countdownText(days);

    return Card(
      margin: const EdgeInsets.only(bottom: 10),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Container(
                  width: 48,
                  height: 48,
                  decoration: BoxDecoration(
                    color: (isPast ? AppColors.textMuted : urgency).withOpacity(0.12),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Center(
                    child: Text(
                      '${exam.examDate.day}',
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: isPast ? AppColors.textMuted : urgency,
                      ),
                    ),
                  ),
                ),
                const SizedBox(width: 14),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(exam.subjectName,
                          style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
                      const SizedBox(height: 2),
                      Text(
                        '${DateFormat('EEEE, d MMM yyyy').format(exam.examDate)} • ${exam.examType.toUpperCase()}',
                        style: const TextStyle(fontSize: 12, color: AppColors.textSecondary),
                      ),
                    ],
                  ),
                ),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                      decoration: BoxDecoration(
                        color: (isPast ? AppColors.textMuted : urgency).withOpacity(0.12),
                        borderRadius: BorderRadius.circular(20),
                      ),
                      child: Text(
                        countdown,
                        style: TextStyle(
                          fontSize: 11,
                          fontWeight: FontWeight.bold,
                          color: isPast ? AppColors.textMuted : urgency,
                        ),
                      ),
                    ),
                    IconButton(
                      icon: const Icon(Icons.delete_outline, size: 20, color: AppColors.textMuted),
                      onPressed: () {
                        showDialog(
                          context: context,
                          builder: (ctx) => AlertDialog(
                            title: const Text('Delete Exam?'),
                            content: Text('Remove "${exam.subjectName}" from your exam list?'),
                            actions: [
                              TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
                              ElevatedButton(
                                style: ElevatedButton.styleFrom(backgroundColor: AppColors.error),
                                onPressed: () { Navigator.pop(ctx); onDelete(); },
                                child: const Text('Delete', style: TextStyle(color: Colors.white)),
                              ),
                            ],
                          ),
                        );
                      },
                    ),
                  ],
                ),
              ],
            ),
            if (exam.venue != null && exam.venue!.isNotEmpty) ...[
              const Divider(height: 20),
              Row(
                children: [
                  const Icon(Icons.location_on_outlined, size: 16, color: AppColors.textSecondary),
                  const SizedBox(width: 6),
                  Text(exam.venue!, style: const TextStyle(fontSize: 13, color: AppColors.textSecondary)),
                  if (exam.durationMinutes != null) ...[
                    const SizedBox(width: 16),
                    const Icon(Icons.timer_outlined, size: 16, color: AppColors.textSecondary),
                    const SizedBox(width: 6),
                    Text('${exam.durationMinutes} min', style: const TextStyle(fontSize: 13, color: AppColors.textSecondary)),
                  ],
                ],
              ),
            ],
          ],
        ),
      ),
    );
  }
}
