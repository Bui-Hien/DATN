import {makeAutoObservable} from "mobx";
import {
    deleteDocumentTemplate,
    deleteMultipleDocumentTemplateByIds,
    getDocumentTemplateById,
    pagingDocumentTemplate,
    saveDocumentTemplate,
} from "./DocumentTemplateService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {DocumentTemplateObject} from "./DocumentTemplate";

export default class DocumentTemplateStore {
    searchObject = JSON.parse(JSON.stringify(new DocumentTemplateObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    selectedRow = new DocumentTemplateObject();
    selectedDataList = [];
    isOpenFilter = false;

    constructor() {
        makeAutoObservable(this);
    }

    resetStore = () => {
        this.searchObject = JSON.parse(JSON.stringify(new DocumentTemplateObject()));
        this.totalElements = 0;
        this.totalPages = 0;
        this.dataList = [];
        this.openCreateEditPopup = false;
        this.selectedRow = null;
        this.openConfirmDeletePopup = false;
        this.openConfirmDeleteListPopup = false;
        this.selectedDataList = [];
        this.isOpenFilter = false;
    };

    pagingDocumentTemplate = async () => {
        try {
            const response = await pagingDocumentTemplate(this.searchObject);
            const result = response.data;
            this.dataList = result.data.content || [];
            this.totalElements = result.data.totalElements || 0;
            this.totalPages = result.data.totalPages || 0;
            console.log(this.dataList)
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    setPageIndex = async (page) => {
        this.searchObject.pageIndex = page;
        await this.pagingDocumentTemplate();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingDocumentTemplate();
    };

    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getDocumentTemplateById(id);
                this.selectedRow = {
                    ...new DocumentTemplateObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new DocumentTemplateObject();
            }
            this.openCreateEditPopup = true;
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    handleClose = () => {
        this.openConfirmDeletePopup = false;
        this.openCreateEditPopup = false;
        this.openConfirmDeleteListPopup = false;
    };

    handleDelete = (row) => {
        this.selectedRow = {...row};
        this.openConfirmDeletePopup = true;
    };

    handleDeleteList = () => {
        this.openConfirmDeleteListPopup = true;
    };

    handleConfirmDelete = async () => {
        try {
            const {data} = await deleteDocumentTemplate(this.selectedRow.id);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingDocumentTemplate();
            this.handleClose();
            return data;
        } catch (error) {
            console.log(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    handleConfirmDeleteMultiple = async () => {
        try {
            const ids = this.selectedDataList.map((item) => item.id);
            await deleteMultipleDocumentTemplateByIds(ids);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingDocumentTemplate();
            this.handleClose();
        } catch (error) {
            console.log(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    handleSelectListDelete = (dataList) => {
        this.selectedDataList = dataList;
    };

    saveDocumentTemplate = async (data) => {
        try {
            await saveDocumentTemplate(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingDocumentTemplate();
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    handleSetSearchObject = (searchObject) => {
        this.searchObject = {...searchObject};
    };
    
    handleCloseFilter = () => {
        this.isOpenFilter = false;
    };

    handleTogglePopupFilter = () => {
        this.isOpenFilter = !this.isOpenFilter;
    };
}
