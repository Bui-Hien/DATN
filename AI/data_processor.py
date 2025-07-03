import logging
import re
import warnings
from typing import List, Tuple

import numpy as np
import pandas as pd
from sentence_transformers import SentenceTransformer

warnings.filterwarnings("ignore")
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class DataProcessor:
    def __init__(self, model_name: str = "all-MiniLM-L6-v2"):
        self.encoder = SentenceTransformer(model_name)
        self.it_keywords = self._get_it_keywords()

    @staticmethod
    def _get_it_keywords() -> List[str]:
        return [
            "python", "java", "c++", "c#", "javascript", "typescript", "html", "css", "react", "angular",
            "vue", "nodejs", "express", "django", "flask", "spring", "mysql", "postgresql", "mongodb",
            "firebase", "docker", "kubernetes", "git", "linux", "aws", "azure", "gcp", "rest api",
            "oop", "microservices", "machine learning", "deep learning", "ai", "data engineer",
            "data scientist", "devops", "agile", "scrum", "jira", "android", "ios", "swift", "kotlin",
            "react native", "flutter", "php", "laravel", "unit test", "test case", "selenium",
            "công nghệ", "lập trình", "phát triển", "triển khai", "hệ thống", "ứng dụng", "phân tích",
            "dữ liệu", "mạng", "bảo mật", "quản trị", "kiến trúc", "thực tập", "kinh nghiệm"
        ]

    @staticmethod
    def _clean_text(text: str) -> str:
        if not isinstance(text, str):
            return ""
        text = text.encode("utf-8", "ignore").decode("utf-8")
        text = re.sub(r"<[^>]+>", " ", text)
        text = re.sub(r"[^\w\s\.,;:!?()\-\–]", " ", text)
        return re.sub(r"\s+", " ", text).strip()

    def load_data(self, file_path: str) -> pd.DataFrame:
        try:
            df = pd.read_json(file_path, encoding="utf-8")
            df.dropna(subset=["job_description", "candidate_cv", "match"], inplace=True)
            df["job_description"] = df["job_description"].astype(str).apply(self._clean_text)
            df["candidate_cv"] = df["candidate_cv"].astype(str).apply(self._clean_text)
            return df
        except Exception as e:
            logger.error(f"Failed to load or parse data: {e}")
            raise

    def encode_texts(self, texts: List[str], batch_size: int = 32) -> np.ndarray:
        return self.encoder.encode(
            texts,
            batch_size=batch_size,
            show_progress_bar=True,
            convert_to_numpy=True,
            normalize_embeddings=True
        )

    def _extract_years_experience(self, text: str) -> float:
        text = text.lower()
        patterns = [
            r'(\d+)\s*năm\s*kinh\s*nghiệm',
            r'(\d+)\s*years?\s*of\s*experience',
            r'(\d+)\s*years?\s*experience',
            r'hơn\s*(\d+)\s*năm',
            r'over\s*(\d+)\s*years?',
            r'more\s*than\s*(\d+)\s*years?',
            r'(\d+)-\d+\s*years?'
        ]
        for pattern in patterns:
            matches = re.findall(pattern, text)
            if matches:
                try:
                    years = float(matches[0])
                    return years if 0 <= years <= 50 else 0.0
                except:
                    continue
        # Heuristic fallback
        if any(kw in text for kw in ["fresher", "intern", "thực tập"]):
            return 0.5
        if "junior" in text:
            return 1.5
        if any(kw in text for kw in ["senior", "lead", "trưởng nhóm"]):
            return 4.0
        if any(kw in text for kw in ["manager", "architect"]):
            return 6.0
        return 0.0

    def _extract_features(self, texts: List[str]) -> np.ndarray:
        features = []
        for text in texts:
            text_lower = text.lower()
            word_count = len(text_lower.split())
            keyword_matches = [kw for kw in self.it_keywords if kw in text_lower]
            keyword_count = len(keyword_matches)
            keyword_ratio = keyword_count / max(word_count, 1)
            unique_keywords = len(set(keyword_matches))
            years_exp = self._extract_years_experience(text)
            features.append([keyword_count, keyword_ratio, unique_keywords, years_exp])
        return np.array(features)

    def create_features(self, df: pd.DataFrame) -> Tuple[np.ndarray, np.ndarray]:
        job_texts = df["job_description"].tolist()
        cv_texts = df["candidate_cv"].tolist()

        # Embeddings
        job_embeds = self.encode_texts(job_texts)
        cv_embeds = self.encode_texts(cv_texts)

        # Features
        job_features = self._extract_features(job_texts)
        cv_features = self._extract_features(cv_texts)

        # Similarity metrics
        cosine_sim = np.sum(job_embeds * cv_embeds, axis=1, keepdims=True)
        euclidean_dist = np.linalg.norm(job_embeds - cv_embeds, axis=1, keepdims=True)

        # Final feature set
        X = np.concatenate([
            job_embeds,
            cv_embeds,
            job_features,
            cv_features,
            cosine_sim,
            euclidean_dist
        ], axis=1)

        y = df["match"].values
        return X, y
