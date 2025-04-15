package com.buihien.datn;

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
            if (value == null) return "";
            for (Platform item : Platform.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return "";
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
            if (value == null) return "";
            for (Gender item : Gender.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return "";
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
            if (value == null) return "";
            for (TokenType item : TokenType.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return "";
        }
    }
    public enum LogMessageQueueStatus {
        SUCCESS (1, "Thành công"),
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
            if (value == null) return "";
            for (TokenType item : TokenType.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return "";
        }
    }
    public enum LogMessageQueueTypes {
        FORGOT_PASSWORD (1, "Quên mật khẩu");

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
            if (value == null) return "";
            for (TokenType item : TokenType.values()) {
                if (item.getValue().equals(value)) {
                    return item.getName();
                }
            }
            return "";
        }
    }
}
