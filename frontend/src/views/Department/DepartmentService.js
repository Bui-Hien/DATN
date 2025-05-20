import api from "../../axiosCustom";

const API_PATH = "/api/department";

export const saveDepartment = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingDepartment = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const pagingTreeDepartment = (searchObject) => {
    var url = API_PATH + "/paging-tree-search";
    return api.post(url, searchObject);
};

export const getDepartmentById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteDepartment = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleDepartmentByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

