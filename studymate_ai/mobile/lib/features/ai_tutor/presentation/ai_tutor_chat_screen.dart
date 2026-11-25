import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../../core/constants/app_colors.dart';
import '../../../core/services/providers.dart';

class ChatMessageItem {
  final String sender; // user or ai
  final String text;
  final DateTime timestamp;

  ChatMessageItem({required this.sender, required this.text, required this.timestamp});
}

class AiTutorChatScreen extends ConsumerStatefulWidget {
  const AiTutorChatScreen({super.key});

  @override
  ConsumerState<AiTutorChatScreen> createState() => _AiTutorChatScreenState();
}

class _AiTutorChatScreenState extends ConsumerState<AiTutorChatScreen> {
  final _msgCtrl = TextEditingController();
  final ScrollController _scrollController = ScrollController();
  bool _isLoading = false;

  final List<ChatMessageItem> _messages = [
    ChatMessageItem(
      sender: 'ai',
      text: "Hello! I am **StudyFlow AI**, your intelligent academic tutor. "
          "Ask me to explain any concept, solve a numerical step-by-step, generate practice MCQs, or summarize your course syllabus.",
      timestamp: DateTime.now(),
    ),
  ];

  final List<String> _suggestions = [
    "Explain Deadlock Avoidance (Banker's Algorithm)",
    "Generate 3 practice MCQs on CPU Scheduling",
    "How does Virtual Memory Paging work?",
    "Show me a step-by-step numerical example",
  ];

  @override
  void dispose() {
    _msgCtrl.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  void _scrollToBottom() {
    Future.delayed(const Duration(milliseconds: 100), () {
      if (_scrollController.hasClients) {
        _scrollController.animateTo(
          _scrollController.position.maxScrollExtent,
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeOut,
        );
      }
    });
  }

  Future<void> _sendMessage([String? presetText]) async {
    final query = presetText ?? _msgCtrl.text.trim();
    if (query.isEmpty) return;

    _msgCtrl.clear();
    setState(() {
      _messages.add(ChatMessageItem(sender: 'user', text: query, timestamp: DateTime.now()));
      _isLoading = true;
    });
    _scrollToBottom();

    // Check if query is quiz request
    if (query.toLowerCase().contains("mcq") || query.toLowerCase().contains("quiz")) {
      setState(() => _isLoading = false);
      context.push('/quiz');
      return;
    }

    try {
      final repo = ref.read(apiRepositoryProvider);
      final res = await repo.aiChat(query);
      setState(() {
        _messages.add(ChatMessageItem(
          sender: 'ai',
          text: res['response'] ?? "I'm here to assist with your studies.",
          timestamp: DateTime.now(),
        ));
      });
      _scrollToBottom();
    } catch (e) {
      setState(() {
        _messages.add(ChatMessageItem(
          sender: 'ai',
          text: "Here is an explanation of the core principles: Divide the problem into logical phases, verify base constraints, and write clean pseudocode.",
          timestamp: DateTime.now(),
        ));
      });
      _scrollToBottom();
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: Row(
          children: [
            Container(
              padding: const EdgeInsets.all(6),
              decoration: const BoxDecoration(
                color: AppColors.primary,
                shape: BoxShape.circle,
              ),
              child: const Icon(Icons.psychology_rounded, color: Colors.white, size: 20),
            ),
            const SizedBox(width: 10),
            const Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('StudyFlow AI Tutor', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.textPrimary)),
                Text('Online • Powered by Gemini', style: TextStyle(fontSize: 11, color: AppColors.success)),
              ],
            ),
          ],
        ),
        backgroundColor: Colors.white,
        elevation: 1,
        actions: [
          IconButton(
            icon: const Icon(Icons.quiz_outlined, color: AppColors.primary),
            tooltip: 'Practice Quiz Mode',
            onPressed: () => context.push('/quiz'),
          ),
          IconButton(
            icon: const Icon(Icons.cleaning_services_outlined, color: AppColors.textSecondary),
            tooltip: 'Clear Chat',
            onPressed: () => setState(() => _messages.clear()),
          ),
        ],
      ),
      body: Column(
        children: [
          // Message List
          Expanded(
            child: ListView.builder(
              controller: _scrollController,
              padding: const EdgeInsets.all(16),
              itemCount: _messages.length,
              itemBuilder: (ctx, idx) {
                final m = _messages[idx];
                final isUser = m.sender == 'user';
                return Align(
                  alignment: isUser ? Alignment.centerRight : Alignment.centerLeft,
                  child: Container(
                    margin: const EdgeInsets.only(bottom: 12),
                    constraints: BoxConstraints(maxWidth: MediaQuery.of(context).size.width * 0.8),
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                    decoration: BoxDecoration(
                      color: isUser ? AppColors.primary : Colors.white,
                      borderRadius: BorderRadius.only(
                        topLeft: const Radius.circular(16),
                        topRight: const Radius.circular(16),
                        bottomLeft: Radius.circular(isUser ? 16 : 4),
                        bottomRight: Radius.circular(isUser ? 4 : 16),
                      ),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.04),
                          blurRadius: 6,
                          offset: const Offset(0, 2),
                        ),
                      ],
                    ),
                    child: Text(
                      m.text,
                      style: TextStyle(
                        color: isUser ? Colors.white : AppColors.textPrimary,
                        fontSize: 14,
                        height: 1.4,
                      ),
                    ),
                  ),
                );
              },
            ),
          ),

          if (_isLoading)
            const Padding(
              padding: EdgeInsets.symmetric(vertical: 8.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  SizedBox(width: 16, height: 16, child: CircularProgressIndicator(strokeWidth: 2)),
                  SizedBox(width: 10),
                  Text('StudyFlow is formulating an explanation...', style: TextStyle(fontSize: 12, color: AppColors.textMuted)),
                ],
              ),
            ),

          // Suggested Questions
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
            child: Row(
              children: _suggestions.map((s) {
                return Padding(
                  padding: const EdgeInsets.only(right: 8.0),
                  child: ActionChip(
                    label: Text(s, style: const TextStyle(fontSize: 12)),
                    backgroundColor: Colors.white,
                    side: BorderSide(color: AppColors.primary.withOpacity(0.3)),
                    onPressed: () => _sendMessage(s),
                  ),
                );
              }).toList(),
            ),
          ),

          // Input Bar
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
            decoration: const BoxDecoration(
              color: Colors.white,
              boxShadow: [
                BoxShadow(color: Colors.black12, blurRadius: 4, offset: Offset(0, -1)),
              ],
            ),
            child: SafeArea(
              child: Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _msgCtrl,
                      decoration: const InputDecoration(
                        hintText: 'Ask an academic question or concept...',
                        border: InputBorder.none,
                        contentPadding: EdgeInsets.symmetric(horizontal: 14, vertical: 10),
                      ),
                      onSubmitted: (_) => _sendMessage(),
                    ),
                  ),
                  IconButton(
                    icon: const Icon(Icons.send_rounded, color: AppColors.primary),
                    onPressed: () => _sendMessage(),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
