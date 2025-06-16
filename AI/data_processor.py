import json
import pandas as pd
import numpy as np
from sentence_transformers import SentenceTransformer
from sklearn.preprocessing import StandardScaler
from sklearn.feature_extraction.text import TfidfVectorizer
from typing import Tuple, List, Dict
import logging
import re
import warnings

warnings.filterwarnings("ignore")

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class DataProcessor:
    def __init__(self, model_name: str = "paraphrase-multilingual-MiniLM-L12-v2"):
        """
        Khởi tạo với model multilingual tốt hơn cho tiếng Việt
        """
        self.encoder = SentenceTransformer(model_name)
        self.scaler = StandardScaler()

        # Thêm TF-IDF vectorizer cho features bổ sung
        self.vectorizer = TfidfVectorizer(
            max_features=500,
            ngram_range=(1, 2),
            min_df=2,
            max_df=0.95
        )

        # Từ khóa CNTT tiếng Việt và tiếng Anh
        self.it_keywords = self._get_it_keywords()

    def _get_it_keywords(self) -> List[str]:
        """Danh sách từ khóa CNTT phổ biến"""
        return [
            # Programming languages
            'python', 'java', 'javascript', 'c++', 'c#', 'php', 'golang', 'kotlin', 'swift',
            'typescript', 'ruby', 'scala', 'dart', 'lập trình',

            # Frameworks & Libraries
            'react', 'angular', 'vue', 'django', 'flask', 'spring', 'laravel', 'nodejs',
            'express', 'bootstrap', 'jquery', 'flutter', 'react native',

            # Databases
            'mysql', 'postgresql', 'mongodb', 'redis', 'oracle', 'sql server', 'sqlite',
            'database', 'sql', 'nosql', 'cơ sở dữ liệu',

            # Cloud & DevOps
            'aws', 'azure', 'google cloud', 'docker', 'kubernetes', 'jenkins', 'git',
            'ci/cd', 'devops', 'microservices', 'api', 'rest api',

            # AI & Data Science
            'machine learning', 'deep learning', 'ai', 'data science', 'big data',
            'tensorflow', 'pytorch', 'pandas', 'numpy', 'trí tuệ nhân tạo', 'học máy',

            # Web & Mobile
            'html', 'css', 'web development', 'mobile development', 'android', 'ios',
            'frontend', 'backend', 'fullstack', 'phát triển web', 'ứng dụng di động',

            # Security & Network
            'security', 'cybersecurity', 'network', 'firewall', 'bảo mật', 'an ninh mạng',

            # Tools & Technologies
            'linux', 'windows', 'macos', 'visual studio', 'intellij', 'eclipse',
            'postman', 'jira', 'confluence', 'slack',

            # Experience keywords - Tiếng Việt
            'kinh nghiệm', 'năm kinh nghiệm', 'năm làm việc', 'năm công tác', 'năm phát triển',
            'năm thiết kế', 'năm xây dựng', 'năm triển khai', 'năm vận hành', 'năm quản lý',
            'năm thực hiện', 'năm tham gia', 'năm nghiên cứu', 'năm học tập',
            'thâm niên', 'senior', 'junior', 'mid-level', 'fresher', 'intern',
            'trưởng nhóm', 'team lead', 'project manager', 'tech lead', 'solution architect',

            # Experience keywords - Tiếng Anh
            'years of experience', 'years experience', 'year experience', 'work experience',
            'professional experience', 'industry experience', 'hands-on experience',
            'proven experience', 'extensive experience', 'solid experience',
            'years in', 'years with', 'years developing', 'years working',
            'years building', 'years designing', 'years implementing',

            # Seniority levels
            'lead developer', 'senior developer', 'principal engineer', 'staff engineer',
            'senior engineer', 'junior developer', 'entry level', 'associate',
            'specialist', 'expert', 'consultant', 'architect', 'manager',

            # Project scale keywords
            'dự án lớn', 'dự án nhỏ', 'dự án vừa', 'quy mô lớn', 'quy mô nhỏ',
            'large scale', 'small scale', 'enterprise', 'startup', 'corporation',
            'team size', 'project duration', 'budget', 'timeline'
        ]

    def load_data(self, file_path: str) -> pd.DataFrame:
        """Load và validate dữ liệu với xử lý encoding tốt hơn"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                data = json.load(f)

            df = pd.DataFrame(data)

            # Validate required columns
            required_cols = ['job_description', 'candidate_cv', 'match']
            missing_cols = [col for col in required_cols if col not in df.columns]
            if missing_cols:
                raise ValueError(f"Missing columns: {missing_cols}")

            # Clean data với xử lý tiếng Việt tốt hơn
            df = df.dropna(subset=required_cols)
            df['job_description'] = df['job_description'].astype(str).apply(self._clean_text)
            df['candidate_cv'] = df['candidate_cv'].astype(str).apply(self._clean_text)

            logger.info(f"Loaded {len(df)} records")
            return df

        except Exception as e:
            logger.error(f"Error loading data: {e}")
            raise

    def _clean_text(self, text: str) -> str:
        """Làm sạch văn bản cho tiếng Việt"""
        # Xử lý encoding issues
        text = text.encode('utf-8').decode('utf-8')

        # Loại bỏ HTML tags nếu có
        text = re.sub(r'<[^>]+>', ' ', text)

        # Chuẩn hóa khoảng trắng
        text = ' '.join(text.split())

        # Loại bỏ ký tự đặc biệt không cần thiết nhưng giữ dấu tiếng Việt
        text = re.sub(r'[^\w\s\-\.\,\;\:\!\?\(\)]', ' ', text)

        return text.strip()

    def preprocess_text(self, texts: List[str]) -> List[str]:
        """Tiền xử lý văn bản tối ưu cho tiếng Việt"""
        processed = []
        for text in texts:
            # Làm sạch text
            clean_text = self._clean_text(text)

            # Normalize case - giữ mixed case cho better embedding
            # Chỉ lowercase cho việc tìm kiếm keywords
            processed.append(clean_text)
        return processed

    def _extract_keyword_features(self, texts: List[str]) -> np.ndarray:
        """Trích xuất features dựa trên từ khóa CNTT và kinh nghiệm"""
        features = []

        for text in texts:
            text_lower = text.lower()

            # Đếm số lượng keywords tổng
            keyword_count = sum(1 for keyword in self.it_keywords if keyword in text_lower)

            # Tỷ lệ keywords/total words
            word_count = len(text.split())
            keyword_ratio = keyword_count / max(word_count, 1)

            # Số lượng keywords unique
            unique_keywords = len(set(keyword for keyword in self.it_keywords if keyword in text_lower))

            # Trích xuất số năm kinh nghiệm
            years_experience = self._extract_years_experience(text)

            # Đếm từ khóa về kinh nghiệm
            experience_keywords = [
                'kinh nghiệm', 'năm kinh nghiệm', 'years of experience', 'years experience',
                'senior', 'junior', 'lead', 'manager', 'architect', 'thâm niên',
                'trưởng nhóm', 'team lead', 'tech lead', 'principal', 'staff'
            ]
            experience_keyword_count = sum(1 for exp_keyword in experience_keywords if exp_keyword in text_lower)

            # Đếm từ khóa technical (loại trừ experience keywords)
            tech_keywords = [kw for kw in self.it_keywords if not any(exp in kw for exp in
                                                                      ['năm', 'year', 'senior', 'junior', 'lead',
                                                                       'manager', 'architect', 'kinh nghiệm',
                                                                       'thâm niên'])]
            tech_keyword_count = sum(1 for tech_keyword in tech_keywords if tech_keyword in text_lower)

            features.append([
                keyword_count,  # Tổng keywords
                keyword_ratio,  # Tỷ lệ keywords
                unique_keywords,  # Keywords unique
                years_experience,  # Số năm kinh nghiệm
                experience_keyword_count,  # Từ khóa về kinh nghiệm
                tech_keyword_count  # Từ khóa technical
            ])

        return np.array(features)

    def _extract_years_experience(self, text: str) -> float:
        """Trích xuất số năm kinh nghiệm từ văn bản"""
        text_lower = text.lower()
        years = 0.0

        # Patterns tiếng Việt
        vi_patterns = [
            r'(\d+)\s*năm\s*kinh\s*nghiệm',
            r'(\d+)\s*năm\s*làm\s*việc',
            r'(\d+)\s*năm\s*công\s*tác',
            r'(\d+)\s*năm\s*phát\s*triển',
            r'(\d+)\s*năm\s*trong',
            r'kinh\s*nghiệm\s*(\d+)\s*năm',
            r'hơn\s*(\d+)\s*năm',
            r'trên\s*(\d+)\s*năm'
        ]

        # Patterns tiếng Anh
        en_patterns = [
            r'(\d+)\s*years?\s*of\s*experience',
            r'(\d+)\s*years?\s*experience',
            r'(\d+)\+?\s*years?\s*in',
            r'(\d+)\+?\s*years?\s*with',
            r'(\d+)\+?\s*years?\s*working',
            r'(\d+)\+?\s*years?\s*developing',
            r'over\s*(\d+)\s*years?',
            r'more\s*than\s*(\d+)\s*years?',
            r'(\d+)\s*to\s*\d+\s*years?',
            r'(\d+)-\d+\s*years?'
        ]

        all_patterns = vi_patterns + en_patterns

        # Tìm tất cả matches
        found_years = []
        for pattern in all_patterns:
            matches = re.findall(pattern, text_lower)
            for match in matches:
                try:
                    year_value = float(match)
                    if 0 <= year_value <= 50:  # Reasonable range
                        found_years.append(year_value)
                except ValueError:
                    continue

        # Trả về giá trị lớn nhất (thường là kinh nghiệm tổng)
        if found_years:
            years = max(found_years)

        # Infer từ seniority levels nếu không tìm thấy số cụ thể
        if years == 0:
            if any(word in text_lower for word in ['fresher', 'intern', 'entry level', 'thực tập']):
                years = 0.5
            elif any(word in text_lower for word in ['junior', 'jr.']):
                years = 2.0
            elif any(word in text_lower for word in ['senior', 'sr.', 'lead', 'trưởng nhóm']):
                years = 5.0
            elif any(word in text_lower for word in ['principal', 'architect', 'manager', 'giám đốc']):
                years = 8.0

        return years

    def encode_texts(self, texts: List[str], batch_size: int = 32) -> np.ndarray:
        """Encode văn bản với xử lý batch tối ưu"""
        processed_texts = self.preprocess_text(texts)

        # Giảm batch size nếu texts quá dài
        avg_length = np.mean([len(text.split()) for text in processed_texts])
        if avg_length > 200:
            batch_size = max(8, batch_size // 2)

        embeddings = self.encoder.encode(
            processed_texts,
            batch_size=batch_size,
            show_progress_bar=True,
            convert_to_numpy=True,
            normalize_embeddings=True  # Normalize để tính cosine similarity tốt hơn
        )
        return embeddings

    def create_features(self, df: pd.DataFrame) -> Tuple[np.ndarray, np.ndarray]:
        """Tạo features nâng cao từ job description và CV"""
        logger.info("Encoding job descriptions...")
        jd_embeddings = self.encode_texts(df["job_description"].tolist())

        logger.info("Encoding CVs...")
        cv_embeddings = self.encode_texts(df["candidate_cv"].tolist())

        logger.info("Extracting keyword features...")
        # Keyword features
        jd_keyword_features = self._extract_keyword_features(df["job_description"].tolist())
        cv_keyword_features = self._extract_keyword_features(df["candidate_cv"].tolist())

        logger.info("Computing similarity features...")
        # Cosine similarity (đã normalize embeddings)
        cosine_sim = np.sum(jd_embeddings * cv_embeddings, axis=1)

        # Euclidean distance
        euclidean_dist = np.linalg.norm(jd_embeddings - cv_embeddings, axis=1)

        # Keyword overlap score
        keyword_overlap = np.sum(jd_keyword_features * cv_keyword_features, axis=1)

        # Text length features
        jd_lengths = df["job_description"].str.len().values.reshape(-1, 1)
        cv_lengths = df["candidate_cv"].str.len().values.reshape(-1, 1)

        # Length ratio
        length_ratio = (cv_lengths / np.maximum(jd_lengths, 1)).reshape(-1, 1)

        # Word count features
        jd_word_counts = df["job_description"].str.split().str.len().values.reshape(-1, 1)
        cv_word_counts = df["candidate_cv"].str.split().str.len().values.reshape(-1, 1)

        # Combine all features
        X = np.concatenate([
            jd_embeddings,  # JD embeddings
            cv_embeddings,  # CV embeddings
            jd_keyword_features,  # JD keyword features
            cv_keyword_features,  # CV keyword features
            cosine_sim.reshape(-1, 1),  # Cosine similarity
            euclidean_dist.reshape(-1, 1),  # Euclidean distance
            keyword_overlap.reshape(-1, 1),  # Keyword overlap
            jd_lengths,  # JD length
            cv_lengths,  # CV length
            length_ratio,  # Length ratio
            jd_word_counts,  # JD word count
            cv_word_counts  # CV word count
        ], axis=1)

        y = df["match"].values

        logger.info(f"Created feature matrix with shape: {X.shape}")
        return X, y

    def fit_scaler(self, X: np.ndarray) -> np.ndarray:
        """Fit scaler và transform dữ liệu với robust scaling"""
        # Sử dụng robust scaling để handle outliers tốt hơn
        from sklearn.preprocessing import RobustScaler
        if not hasattr(self, 'robust_scaler'):
            self.robust_scaler = RobustScaler()

        X_scaled = self.robust_scaler.fit_transform(X)
        # Vẫn giữ standard scaler cho compatibility
        self.scaler.fit(X)
        return X_scaled

    def transform(self, X: np.ndarray) -> np.ndarray:
        """Transform dữ liệu với scaler đã fit"""
        if hasattr(self, 'robust_scaler'):
            return self.robust_scaler.transform(X)
        return self.scaler.transform(X)
