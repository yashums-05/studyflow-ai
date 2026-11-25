class ScheduleSuggestionModel {
  final String id;
  final String category;
  final String title;
  final String suggestion;
  final String priority;
  final String actionableRoute;
  final String? relatedSubject;
  final String? timeSlot;

  ScheduleSuggestionModel({
    required this.id,
    required this.category,
    required this.title,
    required this.suggestion,
    this.priority = 'MEDIUM',
    this.actionableRoute = '/timetable',
    this.relatedSubject,
    this.timeSlot,
  });

  factory ScheduleSuggestionModel.fromJson(Map<String, dynamic> json) {
    return ScheduleSuggestionModel(
      id: json['id']?.toString() ?? '',
      category: json['category']?.toString() ?? 'general',
      title: json['title']?.toString() ?? 'Study Tip',
      suggestion: json['suggestion']?.toString() ?? '',
      priority: json['priority']?.toString() ?? 'MEDIUM',
      actionableRoute: json['actionableRoute']?.toString() ?? '/timetable',
      relatedSubject: json['relatedSubject']?.toString(),
      timeSlot: json['timeSlot']?.toString(),
    );
  }
}
