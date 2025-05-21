import api from "../../axiosCustom";

const API_PATH = "/api/staff";

export const saveStaff = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingStaff = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getStaffById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteStaff = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleStaffByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

