import CountryRoutes from "./views/Country/CountryRoutes";
import BankRoutes from "./views/Bank/BankRoutes";
import FamilyRelationshipRoutes from "./views/FamilyRelationship/FamilyRelationshipRoutes";
import EthnicsRoutes from "./views/Ethnics/EthnicsRoutes";
import EducationDegreeRoutes from "./views/EducationDegree/EducationDegreeRoutes";
import ProfessionRoutes from "./views/Profession/ProfessionRoutes";
import ReligionRoutes from "./views/Religion/ReligionRoutes";
import AdministrativeUnitRoutes from "./views/AdministrativeUnit/AdministrativeUnitRoutes";
import DocumentTemplateRoutes from "./views/DocumentTemplate/DocumentTemplateRoutes";
import DepartmentRoutes from "./views/Department/DepartmentRoutes";
import DepartmentIpRoutes from "./views/DepartmentIp/DepartmentIpRoutes";

const routes = [
    //Danh mục chung
    ...CountryRoutes,
    ...BankRoutes,
    ...EthnicsRoutes,
    ...EducationDegreeRoutes,
    ...FamilyRelationshipRoutes,
    ...ProfessionRoutes,
    ...ReligionRoutes,
    ...AdministrativeUnitRoutes,
    //Nhân viên
    ...DocumentTemplateRoutes,

    //Cơ cấu tổ chức
    ...DepartmentRoutes,

    //Chấm công
    ...DepartmentIpRoutes
];

export default routes;
