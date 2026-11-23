class DashboardAnalyticsModel {
  final int totalSubjects;
  final int totalTopics;
  final int completedTopics;
  final int pendingTopics;
  final double overallCompletionPercentage;
  final double plannedStudyHoursToday;
  final double completedStudyHoursToday;
  final int remainingSessionsToday;
  final int studyStreakDays;
  final double weeklyTotalStudyHours;
  final int upcomingExamsCount;
  final List<String> aiRecommendations;

  DashboardAnalyticsModel({
    required this.totalSubjects,
    required this.totalTopics,
    required this.completedTopics,
    required this.pendingTopics,
    required this.overallCompletionPercentage,
    required this.plannedStudyHoursToday,
    required this.completedStudyHoursToday,
    required this.remainingSessionsToday,
    required this.studyStreakDays,
    required this.weeklyTotalStudyHours,
    required this.upcomingExamsCount,
    required this.aiRecommendations,
  });

  factory DashboardAnalyticsModel.fromJson(Map<String, dynamic> json) {
    return DashboardAnalyticsModel(
      totalSubjects: json['totalSubjects'] ?? 0,
      totalTopics: json['totalTopics'] ?? 0,
      completedTopics: json['completedTopics'] ?? 0,
      pendingTopics: json['pendingTopics'] ?? 0,
      overallCompletionPercentage: (json['overallCompletionPercentage'] as num?)?.toDouble() ?? 0.0,
      plannedStudyHoursToday: (json['plannedStudyHoursToday'] as num?)?.toDouble() ?? 0.0,
      completedStudyHoursToday: (json['completedStudyHoursToday'] as num?)?.toDouble() ?? 0.0,
      remainingSessionsToday: json['remainingSessionsToday'] ?? 0,
      studyStreakDays: json['studyStreakDays'] ?? 0,
      weeklyTotalStudyHours: (json['weeklyTotalStudyHours'] as num?)?.toDouble() ?? 0.0,
      upcomingExamsCount: json['upcomingExamsCount'] ?? 0,
      aiRecommendations: List<String>.from(json['aiRecommendations'] ?? []),
    );
  }
}

class DayStudyDataModel {
  final String date;
  final String dayName;
  final double plannedHours;
  final double actualHours;

  DayStudyDataModel({
    required this.date,
    required this.dayName,
    required this.plannedHours,
    required this.actualHours,
  });

  factory DayStudyDataModel.fromJson(Map<String, dynamic> json) {
    return DayStudyDataModel(
      date: json['date'] ?? '',
      dayName: json['dayName'] ?? '',
      plannedHours: (json['plannedHours'] as num?)?.toDouble() ?? 0.0,
      actualHours: (json['actualHours'] as num?)?.toDouble() ?? 0.0,
    );
  }
}

class WeeklyAnalyticsModel {
  final double totalHoursThisWeek;
  final List<DayStudyDataModel> dailyBreakdown;
  final double plannedVsActualRatio;
  final int studyStreak;

  WeeklyAnalyticsModel({
    required this.totalHoursThisWeek,
    required this.dailyBreakdown,
    required this.plannedVsActualRatio,
    required this.studyStreak,
  });

  factory WeeklyAnalyticsModel.fromJson(Map<String, dynamic> json) {
    var rawList = json['dailyBreakdown'] as List? ?? [];
    return WeeklyAnalyticsModel(
      totalHoursThisWeek: (json['totalHoursThisWeek'] as num?)?.toDouble() ?? 0.0,
      dailyBreakdown: rawList.map((d) => DayStudyDataModel.fromJson(d)).toList(),
      plannedVsActualRatio: (json['plannedVsActualRatio'] as num?)?.toDouble() ?? 1.0,
      studyStreak: json['studyStreak'] ?? 0,
    );
  }
}
