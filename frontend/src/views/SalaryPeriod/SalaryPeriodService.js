import api from "../../axiosCustom";

const API_PATH = "/api/salary-period";

export const saveSalaryPeriod = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingSalaryPeriod = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getSalaryPeriodById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteSalaryPeriod = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleSalaryPeriodByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

