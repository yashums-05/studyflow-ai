import 'package:flutter_local_notifications/flutter_local_notifications.dart';

class LocalNotificationService {
  static final LocalNotificationService _instance = LocalNotificationService._internal();
  factory LocalNotificationService() => _instance;
  LocalNotificationService._internal();

  final FlutterLocalNotificationsPlugin _plugin = FlutterLocalNotificationsPlugin();
  bool _initialized = false;

  Future<void> init() async {
    if (_initialized) return;

    const androidSettings = AndroidInitializationSettings('@mipmap/ic_launcher');
    const initSettings = InitializationSettings(android: androidSettings);

    try {
      await _plugin.initialize(
        initSettings,
        onDidReceiveNotificationResponse: (details) {
          // Handle notification click
        },
      );
      _initialized = true;
    } catch (_) {}
  }

  Future<void> showNotification({
    required int id,
    required String title,
    required String body,
    String? payload,
  }) async {
    const androidDetails = AndroidNotificationDetails(
      'studymate_alerts',
      'StudyMate Reminders',
      channelDescription: 'Alerts for upcoming study sessions, exams, and streak reminders',
      importance: Importance.max,
      priority: Priority.high,
      showWhen: true,
    );
    const details = NotificationDetails(android: androidDetails);

    try {
      await _plugin.show(id, title, body, details, payload: payload);
    } catch (_) {}
  }
}
