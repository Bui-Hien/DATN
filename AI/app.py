import logging

from flask import Flask, request, jsonify
from flask_cors import CORS

from config import Config
from predictor import JobCVPredictor

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

app = Flask(__name__)
CORS(app)

predictor = JobCVPredictor(Config.MODEL_PATH, Config.EMBEDDING_MODEL)


@app.route("/api/pre-screened", methods=["POST"])
def predict_batch():
    try:
        data = request.get_json()  # Lấy dữ liệu JSON từ request

        if not isinstance(data, list):
            logger.error("Dữ liệu nhận được không phải là một danh sách.")
            return jsonify({"error": "Payload phải là một danh sách object"}), 400

        results = []  # Danh sách để lưu trữ kết quả dự đoán

        for item in data:
            candidate_id = item.get("id")
            job_description = item.get("request")
            candidate_cv = item.get("workExperience")

            # Bỏ qua nếu dữ liệu không đầy đủ cho một mục
            if not candidate_id or not job_description or not candidate_cv:
                logger.warning(f"Bỏ qua mục có dữ liệu không đầy đủ: {item.get('id', 'Không có ID')}")
                continue

            # Gọi hàm dự đoán từ predictor
            prediction, confidence = predictor.predict(job_description, candidate_cv)

            # Thêm kết quả vào danh sách
            results.append({
                "id": candidate_id,
                "isPass": True if prediction == 1 else False,  # True nếu dự đoán là khớp (1), ngược lại là False
                "confidence": confidence,  # Độ tin cậy của dự đoán
                "request": job_description,  # Bao gồm lại JD để tiện kiểm tra
                "workExperience": candidate_cv,  # Bao gồm lại CV để tiện kiểm tra
            })

        return jsonify(results), 200  # Trả về kết quả dưới dạng JSON với mã trạng thái 200 (OK)

    except Exception as e:
        logger.exception("Lỗi xảy ra khi xử lý yêu cầu dự đoán hàng loạt.")
        return jsonify({"error": str(e), "message": "Đã xảy ra lỗi trong quá trình xử lý yêu cầu."}), 500


if __name__ == "__main__":
    logger.info("Chạy ứng dụng Flask API...")
    app.run(debug=True, host='0.0.0.0', port=5000)