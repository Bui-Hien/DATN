import api from "@/app/axiosCustom";

const API_PATH = "/api/auth";

export const accessToken = (obj) => {
    var url = API_PATH + "/access-token";
    return api.post(url, obj);
};

export const refreshToken = (refreshToken) => {
    var url = API_PATH + "/refresh-token";

    return api.post(url, null, {
        headers: {
            "X-Refresh-Token": refreshToken, // Sử dụng một header tùy chỉnh
        }
    });
};



export const removeToken = (obj) => {
    var url = API_PATH + "/remove-token";
    return api.post(url, obj);
};

export const forgotPassword = (obj) => {
    var url = API_PATH + "/forgot-password";
    return api.post(url, obj);
};

export const resetPassword = (obj) => {
    var url = API_PATH + "/reset-password";
    return api.post(url, obj);
};

export const changePassword = (obj) => {
    var url = API_PATH + "/change-password";
    return api.post(url, obj);
};