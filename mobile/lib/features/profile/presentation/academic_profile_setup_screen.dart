import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';

class AcademicProfileSetupScreen extends ConsumerStatefulWidget {
  const AcademicProfileSetupScreen({super.key});

  @override
  ConsumerState<AcademicProfileSetupScreen> createState() => _AcademicProfileSetupScreenState();
}

class _AcademicProfileSetupScreenState extends ConsumerState<AcademicProfileSetupScreen> {
  final _collegeCtrl = TextEditingController();
  final _departmentCtrl = TextEditingController();
  String _selectedDegree = 'B.Tech';
  int _selectedSemester = 6;
  String _academicYear = '2026-2027';
  bool _isSaving = false;

  @override
  void dispose() {
    _collegeCtrl.dispose();
    _departmentCtrl.dispose();
    super.dispose();
  }

  Future<void> _handleSaveAndNext() async {
    setState(() => _isSaving = true);
    try {
      final repo = ref.read(apiRepositoryProvider);
      await repo.updateUserProfile({
        'collegeName': _collegeCtrl.text.trim().isNotEmpty ? _collegeCtrl.text.trim() : 'Engineering Institute',
        'department': _departmentCtrl.text.trim().isNotEmpty ? _departmentCtrl.text.trim() : 'Computer Science',
        'degree': _selectedDegree,
        'semester': _selectedSemester,
        'academicYear': _academicYear,
      });
      ref.invalidate(authProvider);
    } catch (_) {}
    if (mounted) {
      setState(() => _isSaving = false);
      context.push('/onboarding/preferences');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Academic Profile (Step 1 of 2)', style: TextStyle(fontSize: 16)),
        backgroundColor: Colors.transparent,
        elevation: 0,
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 8),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                'Tell us about your studies 📚',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                  color: AppColors.textPrimary,
                ),
              ),
              const SizedBox(height: 6),
              const Text(
                'This helps StudyFlow calibrate topic depths and semester requirements.',
                style: TextStyle(color: AppColors.textSecondary, fontSize: 14),
              ),
              const SizedBox(height: 24),
              const Text('College / University Name', style: TextStyle(fontWeight: FontWeight.w600)),
              const SizedBox(height: 8),
              TextFormField(
                controller: _collegeCtrl,
                decoration: const InputDecoration(prefixIcon: Icon(Icons.apartment_rounded)),
              ),
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text('Degree', style: TextStyle(fontWeight: FontWeight.w600)),
                        const SizedBox(height: 8),
                        DropdownButtonFormField<String>(
                          value: _selectedDegree,
                          decoration: const InputDecoration(contentPadding: EdgeInsets.symmetric(horizontal: 12, vertical: 14)),
                          items: ['B.Tech', 'B.E', 'B.Sc', 'BCA', 'M.Tech', 'MCA', 'Other']
                              .map((e) => DropdownMenuItem(value: e, child: Text(e)))
                              .toList(),
                          onChanged: (v) => setState(() => _selectedDegree = v!),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text('Semester', style: TextStyle(fontWeight: FontWeight.w600)),
                        const SizedBox(height: 8),
                        DropdownButtonFormField<int>(
                          value: _selectedSemester,
                          decoration: const InputDecoration(contentPadding: EdgeInsets.symmetric(horizontal: 12, vertical: 14)),
                          items: [1, 2, 3, 4, 5, 6, 7, 8]
                              .map((e) => DropdownMenuItem(value: e, child: Text('Semester $e')))
                              .toList(),
                          onChanged: (v) => setState(() => _selectedSemester = v!),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              const Text('Department / Major', style: TextStyle(fontWeight: FontWeight.w600)),
              const SizedBox(height: 8),
              TextFormField(
                controller: _departmentCtrl,
                decoration: const InputDecoration(prefixIcon: Icon(Icons.school_outlined)),
              ),
              const SizedBox(height: 32),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: _isSaving ? null : _handleSaveAndNext,
                  child: _isSaving
                      ? const SizedBox(height: 20, width: 20, child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2))
                      : const Text('Next: Study Preferences →'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
