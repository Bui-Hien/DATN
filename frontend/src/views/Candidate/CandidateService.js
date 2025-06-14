import api from "../../axiosCustom";

const API_PATH = "/api/candidate";

export const saveCandidate = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingCandidate = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getCandidateById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteCandidate = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleCandidateByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};
export const updateStatus = (obj) => {
    let url = API_PATH + "/update-status";
    return api.post(url, obj);
};
export const preScreened = (obj) => {
    let url = API_PATH + "/pre-screened";
    return api.post(url, obj);
};
