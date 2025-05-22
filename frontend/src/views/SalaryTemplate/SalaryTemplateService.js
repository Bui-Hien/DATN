import api from "../../axiosCustom";

const API_PATH = "/api/salary-template";

export const saveSalaryTemplate = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingSalaryTemplate = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getSalaryTemplateById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteSalaryTemplate = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleSalaryTemplateByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

