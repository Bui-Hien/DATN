import api from "../../axiosCustom";

const API_PATH = "/api/department-ip";

export const saveDepartmentIp = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingDepartmentIp = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getDepartmentIpById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteDepartmentIp = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleDepartmentIpByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

