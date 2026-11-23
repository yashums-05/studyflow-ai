import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import '../../../shared/models/quiz_model.dart';

class QuizScreen extends ConsumerStatefulWidget {
  const QuizScreen({super.key});

  @override
  ConsumerState<QuizScreen> createState() => _QuizScreenState();
}

class _QuizScreenState extends ConsumerState<QuizScreen> {
  bool _isLoading = true;
  QuizResponseModel? _quiz;
  int _currentIndex = 0;
  String? _selectedOption;
  bool _isAnswered = false;
  int _score = 0;
  bool _isFinished = false;

  @override
  void initState() {
    super.initState();
    _loadQuiz();
  }

  Future<void> _loadQuiz() async {
    setState(() => _isLoading = true);
    try {
      final repo = ref.read(apiRepositoryProvider);
      final q = await repo.aiGenerateQuiz("Operating Systems", "CPU Scheduling & Deadlocks");
      setState(() {
        _quiz = q;
        _isLoading = false;
      });
    } catch (_) {
      setState(() => _isLoading = false);
    }
  }

  void _submitAnswer(String option) {
    if (_isAnswered) return;
    final currentQ = _quiz!.questions[_currentIndex];
    final isCorrect = option == currentQ.correctAnswer;

    setState(() {
      _selectedOption = option;
      _isAnswered = true;
      if (isCorrect) _score++;
    });
  }

  void _nextQuestion() {
    if (_currentIndex + 1 < _quiz!.questions.length) {
      setState(() {
        _currentIndex++;
        _selectedOption = null;
        _isAnswered = false;
      });
    } else {
      setState(() => _isFinished = true);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('AI Practice Quiz', style: TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
        backgroundColor: Colors.transparent,
        elevation: 0,
      ),
      body: _isLoading
          ? const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(),
                  SizedBox(height: 16),
                  Text('Generating personalized MCQs with Gemini AI...'),
                ],
              ),
            )
          : _quiz == null || _quiz!.questions.isEmpty
              ? Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text('Could not generate quiz questions.'),
                      const SizedBox(height: 12),
                      ElevatedButton(onPressed: _loadQuiz, child: const Text('Retry')),
                    ],
                  ),
                )
              : _isFinished
                  ? Center(
                      child: Padding(
                        padding: const EdgeInsets.all(24.0),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Container(
                              padding: const EdgeInsets.all(24),
                              decoration: BoxDecoration(
                                color: AppColors.success.withOpacity(0.12),
                                shape: BoxShape.circle,
                              ),
                              child: const Icon(Icons.emoji_events_rounded, color: AppColors.success, size: 64),
                            ),
                            const SizedBox(height: 24),
                            const Text(
                              'Quiz Completed! 🎯',
                              style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
                            ),
                            const SizedBox(height: 8),
                            Text(
                              'You scored $_score out of ${_quiz!.questions.length}',
                              style: const TextStyle(fontSize: 18, color: AppColors.textSecondary),
                            ),
                            const SizedBox(height: 32),
                            ElevatedButton(
                              onPressed: () => context.pop(),
                              child: const Text('Return to Study Dashboard'),
                            ),
                          ],
                        ),
                      ),
                    )
                  : Padding(
                      padding: const EdgeInsets.all(20.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(
                                'Question ${_currentIndex + 1} of ${_quiz!.questions.length}',
                                style: const TextStyle(fontWeight: FontWeight.bold, color: AppColors.textSecondary),
                              ),
                              Text(
                                'Score: $_score',
                                style: const TextStyle(fontWeight: FontWeight.bold, color: AppColors.primary),
                              ),
                            ],
                          ),
                          const SizedBox(height: 12),
                          LinearProgressIndicator(
                            value: (_currentIndex + 1) / _quiz!.questions.length,
                            backgroundColor: Colors.grey.shade200,
                            valueColor: const AlwaysStoppedAnimation<Color>(AppColors.primary),
                            borderRadius: BorderRadius.circular(6),
                          ),
                          const SizedBox(height: 24),
                          Text(
                            _quiz!.questions[_currentIndex].question,
                            style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: AppColors.textPrimary, height: 1.4),
                          ),
                          const SizedBox(height: 24),
                          ..._quiz!.questions[_currentIndex].options.map((opt) {
                            Color btnColor = Colors.white;
                            Color border = Colors.grey.shade300;
                            Color textColor = AppColors.textPrimary;

                            if (_isAnswered) {
                              if (opt == _quiz!.questions[_currentIndex].correctAnswer) {
                                btnColor = AppColors.success.withOpacity(0.15);
                                border = AppColors.success;
                                textColor = AppColors.success;
                              } else if (opt == _selectedOption) {
                                btnColor = AppColors.error.withOpacity(0.15);
                                border = AppColors.error;
                                textColor = AppColors.error;
                              }
                            }

                            return Container(
                              margin: const EdgeInsets.only(bottom: 12),
                              width: double.infinity,
                              child: OutlinedButton(
                                onPressed: () => _submitAnswer(opt),
                                style: OutlinedButton.styleFrom(
                                  backgroundColor: btnColor,
                                  side: BorderSide(color: border, width: 1.5),
                                  padding: const EdgeInsets.all(16),
                                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                                  alignment: Alignment.centerLeft,
                                ),
                                child: Text(
                                  opt,
                                  style: TextStyle(fontSize: 14, fontWeight: FontWeight.w600, color: textColor),
                                ),
                              ),
                            );
                          }),
                          if (_isAnswered) ...[
                            const SizedBox(height: 12),
                            Container(
                              padding: const EdgeInsets.all(14),
                              decoration: BoxDecoration(
                                color: Colors.blue.shade50,
                                borderRadius: BorderRadius.circular(12),
                              ),
                              child: Text(
                                '💡 Explanation: ${_quiz!.questions[_currentIndex].explanation}',
                                style: TextStyle(fontSize: 13, color: Colors.blue.shade900),
                              ),
                            ),
                            const Spacer(),
                            SizedBox(
                              width: double.infinity,
                              child: ElevatedButton(
                                onPressed: _nextQuestion,
                                child: Text(_currentIndex + 1 < _quiz!.questions.length ? 'Next Question →' : 'Finish Quiz'),
                              ),
                            ),
                          ],
                        ],
                      ),
                    ),
    );
  }
}
