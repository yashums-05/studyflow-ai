import 'package:dio/dio.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../constants/api_constants.dart';

class ApiClient {
  late final Dio _dio;
  static final ApiClient _instance = ApiClient._internal();
  factory ApiClient() => _instance;

  ApiClient._internal() {
    _dio = Dio(
      BaseOptions(
        baseUrl: ApiConstants.defaultBaseUrl,
        connectTimeout: const Duration(seconds: 15),
        receiveTimeout: const Duration(seconds: 15),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Bypass-Tunnel-Reminder': 'true',
        },
      ),
    );

    _dio.interceptors.add(
      InterceptorsWrapper(
        onRequest: (options, handler) async {
          final prefs = await SharedPreferences.getInstance();
          final customUrl = prefs.getString('api_base_url');
          if (customUrl != null && customUrl.trim().isNotEmpty) {
            options.baseUrl = customUrl.trim();
          }
          final token = prefs.getString('auth_token');
          if (token != null && token.trim().isNotEmpty) {
            options.headers['Authorization'] = 'Bearer $token';
          }
          return handler.next(options);
        },
        onError: (DioException e, handler) {
          // Log and propagate error
          return handler.next(e);
        },
      ),
    );
  }

  Dio get dio => _dio;

  void updateBaseUrl(String url) {
    _dio.options.baseUrl = url;
  }
}
