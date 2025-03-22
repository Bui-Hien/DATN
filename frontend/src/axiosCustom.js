import axios from 'axios';
import {refreshToken} from "@/auth/authService";
import {API_ENPOINT} from "@/app/appConfig";

const getAccessToken = () => localStorage.getItem("access_token");
const getRefreshToken = () => localStorage.getItem("refresh_token");

const api = axios.create({
    baseURL: API_ENPOINT,
    timeout: 10000,
    headers: {'Content-Type': 'application/json'}
});

const excludedEndpoints =
    [
        "/api/auth/access-token",
        "/api/auth/refresh-token",
        "/api/auth/remove-token",
        "/api/auth/reset-password",
    ];

api.interceptors.request.use(
    (config) => {
        const token = getAccessToken();
        const isExcluded = excludedEndpoints.some((endpoint) => config.url.includes(endpoint));
        if (!isExcluded && token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        if (!isExcluded && !token) {
            window.location.href = "/login";
        }
        return config;
    },
    (error) => Promise.reject(error)
);


api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            const refresh = getRefreshToken();
            if (!refresh) {
                localStorage.clear();
                window.location.href = "/login";
                return Promise.reject(error);
            }

            try {
                const {data} = await refreshToken(refresh);

                if (data?.accessToken) {
                    localStorage.setItem("access_token", data.accessToken);
                    originalRequest.headers.Authorization = `Bearer ${data.accessToken}`;
                    return api(originalRequest);
                }
            } catch (refreshError) {
                localStorage.clear();
                window.location.href = "/login";
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export default api;
