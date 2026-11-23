import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:studymate_ai/core/widgets/stat_card.dart';
import 'package:studymate_ai/core/widgets/status_badge.dart';

void main() {
  group('StudyMate AI Core Widget Tests', () {
    testWidgets('StatCard displays title, value, and subtitle correctly', (WidgetTester tester) async {
      await tester.pumpWidget(
        const MaterialApp(
          home: Scaffold(
            body: StatCard(
              title: 'Total Subjects',
              value: '6',
              subtitle: 'Active Courses',
              icon: Icons.school_rounded,
            ),
          ),
        ),
      );

      expect(find.text('Total Subjects'), findsOneWidget);
      expect(find.text('6'), findsOneWidget);
      expect(find.text('Active Courses'), findsOneWidget);
      expect(find.byIcon(Icons.school_rounded), findsOneWidget);
    });

    testWidgets('StatusBadge renders appropriate labels and colors for session statuses', (WidgetTester tester) async {
      await tester.pumpWidget(
        const MaterialApp(
          home: Scaffold(
            body: Column(
              children: [
                StatusBadge(status: 'completed'),
                StatusBadge(status: 'in_progress'),
                StatusBadge(status: 'missed'),
              ],
            ),
          ),
        ),
      );

      expect(find.text('Completed'), findsOneWidget);
      expect(find.text('In Progress'), findsOneWidget);
      expect(find.text('Missed'), findsOneWidget);
    });
  });
}
