import api from "../../axiosCustom";

const API_PATH = "/api/person-bank-account";

export const savePersonBankAccount = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};

export const pagingPersonBankAccount = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getPersonBankAccountById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deletePersonBankAccount = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultiplePersonBankAccountByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

