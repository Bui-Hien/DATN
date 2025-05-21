import api from "../../axiosCustom";

const API_PATH = "/api/candidate-working-experience";

export const saveCandidateWorkingExperience = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingCandidateWorkingExperience = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getCandidateWorkingExperienceById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteCandidateWorkingExperience = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleCandidateWorkingExperienceByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

