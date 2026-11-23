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
    return StudyPreferences(
      availableDays: List<String>.from(json['availableDays'] ?? []),
      studyStartTime: json['studyStartTime'] ?? "08:00",
      studyEndTime: json['studyEndTime'] ?? "22:00",
      preferredSessionDuration: json['preferredSessionDuration'] ?? 45,
      shortBreakDuration: json['shortBreakDuration'] ?? 10,
      longBreakDuration: json['longBreakDuration'] ?? 30,
      maximumDailyStudyHours: (json['maximumDailyStudyHours'] as num?)?.toDouble() ?? 6.0,
      preferredLearningStyle: json['preferredLearningStyle'] ?? "Visual & Practice",
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
    return UserModel(
      uid: json['uid'] ?? '',
      fullName: json['fullName'] ?? '',
      email: json['email'] ?? '',
      photoUrl: json['photoUrl'],
      college: json['college'] ?? '',
      degree: json['degree'] ?? '',
      department: json['department'] ?? '',
      semester: json['semester'] ?? 1,
      academicYear: json['academicYear'] ?? '2024-2025',
      preferences: json['preferences'] != null
          ? StudyPreferences.fromJson(json['preferences'])
          : StudyPreferences(),
    );
  }

  Map<String, dynamic> toJson() => {
    'uid': uid,
    'fullName': fullName,
    'email': email,
    'photoUrl': photoUrl,
    'college': college,
    'degree': degree,
    'department': department,
    'semester': semester,
    'academicYear': academicYear,
    'preferences': preferences.toJson(),
  };
}
