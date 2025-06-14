from flask import Flask, request, jsonify
from predictor import JobCVPredictor
from config import Config

# Khởi tạo Flask app
app = Flask(__name__)

# Khởi tạo predictor (chỉ load model một lần khi app start)
predictor = JobCVPredictor(Config.MODEL_PATH, Config.EMBEDDING_MODEL)

@app.route("/api/predict", methods=["POST"])
def predict():
    data = request.get_json()

    jd = data.get("job_description")
    cv = data.get("candidate_cv")

    if not jd or not cv:
        return jsonify({"error": "Thiếu job_description hoặc candidate_cv"}), 400

    # Dự đoán
    prediction, confidence = predictor.predict(jd, cv)

    return jsonify({
        "match": int(prediction),  # ép kiểu từ numpy.int64 sang int
    })

if __name__ == "__main__":
    app.run(debug=True)
