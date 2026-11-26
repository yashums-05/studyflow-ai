package com.studymate.ai

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.ByteArrayOutputStream

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.studymate.ai/file_picker"
    private val PICK_FILE_REQUEST_CODE = 9981
    private var pendingResult: MethodChannel.Result? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "pickDocument") {
                pendingResult = result
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "*/*"
                    val mimeTypes = arrayOf(
                        "application/pdf",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/msword",
                        "image/jpeg",
                        "image/png",
                        "image/webp",
                        "text/plain"
                    )
                    putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                startActivityForResult(Intent.createChooser(intent, "Select Academic Timetable or Schedule"), PICK_FILE_REQUEST_CODE)
            } else {
                result.notImplemented()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data?.data != null) {
                val uri: Uri = data.data!!
                var fileName = "document"
                var fileSize: Long = 0

                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        if (nameIndex != -1) fileName = cursor.getString(nameIndex) ?: "document"
                        if (sizeIndex != -1) fileSize = cursor.getLong(sizeIndex)
                    }
                }

                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val buffer = ByteArrayOutputStream()
                    val dataBuffer = ByteArray(4096)
                    var bytesRead: Int
                    while (inputStream?.read(dataBuffer).also { bytesRead = it ?: -1 } != -1) {
                        buffer.write(dataBuffer, 0, bytesRead)
                    }
                    val bytes = buffer.toByteArray()
                    if (fileSize == 0L) fileSize = bytes.size.toLong()

                    val resultMap = hashMapOf(
                        "name" to fileName,
                        "size" to fileSize,
                        "bytes" to bytes
                    )
                    pendingResult?.success(resultMap)
                } catch (e: Exception) {
                    pendingResult?.error("READ_ERROR", "Failed to read file: ${e.message}", null)
                }
            } else {
                pendingResult?.success(null)
            }
            pendingResult = null
        }
    }
}
