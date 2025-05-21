package com.buihien.datn;

import com.buihien.datn.util.DateTimeUtil;

import java.util.Date;

public class DatnConstants {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";
    public static final String ROLE_HR = "ROLE_HR"; // Quản lý nhân sự
    public static final String ROLE_ACCOUNTANT = "ROLE_ACCOUNTANT"; // Kế toán
    public static final String ROLE_PROJECT_MANAGER = "ROLE_PROJECT_MANAGER"; // Quản lý dự án
    public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE"; // Nhân viên
    public static final String ROLE_AUDITOR = "ROLE_AUDITOR"; // Kiểm toán
    public static final String ROLE_SUPPORT = "ROLE_SUPPORT"; // Hỗ trợ kỹ thuật
    public static final String ROLE_IT = "ROLE_IT"; // Quản trị hệ thống

    public enum Platform {

        WEB(1, "Website"),
        IOS(2, "Ios"),
        ANDROID(3, "Android");

        private final Integer value;
        private final String name;

        Platform(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (Platform item : Platform.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return null;
        }
    }

    public enum Gender {
        MALE(1, "Nam"),
        FEMALE(2, "Nữ"),
        OTHER(3, "Khác");
        private final Integer value;
        private final String name;

        Gender(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (Gender item : Gender.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return null;
        }
    }

    public enum MaritalStatus {
        SINGLE(1, "Độc thân"),
        MARRIED(2, "Đã kết hôn"),
        DIVORCED(3, "Ly hôn"),
        WIDOWED(4, "Góa vợ/chồng"),
        SEPARATED(5, "Ly thân");

        private final Integer value;
        private final String name;

        MaritalStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (MaritalStatus item : MaritalStatus.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return null;
        }
    }

    public enum EducationLevel {
        PHD(1, "Tiến sĩ"),
        MASTER(2, "Thạc sĩ"),
        BACHELOR(3, "Cử nhân"),
        ENGINEER(4, "Kỹ sư"),
        COLLEGE(5, "Cao đẳng"),
        INTERMEDIATE(6, "Trung cấp"),
        HIGH_SCHOOL(7, "Tốt nghiệp THPT"),
        OTHER(99, "Khác");

        private final Integer value;
        private final String name;

        EducationLevel(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (EducationLevel level : EducationLevel.values()) {
                if (level.getValue().equals(value)) {
                    return level.getName();
                }
            }
            return null;
        }
    }

    public enum StaffPhase {
        INTERN(1, "Học việc (HV)"), // Học việc
        PROBATION(2, "Thử việc (TV)"), // Thử việc
        OFFICIAL(3, "Chính thức (CT)"); // Chính thức

        private final Integer value;
        private final String name;

        StaffPhase(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (StaffPhase level : StaffPhase.values()) {
                if (level.getValue().equals(value)) {
                    return level.getName();
                }
            }
            return null;
        }
    }

    public enum EmployeeStatus {
        WORKING(1, "Đang làm việc"),
        RESIGNED(2, "Đã nghỉ việc"),
        RETIRED(3, "Đã nghỉ hưu"),
        SUSPENDED(4, "Tạm ngưng"),
        TERMINATED(5, "Chấm dứt hợp đồng");

        private final Integer value;
        private final String name;

        EmployeeStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (EmployeeStatus status : EmployeeStatus.values()) {
                if (status.getValue().equals(value)) {
                    return status.getName();
                }
            }
            return null;
        }
    }

    public enum StaffLabourAgreementStatus {
        UNSIGNED(1, "Hợp đồng chưa được ký"),
        SIGNED(2, "Hợp đồng đã được ký"),
        TERMINATED(3, "Đã chấm dứt"),
        EXPIRED(4, "Đã hết hạn");

        private final Integer value;
        private final String name;

        StaffLabourAgreementStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (StaffLabourAgreementStatus status : StaffLabourAgreementStatus.values()) {
                if (status.getValue().equals(value)) {
                    return status.getName();
                }
            }
            return null;
        }
    }

    public enum StaffSocialInsurancePaidStatus {
        PAID(1, "Đã đóng"),//
        UNPAID(2, "Chưa đóng");

        private final Integer value;
        private final String name;

        StaffSocialInsurancePaidStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (StaffSocialInsurancePaidStatus status : StaffSocialInsurancePaidStatus.values()) {
                if (status.getValue().equals(value)) {
                    return status.getName();
                }
            }
            return null;
        }

    }

    public enum ShiftWorkType {

        MORNING(1, "Ca sáng", DateTimeUtil.getTime(8, 30), DateTimeUtil.getTime(12, 0)),
        AFTERNOON(2, "Ca chiều", DateTimeUtil.getTime(13, 30), DateTimeUtil.getTime(17, 30)),
        FULL_DAY(3, "Ca nguyên ngày", DateTimeUtil.getTime(8, 30), DateTimeUtil.getTime(17, 30));

        private final Integer value;
        private final String name;
        private final Date startTime;
        private final Date endTime;

        ShiftWorkType(Integer value, String name, Date startTime, Date endTime) {
            this.value = value;
            this.name = name;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Date getStartTime() {
            return startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (ShiftWorkType type : ShiftWorkType.values()) {
                if (type.getValue().equals(value)) {
                    return type.getName();
                }
            }
            return null;
        }

        public static ShiftWorkType getShiftWorkType(Integer typeValue) {
            for (ShiftWorkType type : ShiftWorkType.values()) {
                if (type.getValue().equals(typeValue)) {
                    return type;
                }
            }
            throw null;
        }

    }

    public enum RecruitmentRequestStatus {
        CREATED(0, "Chưa duyệt"),
        APPROVED(1, "Đã duyệt"),
        REJECTED(2, "Từ chối"),
        COMPLETED(3, "Đã hoàn thành");

        private final Integer value;
        private final String name;

        RecruitmentRequestStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (RecruitmentRequestStatus status : RecruitmentRequestStatus.values()) {
                if (status.getValue().equals(value)) {
                    return status.getName();
                }
            }
            return null;
        }
    }

    public enum CandidateStatus {
        CREATED(1, "Khởi tạo"),
        PRE_SCREENED(2, "Đã qua sơ lọc"),
        INTERVIEWED(3, "Qua phỏng vấn"),
        HIRED(4, "Đã nhận việc"),
        DECLINED(5, "Từ chối nhận việc");

        private final Integer value;
        private final String name;

        CandidateStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (CandidateStatus status : CandidateStatus.values()) {
                if (status.getValue().equals(value)) {
                    return status.getName();
                }
            }
            return null;
        }
    }

    public enum ShiftWorkStatus {
        NOT_STARTED(1, "Chưa đến ca"),
        CHECKED_IN(2, "Đã check in"),
        INSUFFICIENT_HOURS(3, "Đi làm thiếu giờ"),
        WORKED_FULL_HOURS(4, "Đi làm đủ giờ"),
        ABSENT(5, "Nghỉ");

        private final Integer value;
        private final String name;

        ShiftWorkStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (ShiftWorkStatus status : ShiftWorkStatus.values()) {
                if (status.getValue().equals(value)) {
                    return status.getName();
                }
            }
            return null;
        }

    }

    public enum TokenType {
        ACCESS_TOKEN(1, "Access Token"),
        REFRESH_TOKEN(2, "Refresh Token"),
        RESET_TOKEN(3, "Reset Token");
        private final Integer value;
        private final String name;

        TokenType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (TokenType item : TokenType.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return null;
        }
    }

    public enum LogMessageQueueStatus {
        SUCCESS(1, "Thành công"),
        FAILED(2, "Thất bại");

        private final Integer value;
        private final String name;

        LogMessageQueueStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (TokenType item : TokenType.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return null;
        }
    }

    public enum LogMessageQueueTypes {
        FORGOT_PASSWORD(1, "Quên mật khẩu");

        private final Integer value;
        private final String name;

        LogMessageQueueTypes(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (TokenType item : TokenType.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return null;
        }
    }

    public enum AdministrativeUnitLevel {
        PROVINCE(1, "Tỉnh"),        // Cấp Tỉnh
        DISTRICT(2, "Huyện"),       // Cấp Huyện
        WARD(3, "Xã");              // Cấp Xã

        private final Integer value;
        private final String name;

        AdministrativeUnitLevel(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (AdministrativeUnitLevel level : AdministrativeUnitLevel.values()) {
                if (level.getValue().equals(value)) {
                    return level.getName();
                }
            }
            return null;
        }
    }

    public enum ContractType {
        PROBATION(1, "Thử việc"),
        OFFICIAL(2, "Chính thức"),
        SEASONAL(3, "Thời vụ"),
        FIXED_TERM(4, "Xác định thời hạn"),
        UNLIMITED_TERM(5, "Không xác định thời hạn");

        private final Integer value;
        private final String name;

        ContractType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (ContractType type : ContractType.values()) {
                if (type.getValue().equals(value)) {
                    return type.getName();
                }
            }
            return null;
        }
    }

    public enum SalaryItemType {
        VALUE(1, "Giá trị"),
        FORMULA(2, "Công thức");

        private final Integer value;
        private final String name;

        SalaryItemType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (SalaryItemType type : SalaryItemType.values()) {
                if (type.getValue().equals(value)) {
                    return type.getName();
                }
            }
            return null;
        }
    }

    public enum SalaryPeriodStatus {
        DRAFT(1, "Nháp"),
        APPROVED(2, "Đã duyệt"),
        FINALIZED(3, "Đã chốt");

        private final Integer value;
        private final String name;

        SalaryPeriodStatus(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getNameByValue(Integer value) {
            if (value == null) return null;
            for (SalaryPeriodStatus type : SalaryPeriodStatus.values()) {
                if (type.getValue().equals(value)) {
                    return type.getName();
                }
            }
            return null;
        }
    }
}
