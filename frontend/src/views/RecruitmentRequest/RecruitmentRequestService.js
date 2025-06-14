import api from "../../axiosCustom";

const API_PATH = "/api/recruitment-request";

export const saveRecruitmentRequest = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingRecruitmentRequest = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getRecruitmentRequestById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteRecruitmentRequest = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleRecruitmentRequestByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

