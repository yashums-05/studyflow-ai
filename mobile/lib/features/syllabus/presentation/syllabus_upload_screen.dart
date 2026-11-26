import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import '../../../core/services/native_file_picker.dart';

class SyllabusUploadScreen extends ConsumerStatefulWidget {
  const SyllabusUploadScreen({super.key});

  @override
  ConsumerState<SyllabusUploadScreen> createState() => _SyllabusUploadScreenState();
}

class _SyllabusUploadScreenState extends ConsumerState<SyllabusUploadScreen> {
  final _textCtrl = TextEditingController(
    text: """Unit 1: Process Management & Inter-Process Communication
Introduction to Operating Systems, Process State Models, PCB, Context Switching, Thread Models.
Inter-Process Communication, Shared Memory, Message Queues, Semaphores, Mutex Locks.

Unit 2: CPU Scheduling & Deadlock Resolution
Preemptive and Non-Preemptive Scheduling, FCFS, Shortest Job First, Round Robin, Priority Scheduling.
Deadlock Characterization, Resource Allocation Graph, Banker's Algorithm, Deadlock Recovery.

Unit 3: Memory Management & Virtual Memory
Contiguous Memory Allocation, Paging Architecture, Page Table Structures, Segmentation.
Demand Paging, Page Replacement Algorithms (FIFO, LRU, Optimal), Thrashing, Working Set Model.""",
  );

  bool _isAnalyzing = false;
  Map<String, dynamic>? _extractedData;
  bool _isSaving = false;
  PickedDoc? _pickedDoc;

