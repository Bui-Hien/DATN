import {createContext, useContext} from "react";
import LoginStore from "./views/Login/LoginStore";
import AuthStore from "./auth/AuthStore";
import CountryStore from "./views/Country/CountryStore";
import BankStore from "./views/Bank/BankStore";
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
import PersonFamilyRelationshipStore from "./views/PersonFamilyRelationship/PersonFamilyRelationshipStore";
import StaffDocumentItemStore from "./views/StaffDocumentItem/StaffDocumentItemStore";
import PersonBankAccountStore from "./views/PersonBankAccount/PersonBankAccountStore";
import CertificateStore from "./views/Certificate/CertificateStore";
import StaffLabourAgreementStore from "./views/StaffLabourAgreement/StaffLabourAgreementStore";
import SalaryTemplateStore from "./views/SalaryTemplate/SalaryTemplateStore";
import SalaryPeriodStore from "./views/SalaryPeriod/SalaryPeriodStore";
import SalaryResultStore from "./views/SalaryResult/SalaryResultStore";
import SalaryResultItemStore from "./views/SalaryResultItem/SalaryResultItemStore";

export const store = {
    loginStore: new LoginStore(),
    authStore: new AuthStore(),
    countryStore: new CountryStore(),
    bankStore: new BankStore(),
    ethnicsStore: new EthnicsStore(),
    familyRelationshipStore: new FamilyRelationshipStore(),
    professionStore: new ProfessionStore(),
    religionStore: new ReligionStore(),
    administrativeUnitStore: new AdministrativeUnitStore(),

    //Nhân viên
    staffStore: new StaffStore(),
    documentTemplateStore: new DocumentTemplateStore(),
    personFamilyRelationshipStore: new PersonFamilyRelationshipStore(),
    certificateStore: new CertificateStore(),
    personBankAccountStore: new PersonBankAccountStore(),
    staffDocumentItemStore: new StaffDocumentItemStore(),
    staffLabourAgreementStore: new StaffLabourAgreementStore(),

    //Cơ cấu tổ chức
    departmentStore: new DepartmentStore(),

    //Chấm công
    departmentIpStore: new DepartmentIpStore(),
    //Tuyển dụng
    candidateWorkingExperienceStore: new CandidateWorkingExperienceStore(),

    //Lương
    salaryTemplateStore: new SalaryTemplateStore(),
    salaryPeriodStore: new SalaryPeriodStore(),
    salaryResultStore: new SalaryResultStore(),
    salaryResultItemStore: new SalaryResultItemStore()
};

export const StoreContext = createContext(store);

export function useStore() {
    return useContext(StoreContext);
}
