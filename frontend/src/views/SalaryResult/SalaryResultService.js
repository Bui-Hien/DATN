import api from "../../axiosCustom";

const API_PATH = "/api/salary-result";

export const saveSalaryResult = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingSalaryResult = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getSalaryResultById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const getRecalculateSalary = (id) => {
    let url = API_PATH + "/recalculate-salary/" + id;
    return api.get(url);
};

export const deleteSalaryResult = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleSalaryResultByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

