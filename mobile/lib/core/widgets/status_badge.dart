import 'package:flutter/material.dart';
import '../constants/app_colors.dart';

class StatusBadge extends StatelessWidget {
  final String status;

  const StatusBadge({super.key, required this.status});

  @override
  Widget build(BuildContext context) {
    Color bg;
    Color fg;
    String label;

    switch (status.toLowerCase()) {
      case 'completed':
        bg = AppColors.success.withOpacity(0.12);
        fg = AppColors.success;
        label = 'Completed';
        break;
      case 'in_progress':
        bg = AppColors.info.withOpacity(0.12);
        fg = AppColors.info;
        label = 'In Progress';
        break;
      case 'missed':
        bg = AppColors.error.withOpacity(0.12);
        fg = AppColors.error;
        label = 'Missed';
        break;
      case 'needs_revision':
        bg = AppColors.warning.withOpacity(0.12);
        fg = AppColors.warning;
        label = 'Needs Revision';
        break;
      case 'rescheduled':
        bg = AppColors.secondary.withOpacity(0.15);
        fg = AppColors.primary;
        label = 'Rescheduled';
        break;
      case 'critical':
        bg = AppColors.priorityCritical.withOpacity(0.15);
        fg = AppColors.priorityCritical;
        label = 'Critical';
        break;
      case 'high':
        bg = AppColors.priorityHigh.withOpacity(0.15);
        fg = AppColors.priorityHigh;
        label = 'High Priority';
        break;
      case 'medium':
        bg = AppColors.priorityMedium.withOpacity(0.15);
        fg = Colors.orange.shade800;
        label = 'Medium';
        break;
      case 'low':
        bg = AppColors.priorityLow.withOpacity(0.15);
        fg = AppColors.priorityLow;
        label = 'Low Priority';
        break;
      default:
        bg = Colors.grey.shade200;
        fg = AppColors.textSecondary;
        label = status;
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
      decoration: BoxDecoration(
        color: bg,
        borderRadius: BorderRadius.circular(20),
      ),
      child: Text(
        label,
        style: TextStyle(
          color: fg,
          fontSize: 12,
          fontWeight: FontWeight.w600,
        ),
      ),
    );
  }
}
