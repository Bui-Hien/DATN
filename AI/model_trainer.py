from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split, GridSearchCV, cross_val_score
from sklearn.metrics import classification_report, confusion_matrix, roc_auc_score
import joblib
import numpy as np
from typing import Dict, Any
import logging

logger = logging.getLogger(__name__)


class ModelTrainer:
    def __init__(self, random_state: int = 42):
        self.random_state = random_state
        self.model = None
        self.best_params = None

    def train_with_hyperparameter_tuning(self, X: np.ndarray, y: np.ndarray) -> Dict[str, Any]:
        """Train model với hyperparameter tuning"""

        # Split data
        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=0.2, random_state=self.random_state, stratify=y
        )

        # Hyperparameter grid
        param_grid = {
            'n_estimators': [100, 200, 300],
            'max_depth': [10, 20, None],
            'min_samples_split': [2, 5, 10],
            'min_samples_leaf': [1, 2, 4],
            'max_features': ['sqrt', 'log2']
        }

        # Grid search với cross-validation
        rf = RandomForestClassifier(random_state=self.random_state, n_jobs=-1)
        grid_search = GridSearchCV(
            rf, param_grid, cv=5, scoring='roc_auc', n_jobs=-1, verbose=1
        )

        logger.info("Starting hyperparameter tuning...")
        grid_search.fit(X_train, y_train)

        self.model = grid_search.best_estimator_
        self.best_params = grid_search.best_params_

        # Evaluate
        y_pred = self.model.predict(X_test)
        y_pred_proba = self.model.predict_proba(X_test)[:, 1]

        results = {
            'best_params': self.best_params,
            'classification_report': classification_report(y_test, y_pred),
            'confusion_matrix': confusion_matrix(y_test, y_pred),
            'roc_auc': roc_auc_score(y_test, y_pred_proba),
            'cv_scores': cross_val_score(self.model, X_train, y_train, cv=5, scoring='roc_auc')
        }

        logger.info(f"Best parameters: {self.best_params}")
        logger.info(f"ROC AUC: {results['roc_auc']:.4f}")
        logger.info(f"CV Score: {results['cv_scores'].mean():.4f} (+/- {results['cv_scores'].std() * 2:.4f})")

        return results

    def save_model(self, filepath: str, data_processor):
        """Save model và scaler"""
        model_data = {
            'model': self.model,
            'scaler': data_processor.scaler,
            'best_params': self.best_params
        }
        joblib.dump(model_data, filepath)
        logger.info(f"Model saved to {filepath}")