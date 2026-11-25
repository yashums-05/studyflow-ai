import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import 'add_subject_dialog.dart';

class SubjectsListScreen extends ConsumerWidget {
  const SubjectsListScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final subjectsAsync = ref.watch(subjectsProvider);

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('My Subjects', style: TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
        backgroundColor: Colors.transparent,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.upload_file_rounded, color: AppColors.primary),
            tooltip: 'Upload Syllabus PDF',
            onPressed: () => context.push('/syllabus-upload'),
          ),
          IconButton(
            icon: const Icon(Icons.add_circle_outline_rounded, color: AppColors.primary),
            onPressed: () => showDialog(
              context: context,
              builder: (ctx) => const AddSubjectDialog(),
            ),
          ),
        ],
      ),
      body: subjectsAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (err, _) => Center(child: Text('Error loading subjects: $err')),
        data: (subjects) {
          if (subjects.isEmpty) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(Icons.menu_book_rounded, size: 64, color: AppColors.textMuted),
                  const SizedBox(height: 16),
                  const Text(
                    'No subjects added yet',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                  ),
                  const SizedBox(height: 8),
                  const Text('Add your courses manually or upload a syllabus PDF.'),
                  const SizedBox(height: 20),
                  ElevatedButton.icon(
                    onPressed: () => showDialog(
                      context: context,
                      builder: (ctx) => const AddSubjectDialog(),
                    ),
                    icon: const Icon(Icons.add),
                    label: const Text('Add Subject'),
                  ),
                ],
              ),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: subjects.length,
            itemBuilder: (context, idx) {
              final s = subjects[idx];
              Color cardColor;
              try {
                cardColor = Color(int.parse(s.colorHex.replaceFirst('#', '0xFF')));
              } catch (_) {
                cardColor = AppColors.primary;
              }

              return Card(
                child: InkWell(
                  onTap: () => context.push('/subjects/${s.subjectId}'),
                  borderRadius: BorderRadius.circular(16),
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                              decoration: BoxDecoration(
                                color: cardColor.withOpacity(0.12),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: Text(
                                s.subjectCode,
                                style: TextStyle(color: cardColor, fontWeight: FontWeight.bold, fontSize: 12),
                              ),
                            ),
                            PopupMenuButton<String>(
                              onSelected: (val) {
                                if (val == 'delete') {
                                  ref.read(subjectsProvider.notifier).deleteSubject(s.subjectId);
                                }
                              },
                              itemBuilder: (ctx) => [
                                const PopupMenuItem(value: 'delete', child: Text('Delete Subject', style: TextStyle(color: AppColors.error))),
                              ],
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        Text(
                          s.subjectName,
                          style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                        ),
                        const SizedBox(height: 12),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              '${s.completedTopics}/${s.totalTopics} Topics Done',
                              style: const TextStyle(fontSize: 13, color: AppColors.textSecondary),
                            ),
                            Text(
                              '${s.completionPercentage.toStringAsFixed(0)}%',
                              style: TextStyle(fontWeight: FontWeight.bold, color: cardColor),
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        LinearProgressIndicator(
                          value: (s.completionPercentage / 100).clamp(0.0, 1.0),
                          backgroundColor: Colors.grey.shade200,
                          valueColor: AlwaysStoppedAnimation<Color>(cardColor),
                          borderRadius: BorderRadius.circular(8),
                        ),
                      ],
                    ),
                  ),
                ),
              );
            },
          );
        },
      ),
    );
  }
}
