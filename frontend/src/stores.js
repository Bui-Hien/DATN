import {createContext, useContext} from "react";
import LoginStore from "./views/Login/LoginStore";
import AuthStore from "./auth/AuthStore";
import CountryStore from "./views/Country/CountryStore";
import BankStore from "./views/Bank/BankStore";
import EducationDegreeStore from "./views/EducationDegree/EducationDegreeStore";
import FamilyRelationshipStore from "./views/FamilyRelationship/FamilyRelationshipStore";
import EthnicsStore from "./views/Ethnics/EthnicsStore";
import ProfessionStore from "./views/Profession/ProfessionStore";
import ReligionStore from "./views/Religion/ReligionStore";
import AdministrativeUnitStore from "./views/AdministrativeUnit/AdministrativeUnitStore";
import DocumentTemplateStore from "./views/DocumentTemplate/DocumentTemplateStore";
import DepartmentStore from "./views/Department/DepartmentStore";
import DepartmentIpStore from "./views/DepartmentIp/DepartmentIpStore";
import CandidateWorkingExperienceStore from "./views/CandidateWorkingExperience/CandidateWorkingExperienceStore";
import StaffStore from "./views/Staff/StaffStore";

export const store = {
    loginStore: new LoginStore(),
    authStore: new AuthStore(),
    countryStore: new CountryStore(),
    bankStore: new BankStore(),
    educationDegreeStore: new EducationDegreeStore(),
    ethnicsStore: new EthnicsStore(),
    familyRelationshipStore: new FamilyRelationshipStore(),
    professionStore: new ProfessionStore(),
    religionStore: new ReligionStore(),
    administrativeUnitStore: new AdministrativeUnitStore(),

    //Nhân viên
    staffStore: new StaffStore(),
    documentTemplateStore: new DocumentTemplateStore(),

    //Cơ cấu tổ chức
    departmentStore: new DepartmentStore(),

    //Chấm công
    departmentIpStore: new DepartmentIpStore(),
    //Tuyển dụng
    candidateWorkingExperienceStore: new CandidateWorkingExperienceStore(),
};

export const StoreContext = createContext(store);

export function useStore() {
    return useContext(StoreContext);
}
