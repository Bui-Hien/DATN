import CountryRoutes from "./views/Country/CountryRoutes";
import BankRoutes from "./views/Bank/BankRoutes";
import FamilyRelationshipRoutes from "./views/FamilyRelationship/FamilyRelationshipRoutes";
import EthnicsRoutes from "./views/Ethnics/EthnicsRoutes";
import EducationDegreeRoutes from "./views/EducationDegree/EducationDegreeRoutes";
import ProfessionRoutes from "./views/Profession/ProfessionRoutes";
import ReligionRoutes from "./views/Religion/ReligionRoutes";

const routes = [
    ...CountryRoutes,
    ...BankRoutes,
    ...EthnicsRoutes,
    ...EducationDegreeRoutes,
    ...FamilyRelationshipRoutes,
    ...ProfessionRoutes,
    ...ReligionRoutes
];

export default routes;