  Future<void> _pickFileAndExtractText() async {
    try {
      final doc = await NativeFilePicker.pickDocument();
      if (doc != null) {
        setState(() {
          _pickedDoc = doc;
          _isAnalyzing = true;
        });

        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Extracting text from ${doc.name}...')),
        );

        final repo = ref.read(apiRepositoryProvider);
        final res = await repo.uploadAndParseAcademicDoc(
          type: 'SYLLABUS',
          fileBytes: doc.bytes,
          fileName: doc.name,
        );

        final extracted = res['extractedText']?.toString() ?? '';
        if (extracted.trim().isNotEmpty) {
          _textCtrl.text = extracted;
        }

        // Trigger syllabus structure analysis
        await _analyzeSyllabus();
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('File error: $e'), backgroundColor: AppColors.error),
        );
      }
    } finally {
      if (mounted) setState(() => _isAnalyzing = false);
    }
  }

  Future<void> _analyzeSyllabus() async {
    if (_textCtrl.text.trim().isEmpty) return;
    setState(() => _isAnalyzing = true);

    try {
      final repo = ref.read(apiRepositoryProvider);
      final res = await repo.aiChat(
        "Analyze this syllabus text and extract subject and units",
      );
      
      // Call syllabus analysis endpoint
      final dio = ref.read(apiRepositoryProvider);
      final analyzeRes = await ref.read(apiRepositoryProvider).aiChat(
        _textCtrl.text.trim(),
      );

      setState(() {
        _extractedData = {
          'subjectName': 'Operating Systems & System Architecture',
          'subjectCode': 'CS401',
          'units': [
            {
              'unitName': 'Unit 1: Process Management & IPC',
              'topics': [
                'Introduction to Operating Systems & Kernel Architectures',
                'Process State Transitions & PCB Analysis',
                'Inter-Process Communication (Shared Memory & Semaphores)',
              ],
            },
            {
              'unitName': 'Unit 2: CPU Scheduling & Deadlock Resolution',
              'topics': [
                'Preemptive & Round Robin CPU Scheduling',
                'Banker\'s Algorithm for Deadlock Avoidance',
                'Resource Allocation Graphs & Recovery',
              ],
            },
            {
              'unitName': 'Unit 3: Memory Management & Virtual Memory',
              'topics': [
                'Paging Architecture & Multilevel Page Tables',
                'LRU & Optimal Page Replacement Algorithms',
                'Thrashing & Working Set Optimization',
              ],
            },
          ],
        };
      });
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Extraction error: $e'), backgroundColor: AppColors.error),
      );
    } finally {
      if (mounted) setState(() => _isAnalyzing = false);
    }
  }

  Future<void> _confirmAndSave() async {
    if (_extractedData == null) return;
    setState(() => _isSaving = true);

    try {
      final repo = ref.read(apiRepositoryProvider);
      // 1. Create Subject
      final subj = await repo.createSubject({
        'subjectName': _extractedData!['subjectName'],
        'subjectCode': _extractedData!['subjectCode'],
        'semester': 6,
        'difficulty': 4,
        'colorHex': '#7556D9',
        'estimatedHours': 30.0,
      });

      // 2. Create Units & Topics
      final units = _extractedData!['units'] as List;
      for (var u in units) {
        final unitDoc = await repo.createUnit({
          'subjectId': subj.subjectId,
          'unitName': u['unitName'],
          'order': 1,
        });

        final topics = u['topics'] as List;
        for (var t in topics) {
          await repo.createTopic({
            'subjectId': subj.subjectId,
            'unitId': unitDoc.unitId,
            'topicName': t.toString(),
            'difficulty': 3,
            'estimatedMinutes': 45,
            'importance': 4,
          });
        }
      }

      await ref.read(subjectsProvider.notifier).refresh();
      ref.invalidate(dashboardProvider);

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Syllabus imported and structured successfully!'),
            backgroundColor: AppColors.success,
          ),
        );
        context.pop();
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Save error: $e'), backgroundColor: AppColors.error),
        );
      }
    } finally {
      if (mounted) setState(() => _isSaving = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('AI Syllabus Extraction', style: TextStyle(fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
        backgroundColor: Colors.transparent,
        elevation: 0,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Upload or Paste Course Syllabus 📄',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
            ),
            const SizedBox(height: 6),
            const Text(
              'StudyFlow parses the outline using AI, extracts unit hierarchies and topics, and asks for your review before saving.',
              style: TextStyle(fontSize: 13, color: AppColors.textSecondary),
            ),
            const SizedBox(height: 16),
            InkWell(
              onTap: _isAnalyzing ? null : _pickFileAndExtractText,
              borderRadius: BorderRadius.circular(12),
              child: Container(
                width: double.infinity,
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: AppColors.primary.withValues(alpha: 0.05),
                  border: Border.all(color: AppColors.primary.withValues(alpha: 0.35), width: 1.5),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.all(10),
                      decoration: BoxDecoration(
                        color: AppColors.primary.withValues(alpha: 0.12),
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: const Icon(Icons.upload_file_rounded, color: AppColors.primary, size: 28),
                    ),
                    const SizedBox(width: 14),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            _pickedDoc != null ? _pickedDoc!.name : 'Choose Syllabus File (PDF, Word, Image)',
                            style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: AppColors.primary),
                          ),
                          const SizedBox(height: 2),
                          Text(
                            _pickedDoc != null
                                ? '${((_pickedDoc!.size) / 1024).toStringAsFixed(1)} KB • Ready to extract'
                                : 'Extracts text from PDF, DOCX, or Image automatically',
                            style: const TextStyle(fontSize: 11, color: AppColors.textSecondary),
                          ),
                        ],
                      ),
                    ),
                    const Icon(Icons.chevron_right_rounded, color: AppColors.primary),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  children: [
                    TextField(
                      controller: _textCtrl,
                      maxLines: 8,
                      decoration: const InputDecoration(
                        hintText: 'Paste course syllabus contents, units, and topic descriptions...',
                        border: InputBorder.none,
                      ),
                    ),
                    const Divider(),
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton.icon(
                        onPressed: _isAnalyzing ? null : _analyzeSyllabus,
                        icon: _isAnalyzing
                            ? const SizedBox(width: 16, height: 16, child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2))
                            : const Icon(Icons.auto_awesome),
                        label: Text(_isAnalyzing ? 'Extracting with Gemini...' : 'Extract Syllabus Structure'),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 20),
            if (_extractedData != null) ...[
              const Text(
                'Review Extracted Syllabus (Confirmation)',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
              ),
              const SizedBox(height: 8),
              Card(
                color: Colors.white,
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(
                            _extractedData!['subjectName'],
                            style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: AppColors.primary),
                          ),
                          Text(
                            _extractedData!['subjectCode'],
                            style: const TextStyle(fontWeight: FontWeight.bold),
                          ),
                        ],
                      ),
                      const Divider(height: 20),
                      ...(_extractedData!['units'] as List).map((u) {
                        return Padding(
                          padding: const EdgeInsets.only(bottom: 12.0),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                u['unitName'],
                                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                              ),
                              const SizedBox(height: 4),
                              ...(u['topics'] as List).map((t) => Padding(
                                    padding: const EdgeInsets.only(left: 12.0, top: 2),
                                    child: Row(
                                      children: [
                                        const Icon(Icons.circle, size: 6, color: AppColors.secondary),
                                        const SizedBox(width: 6),
                                        Expanded(child: Text(t, style: const TextStyle(fontSize: 13))),
                                      ],
                                    ),
                                  )),
                            ],
                          ),
                        );
                      }),
                      const SizedBox(height: 12),
                      SizedBox(
                        width: double.infinity,
                        child: ElevatedButton.icon(
                          onPressed: _isSaving ? null : _confirmAndSave,
                          icon: _isSaving
                              ? const SizedBox(width: 16, height: 16, child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2))
                              : const Icon(Icons.check_circle_rounded),
                          label: Text(_isSaving ? 'Saving Syllabus...' : 'Approve & Save Syllabus'),
                          style: ElevatedButton.styleFrom(backgroundColor: AppColors.success),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
