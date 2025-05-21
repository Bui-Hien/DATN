const SYSTEM_ROLE = {
    ROLE_ADMIN: "ROLE_ADMIN",
    ROLE_USER: "ROLE_USER",
    ROLE_MANAGER: "ROLE_MANAGER",
    ROLE_HR: "ROLE_HR",
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
    PROVINCE: {value: true, name: "Bắt buộc"},
    DISTRICT: {value: false, name: "Không bắt buộc"},
    getListData() {
        return Object.keys(this)
            .filter(key => typeof this[key] === 'object' && this[key] !== null && 'value' in this[key])
            .map(key => this[key]);
    },
};

const Gender = {
    MALE: {value: 1, name: "Nam"},
    FEMALE: {value: 2, name: "Nữ"},
    OTHER: {value: 3, name: "Khác"},
    getListData() {
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

module.exports = Object.freeze({
    SystemRole: SYSTEM_ROLE,
    EducationLevel: EducationLevel,
    AdministrativeUnitLevel: AdministrativeUnitLevel,
    DocumentItemRequired: DocumentItemRequired,
    Gender: Gender,
    MaritalStatus: MaritalStatus
});
