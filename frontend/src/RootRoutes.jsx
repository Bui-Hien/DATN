import CountryRoutes from "./views/Country/CountryRoutes";
import BankRoutes from "./views/Bank/BankRoutes";
import FamilyRelationshipRoutes from "./views/FamilyRelationship/FamilyRelationshipRoutes";
import EthnicsRoutes from "./views/Ethnics/EthnicsRoutes";
import ProfessionRoutes from "./views/Profession/ProfessionRoutes";
import ReligionRoutes from "./views/Religion/ReligionRoutes";
import AdministrativeUnitRoutes from "./views/AdministrativeUnit/AdministrativeUnitRoutes";
import DocumentTemplateRoutes from "./views/DocumentTemplate/DocumentTemplateRoutes";
import DepartmentRoutes from "./views/Department/DepartmentRoutes";
import StaffRoutes from "./views/Staff/StaffRoutes";
import SalaryTemplateRoutes from "./views/SalaryTemplate/SalaryTemplateRoutes";
import SalaryPeriodRoutes from "./views/SalaryPeriod/SalaryPeriodRoutes";
import SalaryResultRoutes from "./views/SalaryResult/SalaryResultRoutes";
import TimeSheetDetailRoutes from "./views/TimeSheetDetail/TimeSheetDetailRoutes";
import StaffWorkScheduleRoutes from "./views/StaffWorkSchedule/StaffWorkScheduleRoutes";
import StaffMonthScheduleCalendarRoutes from "./views/StaffMonthScheduleCalendar/StaffMonthScheduleCalendarRoutes";
import UserRoutes from "./views/User/UserRoutes";
import RecruitmentRequestRoutes from "./views/RecruitmentRequest/RecruitmentRequestRoutes";
import CandidateRoutes from "./views/Candidate/CandidateRoutes";

const routes = [
    //Danh mục chung
    ...CountryRoutes,
    ...BankRoutes,
    ...EthnicsRoutes,
    ...ProfessionRoutes,
    ...ReligionRoutes,
    ...AdministrativeUnitRoutes,
    //Nhân viên
    ...StaffRoutes,
    ...DocumentTemplateRoutes,
    ...FamilyRelationshipRoutes,

    //Cơ cấu tổ chức
    ...DepartmentRoutes,

    //Chấm công
    ...StaffWorkScheduleRoutes,
    ...TimeSheetDetailRoutes,
    ...StaffMonthScheduleCalendarRoutes,
    //Lương
    ...SalaryTemplateRoutes,
    ...SalaryPeriodRoutes,
    ...SalaryResultRoutes,
    //Quản lý người dùng
    ...UserRoutes,

    //tuyển dụng
    ...RecruitmentRequestRoutes,
    ...CandidateRoutes
];

export default routes;
