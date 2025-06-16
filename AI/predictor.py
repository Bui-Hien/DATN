import os
import joblib
import numpy as np
import pandas as pd
from typing import Tuple, List
import logging
from data_processor import DataProcessor

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)


class JobCVPredictor:
    def __init__(self, model_path: str, embedding_model: str = "all-MiniLM-L6-v2"):
        self.model_path = model_path
        self.model_data = None
        self.data_processor = None
        self.load_model(embedding_model)

    def load_model(self, embedding_model: str):
        """Load model, scaler, vectorizer,..."""
        if os.path.exists(self.model_path):
            self.model_data = joblib.load(self.model_path)

            # Tạo DataProcessor nhưng nạp lại scaler & các thành phần đã huấn luyện
            self.data_processor = DataProcessor(embedding_model)

            # Nạp scaler đã huấn luyện
            self.data_processor.scaler = self.model_data.get('scaler', None)

            # Nạp vectorizer nếu có
            if 'vectorizer' in self.model_data:
                self.data_processor.vectorizer = self.model_data['vectorizer']

            logger.info("Model loaded successfully")
        else:
            raise FileNotFoundError(f"Model not found at {self.model_path}")

    def predict(self, job_description: str, candidate_cv: str) -> Tuple[int, float]:
        """Dự đoán JD-CV matching và trả về nhãn + độ tin cậy"""

        # Tạo DataFrame 1 dòng giống lúc huấn luyện
        df = pd.DataFrame([{
            "job_description": job_description,
            "candidate_cv": candidate_cv,
            "match": 0  # Dummy label để hàm không lỗi
        }])

        # Tạo đặc trưng
        X, _ = self.data_processor.create_features(df)

        # Scale với scaler đã fit
        X_scaled = self.data_processor.transform(X)

        # Dự đoán
        model = self.model_data['model']
        prediction = model.predict(X_scaled)[0]
        confidence = model.predict_proba(X_scaled)[0].max()

        return prediction, confidence

    def batch_predict(self, job_descriptions: List[str], candidate_cvs: List[str]) -> List[Tuple[int, float]]:
        """Dự đoán hàng loạt JD-CV"""
        return [self.predict(jd, cv) for jd, cv in zip(job_descriptions, candidate_cvs)]
