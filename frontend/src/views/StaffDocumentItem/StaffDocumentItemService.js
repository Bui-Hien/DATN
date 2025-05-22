import api from "../../axiosCustom";

const API_PATH = "/api/staff-document-item";

export const saveListStaffDocumentItem = (obj) => {
    let url = API_PATH + "/save-all";
    return api.post(url, obj);
};

export const getStaffDocumentItemByDocumentTemplate = (staffId) => {
    let url = API_PATH + "/list-staff-document-item-by-staff/" + staffId;
    return api.get(url);
};
