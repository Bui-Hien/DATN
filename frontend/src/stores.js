import {createContext, useContext} from "react";
import LoginStore from "./views/login/LoginStore";
import AuthStore from "./auth/AuthStore";
import CountryStore from "./views/Country/CountryStore";
import BankStore from "./views/Bank/BankStore";
import EducationDegreeStore from "./views/EducationDegree/EducationDegreeStore";
import FamilyRelationshipStore from "./views/FamilyRelationship/FamilyRelationshipStore";
import EthnicsStore from "./views/Ethnics/EthnicsStore";
import ProfessionStore from "./views/Profession/ProfessionStore";
import ReligionStore from "./views/Religion/ReligionStore";

export const store = {
    loginStore: new LoginStore(),
    authStore: new AuthStore(),
    countryStore: new CountryStore(),
    bankStore: new BankStore(),
    educationDegreeStore: new EducationDegreeStore(),
    ethnicsStore: new EthnicsStore(),
    familyRelationshipStore: new FamilyRelationshipStore(),
    professionStore: new ProfessionStore(),
    religionStore: new ReligionStore()
};

export const StoreContext = createContext(store);

export function useStore() {
    return useContext(StoreContext);
}
