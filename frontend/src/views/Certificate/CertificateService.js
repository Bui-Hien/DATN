import api from "../../axiosCustom";

const API_PATH = "/api/certificate";

export const saveCertificate = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingCertificate = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getCertificateById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteCertificate = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleCertificateByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

