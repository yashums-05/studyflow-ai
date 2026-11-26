class StudyPreferences {
  final List<String> availableDays;
  final String studyStartTime;
  final String studyEndTime;
  final int preferredSessionDuration;
  final int shortBreakDuration;
  final int longBreakDuration;
  final double maximumDailyStudyHours;
  final String preferredLearningStyle;

  StudyPreferences({
    this.availableDays = const ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
    this.studyStartTime = "08:00",
    this.studyEndTime = "22:00",
    this.preferredSessionDuration = 45,
    this.shortBreakDuration = 10,
    this.longBreakDuration = 30,
    this.maximumDailyStudyHours = 6.0,
    this.preferredLearningStyle = "Visual & Practice",
  });

  factory StudyPreferences.fromJson(Map<String, dynamic> json) {
    List<String> days = [];
    if (json['availableDays'] is List) {
      days = List<String>.from(json['availableDays']);
    } else if (json['studyDays'] is List) {
      days = List<String>.from(json['studyDays']);
    } else {
      days = const ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
    }

    return StudyPreferences(
      availableDays: days,
      studyStartTime: json['studyStartTime'] ?? json['preferredStudyStartTime'] ?? "08:00",
      studyEndTime: json['studyEndTime'] ?? json['preferredStudyEndTime'] ?? "22:00",
      preferredSessionDuration: json['preferredSessionDuration'] ?? json['preferredSessionDurationMinutes'] ?? 45,
      shortBreakDuration: json['shortBreakDuration'] ?? json['preferredBreakDurationMinutes'] ?? 10,
      longBreakDuration: json['longBreakDuration'] ?? 30,
      maximumDailyStudyHours: (json['maximumDailyStudyHours'] as num?)?.toDouble() ??
          (json['availableDailyStudyHours'] as num?)?.toDouble() ??
          6.0,
      preferredLearningStyle: json['preferredLearningStyle'] ?? json['learningStyle'] ?? "Visual & Practice",
    );
  }

  Map<String, dynamic> toJson() => {
    'availableDays': availableDays,
    'studyStartTime': studyStartTime,
    'studyEndTime': studyEndTime,
    'preferredSessionDuration': preferredSessionDuration,
    'shortBreakDuration': shortBreakDuration,
    'longBreakDuration': longBreakDuration,
    'maximumDailyStudyHours': maximumDailyStudyHours,
    'preferredLearningStyle': preferredLearningStyle,
  };
}

class UserModel {
  final String uid;
  final String fullName;
  final String email;
  final String? photoUrl;
  final String college;
  final String degree;
  final String department;
  final int semester;
  final String academicYear;
  final StudyPreferences preferences;

  UserModel({
    required this.uid,
    required this.fullName,
    required this.email,
    this.photoUrl,
    required this.college,
    required this.degree,
    required this.department,
    required this.semester,
    required this.academicYear,
    required this.preferences,
  });

  factory UserModel.fromJson(Map<String, dynamic> json) {
    String col = '';
    if (json['college'] != null && json['college'].toString().trim().isNotEmpty) {
      col = json['college'].toString().trim();
    } else if (json['collegeName'] != null && json['collegeName'].toString().trim().isNotEmpty) {
      col = json['collegeName'].toString().trim();
    } else if (json['university'] != null && json['university'].toString().trim().isNotEmpty) {
      col = json['university'].toString().trim();
    } else {
      col = 'Engineering Institute';
    }

    StudyPreferences prefs;
    if (json['preferences'] != null && json['preferences'] is Map<String, dynamic>) {
      prefs = StudyPreferences.fromJson(json['preferences'] as Map<String, dynamic>);
    } else {
      prefs = StudyPreferences.fromJson(json);
    }

    return UserModel(
      uid: json['uid']?.toString() ?? json['id']?.toString() ?? '',
      fullName: json['fullName']?.toString() ?? 'StudyFlow Student',
      email: json['email']?.toString() ?? '',
      photoUrl: json['photoUrl']?.toString(),
      college: col,
      degree: json['degree']?.toString() ?? 'B.Tech',
      department: json['department']?.toString() ?? 'Computer Science',
      semester: (json['semester'] as num?)?.toInt() ?? 6,
      academicYear: json['academicYear']?.toString() ?? '2026-2027',
      preferences: prefs,
    );
  }

  Map<String, dynamic> toJson() => {
    'uid': uid,
    'id': uid,
    'fullName': fullName,
    'email': email,
    'photoUrl': photoUrl,
    'college': college,
    'collegeName': college,
    'university': college,
    'degree': degree,
    'department': department,
    'semester': semester,
    'academicYear': academicYear,
    'preferences': preferences.toJson(),
  };
}
