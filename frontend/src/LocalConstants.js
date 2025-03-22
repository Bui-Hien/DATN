const SHIFT_WORK_TYPE = {
    ADMINISTRATIVE: { value: 1, name: "Hành chính" },
    OVERTIME: { value: 2, name: "Tăng ca" },

    getListData: function () {
        return Object.values(this).filter(type => typeof type === "object");
    }
};

// Các loại validation đặc biệt
const VALIDATION = {
    REQUIRED: "Trường này không được để trống",
    EMAIL: "Email không hợp lệ",
    PHONE_NUMBER: "Số điện thoại không hợp lệ",
    PASSWORD_STRONG:
        "Mật khẩu phải chứa ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt",
    CONFIRM_PASSWORD: "Mật khẩu xác nhận không khớp",
    MIN_LENGTH: (length) => `Phải có ít nhất ${length} ký tự`,
    MAX_LENGTH: (length) => `Không được vượt quá ${length} ký tự`,
    ONLY_NUMBER: "Chỉ được nhập số",
    DATE_FORMAT: "Ngày không hợp lệ, định dạng đúng là YYYY-MM-DD",
    RANGE_NUMBER: (min, max) => `Giá trị phải trong khoảng từ ${min} đến ${max}`,
    CUSTOM_REGEX: (regex, message) => ({ regex, message }),
};

module.exports = Object.freeze({
    ShiftWorkType: SHIFT_WORK_TYPE,
    Validation: VALIDATION,
});
