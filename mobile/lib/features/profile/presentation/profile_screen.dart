import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/constants/api_constants.dart';
import '../../../core/services/providers.dart';
import '../../../shared/models/user_model.dart';

class ProfileScreen extends ConsumerStatefulWidget {
  const ProfileScreen({super.key});

  @override
  ConsumerState<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends ConsumerState<ProfileScreen> {
  bool _studyReminders = true;
  bool _examAlerts = true;
  bool _streakAlerts = true;

  @override
  Widget build(BuildContext context) {
    final userAsync = ref.watch(authProvider);

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Academic Profile', style: TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
        backgroundColor: Colors.transparent,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.logout_rounded, color: AppColors.error),
            tooltip: 'Log Out',
            onPressed: () async {
              await ref.read(authProvider.notifier).logout();
              if (mounted) context.go('/welcome');
            },
          ),
        ],
      ),
      body: userAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (err, _) => Center(child: Text('Error: $err')),
        data: (user) {
          final u = user;
          final universityName = (u?.college != null && u!.college.trim().isNotEmpty)
              ? u.college.trim()
              : 'Engineering Institute';

          return SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: Column(
              children: [
                // Avatar Card
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: Row(
                      children: [
                        CircleAvatar(
                          radius: 36,
                          backgroundColor: AppColors.primary.withOpacity(0.15),
                          child: const Icon(Icons.person_rounded, size: 40, color: AppColors.primary),
                        ),
                        const SizedBox(width: 16),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                u?.fullName ?? 'StudyFlow Student',
                                style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                u?.email ?? 'student@univ.edu',
                                style: const TextStyle(fontSize: 13, color: AppColors.textSecondary),
                              ),
                              const SizedBox(height: 6),
                              Container(
                                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                                decoration: BoxDecoration(
                                  color: AppColors.primary.withOpacity(0.12),
                                  borderRadius: BorderRadius.circular(12),
                                ),
                                child: Text(
                                  '${u?.degree ?? "B.Tech"} • Sem ${u?.semester ?? 6}',
                                  style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: AppColors.primary),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 16),

                // Academic Information Card
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            const Text(
                              'Academic Details',
                              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                            ),
                            TextButton.icon(
                              style: TextButton.styleFrom(padding: EdgeInsets.zero, visualDensity: VisualDensity.compact),
                              icon: const Icon(Icons.edit_outlined, size: 15, color: AppColors.primary),
                              label: const Text('Edit', style: TextStyle(fontSize: 13, fontWeight: FontWeight.bold, color: AppColors.primary)),
                              onPressed: () => _showEditAcademicDialog(context, u),
                            ),
                          ],
                        ),
                        const Divider(height: 16),
                        _buildInfoRow('University', universityName),
                        _buildInfoRow('Department', (u?.department != null && u!.department.trim().isNotEmpty) ? u.department : 'Computer Science'),
                        _buildInfoRow('Academic Year', (u?.academicYear != null && u!.academicYear.trim().isNotEmpty) ? u.academicYear : '2026-2027'),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 16),

                // Study Preferences Card
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            const Text(
                              'Study Configuration',
                              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                            ),
                            TextButton.icon(
                              style: TextButton.styleFrom(padding: EdgeInsets.zero, visualDensity: VisualDensity.compact),
                              icon: const Icon(Icons.tune_rounded, size: 15, color: AppColors.primary),
                              label: const Text('Configure', style: TextStyle(fontSize: 13, fontWeight: FontWeight.bold, color: AppColors.primary)),
                              onPressed: () => _showEditPreferencesDialog(context, u),
                            ),
                          ],
                        ),
                        const Divider(height: 16),
                        _buildInfoRow('Daily Study Target', '${u?.preferences.maximumDailyStudyHours.toStringAsFixed(1) ?? "6.0"} Hours'),
                        _buildInfoRow('Pomodoro Focus Duration', '${u?.preferences.preferredSessionDuration ?? 45} Minutes'),
                        _buildInfoRow('Short Break Duration', '${u?.preferences.shortBreakDuration ?? 10} Minutes'),
                        _buildInfoRow('Active Study Window', '${u?.preferences.studyStartTime ?? "08:00"} - ${u?.preferences.studyEndTime ?? "22:00"}'),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 16),

                // Notification Settings Card
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          'Notification Preferences',
                          style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                        ),
                        const Divider(height: 16),
                        SwitchListTile(
                          contentPadding: EdgeInsets.zero,
                          title: const Text('Study Session Reminders', style: TextStyle(fontSize: 14)),
                          value: _studyReminders,
                          activeColor: AppColors.primary,
                          onChanged: (v) => setState(() => _studyReminders = v),
                        ),
                        SwitchListTile(
                          contentPadding: EdgeInsets.zero,
                          title: const Text('Upcoming Exam Countdown Alerts', style: TextStyle(fontSize: 14)),
                          value: _examAlerts,
                          activeColor: AppColors.primary,
                          onChanged: (v) => setState(() => _examAlerts = v),
                        ),
                        SwitchListTile(
                          contentPadding: EdgeInsets.zero,
                          title: const Text('Daily Study Streak Reminders', style: TextStyle(fontSize: 14)),
                          value: _streakAlerts,
                          activeColor: AppColors.primary,
                          onChanged: (v) => setState(() => _streakAlerts = v),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 16),

                // Developer / Backend Settings Card
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Row(
                          children: [
                            Icon(Icons.cloud_sync_rounded, size: 18, color: AppColors.primary),
                            SizedBox(width: 8),
                            Text(
                              'Backend Connection',
                              style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        const Text(
                          'Connected API Endpoint:',
                          style: TextStyle(fontSize: 12, color: AppColors.textSecondary),
                        ),
                        const SizedBox(height: 6),
                        FutureBuilder<SharedPreferences>(
                          future: SharedPreferences.getInstance(),
                          builder: (context, snap) {
                            final currentUrl = snap.data?.getString('api_base_url') ?? ApiConstants.defaultBaseUrl;
                            return Container(
                              padding: const EdgeInsets.all(10),
                              decoration: BoxDecoration(
                                color: AppColors.background,
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: Row(
                                children: [
                                  const Icon(Icons.link, size: 18, color: AppColors.primary),
                                  const SizedBox(width: 8),
                                  Expanded(
                                    child: Text(
                                      currentUrl,
                                      style: const TextStyle(fontSize: 12, fontFamily: 'monospace'),
                                      overflow: TextOverflow.ellipsis,
                                    ),
                                  ),
                                  IconButton(
                                    icon: const Icon(Icons.edit, size: 18),
                                    onPressed: () => _showServerUrlDialog(context, currentUrl),
                                  ),
                                ],
                              ),
                            );
                          },
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 24),

                // Logout Button
                SizedBox(
                  width: double.infinity,
                  child: OutlinedButton.icon(
                    onPressed: () async {
                      await ref.read(authProvider.notifier).logout();
                      if (mounted) context.go('/welcome');
                    },
                    icon: const Icon(Icons.logout, color: AppColors.error),
                    label: const Text('Log Out of StudyFlow', style: TextStyle(color: AppColors.error)),
                    style: OutlinedButton.styleFrom(
                      side: const BorderSide(color: AppColors.error),
                    ),
                  ),
                ),
                const SizedBox(height: 20),
              ],
            ),
          );
        },
      ),
    );
  }

  Widget _buildInfoRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label, style: const TextStyle(fontSize: 13, color: AppColors.textSecondary)),
          Flexible(
            child: Text(
              value,
              textAlign: TextAlign.end,
              style: const TextStyle(fontSize: 13, fontWeight: FontWeight.w600, color: AppColors.textPrimary),
            ),
          ),
        ],
      ),
    );
  }

  void _showEditAcademicDialog(BuildContext context, UserModel? u) {
    final collegeCtrl = TextEditingController(text: u?.college ?? 'Engineering Institute');
    final deptCtrl = TextEditingController(text: u?.department ?? 'Computer Science');
    String degree = u?.degree ?? 'B.Tech';
    int semester = u?.semester ?? 6;
    String academicYear = u?.academicYear ?? '2026-2027';

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (ctx, setDialogState) => AlertDialog(
          title: const Text('Edit Academic Details', style: TextStyle(fontSize: 17, fontWeight: FontWeight.bold)),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text('University / College', style: TextStyle(fontSize: 12, fontWeight: FontWeight.w600)),
                const SizedBox(height: 4),
                TextField(
                  controller: collegeCtrl,
                  decoration: const InputDecoration(border: OutlineInputBorder(), isDense: true),
                ),
                const SizedBox(height: 12),
                const Text('Department / Major', style: TextStyle(fontSize: 12, fontWeight: FontWeight.w600)),
                const SizedBox(height: 4),
                TextField(
                  controller: deptCtrl,
                  decoration: const InputDecoration(border: OutlineInputBorder(), isDense: true),
                ),
                const SizedBox(height: 12),
                Row(
                  children: [
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text('Degree', style: TextStyle(fontSize: 12, fontWeight: FontWeight.w600)),
                          const SizedBox(height: 4),
                          DropdownButtonFormField<String>(
                            value: ['B.Tech', 'B.E', 'B.Sc', 'BCA', 'M.Tech', 'MCA', 'Other'].contains(degree) ? degree : 'B.Tech',
                            decoration: const InputDecoration(border: OutlineInputBorder(), isDense: true),
                            items: ['B.Tech', 'B.E', 'B.Sc', 'BCA', 'M.Tech', 'MCA', 'Other']
                                .map((e) => DropdownMenuItem(value: e, child: Text(e, style: const TextStyle(fontSize: 13))))
                                .toList(),
                            onChanged: (v) => setDialogState(() => degree = v!),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text('Semester', style: TextStyle(fontSize: 12, fontWeight: FontWeight.w600)),
                          const SizedBox(height: 4),
                          DropdownButtonFormField<int>(
                            value: semester,
                            decoration: const InputDecoration(border: OutlineInputBorder(), isDense: true),
                            items: [1, 2, 3, 4, 5, 6, 7, 8]
                                .map((e) => DropdownMenuItem(value: e, child: Text('Sem $e', style: const TextStyle(fontSize: 13))))
                                .toList(),
                            onChanged: (v) => setDialogState(() => semester = v!),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(ctx),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () async {
                final col = collegeCtrl.text.trim().isNotEmpty ? collegeCtrl.text.trim() : 'Engineering Institute';
                final dept = deptCtrl.text.trim().isNotEmpty ? deptCtrl.text.trim() : 'Computer Science';
                
                final repo = ref.read(apiRepositoryProvider);
                await repo.updateUserProfile({
                  'college': col,
                  'collegeName': col,
                  'university': col,
                  'department': dept,
                  'degree': degree,
                  'semester': semester,
                  'academicYear': academicYear,
                });

                ref.invalidate(authProvider);
                if (ctx.mounted) Navigator.pop(ctx);
                if (mounted) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Academic details updated successfully!'), backgroundColor: AppColors.success),
                  );
                }
              },
              child: const Text('Save'),
            ),
          ],
        ),
      ),
    );
  }

  void _showEditPreferencesDialog(BuildContext context, UserModel? u) {
    double dailyHours = u?.preferences.maximumDailyStudyHours ?? 6.0;
    int sessionMins = u?.preferences.preferredSessionDuration ?? 45;
    int breakMins = u?.preferences.shortBreakDuration ?? 10;
    String startTime = u?.preferences.studyStartTime ?? "08:00";
    String endTime = u?.preferences.studyEndTime ?? "22:00";

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (ctx, setDialogState) => AlertDialog(
          title: const Text('Configure Study Schedule', style: TextStyle(fontSize: 17, fontWeight: FontWeight.bold)),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text('Daily Study Target:', style: TextStyle(fontSize: 13, fontWeight: FontWeight.w600)),
                    Text('${dailyHours.toStringAsFixed(1)} hrs', style: const TextStyle(fontWeight: FontWeight.bold, color: AppColors.primary)),
                  ],
                ),
                Slider(
                  value: dailyHours,
                  min: 1.0,
                  max: 10.0,
                  divisions: 18,
                  activeColor: AppColors.primary,
                  onChanged: (v) => setDialogState(() => dailyHours = v),
                ),
                const SizedBox(height: 12),
                const Text('Pomodoro Focus Duration', style: TextStyle(fontSize: 13, fontWeight: FontWeight.w600)),
                const SizedBox(height: 6),
                Row(
                  children: [25, 45, 60].map((mins) {
                    final sel = sessionMins == mins;
                    return Expanded(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 2.0),
                        child: ChoiceChip(
                          label: Center(child: Text('$mins m', style: TextStyle(fontSize: 12, color: sel ? Colors.white : AppColors.textPrimary))),
                          selected: sel,
                          selectedColor: AppColors.primary,
                          onSelected: (_) => setDialogState(() => sessionMins = mins),
                        ),
                      ),
                    );
                  }).toList(),
                ),
                const SizedBox(height: 12),
                const Text('Short Break Duration', style: TextStyle(fontSize: 13, fontWeight: FontWeight.w600)),
                const SizedBox(height: 6),
                Row(
                  children: [5, 10, 15].map((mins) {
                    final sel = breakMins == mins;
                    return Expanded(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 2.0),
                        child: ChoiceChip(
                          label: Center(child: Text('$mins m', style: TextStyle(fontSize: 12, color: sel ? Colors.white : AppColors.textPrimary))),
                          selected: sel,
                          selectedColor: AppColors.primary,
                          onSelected: (_) => setDialogState(() => breakMins = mins),
                        ),
                      ),
                    );
                  }).toList(),
                ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(ctx),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () async {
                final repo = ref.read(apiRepositoryProvider);
                await repo.updateUserProfile({
                  'preferences': {
                    'maximumDailyStudyHours': dailyHours,
                    'preferredSessionDuration': sessionMins,
                    'shortBreakDuration': breakMins,
                    'studyStartTime': startTime,
                    'studyEndTime': endTime,
                    'preferredLearningStyle': u?.preferences.preferredLearningStyle ?? 'Visual & Practice',
                  },
                  'availableDailyStudyHours': dailyHours,
                  'maximumDailyStudyHours': dailyHours,
                  'preferredSessionDurationMinutes': sessionMins,
                  'preferredBreakDurationMinutes': breakMins,
                  'studyStartTime': startTime,
                  'studyEndTime': endTime,
                });

                ref.invalidate(authProvider);
                if (ctx.mounted) Navigator.pop(ctx);
                if (mounted) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Study configuration saved!'), backgroundColor: AppColors.success),
                  );
                }
              },
              child: const Text('Save'),
            ),
          ],
        ),
      ),
    );
  }

  void _showServerUrlDialog(BuildContext context, String currentUrl) {
    final controller = TextEditingController(text: currentUrl);
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Update Backend Server URL', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Enter the URL of your local server or deployed cloud backend:',
              style: TextStyle(fontSize: 12, color: AppColors.textSecondary),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: controller,
              decoration: const InputDecoration(
                labelText: 'Server Base URL',
                hintText: 'https://studyflow-ai-app.onrender.com/api/v1',
                border: const OutlineInputBorder(),
                prefixIcon: Icon(Icons.dns_rounded),
              ),
              style: const TextStyle(fontSize: 13),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () async {
              final prefs = await SharedPreferences.getInstance();
              await prefs.remove('api_base_url');
              if (mounted) setState(() {});
              if (ctx.mounted) Navigator.pop(ctx);
            },
            child: const Text('Reset to Default'),
          ),
          ElevatedButton(
            onPressed: () async {
              final newUrl = controller.text.trim();
              if (newUrl.isNotEmpty) {
                final prefs = await SharedPreferences.getInstance();
                await prefs.setString('api_base_url', newUrl);
                if (mounted) setState(() {});
              }
              if (ctx.mounted) Navigator.pop(ctx);
            },
            child: const Text('Save'),
          ),
        ],
      ),
    );
  }
}
