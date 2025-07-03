from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.metrics import classification_report, confusion_matrix, roc_auc_score
import joblib
import numpy as np
from typing import Dict, Any, Optional
import logging

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)


class ModelTrainer:
    def __init__(self, random_state: int = 42):
        self.random_state = random_state
        self.model: Optional[RandomForestClassifier] = None
        self.best_params_: Optional[Dict[str, Any]] = None

    def train_model(self, X: np.ndarray, y: np.ndarray) -> Dict[str, Any]:
        """
        Main training method that calls train_model_with_grid_search
        """
        logger.info("🚀 Bắt đầu quá trình huấn luyện mô hình...")
        return self.train_model_with_grid_search(X, y)

    def train_model_with_grid_search(
            self, X: np.ndarray, y: np.ndarray,
            param_grid: Optional[Dict[str, Any]] = None,
            cv: int = 5,
            scoring: str = "roc_auc"
    ) -> Dict[str, Any]:

        logger.info("🔍 Bắt đầu tìm kiếm siêu tham số với GridSearchCV...")

        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=0.2, random_state=self.random_state, stratify=y
        )

        base_model = RandomForestClassifier(random_state=self.random_state, n_jobs=-1)

        # Nếu không truyền thì dùng grid mặc định
        if param_grid is None:
            param_grid = {
                "n_estimators": [100, 200],  # Số lượng cây trong rừng: 100 đủ nhanh, 200 ổn định hơn
                "max_depth": [10, 20, None],  # 10 & 20 giúp kiểm soát overfitting; None cho phép học sâu nếu cần
                "min_samples_split": [2, 5],  # 2 là mặc định; 5 giảm độ phức tạp cây, tránh chia nhỏ quá mức
                "min_samples_leaf": [1, 3]  # 1 = học chi tiết; 3 = tổng quát hóa tốt hơn, chống nhiễu
            }

        grid_search = GridSearchCV(
            estimator=base_model,
            param_grid=param_grid,
            scoring=scoring,
            cv=cv,
            n_jobs=-1,
            verbose=1
        )

        grid_search.fit(X_train, y_train)
        self.model = grid_search.best_estimator_
        self.best_params_ = grid_search.best_params_

        logger.info(f"✅ Đã tìm được tham số tốt nhất: {self.best_params_}")

        # Đánh giá mô hình tốt nhất trên tập test
        y_pred = self.model.predict(X_test)
        y_pred_proba = self.model.predict_proba(X_test)[:, 1]

        roc_auc = roc_auc_score(y_test, y_pred_proba)
        confusion = confusion_matrix(y_test, y_pred)
        classification_rep = classification_report(y_test, y_pred, output_dict=True)

        results = {
            "best_params": self.best_params_,
            "roc_auc_score": roc_auc,
            "confusion_matrix": confusion.tolist(),
            "classification_report": classification_rep
        }

        logger.info(f"🎯 ROC AUC Score: {roc_auc:.4f}")
        logger.info(f"📊 Confusion Matrix:\n{confusion}")
        logger.info("📈 Classification Report:")
        for class_name, metrics in classification_rep.items():
            if isinstance(metrics, dict):
                logger.info(f"  {class_name}: precision={metrics.get('precision', 'N/A'):.4f}, "
                           f"recall={metrics.get('recall', 'N/A'):.4f}, "
                           f"f1-score={metrics.get('f1-score', 'N/A'):.4f}")

        return results

    def save_model(self, model_path: str, data_processor_instance: Optional[Any] = None) -> None:
        if not self.model:
            raise ValueError("Model not trained yet.")

        obj = {'model': self.model}
        if data_processor_instance is not None:
            obj['data_processor'] = data_processor_instance

        joblib.dump(obj, model_path)
        logger.info(f"💾 Model saved to: {model_path}")