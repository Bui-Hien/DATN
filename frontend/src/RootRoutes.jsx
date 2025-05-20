import CountryRoutes from "./views/Country/CountryRoutes";
import BankRoutes from "./views/Bank/BankRoutes";
import FamilyRelationshipRoutes from "./views/FamilyRelationship/FamilyRelationshipRoutes";
import EthnicsRoutes from "./views/Ethnics/EthnicsRoutes";
import EducationDegreeRoutes from "./views/EducationDegree/EducationDegreeRoutes";
import ProfessionRoutes from "./views/Profession/ProfessionRoutes";
import ReligionRoutes from "./views/Religion/ReligionRoutes";
import AdministrativeUnitRoutes from "./views/AdministrativeUnit/AdministrativeUnitRoutes";
import DocumentTemplateRoutes from "./views/DocumentTemplate/DocumentTemplateRoutes";

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
];

export default routes;
