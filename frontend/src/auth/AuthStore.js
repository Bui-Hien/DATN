import {makeAutoObservable} from "mobx";
import "react-toastify/dist/ReactToastify.css";
import {jwtDecode} from "jwt-decode";

export default class AuthStore {
    roles = [];
    username = null;

    constructor() {
        makeAutoObservable(this);
    }

    getUserName = () => {
        const
            token = localStorage.getItem("access_token");
        if (token) {
            try {
                const decoded = jwtDecode(token);
                this.username = decoded?.sub || null;
            } catch (error) {
                console.error("Invalid token:", error);
                this.resetStore();
            }
        } else {
            this.resetStore();
        }
    }

    getRoles = () => {
        const
            token = localStorage.getItem("access_token");
        if (token) {
            try {
                const decoded = jwtDecode(token);
                this.roles = decoded?.roles || [];
            } catch (error) {
                console.error("Invalid token:", error);
                this.resetStore();
            }
        } else {
            this.resetStore();
        }
    }
    resetStore = () => {
        this.username = null;
        this.roles = [];
    };
}
