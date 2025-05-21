import api from "../../axiosCustom";

const API_PATH = "/api/position";

export const pagingPosition = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};