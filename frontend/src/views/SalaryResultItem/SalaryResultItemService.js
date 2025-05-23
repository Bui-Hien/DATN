import api from "../../axiosCustom";

const API_PATH = "/api/salary-result-item";

export const pagingSalaryResultItem = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};
