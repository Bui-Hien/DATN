import os
import sys
from config import Config
from data_processor import DataProcessor
from model_trainer import ModelTrainer
from predictor import JobCVPredictor


def train_model():
    """Train model từ đầu"""
    # Initialize components
    data_processor = DataProcessor(Config.EMBEDDING_MODEL)
    trainer = ModelTrainer(Config.RANDOM_STATE)

    # Load và process data
    df = data_processor.load_data(Config.DATA_PATH)
    X, y = data_processor.create_features(df)
    X_scaled = data_processor.fit_scaler(X)

    # Train model
    results = trainer.train_with_hyperparameter_tuning(X_scaled, y)

    # Save model
    os.makedirs(os.path.dirname(Config.MODEL_PATH), exist_ok=True)
    trainer.save_model(Config.MODEL_PATH, data_processor)

    return results


def predict_single():
    """Predict cho single input"""
    predictor = JobCVPredictor(Config.MODEL_PATH, Config.EMBEDDING_MODEL)

    jd = """Bằng cử nhân về khoa học máy tính, Kỹ thuật phần mềm hoặc lĩnh vực liên quan. 
    Hơn 3 năm kinh nghiệm phát triển back-end Java. Trải nghiệm thực tế vững chắc với Spring Boot và lõi Java. 
    Thành thạo viết và tối ưu hóa các truy vấn SQL (MySQL, PostgreSQL hoặc tương tự). 
    Có kinh nghiệm với Eclipse RCP hoặc sẵn sàng học hỏi và làm việc trong khuôn khổ dựa trên máy tính để bàn. 
    Làm quen với kiến trúc RESTful API và microservice. Kỹ năng giải quyết vấn đề mạnh mẽ và khả năng gỡ lỗi. 
    Kỹ năng giao tiếp tốt bằng tiếng Anh (đọc và viết)."""

    cv = input("Nhập CV: ")

    prediction, confidence = predictor.predict(jd, cv)

    print(f"Kết quả khớp: {'Khớp' if prediction == 1 else 'Không khớp'}")
    print(f"Độ tin cậy: {confidence:.2%}")


if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "train":
        print("Training model...")
        results = train_model()
        print("Training completed!")
        print(results['classification_report'])
    else:
        predict_single()