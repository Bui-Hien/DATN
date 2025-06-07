import json
import pandas as pd
import numpy as np
from sentence_transformers import SentenceTransformer
from sklearn.preprocessing import StandardScaler
from typing import Tuple, List
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class DataProcessor:
    def __init__(self, model_name: str = "all-MiniLM-L6-v2"):
        self.encoder = SentenceTransformer(model_name)
        self.scaler = StandardScaler()

    def load_data(self, file_path: str) -> pd.DataFrame:
        """Load và validate dữ liệu"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                data = json.load(f)

            df = pd.DataFrame(data)

            # Validate required columns
            required_cols = ['job_description', 'candidate_cv', 'match']
            missing_cols = [col for col in required_cols if col not in df.columns]
            if missing_cols:
                raise ValueError(f"Missing columns: {missing_cols}")

            # Clean data
            df = df.dropna(subset=required_cols)
            df['job_description'] = df['job_description'].astype(str)
            df['candidate_cv'] = df['candidate_cv'].astype(str)

            logger.info(f"Loaded {len(df)} records")
            return df

        except Exception as e:
            logger.error(f"Error loading data: {e}")
            raise

    def preprocess_text(self, texts: List[str]) -> List[str]:
        """Tiền xử lý văn bản"""
        processed = []
        for text in texts:
            # Remove extra whitespace và normalize
            text = ' '.join(text.split())
            # Convert to lowercase for consistency
            text = text.lower()
            processed.append(text)
        return processed

    def encode_texts(self, texts: List[str], batch_size: int = 32) -> np.ndarray:
        """Encode văn bản thành vector với batch processing"""
        processed_texts = self.preprocess_text(texts)
        embeddings = self.encoder.encode(
            processed_texts,
            batch_size=batch_size,
            show_progress_bar=True,
            convert_to_numpy=True
        )
        return embeddings

    def create_features(self, df: pd.DataFrame) -> Tuple[np.ndarray, np.ndarray]:
        """Tạo features từ job description và CV"""
        logger.info("Encoding job descriptions...")
        jd_embeddings = self.encode_texts(df["job_description"].tolist())

        logger.info("Encoding CVs...")
        cv_embeddings = self.encode_texts(df["candidate_cv"].tolist())

        # Combine embeddings
        X = np.concatenate([jd_embeddings, cv_embeddings], axis=1)

        # Add similarity features
        cosine_sim = np.sum(jd_embeddings * cv_embeddings, axis=1) / (
                np.linalg.norm(jd_embeddings, axis=1) * np.linalg.norm(cv_embeddings, axis=1)
        )

        # Add text length features
        jd_lengths = df["job_description"].str.len().values.reshape(-1, 1)
        cv_lengths = df["candidate_cv"].str.len().values.reshape(-1, 1)

        # Combine all features
        X = np.concatenate([X, cosine_sim.reshape(-1, 1), jd_lengths, cv_lengths], axis=1)

        y = df["match"].values

        return X, y

    def fit_scaler(self, X: np.ndarray) -> np.ndarray:
        """Fit scaler và transform dữ liệu"""
        X_scaled = self.scaler.fit_transform(X)
        return X_scaled

    def transform(self, X: np.ndarray) -> np.ndarray:
        """Transform dữ liệu với scaler đã fit"""
        return self.scaler.transform(X)