import api from "../../axiosCustom";

const API_PATH = "api/family-relationship";

export const saveFamilyRelationship = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingFamilyRelationship = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getFamilyRelationshipById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteFamilyRelationship = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleFamilyRelationshipByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

