import api from "../../axiosCustom";

const API_PATH = "/api/document-template";

export const saveDocumentTemplate = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingDocumentTemplate = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getDocumentTemplateById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteDocumentTemplate = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleDocumentTemplateByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

