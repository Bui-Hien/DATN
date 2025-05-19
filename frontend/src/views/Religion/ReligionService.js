import api from "../../axiosCustom";

const API_PATH = "/api/religion";

export const saveReligion = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingReligion = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getReligionById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteReligion = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleReligionByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

