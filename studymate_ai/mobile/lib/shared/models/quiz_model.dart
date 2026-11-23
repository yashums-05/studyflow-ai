class QuizQuestionModel {
  final String questionId;
  final String question;
  final List<String> options;
  final String correctAnswer;
  final String explanation;
  final String difficulty;

  QuizQuestionModel({
    required this.questionId,
    required this.question,
    required this.options,
    required this.correctAnswer,
    required this.explanation,
    required this.difficulty,
  });

  factory QuizQuestionModel.fromJson(Map<String, dynamic> json) {
    return QuizQuestionModel(
      questionId: json['questionId'] ?? '',
      question: json['question'] ?? '',
      options: List<String>.from(json['options'] ?? []),
      correctAnswer: json['correctAnswer'] ?? '',
      explanation: json['explanation'] ?? '',
      difficulty: json['difficulty'] ?? 'Medium',
    );
  }
}

class QuizResponseModel {
  final String subject;
  final String topic;
  final List<QuizQuestionModel> questions;

  QuizResponseModel({
    required this.subject,
    required this.topic,
    required this.questions,
  });

  factory QuizResponseModel.fromJson(Map<String, dynamic> json) {
    var rawList = json['questions'] as List? ?? [];
    return QuizResponseModel(
      subject: json['subject'] ?? '',
      topic: json['topic'] ?? '',
      questions: rawList.map((q) => QuizQuestionModel.fromJson(q)).toList(),
    );
  }
}
