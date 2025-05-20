import {makeAutoObservable} from "mobx";
import {toast} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {accessToken} from "../../auth/authService";
import i18n from "i18next";

export default class LoginStore {
    intactLoginObject = {
        username: "",
        password: "",
    };

    loadingInitial = false;

    loginObject = JSON.parse(JSON.stringify(this.intactLoginObject));

    constructor() {
        makeAutoObservable(this);
    }

    setLoadingInitial = (state) => {
        this.loadingInitial = state;
    };

    handleLogin = async (payload) => {
        this.setLoadingInitial(true);
        try {
            const response = await accessToken(payload);
            const data = response.data;
            localStorage.setItem("access_token", data?.accessToken);
            localStorage.setItem("refresh_token", data?.refreshToken);
            return true;
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }

        this.setLoadingInitial(false);
    };

    resetStore = () => {
        this.loginObject = {...this.intactLoginObject};
    };
}
