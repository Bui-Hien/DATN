export class StaffObject {
    id = null;
    firstName = '';
    lastName = '';
    displayName = '';
    gender = null; // enum (e.g., 0: Nam, 1: Nữ,...)
    birthDate = null;
    birthPlace = '';
    phoneNumber = '';
    idNumber = '';
    idNumberIssueBy = '';
    idNumberIssueDate = null;
    avatar = null;
    email = '';
    nationality = null; // object: CountryDto
    ethnics = null;     // object: EthnicsDto
    religion = null;    // object: ReligionDto
    maritalStatus = null; // enum
    taxCode = '';
    user = null;        // object: UserDto
    educationLevel = null; //trình độ học vấn
    height = null;
    weight = null;
    permanentResidence = {
        addressDetail: "",
        province: null,
        district: null,
        ward: null,
    };
    temporaryResidence = {
        addressDetail: "",
        province: null,
        district: null,
        ward: null,
    };
    familyRelationships = []; // array of PersonFamilyRelationshipDto
    certificates = [];        // array of CertificateDto
    personBankAccounts = [];  // array of PersonBankAccountDto
    staffCode = ''; // Mã nhân viên
    recruitmentDate = null; // Ngày tuyển dụng
    startDate = null; // Ngày bắt đầu công việc
    apprenticeDays = null; // Số ngày học việc/thử việc
    agreements = []; // Danh sách hợp đồng: StaffLabourAgreementDto[]
    employeeStatus = null; // Enum trạng thái nhân viên (DatnConstants.EmployeeStatus)
    documentTemplate = null; // DocumentTemplateDto
    staffDocumentItems = []; // StaffDocumentItemDto[]
    staffPhase = null; // Enum tình trạng nhân viên (DatnConstants.StaffPhase)
    requireAttendance = true; // Nhân viên cần chấm công hay không
    allowExternalIpTimekeeping = false; // Cho phép chấm công ngoài công ty
    hasSocialIns = false; // Có đóng BHXH không
    constructor() {
    }
}
