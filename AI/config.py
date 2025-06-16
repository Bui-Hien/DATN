import os

class Config:
    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    DATA_PATH = os.path.join(BASE_DIR, "data", "dataset.json")
    MODEL_PATH = os.path.join(BASE_DIR, "models", "job_cv_match_model.pkl")
    EMBEDDING_MODEL = "all-MiniLM-L6-v2"
    TEST_SIZE = 0.2
    RANDOM_STATE = 42
