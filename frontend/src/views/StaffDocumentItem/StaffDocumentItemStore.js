import {makeAutoObservable} from "mobx";
import {saveListStaffDocumentItem, getStaffDocumentItemByDocumentTemplate} from "./StaffDocumentItemService";
import {toast} from "react-toastify";
import i18n from "i18next";

export default class StaffDocumentItemStore {
    dataList = [];
    staffId = null;

    constructor() {
        makeAutoObservable(this);
    }

    resetStore = () => {
        this.dataList = [];
        this.staffId = null;
    };

    handleSetStaffId = (staffId) => {
        this.staffId = staffId;
    }
    getStaffDocumentItemByDocumentTemplate = async () => {
        try {
            const response = await getStaffDocumentItemByDocumentTemplate(this.staffId);
            const result = response.data;
            this.dataList = result.data || [];
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    saveListStaffDocumentItem = async (list) => {
        try {
            await saveListStaffDocumentItem(list);
            toast.success(i18n.t("toast.save_success"));
            await this.getStaffDocumentItemByDocumentTemplate();
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };
}
