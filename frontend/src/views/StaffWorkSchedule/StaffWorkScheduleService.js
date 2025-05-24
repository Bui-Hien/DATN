import api from "../../axiosCustom";

const API_PATH = "/api/staff-work-schedule";

export const saveStaffWorkSchedule = (obj) => {
    let url = API_PATH + "/save-or-update";
    return api.post(url, obj);
};
export const saveListStaffWorkSchedule = (obj) => {
    let url = API_PATH + "/save-list";
    return api.post(url, obj);
};

export const pagingStaffWorkSchedule = (searchObject) => {
    var url = API_PATH + "/paging-search";
    return api.post(url, searchObject);
};

export const getStaffWorkScheduleById = (id) => {
    let url = API_PATH + "/" + id;
    return api.get(url);
};

export const deleteStaffWorkSchedule = (id) => {
    let url = API_PATH + "/" + id;
    return api.delete(url);
};

export const deleteMultipleStaffWorkScheduleByIds = (obj) => {
    let url = API_PATH + "/delete-multiple";
    return api.post(url, obj);
};

