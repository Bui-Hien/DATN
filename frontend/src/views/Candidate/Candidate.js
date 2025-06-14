export class CandidateObject {
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
    maritalStatus = null; // enum
    taxCode = '';
    user = null;        // object: UserDto
    educationLevel = null; //trình độ học vấn
    height = null;
    weight = null;
    certificates = []; // array of CertificateDto

    candidateCode = null;
    position = null;
    submissionDate = null;
    interviewDate = null;
    desiredPay = null;
    possibleWorkingDate = null;
    onboardDate = null;
    introducer = null;
    staff = null;
    candidateStatus = null;
    workExperience = null;
    recruitmentRequest = null;

    constructor() {
    }
}
