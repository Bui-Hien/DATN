import api from "../axiosCustom";
import ConstantList from "../appConfig";

const API_PATH = ConstantList.API_ENDPOINT + "/api/auth";
const API_PATH_USER = ConstantList.API_ENDPOINT + "/api/v1/user";

export const accessToken = (obj) => {
    const url = API_PATH + "/access-token";
    return api.post(url, obj);
};

export const refreshToken = (refreshToken) => {
    const url = API_PATH + "/refresh-token";

    return api.post(url, null, {
        headers: {
            "X-Refresh-Token": refreshToken, // Sử dụng một header tùy chỉnh
        }
    });
};

export const removeToken = (obj) => {
    const url = API_PATH + "/remove-token";
    return api.post(url, obj);
};

export const forgotPassword = (obj) => {
    const url = API_PATH + "/forgot-password";
    return api.post(url, obj);
};

export const resetPassword = (obj) => {
    const url = API_PATH + "/reset-password";
    return api.post(url, obj);
};

export const changePassword = (obj) => {
    const url = API_PATH + "/change-password";
    return api.post(url, obj);
};

export const getCurrentUser = () => {
    const url = API_PATH_USER + "/get-current-user";
    return api.get(url);
};