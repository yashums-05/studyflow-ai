import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';
import '../../../shared/models/class_slot_model.dart';
import '../../../shared/models/academic_event_model.dart';
import '../../../shared/models/schedule_suggestion_model.dart';
import '../../../shared/models/exam_model.dart';

class AcademicHubScreen extends ConsumerStatefulWidget {
  const AcademicHubScreen({super.key});

  @override
  ConsumerState<AcademicHubScreen> createState() => _AcademicHubScreenState();
}

class _AcademicHubScreenState extends ConsumerState<AcademicHubScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  String _selectedDay = 'Monday';
  final List<String> _days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 4, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  void _refreshAll() {
    ref.invalidate(academicSuggestionsProvider);
    ref.invalidate(classTimetableProvider);
    ref.invalidate(calendarEventsProvider);
    ref.invalidate(examsProvider);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Academic Hub & Schedules'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh_rounded),
            tooltip: 'Refresh Schedules',
            onPressed: _refreshAll,
          ),
          IconButton(
            icon: const Icon(Icons.cloud_upload_outlined),
            tooltip: 'Quick Upload',
            onPressed: () => _showQuickUploadDialog(context),
          ),
        ],
        bottom: TabBar(
          controller: _tabController,
          isScrollable: true,
          tabAlignment: TabAlignment.start,
          indicatorColor: AppColors.primary,
          labelColor: AppColors.primary,
          unselectedLabelColor: AppColors.textSecondary,
          tabs: const [
            Tab(icon: Icon(Icons.auto_awesome, size: 18), text: 'AI Insights'),
            Tab(icon: Icon(Icons.calendar_view_week_rounded, size: 18), text: 'Class Timetable'),
            Tab(icon: Icon(Icons.event_note_rounded, size: 18), text: 'Events & Holidays'),
            Tab(icon: Icon(Icons.assignment_turned_in_rounded, size: 18), text: 'Exams'),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          _buildAiInsightsTab(),
          _buildClassTimetableTab(),
          _buildAcademicEventsTab(),
          _buildExamsTab(),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _showQuickUploadDialog(context),
        icon: const Icon(Icons.upload_file_rounded),
        label: const Text('Upload Schedule'),
        backgroundColor: AppColors.primary,
        foregroundColor: Colors.white,
      ),
    );
  }

  // ==========================================
  // TAB 1: AI SMART SUGGESTIONS
  // ==========================================
  Widget _buildAiInsightsTab() {
    final suggestionsAsync = ref.watch(academicSuggestionsProvider);

    return suggestionsAsync.when(
      loading: () => const Center(child: CircularProgressIndicator()),
      error: (e, _) => Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.error_outline, size: 48, color: Colors.red),
            const SizedBox(height: 12),
            Text('Failed to load suggestions: $e'),
            const SizedBox(height: 8),
            ElevatedButton(onPressed: _refreshAll, child: const Text('Retry')),
          ],
        ),
      ),
      data: (suggestions) {
        if (suggestions.isEmpty) {
          return Center(
            child: Padding(
              padding: const EdgeInsets.all(24.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.lightbulb_outline, size: 64, color: AppColors.primary.withOpacity(0.5)),
                  const SizedBox(height: 16),
                  const Text('No suggestions yet', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                  const SizedBox(height: 8),
                  const Text(
                    'Upload your class timetable, exam dates, or events calendar to unlock personalized suggestions!',
                    textAlign: TextAlign.center,
                    style: TextStyle(color: AppColors.textSecondary),
                  ),
                  const SizedBox(height: 20),
                  ElevatedButton.icon(
                    onPressed: () => _showQuickUploadDialog(context),
                    icon: const Icon(Icons.cloud_upload),
                    label: const Text('Upload Your Timetable'),
                  ),
                ],
              ),
            ),
          );
        }

        return ListView(
          padding: const EdgeInsets.all(16),
          children: [
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [AppColors.primary.withOpacity(0.12), AppColors.accent.withOpacity(0.08)],
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                ),
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: AppColors.primary.withOpacity(0.2)),
              ),
              child: Row(
                children: [
                  Container(
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      color: AppColors.primary.withOpacity(0.15),
                      shape: BoxShape.circle,
                    ),
                    child: const Icon(Icons.psychology, color: AppColors.primary, size: 28),
                  ),
                  const SizedBox(width: 14),
                  const Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('Adaptive Academic Intelligence', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                        SizedBox(height: 4),
                        Text(
                          'Suggestions synthesized across your class hours, exam dates, holidays, and topic mastery.',
                          style: TextStyle(fontSize: 12, color: AppColors.textSecondary),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),
            ...suggestions.map((s) => _buildSuggestionCard(s)),
            const SizedBox(height: 60),
          ],
        );
      },
    );
  }

  Widget _buildSuggestionCard(ScheduleSuggestionModel s) {
    Color priorityColor;
    switch (s.priority.toUpperCase()) {
      case 'HIGH':
        priorityColor = Colors.redAccent;
        break;
      case 'MEDIUM':
        priorityColor = Colors.amber.shade800;
        break;
      default:
        priorityColor = Colors.blueAccent;
    }

    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      elevation: 1,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  decoration: BoxDecoration(
                    color: priorityColor.withOpacity(0.12),
                    borderRadius: BorderRadius.circular(6),
                    border: Border.all(color: priorityColor.withOpacity(0.3)),
                  ),
                  child: Text(
                    s.priority,
                    style: TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: priorityColor),
                  ),
                ),
                const SizedBox(width: 8),
                if (s.timeSlot != null)
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                    decoration: BoxDecoration(
                      color: Colors.grey.withOpacity(0.1),
                      borderRadius: BorderRadius.circular(6),
                    ),
                    child: Row(
                      children: [
                        const Icon(Icons.access_time, size: 11, color: AppColors.textSecondary),
                        const SizedBox(width: 4),
                        Text(
                          s.timeSlot!,
                          style: const TextStyle(fontSize: 10, color: AppColors.textSecondary),
                        ),
                      ],
                    ),
                  ),
                const Spacer(),
                if (s.relatedSubject != null)
                  Text(
                    s.relatedSubject!,
                    style: const TextStyle(fontSize: 11, fontWeight: FontWeight.w600, color: AppColors.primary),
                  ),
              ],
            ),
            const SizedBox(height: 10),
            Text(
              s.title,
              style: const TextStyle(fontSize: 15, fontWeight: FontWeight.bold, color: AppColors.textPrimary),
            ),
            const SizedBox(height: 6),
            Text(
              s.suggestion,
              style: const TextStyle(fontSize: 13, color: AppColors.textSecondary, height: 1.35),
            ),
            const SizedBox(height: 12),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                TextButton.icon(
                  onPressed: () {
                    context.go(s.actionableRoute);
                  },
                  icon: const Icon(Icons.arrow_forward_rounded, size: 16),
                  label: const Text('Take Action', style: TextStyle(fontWeight: FontWeight.bold)),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  // ==========================================
  // TAB 2: CLASS TIMETABLE
  // ==========================================
  Widget _buildClassTimetableTab() {
    final classAsync = ref.watch(classTimetableProvider);

    return Column(
      children: [
        // Day selector chips
        Container(
          padding: const EdgeInsets.symmetric(vertical: 10),
          child: SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Row(
              children: _days.map((day) {
                final isSelected = day == _selectedDay;
                return Padding(
                  padding: const EdgeInsets.only(right: 8.0),
                  child: FilterChip(
                    label: Text(day),
                    selected: isSelected,
                    selectedColor: AppColors.primary,
                    labelStyle: TextStyle(
                      color: isSelected ? Colors.white : AppColors.textPrimary,
                      fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                    ),
                    onSelected: (selected) {
                      if (selected) setState(() => _selectedDay = day);
                    },
                  ),
                );
              }).toList(),
            ),
          ),
        ),
        const Divider(height: 1),
        Expanded(
          child: classAsync.when(
            loading: () => const Center(child: CircularProgressIndicator()),
            error: (e, _) => Center(child: Text('Error loading class slots: $e')),
            data: (slots) {
              final daySlots = slots.where((s) => s.dayOfWeek.equalsIgnoreCase(_selectedDay)).toList();

              if (daySlots.isEmpty) {
                return Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(Icons.event_busy, size: 56, color: Colors.grey),
                      const SizedBox(height: 12),
                      Text('No classes scheduled for $_selectedDay', style: const TextStyle(fontWeight: FontWeight.bold)),
                      const SizedBox(height: 6),
                      const Text('You have open study hours all day!', style: TextStyle(color: AppColors.textSecondary)),
                      const SizedBox(height: 16),
                      ElevatedButton.icon(
                        onPressed: () => _showAddClassSlotDialog(context, _selectedDay),
                        icon: const Icon(Icons.add),
                        label: Text('Add Class to $_selectedDay'),
                      ),
                    ],
                  ),
                );
              }

              return ListView.builder(
                padding: const EdgeInsets.all(16),
                itemCount: daySlots.length,
                itemBuilder: (ctx, i) {
                  final slot = daySlots[i];
                  final isLab = slot.slotType.toLowerCase().contains('lab');

                  return Card(
                    margin: const EdgeInsets.only(bottom: 10),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    child: ListTile(
                      leading: Container(
                        padding: const EdgeInsets.all(8),
                        decoration: BoxDecoration(
                          color: (isLab ? Colors.orange : AppColors.primary).withOpacity(0.12),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Icon(
                          isLab ? Icons.biotech_rounded : Icons.school_rounded,
                          color: isLab ? Colors.orange : AppColors.primary,
                        ),
                      ),
                      title: Text(slot.subjectName, style: const TextStyle(fontWeight: FontWeight.bold)),
                      subtitle: Text('${slot.startTime} - ${slot.endTime} • ${slot.room}'),
                      trailing: Chip(
                        label: Text(slot.slotType, style: const TextStyle(fontSize: 10)),
                        padding: EdgeInsets.zero,
                        backgroundColor: Colors.grey.withOpacity(0.12),
                      ),
                    ),
                  );
                },
              );
            },
          ),
        ),
      ],
    );
  }

  // ==========================================
  // TAB 3: ACADEMIC EVENTS & HOLIDAYS
  // ==========================================
  Widget _buildAcademicEventsTab() {
    final eventsAsync = ref.watch(calendarEventsProvider);

    return eventsAsync.when(
      loading: () => const Center(child: CircularProgressIndicator()),
      error: (e, _) => Center(child: Text('Error loading events: $e')),
      data: (events) {
        if (events.isEmpty) {
          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Icon(Icons.event_note, size: 56, color: Colors.grey),
                const SizedBox(height: 12),
                const Text('No Academic Events or Holidays Added', style: TextStyle(fontWeight: FontWeight.bold)),
                const SizedBox(height: 16),
                ElevatedButton.icon(
                  onPressed: () => _showAddEventDialog(context),
                  icon: const Icon(Icons.add),
                  label: const Text('Add Event or Holiday'),
                ),
              ],
            ),
          );
        }

        return ListView.builder(
          padding: const EdgeInsets.all(16),
          itemCount: events.length,
          itemBuilder: (ctx, i) {
            final ev = events[i];
            final isHoliday = ev.holiday || ev.eventType.toLowerCase().contains('holiday');

            return Card(
              margin: const EdgeInsets.only(bottom: 12),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
              child: Padding(
                padding: const EdgeInsets.all(14.0),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
                      decoration: BoxDecoration(
                        color: (isHoliday ? Colors.green : AppColors.primary).withOpacity(0.12),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Column(
                        children: [
                          Icon(
                            isHoliday ? Icons.beach_access : Icons.event,
                            color: isHoliday ? Colors.green : AppColors.primary,
                            size: 20,
                          ),
                          const SizedBox(height: 4),
                          Text(
                            ev.eventDate.length >= 10 ? ev.eventDate.substring(5) : ev.eventDate,
                            style: TextStyle(
                              fontSize: 10,
                              fontWeight: FontWeight.bold,
                              color: isHoliday ? Colors.green : AppColors.primary,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(width: 14),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            children: [
                              Expanded(
                                child: Text(ev.title, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                              ),
                              if (isHoliday)
                                const Chip(
                                  label: Text('Holiday', style: TextStyle(fontSize: 10, color: Colors.green)),
                                  backgroundColor: Color(0x1B4CAF50),
                                  padding: EdgeInsets.zero,
                                )
                              else
                                Chip(
                                  label: Text(ev.eventType, style: const TextStyle(fontSize: 10)),
                                  backgroundColor: Colors.grey.withOpacity(0.12),
                                  padding: EdgeInsets.zero,
                                ),
                            ],
                          ),
                          const SizedBox(height: 4),
                          Text(
                            ev.description.isNotEmpty ? ev.description : 'Academic calendar entry',
                            style: const TextStyle(fontSize: 12, color: AppColors.textSecondary),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            );
          },
        );
      },
    );
  }

  // ==========================================
  // TAB 4: EXAM TIMETABLE
  // ==========================================
  Widget _buildExamsTab() {
    final examsAsync = ref.watch(examsProvider);

    return examsAsync.when(
      loading: () => const Center(child: CircularProgressIndicator()),
      error: (e, _) => Center(child: Text('Error loading exams: $e')),
      data: (exams) {
        if (exams.isEmpty) {
          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Icon(Icons.assignment, size: 56, color: Colors.grey),
                const SizedBox(height: 12),
                const Text('No Exams Scheduled', style: TextStyle(fontWeight: FontWeight.bold)),
                const SizedBox(height: 16),
                ElevatedButton.icon(
                  onPressed: () => _showAddExamDialog(context),
                  icon: const Icon(Icons.add),
                  label: const Text('Add Exam'),
                ),
              ],
            ),
          );
        }

        return ListView.builder(
          padding: const EdgeInsets.all(16),
          itemCount: exams.length,
          itemBuilder: (ctx, i) {
            final ex = exams[i];
            return Card(
              margin: const EdgeInsets.only(bottom: 12),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
              child: ListTile(
                leading: Container(
                  padding: const EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    color: Colors.redAccent.withOpacity(0.12),
                    shape: BoxShape.circle,
                  ),
                  child: const Icon(Icons.assignment_late, color: Colors.redAccent, size: 22),
                ),
                title: Text(ex.subjectName, style: const TextStyle(fontWeight: FontWeight.bold)),
                subtitle: Text('Date: ${ex.examDate} • Hall: ${ex.venue}'),
                trailing: Text(
                  'Goal: ${ex.targetScore}%',
                  style: const TextStyle(fontWeight: FontWeight.bold, color: AppColors.primary),
                ),
              ),
            );
          },
        );
      },
    );
  }

  // ==========================================
  // DIALOGS: UPLOAD & ADD
  // ==========================================
  void _showQuickUploadDialog(BuildContext context) {
    String selectedType = 'CLASS_TIMETABLE';
    final textController = TextEditingController(
      text: "Monday 09:00 - 10:00: Operating Systems\nMonday 10:00 - 11:00: Computer Networks\nWednesday 14:00 - 16:00: Systems Lab",
    );

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (ctx, setDialogState) => AlertDialog(
          title: const Text('Upload & AI Extract Schedule', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text('Select what you are uploading:', style: TextStyle(fontSize: 12, color: AppColors.textSecondary)),
                const SizedBox(height: 8),
                DropdownButtonFormField<String>(
                  value: selectedType,
                  decoration: const InputDecoration(border: OutlineInputBorder()),
                  items: const [
                    DropdownMenuItem(value: 'CLASS_TIMETABLE', child: Text('📅 Class Timetable (Routine)')),
                    DropdownMenuItem(value: 'CALENDAR_OF_EVENTS', child: Text('🗓️ Calendar of Events / Holidays')),
                    DropdownMenuItem(value: 'EXAM_TIMETABLE', child: Text('📝 Exam Schedule / Dates')),
                  ],
                  onChanged: (val) {
                    if (val != null) {
                      setDialogState(() {
                        selectedType = val;
                        if (val == 'CALENDAR_OF_EVENTS') {
                          textController.text = "Internal Assessment 1 (IA-1)\nCollege Sports Day (Holiday)\nSemester Finals";
                        } else if (val == 'EXAM_TIMETABLE') {
                          textController.text = "Computer Networks Final\nOperating Systems Final\nDatabase Systems Final";
                        }
                      });
                    }
                  },
                ),
                const SizedBox(height: 14),
                const Text('Paste text, schedule, or routine:', style: TextStyle(fontSize: 12, color: AppColors.textSecondary)),
                const SizedBox(height: 6),
                TextField(
                  controller: textController,
                  maxLines: 6,
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    hintText: 'Paste routine lines or calendar events...',
                  ),
                ),
              ],
            ),
          ),
          actions: [
            TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
            ElevatedButton.icon(
              onPressed: () async {
                final txt = textController.text.trim();
                if (txt.isEmpty) return;
                Navigator.pop(ctx);

                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('AI is parsing and indexing your schedule...')),
                );

                try {
                  final repo = ref.read(apiRepositoryProvider);
                  await repo.uploadAndParseAcademicDoc(type: selectedType, rawText: txt);
                  _refreshAll();

                  if (mounted) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Schedule extracted & AI suggestions updated!')),
                    );
                  }
                } catch (e) {
                  if (mounted) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('Upload failed: $e')),
                    );
                  }
                }
              },
              icon: const Icon(Icons.auto_awesome),
              label: const Text('Parse & Apply'),
            ),
          ],
        ),
      ),
    );
  }

  void _showAddClassSlotDialog(BuildContext context, String defaultDay) {
    final subCtrl = TextEditingController();
    final startCtrl = TextEditingController(text: '09:00');
    final endCtrl = TextEditingController(text: '10:00');
    final roomCtrl = TextEditingController(text: 'LH-301');
    String slotType = 'Lecture';

    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text('Add Class to $defaultDay', style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(controller: subCtrl, decoration: const InputDecoration(labelText: 'Subject Name')),
            const SizedBox(height: 8),
            Row(
              children: [
                Expanded(child: TextField(controller: startCtrl, decoration: const InputDecoration(labelText: 'Start (HH:mm)'))),
                const SizedBox(width: 8),
                Expanded(child: TextField(controller: endCtrl, decoration: const InputDecoration(labelText: 'End (HH:mm)'))),
              ],
            ),
            const SizedBox(height: 8),
            TextField(controller: roomCtrl, decoration: const InputDecoration(labelText: 'Room / Hall')),
          ],
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
          ElevatedButton(
            onPressed: () async {
              if (subCtrl.text.trim().isEmpty) return;
              Navigator.pop(ctx);
              await ref.read(apiRepositoryProvider).addClassSlot({
                'dayOfWeek': defaultDay,
                'startTime': startCtrl.text.trim(),
                'endTime': endCtrl.text.trim(),
                'subjectName': subCtrl.text.trim(),
                'room': roomCtrl.text.trim(),
                'slotType': slotType,
              });
              _refreshAll();
            },
            child: const Text('Save Slot'),
          ),
        ],
      ),
    );
  }

  void _showAddEventDialog(BuildContext context) {
    final titleCtrl = TextEditingController();
    final dateCtrl = TextEditingController(text: DateTime.now().add(const Duration(days: 7)).toString().substring(0, 10));
    final descCtrl = TextEditingController();
    bool isHoliday = false;

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (ctx, setDState) => AlertDialog(
          title: const Text('Add Academic Event', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(controller: titleCtrl, decoration: const InputDecoration(labelText: 'Event Title')),
              const SizedBox(height: 8),
              TextField(controller: dateCtrl, decoration: const InputDecoration(labelText: 'Date (YYYY-MM-DD)')),
              const SizedBox(height: 8),
              TextField(controller: descCtrl, decoration: const InputDecoration(labelText: 'Description')),
              const SizedBox(height: 8),
              CheckboxListTile(
                title: const Text('Is Campus Holiday?', style: TextStyle(fontSize: 13)),
                value: isHoliday,
                onChanged: (v) => setDState(() => isHoliday = v ?? false),
                contentPadding: EdgeInsets.zero,
              ),
            ],
          ),
          actions: [
            TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
            ElevatedButton(
              onPressed: () async {
                if (titleCtrl.text.trim().isEmpty) return;
                Navigator.pop(ctx);
                await ref.read(apiRepositoryProvider).addCalendarEvent({
                  'title': titleCtrl.text.trim(),
                  'eventDate': dateCtrl.text.trim(),
                  'description': descCtrl.text.trim(),
                  'eventType': isHoliday ? 'holiday' : 'event',
                  'holiday': isHoliday,
                });
                _refreshAll();
              },
              child: const Text('Save Event'),
            ),
          ],
        ),
      ),
    );
  }

  void _showAddExamDialog(BuildContext context) {
    final subCtrl = TextEditingController();
    final dateCtrl = TextEditingController(text: DateTime.now().add(const Duration(days: 14)).toString().substring(0, 10));
    final hallCtrl = TextEditingController(text: 'Exam Hall 1');

    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Add Exam', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(controller: subCtrl, decoration: const InputDecoration(labelText: 'Subject Name')),
            const SizedBox(height: 8),
            TextField(controller: dateCtrl, decoration: const InputDecoration(labelText: 'Exam Date (YYYY-MM-DD)')),
            const SizedBox(height: 8),
            TextField(controller: hallCtrl, decoration: const InputDecoration(labelText: 'Venue / Hall')),
          ],
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
          ElevatedButton(
            onPressed: () async {
              if (subCtrl.text.trim().isEmpty) return;
              Navigator.pop(ctx);
              await ref.read(apiRepositoryProvider).createExam({
                'subjectName': subCtrl.text.trim(),
                'examDate': dateCtrl.text.trim(),
                'venue': hallCtrl.text.trim(),
                'targetScore': 85,
              });
              _refreshAll();
            },
            child: const Text('Save Exam'),
          ),
        ],
      ),
    );
  }
}

extension StringExtension on String {
  bool equalsIgnoreCase(String other) => toLowerCase() == other.toLowerCase();
}
