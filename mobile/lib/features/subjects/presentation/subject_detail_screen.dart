import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import '../../../core/widgets/status_badge.dart';

class SubjectDetailScreen extends ConsumerStatefulWidget {
  final String subjectId;

  const SubjectDetailScreen({super.key, required this.subjectId});

  @override
  ConsumerState<SubjectDetailScreen> createState() => _SubjectDetailScreenState();
}

class _SubjectDetailScreenState extends ConsumerState<SubjectDetailScreen> {
  Future<void> _showAddUnitDialog() async {
    final nameCtrl = TextEditingController();
    await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Add Unit / Module'),
        content: TextField(
          controller: nameCtrl,
          decoration: const InputDecoration(hintText: 'e.g. Unit 2: Memory Management'),
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
          ElevatedButton(
            onPressed: () async {
              if (nameCtrl.text.isNotEmpty) {
                await ref.read(apiRepositoryProvider).createUnit({
                  'subjectId': widget.subjectId,
                  'unitName': nameCtrl.text.trim(),
                  'order': 1,
                });
                if (mounted) {
                  Navigator.pop(ctx);
                  ref.read(subjectsProvider.notifier).refresh();
                }
              }
            },
            child: const Text('Add Unit'),
          ),
        ],
      ),
    );
  }

  Future<void> _showAddTopicDialog(String unitId) async {
    final nameCtrl = TextEditingController();
    int diff = 3;
    await showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (context, setDlgState) => AlertDialog(
          title: const Text('Add Topic'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              TextField(
                controller: nameCtrl,
                decoration: const InputDecoration(hintText: 'e.g. Paging & Segmentation'),
              ),
              const SizedBox(height: 12),
              const Text('Difficulty (1 to 5)', style: TextStyle(fontWeight: FontWeight.w600)),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [1, 2, 3, 4, 5].map((d) {
                  final sel = diff == d;
                  return ChoiceChip(
                    label: Text('$d'),
                    selected: sel,
                    onSelected: (_) => setDlgState(() => diff = d),
                  );
                }).toList(),
              ),
            ],
          ),
          actions: [
            TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
            ElevatedButton(
              onPressed: () async {
                if (nameCtrl.text.isNotEmpty) {
                  await ref.read(apiRepositoryProvider).createTopic({
                    'subjectId': widget.subjectId,
                    'unitId': unitId,
                    'topicName': nameCtrl.text.trim(),
                    'difficulty': diff,
                    'estimatedMinutes': 45,
                    'importance': 4,
                  });
                  if (mounted) {
                    Navigator.pop(ctx);
                    ref.read(subjectsProvider.notifier).refresh();
                  }
                }
              },
              child: const Text('Add Topic'),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final subjectsAsync = ref.watch(subjectsProvider);

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Subject Syllabus', style: TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
        backgroundColor: Colors.transparent,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.add_box_outlined, color: AppColors.primary),
            tooltip: 'Add Unit',
            onPressed: _showAddUnitDialog,
          ),
        ],
      ),
      body: subjectsAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (err, _) => Center(child: Text('Error: $err')),
        data: (subjects) {
          final subj = subjects.firstWhere(
            (s) => s.subjectId == widget.subjectId,
            orElse: () => subjects.first,
          );

          return SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Header Info Card
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(18.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              subj.subjectCode,
                              style: const TextStyle(color: AppColors.primary, fontWeight: FontWeight.bold, fontSize: 13),
                            ),
                            Text(
                              "${subj.completionPercentage.toStringAsFixed(0)}% Completed",
                              style: const TextStyle(color: AppColors.success, fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                        const SizedBox(height: 6),
                        Text(
                          subj.subjectName,
                          style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                        ),
                        const SizedBox(height: 12),
                        LinearProgressIndicator(
                          value: (subj.completionPercentage / 100).clamp(0.0, 1.0),
                          backgroundColor: Colors.grey.shade200,
                          valueColor: const AlwaysStoppedAnimation<Color>(AppColors.primary),
                          borderRadius: BorderRadius.circular(6),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 18),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      'Units & Topics',
                      style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                    ),
                    TextButton.icon(
                      onPressed: _showAddUnitDialog,
                      icon: const Icon(Icons.add, size: 18),
                      label: const Text('Add Unit'),
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                if (subj.units.isEmpty)
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(24.0),
                      child: Center(
                        child: Column(
                          children: [
                            const Icon(Icons.post_add_rounded, size: 48, color: AppColors.textMuted),
                            const SizedBox(height: 8),
                            const Text('No units added yet.'),
                            const SizedBox(height: 12),
                            ElevatedButton(
                              onPressed: _showAddUnitDialog,
                              child: const Text('Create First Unit'),
                            ),
                          ],
                        ),
                      ),
                    ),
                  )
                else
                  ...subj.units.map((unit) {
                    return Card(
                      margin: const EdgeInsets.only(bottom: 12),
                      child: ExpansionTile(
                        initiallyExpanded: true,
                        title: Text(
                          unit.unitName,
                          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                        ),
                        subtitle: Text('${unit.topics.length} topics'),
                        trailing: IconButton(
                          icon: const Icon(Icons.add_circle_outline, color: AppColors.primary),
                          tooltip: 'Add Topic',
                          onPressed: () => _showAddTopicDialog(unit.unitId),
                        ),
                        children: unit.topics.isEmpty
                            ? [
                                const Padding(
                                  padding: EdgeInsets.all(16.0),
                                  child: Text('No topics in this unit yet.'),
                                )
                              ]
                            : unit.topics.map((t) {
                                return ListTile(
                                  leading: Icon(
                                    t.status == 'completed'
                                        ? Icons.check_circle_rounded
                                        : Icons.radio_button_unchecked_rounded,
                                    color: t.status == 'completed' ? AppColors.success : AppColors.textMuted,
                                  ),
                                  title: Text(t.topicName, style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500)),
                                  subtitle: Text('Diff: ${t.difficulty}/5 • Est: ${t.estimatedMinutes}m • Priority: ${t.priorityScore.toStringAsFixed(1)}'),
                                  trailing: IconButton(
                                    icon: const Icon(Icons.psychology_rounded, color: AppColors.secondary),
                                    tooltip: 'Explain with AI Tutor',
                                    onPressed: () {
                                      context.push('/ai-tutor');
                                    },
                                  ),
                                );
                              }).toList(),
                      ),
                    );
                  }),
              ],
            ),
          );
        },
      ),
    );
  }
}
