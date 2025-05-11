import {createContext, useContext} from "react";
import LoginStore from "./views/login/LoginStore";
import AuthStore from "./auth/AuthStore";

export const store = {
    loginStore: new LoginStore(),
    authStore: new AuthStore(),
};

export const StoreContext = createContext(store);

export function useStore() {
    return useContext(StoreContext);
}
