import 'dart:typed_data';
import 'package:flutter/services.dart';

class PickedDoc {
  final String name;
  final int size;
  final Uint8List bytes;

  PickedDoc({
    required this.name,
    required this.size,
    required this.bytes,
  });
}

class NativeFilePicker {
  static const MethodChannel _channel = MethodChannel('com.studymate.ai/file_picker');

  static Future<PickedDoc?> pickDocument() async {
    try {
      final res = await _channel.invokeMethod<Map>('pickDocument');
      if (res != null) {
        final name = res['name']?.toString() ?? 'document';
        final size = (res['size'] as num?)?.toInt() ?? 0;
        final rawBytes = res['bytes'];
        Uint8List bytes;
        if (rawBytes is Uint8List) {
          bytes = rawBytes;
        } else if (rawBytes is List) {
          bytes = Uint8List.fromList(List<int>.from(rawBytes));
        } else {
          bytes = Uint8List(0);
        }

        return PickedDoc(
          name: name,
          size: size,
          bytes: bytes,
        );
      }
    } catch (e) {
      // Platform error
    }
    return null;
  }
}
