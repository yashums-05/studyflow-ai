class TopicModel {
  final String topicId;
  final String userId;
  final String subjectId;
  final String unitId;
  final String topicName;
  final int difficulty;
  final int estimatedMinutes;
  final int importance;
  final String status; // not_started, in_progress, completed, needs_revision
  final int? understandingRating;
  final double priorityScore;
  final String? notes;

  TopicModel({
    required this.topicId,
    required this.userId,
    required this.subjectId,
    required this.unitId,
    required this.topicName,
    this.difficulty = 3,
    this.estimatedMinutes = 60,
    this.importance = 3,
    this.status = 'not_started',
    this.understandingRating,
    this.priorityScore = 0.0,
    this.notes = '',
  });

  factory TopicModel.fromJson(Map<String, dynamic> json) {
    return TopicModel(
      topicId: json['topicId'] ?? '',
      userId: json['userId'] ?? '',
      subjectId: json['subjectId'] ?? '',
      unitId: json['unitId'] ?? '',
      topicName: json['topicName'] ?? '',
      difficulty: json['difficulty'] ?? 3,
      estimatedMinutes: json['estimatedMinutes'] ?? 60,
      importance: json['importance'] ?? 3,
      status: json['status'] ?? 'not_started',
      understandingRating: json['understandingRating'],
      priorityScore: (json['priorityScore'] as num?)?.toDouble() ?? 0.0,
      notes: json['notes'],
    );
  }

  Map<String, dynamic> toJson() => {
    'topicId': topicId,
    'userId': userId,
    'subjectId': subjectId,
    'unitId': unitId,
    'topicName': topicName,
    'difficulty': difficulty,
    'estimatedMinutes': estimatedMinutes,
    'importance': importance,
    'status': status,
    'understandingRating': understandingRating,
    'priorityScore': priorityScore,
    'notes': notes,
  };
}

class UnitModel {
  final String unitId;
  final String subjectId;
  final String userId;
  final String unitName;
  final int order;
  final List<TopicModel> topics;

  UnitModel({
    required this.unitId,
    required this.subjectId,
    required this.userId,
    required this.unitName,
    this.order = 1,
    this.topics = const [],
  });

  factory UnitModel.fromJson(Map<String, dynamic> json) {
    var rawTopics = json['topics'] as List? ?? [];
    List<TopicModel> topicsList = rawTopics.map((t) => TopicModel.fromJson(t)).toList();
    return UnitModel(
      unitId: json['unitId'] ?? '',
      subjectId: json['subjectId'] ?? '',
      userId: json['userId'] ?? '',
      unitName: json['unitName'] ?? '',
      order: json['order'] ?? 1,
      topics: topicsList,
    );
  }

  Map<String, dynamic> toJson() => {
    'unitId': unitId,
    'subjectId': subjectId,
    'userId': userId,
    'unitName': unitName,
    'order': order,
    'topics': topics.map((t) => t.toJson()).toList(),
  };
}

class SubjectModel {
  final String subjectId;
  final String userId;
  final String subjectName;
  final String subjectCode;
  final int semester;
  final int difficulty;
  final String colorHex;
  final String? examDate;
  final double estimatedHours;
  final int totalUnits;
  final int totalTopics;
  final int completedTopics;
  final double completionPercentage;
  final List<UnitModel> units;

  SubjectModel({
    required this.subjectId,
    required this.userId,
    required this.subjectName,
    required this.subjectCode,
    required this.semester,
    this.difficulty = 3,
    this.colorHex = "#7556D9",
    this.examDate,
    this.estimatedHours = 20.0,
    this.totalUnits = 0,
    this.totalTopics = 0,
    this.completedTopics = 0,
    this.completionPercentage = 0.0,
    this.units = const [],
  });

  factory SubjectModel.fromJson(Map<String, dynamic> json) {
    var rawUnits = json['units'] as List? ?? [];
    List<UnitModel> unitsList = rawUnits.map((u) => UnitModel.fromJson(u)).toList();
    return SubjectModel(
      subjectId: json['subjectId'] ?? '',
      userId: json['userId'] ?? '',
      subjectName: json['subjectName'] ?? '',
      subjectCode: json['subjectCode'] ?? '',
      semester: json['semester'] ?? 1,
      difficulty: json['difficulty'] ?? 3,
      colorHex: json['colorHex'] ?? "#7556D9",
      examDate: json['examDate'],
      estimatedHours: (json['estimatedHours'] as num?)?.toDouble() ?? 20.0,
      totalUnits: json['totalUnits'] ?? unitsList.length,
      totalTopics: json['totalTopics'] ?? 0,
      completedTopics: json['completedTopics'] ?? 0,
      completionPercentage: (json['completionPercentage'] as num?)?.toDouble() ?? 0.0,
      units: unitsList,
    );
  }

  Map<String, dynamic> toJson() => {
    'subjectId': subjectId,
    'userId': userId,
    'subjectName': subjectName,
    'subjectCode': subjectCode,
    'semester': semester,
    'difficulty': difficulty,
    'colorHex': colorHex,
    'examDate': examDate,
    'estimatedHours': estimatedHours,
    'totalUnits': totalUnits,
    'totalTopics': totalTopics,
    'completedTopics': completedTopics,
    'completionPercentage': completionPercentage,
    'units': units.map((u) => u.toJson()).toList(),
  };
}
