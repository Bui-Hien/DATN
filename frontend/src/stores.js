import {createContext, useContext} from "react";
import LoginStore from "./(auth)/login/LoginStore";
import AuthStore from "@/auth/AuthStore";
import UserStore from "@/app/user/UserStore";

const store = {
    loginStore: new LoginStore(),
    authStore: new AuthStore(),
    userStore: new UserStore(),
};

export const StoreContext = createContext(store);

export function useStore() {
    return useContext(StoreContext);
}
