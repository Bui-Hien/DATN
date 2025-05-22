import api from "../../axiosCustom";

const API_PATH = "/api/person-family-relationship";

export const savePersonFamilyRelationship = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingPersonFamilyRelationship = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getPersonFamilyRelationshipById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deletePersonFamilyRelationship = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultiplePersonFamilyRelationshipByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

