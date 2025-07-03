import logging
import os
from typing import Tuple, List, Dict, Any

import joblib
import pandas as pd

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)


class JobCVPredictor:
    """
    Thực hiện dự đoán về sự phù hợp giữa JD và CV sử dụng mô hình đã huấn luyện.
    """

    def __init__(self, model_path: str, embedding_model: str = "all-MiniLM-L6-v2"):
        """
        Khởi tạo Preditor bằng cách tải mô hình và các thành phần tiền xử lý.
        """
        self.model_path = model_path
        self.model = None  # Để lưu trữ mô hình học máy
        self.data_processor = None  # Để lưu trữ instance của DataProcessor (chứa encoder)
        self.load_model(embedding_model)  # Tải mô hình khi khởi tạo

    def load_model(self, embedding_model: str):
        """
        Tải mô hình đã huấn luyện và DataProcessor instance từ file .pkl.
        """
        if os.path.exists(self.model_path):
            logger.info(f"Đang tải mô hình từ: {self.model_path}...")
            model_data_bundle = joblib.load(self.model_path)

            self.model = model_data_bundle['model']  # Gán mô hình đã tải
            self.data_processor = model_data_bundle['data_processor']  # Gán DataProcessor instance đã tải

        else:
            raise FileNotFoundError(f"Không tìm thấy mô hình tại {self.model_path}")

    def predict(self, job_description: str, candidate_cv: str) -> Tuple[int, float]:
        df = pd.DataFrame([{"job_description": job_description,
                            "candidate_cv": candidate_cv,
                            "match": 0  # Gán nhãn giả, vì đây là bước dự đoán
                            }])

        X, _ = self.data_processor.create_features(df)

        # Thực hiện dự đoán trên ma trận đặc trưng X
        prediction = self.model.predict(X)[0]  # Lấy nhãn dự đoán đầu tiên
        confidence = self.model.predict_proba(X)[0].max()  # Lấy độ tin cậy (xác suất cao nhất)

        logger.info(f"Dự đoán: {prediction}, Độ tin cậy: {confidence:.4f}")
        return prediction, confidence

    def batch_predict(self, job_cv_list: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """
        Thực hiện dự đoán cho một danh sách các cặp JD và CV.
        """
        results = []
        for item in job_cv_list:
            candidate_id = item.get("id")
            job_description = item.get("request")
            candidate_cv = item.get("workExperience")

            if not candidate_id or not job_description or not candidate_cv:
                logger.warning(f"Bỏ qua mục không đầy đủ dữ liệu: {item}")
                continue

            prediction, confidence = self.predict(job_description, candidate_cv)

            results.append({
                "id": candidate_id,
                "isPass": True if prediction == 1 else False,
                "confidence": confidence,
                "request": job_description,  # Giữ lại dữ liệu gốc để tiện kiểm tra
                "workExperience": candidate_cv,
            })
        return results