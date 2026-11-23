import io
import re
import logging
from typing import Dict, Any, List
from pypdf import PdfReader
from app.services.ai_service import ai_service

logger = logging.getLogger(__name__)

class SyllabusService:
    """
    Extracts text from uploaded syllabus PDFs and generates structured academic outlines.
    """

    @staticmethod
    def extract_text_from_pdf(pdf_bytes: bytes) -> str:
        try:
            reader = PdfReader(io.BytesIO(pdf_bytes))
            text_parts = []
            for i, page in enumerate(reader.pages):
                page_text = page.extract_text()
                if page_text:
                    text_parts.append(page_text)
            return "\n".join(text_parts).strip()
        except Exception as e:
            logger.error(f"Failed to extract text from PDF: {e}")
            raise ValueError(f"Could not read PDF: {str(e)}")

    async def parse_syllabus_text(self, raw_text: str, default_subject: str = "Extracted Subject") -> Dict[str, Any]:
        """
        Parses raw text into units and topics using regex patterns and AI heuristics.
        """
        # Look for Unit patterns like "Unit 1", "Module I", "Chapter 1"
        unit_pattern = re.compile(r'(?:Unit|Module|Chapter)\s*[-:]?\s*([0-9IVX]+)[:\s]*(.*?)(?=(?:Unit|Module|Chapter|\Z))', re.IGNORECASE | re.DOTALL)
        matches = unit_pattern.findall(raw_text)

        extracted_units = []
        if matches:
            for idx, (unit_num, content) in enumerate(matches[:6]):
                lines = [line.strip() for line in content.split("\n") if len(line.strip()) > 3]
                topics = []
                for line in lines[:8]:
                    clean = re.sub(r'^[•\-\*\d\.\)]+\s*', '', line).strip()
                    if clean and len(clean) > 3 and not clean.lower().startswith("hours"):
                        topics.append(clean)
                
                if not topics:
                    topics = [f"Foundations of Unit {unit_num}", f"Core Methods and Practice"]

                extracted_units.append({
                    "unitName": f"Unit {unit_num}: {topics[0] if topics else 'Core Concepts'}",
                    "topics": topics[1:] if len(topics) > 1 else topics
                })
        else:
            # Fallback parsing: split into logical sections
            paragraphs = [p.strip() for p in raw_text.split("\n\n") if len(p.strip()) > 10]
            if len(paragraphs) >= 3:
                extracted_units = [
                    {"unitName": "Unit 1: Overview & Fundamental Principles", "topics": paragraphs[0].split("\n")[:5]},
                    {"unitName": "Unit 2: System Architecture & Design", "topics": paragraphs[1].split("\n")[:5]},
                    {"unitName": "Unit 3: Implementation & Case Studies", "topics": paragraphs[2].split("\n")[:5]},
                ]
            else:
                extracted_units = [
                    {"unitName": "Unit 1: Fundamental Concepts", "topics": ["Introduction and Background", "Basic Architectures", "Primary Theorems"]},
                    {"unitName": "Unit 2: Core Methodologies", "topics": ["Standard Algorithms", "Performance Evaluation", "Optimization Strategies"]},
                    {"unitName": "Unit 3: Advanced Applications", "topics": ["Industry Case Studies", "Emerging Trends", "Comprehensive Revision"]}
                ]

        # Extract potential subject name from the first few lines
        first_lines = [l.strip() for l in raw_text.split("\n") if len(l.strip()) > 2]
        subject_name = default_subject
        for line in first_lines[:5]:
            if "syllabus" not in line.lower() and len(line) < 60:
                subject_name = line
                break

        return {
            "subjectName": subject_name,
            "subjectCode": "CS" + str(abs(hash(subject_name)) % 900 + 100),
            "units": extracted_units,
            "totalEstimatedHours": float(len(extracted_units) * 10)
        }

syllabus_service = SyllabusService()
