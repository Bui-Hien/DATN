import api from "../../axiosCustom";

const API_PATH = "/api/education-degree";

export const saveEducationDegree = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingEducationDegree = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getEducationDegreeById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteEducationDegree = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleEducationDegreeByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

