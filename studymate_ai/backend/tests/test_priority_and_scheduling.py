import pytest
from datetime import date, timedelta
from app.services.timetable_service import timetable_service

def test_priority_score_formula():
    """
    Verify exact priority score formula:
    P = 0.35D + 0.30U + 0.20W + 0.15I
    """
    # Case 1: Max difficulty (5 -> 100), urgent exam today (100), worst understanding (1 -> 100), max importance (5 -> 100)
    # P = 0.35(100) + 0.30(100) + 0.20(100) + 0.15(100) = 100.0
    today_str = date.today().strftime("%Y-%m-%d")
    score_max = timetable_service.calculate_priority_score(
        difficulty=5,
        exam_date_str=today_str,
        understanding_rating=1,
        importance=5
    )
    assert score_max == 100.0
    assert timetable_service.get_priority_category(score_max) == "critical"

    # Case 2: Min difficulty (1 -> 0), distant exam (15), perfect understanding (5 -> 0), min importance (1 -> 0)
    # P = 0.35(0) + 0.30(15) + 0.20(0) + 0.15(0) = 4.5
    far_exam = (date.today() + timedelta(days=60)).strftime("%Y-%m-%d")
    score_min = timetable_service.calculate_priority_score(
        difficulty=1,
        exam_date_str=far_exam,
        understanding_rating=5,
        importance=1
    )
    assert score_min == 4.5
    assert timetable_service.get_priority_category(score_min) == "low"

    # Case 3: Balanced medium values
    # D: 3 -> (3-1)*25 = 50
    # U: 10 days left -> 100 - (10*3) = 70
    # W: rating 3 -> (5-3)*25 = 50
    # I: 3 -> (3-1)*25 = 50
    # P = 0.35(50) + 0.30(70) + 0.20(50) + 0.15(50) = 17.5 + 21.0 + 10.0 + 7.5 = 56.0
    exam_10d = (date.today() + timedelta(days=10)).strftime("%Y-%m-%d")
    score_med = timetable_service.calculate_priority_score(
        difficulty=3,
        exam_date_str=exam_10d,
        understanding_rating=3,
        importance=3
    )
    assert score_med == 56.0
    assert timetable_service.get_priority_category(score_med) == "high"

def test_priority_reasons():
    reason = timetable_service.get_priority_reason(
        topic_name="Dynamic Programming",
        difficulty=5,
        days_to_exam=5,
        understanding_rating=2
    )
    assert "Dynamic Programming is prioritized" in reason
    assert "examination is in 5 days" in reason
    assert "high conceptual difficulty" in reason
    assert "previous understanding rating was low" in reason
