import api from "../../axiosCustom";

const API_PATH = "/api/ethnics";

export const saveEthnics = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingEthnics = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getEthnicsById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteEthnics = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleEthnicsByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

