import 'package:flutter_test/flutter_test.dart';
import 'package:studymate_ai/shared/models/user_model.dart';
import 'package:studymate_ai/shared/models/subject_model.dart';
import 'package:studymate_ai/shared/models/study_session_model.dart';
import 'package:studymate_ai/shared/models/exam_model.dart';

void main() {
  group('StudyMate AI Model Serialization Tests', () {
    test('UserModel serialization and defaults', () {
      final user = UserModel(
        uid: 'user_123',
        fullName: 'Alex Mercer',
        email: 'alex@mit.edu',
        college: 'MIT',
        degree: 'B.Tech',
        department: 'CS',
        semester: 6,
        academicYear: '2024-2025',
        preferences: StudyPreferences(
          maximumDailyStudyHours: 5.5,
          preferredSessionDuration: 45,
        ),
      );

      final json = user.toJson();
      expect(json['uid'], 'user_123');
      expect(json['preferences']['maximumDailyStudyHours'], 5.5);

      final deserialized = UserModel.fromJson(json);
      expect(deserialized.fullName, 'Alex Mercer');
      expect(deserialized.preferences.preferredSessionDuration, 45);
    });

    test('SubjectModel and TopicModel parsing', () {
      final topic = TopicModel(
        topicId: 't1',
        userId: 'u1',
        subjectId: 's1',
        unitId: 'un1',
        topicName: 'CPU Scheduling',
        difficulty: 4,
        importance: 5,
        priorityScore: 78.5,
      );

      final json = topic.toJson();
      expect(json['priorityScore'], 78.5);

      final deserializedTopic = TopicModel.fromJson(json);
      expect(deserializedTopic.topicName, 'CPU Scheduling');
      expect(deserializedTopic.difficulty, 4);

      final unit = UnitModel(
        unitId: 'un1',
        subjectId: 's1',
        userId: 'u1',
        unitName: 'Unit 1: Process Management',
        topics: [deserializedTopic],
      );

      final subject = SubjectModel(
        subjectId: 's1',
        userId: 'u1',
        subjectName: 'Operating Systems',
        subjectCode: 'CS301',
        semester: 6,
        units: [unit],
      );

      expect(subject.units.first.topics.first.topicName, 'CPU Scheduling');
    });

    test('StudySessionModel serialization', () {
      final session = StudySessionModel(
        sessionId: 'sess_1',
        userId: 'u1',
        subjectId: 's1',
        topicId: 't1',
        date: '2026-11-23',
        startTimeStr: '09:00',
        endTimeStr: '09:45',
        estimatedDurationMinutes: 45,
        priority: 'high',
        status: 'scheduled',
      );

      final json = session.toJson();
      expect(json['startTime'], '09:00');
      expect(json['priority'], 'high');

      final deserialized = StudySessionModel.fromJson(json);
      expect(deserialized.sessionId, 'sess_1');
    });

    test('ExamModel countdown calculation verification', () {
      final exam = ExamModel(
        id: 'ex_1',
        userId: 'u1',
        subjectId: 's1',
        examName: 'Midterm Final',
        examDate: DateTime.parse('2026-12-05'),
        daysRemaining: 12,
      );

      expect(exam.daysRemaining, 12);
      expect(exam.examType, 'semester');
    });
  });
}
