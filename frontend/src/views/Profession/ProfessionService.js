import api from "../../axiosCustom";

const API_PATH = "/api/profession";

export const saveProfession = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingProfession = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getProfessionById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteProfession = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleProfessionByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

