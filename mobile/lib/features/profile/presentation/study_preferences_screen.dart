import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';

class StudyPreferencesScreen extends ConsumerStatefulWidget {
  const StudyPreferencesScreen({super.key});

  @override
  ConsumerState<StudyPreferencesScreen> createState() => _StudyPreferencesScreenState();
}

class _StudyPreferencesScreenState extends ConsumerState<StudyPreferencesScreen> {
  double _dailyHours = 5.0;
  int _sessionMinutes = 45;
  int _breakMinutes = 10;
  String _startTime = "08:00";
  String _endTime = "22:00";
  String _learningStyle = "Visual & Practice";
  bool _isLoading = false;

  Future<void> _saveAndFinish() async {
    setState(() => _isLoading = true);
    try {
      await ref.read(authProvider.notifier).updateProfile({
        'preferences': {
          'studyStartTime': _startTime,
          'studyEndTime': _endTime,
          'maximumDailyStudyHours': _dailyHours,
          'preferredSessionDuration': _sessionMinutes,
          'shortBreakDuration': _breakMinutes,
          'preferredLearningStyle': _learningStyle,
        },
        'availableDailyStudyHours': _dailyHours,
        'maximumDailyStudyHours': _dailyHours,
        'preferredSessionDurationMinutes': _sessionMinutes,
        'preferredBreakDurationMinutes': _breakMinutes,
        'studyStartTime': _startTime,
        'studyEndTime': _endTime,
      });
      if (mounted) {
        context.go('/home');
      }
    } catch (e) {
      if (mounted) {
        context.go('/home');
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Study Preferences (Step 2 of 2)', style: TextStyle(fontSize: 16)),
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
                'Personalize Your Schedule ⏰',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                  color: AppColors.textPrimary,
                ),
              ),
              const SizedBox(height: 6),
              const Text(
                'The scheduling algorithm uses these parameters to generate conflict-free study sessions.',
                style: TextStyle(color: AppColors.textSecondary, fontSize: 14),
              ),
              const SizedBox(height: 24),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text('Daily Available Study Time', style: TextStyle(fontWeight: FontWeight.w600)),
                          Text('${_dailyHours.toStringAsFixed(1)} hrs', style: const TextStyle(fontWeight: FontWeight.bold, color: AppColors.primary)),
                        ],
                      ),
                      Slider(
                        value: _dailyHours,
                        min: 1.0,
                        max: 10.0,
                        divisions: 18,
                        activeColor: AppColors.primary,
                        onChanged: (v) => setState(() => _dailyHours = v),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 12),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text('Preferred Session Length', style: TextStyle(fontWeight: FontWeight.w600)),
                      const SizedBox(height: 12),
                      Row(
                        children: [25, 45, 60].map((mins) {
                          final selected = _sessionMinutes == mins;
                          return Expanded(
                            child: Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 4.0),
                              child: ChoiceChip(
                                label: Center(child: Text('$mins mins')),
                                selected: selected,
                                selectedColor: AppColors.primary,
                                labelStyle: TextStyle(
                                  color: selected ? Colors.white : AppColors.textPrimary,
                                  fontWeight: FontWeight.w600,
                                ),
                                onSelected: (_) => setState(() => _sessionMinutes = mins),
                              ),
                            ),
                          );
                        }).toList(),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 12),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text('Break Length', style: TextStyle(fontWeight: FontWeight.w600)),
                      const SizedBox(height: 12),
                      Row(
                        children: [5, 10, 15].map((mins) {
                          final selected = _breakMinutes == mins;
                          return Expanded(
                            child: Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 4.0),
                              child: ChoiceChip(
                                label: Center(child: Text('$mins mins')),
                                selected: selected,
                                selectedColor: AppColors.primary,
                                labelStyle: TextStyle(
                                  color: selected ? Colors.white : AppColors.textPrimary,
                                  fontWeight: FontWeight.w600,
                                ),
                                onSelected: (_) => setState(() => _breakMinutes = mins),
                              ),
                            ),
                          );
                        }).toList(),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 28),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: _isLoading ? null : _saveAndFinish,
                  child: _isLoading
                      ? const SizedBox(height: 20, width: 20, child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2))
                      : const Text('Complete Setup & Open Dashboard ✨'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
