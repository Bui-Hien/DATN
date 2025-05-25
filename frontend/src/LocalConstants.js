const {getTimeSchedule} = require("./LocalFunction");
const SYSTEM_ROLE = {
    ROLE_ADMIN: "ROLE_ADMIN", ROLE_USER: "ROLE_USER", ROLE_MANAGER: "ROLE_MANAGER", ROLE_HR: "ROLE_HR",
};
const EducationLevel = {
    PHD: {value: 1, name: "Tiến sĩ"},
    MASTER: {value: 2, name: "Thạc sĩ"},
    BACHELOR: {value: 3, name: "Cử nhân"},
    ENGINEER: {value: 4, name: "Kỹ sư"},
    COLLEGE: {value: 5, name: "Cao đẳng"},
    INTERMEDIATE: {value: 6, name: "Trung cấp"},
    HIGH_SCHOOL: {value: 7, name: "Tốt nghiệp THPT"},
    OTHER: {value: 99, name: "Khác"},
    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};

const AdministrativeUnitLevel = {
    PROVINCE: {value: 1, name: "Tỉnh"},
    DISTRICT: {value: 2, name: "Huyện"},
    WARD: {value: 3, name: "Xã"},
    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};
const DocumentItemRequired = {
    PROVINCE: {value: true, name: "Bắt buộc"}, DISTRICT: {value: false, name: "Không bắt buộc"}, getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};

const Gender = {
    MALE: {value: 1, name: "Nam"}, FEMALE: {value: 2, name: "Nữ"}, OTHER: {value: 3, name: "Khác"}, getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};


const MaritalStatus = {
    SINGLE: {value: 1, name: "Độc thân"},
    MARRIED: {value: 2, name: "Đã kết hôn"},
    DIVORCED: {value: 3, name: "Ly hôn"},
    WIDOWED: {value: 4, name: "Góa vợ/chồng"},
    SEPARATED: {value: 5, name: "Ly thân"},
    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};

const EmployeeStatus = {
    WORKING: {value: 1, name: "Đang làm việc"},
    RESIGNED: {value: 2, name: "Đã nghỉ việc"},
    RETIRED: {value: 3, name: "Đã nghỉ hưu"},
    SUSPENDED: {value: 4, name: "Tạm ngưng"},
    TERMINATED: {value: 5, name: "Chấm dứt hợp đồng"},
    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};
const StaffPhase = {
    INTERN: {value: 1, name: "Học việc (HV)"},
    PROBATION: {value: 2, name: "Thử việc (TV)"},
    OFFICIAL: {value: 3, name: "Chính thức (CT)"},
    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};


const StaffLabourAgreementStatus = {
    UNSIGNED: {value: 1, name: "Hợp đồng chưa được ký"},
    SIGNED: {value: 2, name: "Hợp đồng đã được ký"},
    TERMINATED: {value: 3, name: "Đã chấm dứt"},
    EXPIRED: {value: 4, name: "Đã hết hạn"},
    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};
const ContractType = {
    PROBATION: {value: 1, name: "Thử việc"},
    OFFICIAL: {value: 2, name: "Chính thức"},
    SEASONAL: {value: 3, name: "Thời vụ"},
    FIXED_TERM: {value: 4, name: "Xác định thời hạn"},
    UNLIMITED_TERM: {value: 5, name: "Không xác định thời hạn"},

    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};
const SalaryItemType = {
    VALUE: {value: 1, name: "Giá trị"},
    FORMULA: {value: 2, name: "Công thức"},
    SYSTEM: {value: 3, name: "Hệ thống lấy dự liệu"},

    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};
const SalaryTemplateItemSystem = {
    ACTUAL_NUMBER_OF_WORKING_DAYS: {
        name: "Số ngày công thực tế", code: "SO_NGAY_CONG_THUC_TE", salaryItemType: SalaryItemType.SYSTEM.value
    }, STANDARD_NUMBER_OF_WORKING_DAYS: {
        name: "Số ngày công tiêu chuẩn", code: "SO_NGAY_CONG_TIEU_CHUAN", salaryItemType: SalaryItemType.SYSTEM.value
    }, BASIC_SALARY: {
        name: "Lương cơ bản", code: "LUONG_CO_BAN", salaryItemType: SalaryItemType.SYSTEM.value
    }, getListData() {
        return Object.keys(this)
            .filter((key) => typeof this[key] === "object" && this[key] !== null && key !== "getListData")
            .map((key) => this[key]);
    },
}

const SalaryPeriodStatus = {
    DRAFT: {value: 1, name: "Nháp"}, APPROVED: {value: 2, name: "Đã duyệt"}, FINALIZED: {value: 3, name: "Đã chốt"},

    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },

};

const ShiftWorkType = {
    MORNING: {
        value: 1,
        name: "Ca sáng",
        calculatedWorkingDay: 0.5,
        startTime: getTimeSchedule(8, 30),
        endTime: getTimeSchedule(12, 0)
    }, AFTERNOON: {
        value: 2,
        name: "Ca chiều",
        calculatedWorkingDay: 0.5,
        startTime: getTimeSchedule(13, 30),
        endTime: getTimeSchedule(17, 30)
    }, FULL_DAY: {
        value: 3,
        name: "Ca nguyên ngày",
        calculatedWorkingDay: 1.0,
        startTime: getTimeSchedule(8, 30),
        endTime: getTimeSchedule(17, 30)
    },

    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && 'value' in this[key])
            .map(key => this[key]);
    },

};
const Weekdays = {
    MONDAY: {value: 1, name: "Thứ 2"},
    TUESDAY: {value: 2, name: "Thứ 3"},
    WEDNESDAY: {value: 3, name: "Thứ 4"},
    THURSDAY: {value: 4, name: "Thứ 5"},
    FRIDAY: {value: 5, name: "Thứ 6"},
    SATURDAY: {value: 6, name: "Thứ 7"},
    SUNDAY: {value: 7, name: "Chủ nhật"},

    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    }
};

const ShiftWorkStatus = {
    CREATED: {value: 1, name: "Khởi tạo"},
    CHECKED_IN: {value: 2, name: "Đã check in"},
    INSUFFICIENT_HOURS: {value: 3, name: "Đi làm đủ giờ"},
    WORKED_FULL_HOURS: {value: 4, name: "Đi làm đủ giờ"},
    ABSENT: {value: 5, name: "Nghỉ"},
    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    }
};

module.exports = Object.freeze({
    SystemRole: SYSTEM_ROLE,
    EducationLevel: EducationLevel,
    AdministrativeUnitLevel: AdministrativeUnitLevel,
    DocumentItemRequired: DocumentItemRequired,
    Gender: Gender,
    MaritalStatus: MaritalStatus,
    EmployeeStatus: EmployeeStatus,
    StaffPhase: StaffPhase,
    StaffLabourAgreementStatus: StaffLabourAgreementStatus,
    ContractType: ContractType,
    SalaryItemType: SalaryItemType,
    SalaryTemplateItemSystem: SalaryTemplateItemSystem,
    SalaryPeriodStatus: SalaryPeriodStatus,
    ShiftWorkType: ShiftWorkType,
    Weekdays: Weekdays,
    ShiftWorkStatus: ShiftWorkStatus
});
