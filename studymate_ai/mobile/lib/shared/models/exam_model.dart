class ExamModel {
  final String id;
  final String userId;
  final String subjectId;
  final String examName;
  final DateTime examDate;
  final String examType;
  final double targetScore;
  final String? notes;
  final String subjectName;
  final String subjectCode;
  final String subjectColor;
  final int daysRemaining;
  final int totalTopics;
  final int completedTopics;
  final double completionPercentage;
  final String status;
  final String? venue;
  final int? durationMinutes;

  ExamModel({
    required this.id,
    required this.userId,
    required this.subjectId,
    required this.examName,
    required this.examDate,
    this.examType = 'semester',
    this.targetScore = 90.0,
    this.notes = '',
    this.subjectName = 'Subject',
    this.subjectCode = '',
    this.subjectColor = '#7556D9',
    this.daysRemaining = 0,
    this.totalTopics = 0,
    this.completedTopics = 0,
    this.completionPercentage = 0.0,
    this.status = 'upcoming',
    this.venue,
    this.durationMinutes,
  });

  factory ExamModel.fromJson(Map<String, dynamic> json) {
    // Support both 'examId' (backend legacy) and 'id' keys
    final rawId = (json['id'] ?? json['examId'] ?? '').toString();

    // Parse examDate — could be ISO string or already a date string
    DateTime parsedDate = DateTime.now().add(const Duration(days: 14));
    final rawDate = json['examDate'] ?? json['exam_date'] ?? '';
    if (rawDate is String && rawDate.isNotEmpty) {
      try {
        parsedDate = DateTime.parse(rawDate);
      } catch (_) {}
    }

    return ExamModel(
      id: rawId,
      userId: json['userId'] ?? '',
      subjectId: json['subjectId'] ?? '',
      examName: json['examName'] ?? json['subjectName'] ?? '',
      examDate: parsedDate,
      examType: json['examType'] ?? 'semester',
      targetScore: (json['targetScore'] as num?)?.toDouble() ?? 90.0,
      notes: json['notes'],
      subjectName: json['subjectName'] ?? json['examName'] ?? 'Subject',
      subjectCode: json['subjectCode'] ?? '',
      subjectColor: json['subjectColor'] ?? '#7556D9',
      daysRemaining: json['daysRemaining'] ?? 0,
      totalTopics: json['totalTopics'] ?? 0,
      completedTopics: json['completedTopics'] ?? 0,
      completionPercentage: (json['completionPercentage'] as num?)?.toDouble() ?? 0.0,
      status: json['status'] ?? 'upcoming',
      venue: json['venue'],
      durationMinutes: json['durationMinutes'] as int?,
    );
  }

  Map<String, dynamic> toJson() => {
    'id': id,
    'userId': userId,
    'subjectId': subjectId,
    'examName': examName,
    'examDate': examDate.toIso8601String(),
    'examType': examType,
    'targetScore': targetScore,
    'notes': notes,
    'status': status,
    'venue': venue,
    'durationMinutes': durationMinutes,
  };
}
