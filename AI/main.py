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

    jd = """Là sinh viên đã/sắp tốt nghiệp chuyên ngành Công nghệ thông tin, Toán tin, Khoa học máy tính, Kỹ thuật phần mềm,...
        Nắm vững kiến thức cơ bản trong các ngôn ngữ/nền tảng sau
        (BE) Java/NodeJS/Python/Golang
        (FE) HTML, CSS, JS
        Ưu tiên ứng viên có khả năng giao tiếp tiếng Anh tốt (tương đương IELTS 6.0 trở lên, TOEIC 650 trở lên); và GPA: 7/10 hoặc 3/4
        Có thể tham gia đào tạo/làm việc Full-time từ thứ 2 - thứ 6
        Ham học hỏi, nhiệt huyết"""

    while True:
        cv = input("\nNhập CV (hoặc gõ 'exit' để thoát): ")
        if cv.lower() == "exit":
            print("Đã thoát.")
            break

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
# python main.py train
# python main.py