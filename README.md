
# Dự Án Tốt Nghiệp - Hệ Thống Quản Lý Nhân Sự & Tuyển Dụng Tích Hợp AI

---

## Cấu Trúc Thư Mục

```
DATN/
├── AI/                    # Python Flask AI Service
│   ├── app.py             # Flask entry point
│   ├── main.py
│   ├── config.py
│   ├── data_processor.py
│   ├── model_trainer.py
│   ├── predictor.py
│   ├── requirements.txt
│   ├── logs/
│   ├── data/
│   └── models/
│
├── backend/               # Spring Boot 3 Backend (Java 17)
│   ├── pom.xml
│   ├── src/
│   └── uploads/           # Lưu trữ file tải lên
│
├── frontend/              # ReactJS + MUI Frontend (Node.js 22)
│   ├── package.json
│   ├── tailwind.config.js
│   ├── src/
│   ├── public/
│   └── README.md
```

---

## Môi Trường & Phiên Bản

| Thành phần    | Phiên bản               |
|---------------|--------------------------|
| Java          | 17                       |
| Spring Boot   | 3.4.2                    |
| Node.js       | 22.x                     |
| npm           | 10.x                     |
| Python        | 3.10+                    |
| pip           | Python package manager   |
| Maven         | 3.8+                     |
| ReactJS       | 19.x                     |

---

## Các Bước Cài Đặt & Chạy Dự Án

### 1. Backend - Spring Boot

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Mặc định API chạy tại: `http://localhost:8071`

---

### 2. Frontend - ReactJS

```bash
cd frontend
npm install
npm start
```

Ứng dụng chạy tại: `http://localhost:3000`


### 3. AI Service - Flask (Python)

```bash
cd AI
pip install -r requirements.txt
python app.py
```

Chạy tại: `http://localhost:5000`

---
