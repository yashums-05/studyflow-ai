class AcademicEventModel {
  final String id;
  final String title;
  final String eventDate;
  final String? endDate;
  final String eventType;
  final String description;
  final bool holiday;

  AcademicEventModel({
    required this.id,
    required this.title,
    required this.eventDate,
    this.endDate,
    this.eventType = 'event',
    this.description = '',
    this.holiday = false,
  });

  factory AcademicEventModel.fromJson(Map<String, dynamic> json) {
    return AcademicEventModel(
      id: json['id']?.toString() ?? '',
      title: json['title']?.toString() ?? 'Event',
      eventDate: json['eventDate']?.toString() ?? '',
      endDate: json['endDate']?.toString(),
      eventType: json['eventType']?.toString() ?? 'event',
      description: json['description']?.toString() ?? '',
      holiday: json['holiday'] == true,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'eventDate': eventDate,
      if (endDate != null) 'endDate': endDate,
      'eventType': eventType,
      'description': description,
      'holiday': holiday,
    };
  }
}
