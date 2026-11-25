import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';

class AddSubjectDialog extends ConsumerStatefulWidget {
  const AddSubjectDialog({super.key});

  @override
  ConsumerState<AddSubjectDialog> createState() => _AddSubjectDialogState();
}

class _AddSubjectDialogState extends ConsumerState<AddSubjectDialog> {
  final _nameCtrl = TextEditingController();
  final _codeCtrl = TextEditingController();
  int _difficulty = 3;
  String _selectedColor = '#7556D9';
  bool _isLoading = false;

  final List<String> _colors = [
    '#7556D9',
    '#22A06B',
    '#3B82F6',
    '#F2A93B',
    '#E05252',
    '#EC4899',
  ];

  @override
  void dispose() {
    _nameCtrl.dispose();
    _codeCtrl.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    if (_nameCtrl.text.trim().isEmpty) return;
    setState(() => _isLoading = true);

    try {
      await ref.read(subjectsProvider.notifier).addSubject({
        'subjectName': _nameCtrl.text.trim(),
        'subjectCode': _codeCtrl.text.trim().isEmpty ? 'SUB101' : _codeCtrl.text.trim().toUpperCase(),
        'semester': 6,
        'difficulty': _difficulty,
        'colorHex': _selectedColor,
        'estimatedHours': 25.0,
      });
      if (mounted) Navigator.pop(context);
    } catch (_) {
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Add New Subject', style: TextStyle(fontWeight: FontWeight.bold)),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
      content: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('Subject Name', style: TextStyle(fontWeight: FontWeight.w600, fontSize: 13)),
            const SizedBox(height: 6),
            TextField(
              controller: _nameCtrl,
              decoration: const InputDecoration(hintText: 'e.g. Distributed Systems'),
            ),
            const SizedBox(height: 14),
            const Text('Subject Code', style: TextStyle(fontWeight: FontWeight.w600, fontSize: 13)),
            const SizedBox(height: 6),
            TextField(
              controller: _codeCtrl,
              decoration: const InputDecoration(hintText: 'e.g. CS601'),
            ),
            const SizedBox(height: 14),
            const Text('Difficulty Level (1 = Easy, 5 = Very Hard)', style: TextStyle(fontWeight: FontWeight.w600, fontSize: 13)),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [1, 2, 3, 4, 5].map((d) {
                final selected = _difficulty == d;
                return ChoiceChip(
                  label: Text('$d'),
                  selected: selected,
                  selectedColor: AppColors.primary,
                  labelStyle: TextStyle(color: selected ? Colors.white : Colors.black),
                  onSelected: (_) => setState(() => _difficulty = d),
                );
              }).toList(),
            ),
            const SizedBox(height: 14),
            const Text('Theme Color', style: TextStyle(fontWeight: FontWeight.w600, fontSize: 13)),
            const SizedBox(height: 8),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: _colors.map((hex) {
                final c = Color(int.parse(hex.replaceFirst('#', '0xFF')));
                final isSel = _selectedColor == hex;
                return GestureDetector(
                  onTap: () => setState(() => _selectedColor = hex),
                  child: Container(
                    width: 34,
                    height: 34,
                    decoration: BoxDecoration(
                      color: c,
                      shape: BoxShape.circle,
                      border: isSel ? Border.all(color: Colors.black, width: 3) : null,
                    ),
                  ),
                );
              }).toList(),
            ),
          ],
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.pop(context),
          child: const Text('Cancel'),
        ),
        ElevatedButton(
          onPressed: _isLoading ? null : _submit,
          child: _isLoading ? const SizedBox(height: 16, width: 16, child: CircularProgressIndicator(color: Colors.white, strokeWidth: 2)) : const Text('Save Subject'),
        ),
      ],
    );
  }
}
