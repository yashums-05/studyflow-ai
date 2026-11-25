class ClassSlotModel {
  final String id;
  final String dayOfWeek;
  final String startTime;
  final String endTime;
  final String subjectName;
  final String room;
  final String slotType;

  ClassSlotModel({
    required this.id,
    required this.dayOfWeek,
    required this.startTime,
    required this.endTime,
    required this.subjectName,
    this.room = 'Room 301',
    this.slotType = 'Lecture',
  });

  factory ClassSlotModel.fromJson(Map<String, dynamic> json) {
    return ClassSlotModel(
      id: json['id']?.toString() ?? '',
      dayOfWeek: json['dayOfWeek']?.toString() ?? 'Monday',
      startTime: json['startTime']?.toString() ?? '09:00',
      endTime: json['endTime']?.toString() ?? '10:00',
      subjectName: json['subjectName']?.toString() ?? 'Subject',
      room: json['room']?.toString() ?? 'Room 301',
      slotType: json['slotType']?.toString() ?? 'Lecture',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'dayOfWeek': dayOfWeek,
      'startTime': startTime,
      'endTime': endTime,
      'subjectName': subjectName,
      'room': room,
      'slotType': slotType,
    };
  }
}
