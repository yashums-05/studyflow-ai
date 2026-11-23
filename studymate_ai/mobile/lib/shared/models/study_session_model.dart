class StudySessionModel {
  final String sessionId;
  final String userId;
  final String subjectId;
  final String topicId;
  final String date;
  final String startTimeStr;   // "HH:MM" string
  final String endTimeStr;     // "HH:MM" string
  final int estimatedDurationMinutes;
  final int? actualDurationMinutes;
  final String sessionType;   // learning, revision, practice, mock_test
  final String priority;      // critical, high, medium, low
  final String status;        // scheduled, in_progress, completed, missed, rescheduled
  final String? notes;
  final String subjectName;
  final String topicName;
  final String? unitName;
  final String subjectColor;
  final int? understandingRating;

  StudySessionModel({
    required this.sessionId,
    required this.userId,
    required this.subjectId,
    required this.topicId,
    required this.date,
    required this.startTimeStr,
    required this.endTimeStr,
    required this.estimatedDurationMinutes,
    this.actualDurationMinutes,
    this.sessionType = 'learning',
    this.priority = 'medium',
    this.status = 'scheduled',
    this.notes = '',
    this.subjectName = 'Subject',
    this.topicName = 'Topic',
    this.unitName,
    this.subjectColor = '#7556D9',
    this.understandingRating,
  });

  /// Parses date + time string into a DateTime. Falls back gracefully.
  DateTime get startTime {
    try {
      // date = "2024-11-23", startTimeStr = "09:00"
      return DateTime.parse('$date $startTimeStr:00');
    } catch (_) {
      return DateTime.now();
    }
  }

  DateTime get endTime {
    try {
      return DateTime.parse('$date $endTimeStr:00');
    } catch (_) {
      return startTime.add(Duration(minutes: estimatedDurationMinutes));
    }
  }

  factory StudySessionModel.fromJson(Map<String, dynamic> json) {
    return StudySessionModel(
      sessionId: (json['sessionId'] ?? json['id'] ?? '').toString(),
      userId: json['userId'] ?? '',
      subjectId: json['subjectId'] ?? '',
      topicId: json['topicId'] ?? '',
      date: json['date'] ?? '',
      startTimeStr: json['startTime'] ?? '09:00',
      endTimeStr: json['endTime'] ?? '10:00',
      estimatedDurationMinutes: json['estimatedMinutes'] ?? json['estimatedDurationMinutes'] ?? 45,
      actualDurationMinutes: json['actualMinutes'] as int? ?? json['actualDurationMinutes'] as int?,
      sessionType: json['sessionType'] ?? 'learning',
      priority: json['priority'] ?? 'medium',
      status: json['status'] ?? 'scheduled',
      notes: json['notes'],
      subjectName: json['subjectName'] ?? 'Subject',
      topicName: json['topicName'] ?? 'Topic',
      unitName: json['unitName'],
      subjectColor: json['subjectColor'] ?? '#7556D9',
      understandingRating: json['understandingRating'] as int?,
    );
  }

  Map<String, dynamic> toJson() => {
    'sessionId': sessionId,
    'userId': userId,
    'subjectId': subjectId,
    'topicId': topicId,
    'date': date,
    'startTime': startTimeStr,
    'endTime': endTimeStr,
    'estimatedDurationMinutes': estimatedDurationMinutes,
    'actualDurationMinutes': actualDurationMinutes,
    'sessionType': sessionType,
    'priority': priority,
    'status': status,
    'notes': notes,
    'subjectName': subjectName,
    'topicName': topicName,
    'subjectColor': subjectColor,
    'understandingRating': understandingRating,
  };
}
