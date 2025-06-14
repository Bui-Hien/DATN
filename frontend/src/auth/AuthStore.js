import {makeAutoObservable} from "mobx";
import "react-toastify/dist/ReactToastify.css";
import {getCurrentUser} from "./authService";
import {toast} from "react-toastify";
import i18next from "i18next";

export default class AuthStore {
    roles = [];
    currentUser = null;

    constructor() {
        makeAutoObservable(this);
    }

    getCurrentUser = async () => {
        try {
            const {data} = await getCurrentUser();
            // Gán currentUser
            this.currentUser = data.data || null;

            // Gán roles là danh sách name
            this.roles = Array.isArray(data.data.roles)
                ? data.data.roles.map(r => r?.name).filter(Boolean)
                : [];
            return this.currentUser;
        } catch (error) {
            console.error(error);
            toast.error(i18next.t("notLoggedIn"));
            return null;
        }
    }

    resetStore = () => {
        this.currentUser = null;
        this.roles = [];
    };
}
