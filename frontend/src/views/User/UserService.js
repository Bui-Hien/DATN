import api from "../../axiosCustom";
import ConstantList from "../../appConfig";

const API_PATH = "/api/v1/user";
const API_PATH_ROLE = "/api/role";
const API_PATH_AUTH = ConstantList.API_ENDPOINT + "/api/auth";

export const saveUser = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};
export const changePassword = (obj) => {
    const url = API_PATH_AUTH + "/change-password";
    return api.post(url, obj);
};
export const pagingUser = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getUserById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteUser = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleUserByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

export const pagingRole = (searchObject) => {
    var url = API_PATH_ROLE + "/paging-search";
    return api.post(url, searchObject);
};