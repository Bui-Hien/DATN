import api from "../../axiosCustom";

const API_PATH = "api/staff-labour-agreement";

export const saveStaffLabourAgreement = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingStaffLabourAgreement = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getStaffLabourAgreementById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteStaffLabourAgreement = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleStaffLabourAgreementByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

