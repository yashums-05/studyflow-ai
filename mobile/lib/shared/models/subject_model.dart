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
    this.estimatedMinutes = 45,
    this.importance = 3,
    this.status = 'not_started',
    this.understandingRating,
    this.priorityScore = 0.0,
    this.notes = '',
  });

  factory TopicModel.fromJson(Map<String, dynamic> json) {
    final tId = json['topicId']?.toString() ?? json['id']?.toString() ?? '';
    final tName = json['topicName']?.toString() ?? json['name']?.toString() ?? 'Topic';
    final diff = (json['difficulty'] as num?)?.toInt() ?? (json['difficultyLevel'] as num?)?.toInt() ?? 3;
    final mins = (json['estimatedMinutes'] as num?)?.toInt() ?? (json['estimatedDurationMinutes'] as num?)?.toInt() ?? 45;
    final imp = (json['importance'] as num?)?.toInt() ?? (json['importanceScore'] as num?)?.toInt() ?? 3;
    final stat = json['status']?.toString() ?? json['completionStatus']?.toString() ?? 'not_started';

    return TopicModel(
      topicId: tId,
      userId: json['userId']?.toString() ?? '',
      subjectId: json['subjectId']?.toString() ?? '',
      unitId: json['unitId']?.toString() ?? '',
      topicName: tName,
      difficulty: diff,
      estimatedMinutes: mins,
      importance: imp,
      status: stat,
      understandingRating: (json['understandingRating'] as num?)?.toInt(),
      priorityScore: (json['priorityScore'] as num?)?.toDouble() ?? (diff * imp).toDouble(),
      notes: json['notes']?.toString(),
    );
  }

  Map<String, dynamic> toJson() => {
    'topicId': topicId,
    'id': topicId,
    'userId': userId,
    'subjectId': subjectId,
    'unitId': unitId,
    'topicName': topicName,
    'name': topicName,
    'difficulty': difficulty,
    'difficultyLevel': difficulty,
    'estimatedMinutes': estimatedMinutes,
    'estimatedDurationMinutes': estimatedMinutes,
    'importance': importance,
    'importanceScore': importance,
    'status': status,
    'completionStatus': status,
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
    final uId = json['unitId']?.toString() ?? json['id']?.toString() ?? '';
    final uName = json['unitName']?.toString() ?? json['title']?.toString() ?? 'Unit';
    final ord = (json['order'] as num?)?.toInt() ?? (json['orderIndex'] as num?)?.toInt() ?? (json['unitNumber'] as num?)?.toInt() ?? 1;

    return UnitModel(
      unitId: uId,
      subjectId: json['subjectId']?.toString() ?? '',
      userId: json['userId']?.toString() ?? '',
      unitName: uName,
      order: ord,
      topics: topicsList,
    );
  }

  Map<String, dynamic> toJson() => {
    'unitId': unitId,
    'id': unitId,
    'subjectId': subjectId,
    'userId': userId,
    'unitName': unitName,
    'title': unitName,
    'order': order,
    'orderIndex': order,
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

    int totalTop = (json['totalTopics'] as num?)?.toInt() ?? 0;
    int compTop = (json['completedTopics'] as num?)?.toInt() ?? 0;

    if (totalTop == 0 && unitsList.isNotEmpty) {
      for (var u in unitsList) {
        totalTop += u.topics.length;
        for (var t in u.topics) {
          if (t.status == 'completed') compTop++;
        }
      }
    }

    double compPct = (json['completionPercentage'] as num?)?.toDouble() ?? 0.0;
    if (compPct == 0.0 && totalTop > 0) {
      compPct = (compTop * 100.0) / totalTop;
    }

    final sId = json['subjectId']?.toString() ?? json['id']?.toString() ?? '';
    final sName = json['subjectName']?.toString() ?? json['name']?.toString() ?? 'Subject';
    final sCode = json['subjectCode']?.toString() ?? json['code']?.toString() ?? 'SUB101';
    final diff = (json['difficulty'] as num?)?.toInt() ?? (json['difficultyLevel'] as num?)?.toInt() ?? 3;
    final exDate = json['examDate']?.toString() ?? json['examinationDate']?.toString();
    final estHrs = (json['estimatedHours'] as num?)?.toDouble() ?? (json['estimatedStudyHours'] as num?)?.toDouble() ?? 20.0;

    return SubjectModel(
      subjectId: sId,
      userId: json['userId']?.toString() ?? '',
      subjectName: sName,
      subjectCode: sCode,
      semester: (json['semester'] as num?)?.toInt() ?? 6,
      difficulty: diff,
      colorHex: json['colorHex']?.toString() ?? "#7556D9",
      examDate: exDate,
      estimatedHours: estHrs,
      totalUnits: (json['totalUnits'] as num?)?.toInt() ?? unitsList.length,
      totalTopics: totalTop,
      completedTopics: compTop,
      completionPercentage: compPct,
      units: unitsList,
    );
  }

  Map<String, dynamic> toJson() => {
    'subjectId': subjectId,
    'id': subjectId,
    'userId': userId,
    'subjectName': subjectName,
    'name': subjectName,
    'subjectCode': subjectCode,
    'code': subjectCode,
    'semester': semester,
    'difficulty': difficulty,
    'difficultyLevel': difficulty,
    'colorHex': colorHex,
    'examDate': examDate,
    'examinationDate': examDate,
    'estimatedHours': estimatedHours,
    'estimatedStudyHours': estimatedHours,
    'totalUnits': totalUnits,
    'totalTopics': totalTopics,
    'completedTopics': completedTopics,
    'completionPercentage': completionPercentage,
    'units': units.map((u) => u.toJson()).toList(),
  };
}
