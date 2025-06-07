import os
import joblib
import numpy as np
from typing import Tuple, List
import logging
from data_processor import DataProcessor

logger = logging.getLogger(__name__)


class JobCVPredictor:
    def __init__(self, model_path: str, embedding_model: str = "all-MiniLM-L6-v2"):
        self.model_path = model_path
        self.model_data = None
        self.data_processor = DataProcessor(embedding_model)
        self.load_model()

    def load_model(self):
        """Load trained model và scaler"""
        if os.path.exists(self.model_path):
            self.model_data = joblib.load(self.model_path)
            self.data_processor.scaler = self.model_data['scaler']
            logger.info("Model loaded successfully")
        else:
            raise FileNotFoundError(f"Model not found at {self.model_path}")

    def predict(self, job_description: str, candidate_cv: str) -> Tuple[int, float]:
        """Predict matching với confidence score"""
        # Create features
        jd_embedding = self.data_processor.encode_texts([job_description])
        cv_embedding = self.data_processor.encode_texts([candidate_cv])

        # Combine embeddings
        X = np.concatenate([jd_embedding, cv_embedding], axis=1)

        # Add similarity feature
        cosine_sim = np.sum(jd_embedding * cv_embedding) / (
                np.linalg.norm(jd_embedding) * np.linalg.norm(cv_embedding)
        )

        # Add text length features
        jd_length = len(job_description)
        cv_length = len(candidate_cv)

        # Combine all features
        X = np.concatenate([X, [[cosine_sim, jd_length, cv_length]]], axis=1)

        # Scale features
        X_scaled = self.data_processor.transform(X)

        # Predict
        prediction = self.model_data['model'].predict(X_scaled)[0]
        confidence = self.model_data['model'].predict_proba(X_scaled)[0].max()

        return prediction, confidence

    def batch_predict(self, job_descriptions: List[str], candidate_cvs: List[str]) -> List[Tuple[int, float]]:
        """Batch prediction cho nhiều cặp JD-CV"""
        results = []
        for jd, cv in zip(job_descriptions, candidate_cvs):
            pred, conf = self.predict(jd, cv)
            results.append((pred, conf))
        return results