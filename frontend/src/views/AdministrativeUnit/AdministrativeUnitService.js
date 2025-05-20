import api from "../../axiosCustom";

const API_PATH = "/api/administrative-unit";

export const saveAdministrativeUnit = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingAdministrativeUnit = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const pagingTreeAdministrativeUnit = (searchObject) => {
    var url = API_PATH + "/paging-tree-search";
    return api.post(url, searchObject);
};

export const getAdministrativeUnitById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteAdministrativeUnit = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleAdministrativeUnitByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

