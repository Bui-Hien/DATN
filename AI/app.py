from flask import Flask, request, jsonify
from flask_cors import CORS
from predictor import JobCVPredictor
from config import Config

app = Flask(__name__)

# Cho phép CORS từ tất cả origin (có thể cấu hình cụ thể hơn nếu cần)
CORS(app)

# Khởi tạo predictor
predictor = JobCVPredictor(Config.MODEL_PATH, Config.EMBEDDING_MODEL)

@app.route("/api/pre-screened", methods=["POST"])
def predict_batch():
    try:
        data = request.get_json()

        if not isinstance(data, list):
            return jsonify({"error": "Payload phải là một danh sách object"}), 400

        results = []

        for item in data:
            candidate_id = item.get("id")
            job_description = item.get("request")
            candidate_cv = item.get("workExperience")

            if not candidate_id or not job_description or not candidate_cv:
                continue

            prediction, confidence = predictor.predict(job_description, candidate_cv)

            results.append({
                "id": candidate_id,
                "isPass": True if prediction == 1 else False,
                "confidence": confidence,
                "request": job_description,
                "workExperience": candidate_cv,
            })

        return jsonify(results), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    # Chạy trên cổng 5001 và cho phép truy cập từ bên ngoài (Java có thể gọi tới)
    app.run(host="0.0.0.0", port=5000, debug=True)
