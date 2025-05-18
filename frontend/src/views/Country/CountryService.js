import api from "../../axiosCustom";

const API_PATH = "/api/country";

export const saveCountry = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingCountry = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getCountryById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteCountry = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleCountryByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

