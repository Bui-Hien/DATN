import {makeAutoObservable} from "mobx";
import {toast} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {accessToken} from "@/auth/authService";

export default class LoginStore {
    intactLoginObject = {
        username: "",
        password: "",
    };

    loadingInitial = false;

    loginObject = JSON.parse(JSON.stringify(this.intactLoginObject));

    constructor() {
        makeAutoObservable(this);
        this.loginObject = {...this.intactLoginObject};
    }

    setLoadingInitial = (state) => {
        this.loadingInitial = state;
    };

    handleLogin = async () => {
        this.setLoadingInitial(true);
        try {
            const payload = {...this.loginObject};
            const response = await accessToken(payload);

            if (response.status === 200) {
                const data = response.data;
                if (data) {
                    localStorage.setItem("access_token", data?.accessToken);
                    localStorage.setItem("refresh_token", data?.refreshToken);
                    return true;
                } else {
                }
                toast.error("Lỗi không xác định!");
            } else {
                toast.error("Đã có lỗi xảy ra, vui lòng thử lại sau!");
            }
        } catch (error) {
            console.error(error);
            toast.error("Đã có lỗi xảy ra, vui lòng thử lại sau!");
        }
        this.setLoadingInitial(false);
    };

    handleSetLoginObject = (searchObject) => {
        this.loginObject = {...searchObject};
    };

    resetStore = () => {
        this.loginObject = {...this.intactLoginObject};
    };
}
