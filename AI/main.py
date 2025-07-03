import logging
import os

import pandas as pd

from config import Config
from data_processor import DataProcessor
from model_trainer import ModelTrainer
from predictor import JobCVPredictor

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


def train_model():
    logger.info("--- Bắt đầu quá trình huấn luyện mô hình ---")

    # Khởi tạo các thành phần: DataProcessor để tiền xử lý dữ liệu và ModelTrainer để huấn luyện
    data_processor = DataProcessor(Config.EMBEDDING_MODEL)
    trainer = ModelTrainer(Config.RANDOM_STATE)

    # Tải và xử lý dữ liệu
    df = data_processor.load_data(Config.DATA_PATH)
    X, y = data_processor.create_features(df)

    # Huấn luyện mô hình
    results = trainer.train_model(X, y)

    os.makedirs(os.path.dirname(Config.MODEL_PATH), exist_ok=True)
    trainer.save_model(Config.MODEL_PATH, data_processor)

    logger.info("--- Quá trình huấn luyện mô hình hoàn tất ---")
    return results


def predict_single():
    """
    Hàm để kiểm thử dự đoán cho một cặp JD và CV đơn lẻ thông qua console.
    """
    logger.info("--- Bắt đầu chế độ dự đoán đơn lẻ ---")
    # Khởi tạo predictor, nó sẽ tự động tải mô hình đã lưu
    predictor = JobCVPredictor(Config.MODEL_PATH, Config.EMBEDDING_MODEL)

    # Ví dụ về một mô tả công việc (Job Description - JD)
    jd_example = """Là sinh viên đã/sắp tốt nghiệp chuyên ngành Công nghệ thông tin, Toán tin, Khoa học máy tính, Kỹ thuật phần mềm,...
        Nắm vững kiến thức cơ bản trong các ngôn ngữ/nền tảng sau, (BE) Java/NodeJS/Python/Golang, (FE) HTML, CSS, JS
        Ưu tiên ứng viên có khả năng giao tiếp tiếng Anh tốt (tương đương IELTS 6.0 trở lên, TOEIC 650 trở lên); và GPA: 7/10 hoặc 3/4
        Có thể tham gia đào tạo/làm việc Full-time từ thứ 2 - thứ 6
        Ham học hỏi, nhiệt huyết"""

    while True:
        # Cho phép người dùng nhập CV từ bàn phím
        cv_input = input("\nNhập CV của ứng viên (hoặc gõ 'exit' để thoát): ")
        if cv_input.lower() == "exit":
            logger.info("Đã thoát chế độ dự đoán đơn lẻ.")
            break

        # Thực hiện dự đoán
        prediction, confidence = predictor.predict(jd_example, cv_input)

        # Hiển thị kết quả
        match_status = 'Khớp' if prediction == 1 else 'Không khớp'
        logger.info(f"Kết quả dự đoán: {match_status} (Độ tin cậy: {confidence:.4f})")


if __name__ == "__main__":
    # Đây là điểm khởi chạy chính của script.
    # Bạn có thể lựa chọn chạy hàm huấn luyện hoặc hàm dự đoán.
    # Để huấn luyện mô hình, bỏ comment dòng train_model()
    # Để kiểm thử dự đoán, bỏ comment dòng predict_single()

    # Nếu muốn huấn luyện mô hình:
    train_model()

    # Nếu muốn kiểm thử dự đoán đơn lẻ:
    # predict_single()
