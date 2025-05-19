import {makeAutoObservable} from "mobx";
import {
    pagingReligion,
    getReligionById,
    saveReligion,
    deleteReligion,
    deleteMultipleReligionByIds,
} from "./ReligionService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "./SearchObject";
import {ReligionObject} from "./Religion";

export default class ReligionStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    selectedRow = new ReligionObject();
    selectedDataList = [];
    isOpenFilter = false;

    constructor() {
        makeAutoObservable(this);
    }

    resetStore = () => {
        this.searchObject = JSON.parse(JSON.stringify(new SearchObject()));
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

    pagingReligion = async () => {
        try {
            const response = await pagingReligion(this.searchObject);
            const result = response.data;
            this.dataList = result.data.content || [];
            this.totalElements = result.data.totalElements || 0;
            this.totalPages = result.data.totalPages || 0;
            console.log(this.dataList)
        } catch (error) {
            console.error(error);
            toast.error(i18n.t("toast.error"));
        }
    };

    setPageIndex = async (page) => {
        this.searchObject.pageIndex = page;
        await this.pagingReligion();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingReligion();
    };

    handleChangePage = async (event, newPage) => {
        await this.setPageIndex(newPage);
    };

    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getReligionById(id);
                this.selectedRow = {
                    ...new ReligionObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new ReligionObject();
            }
            this.openCreateEditPopup = true;
        } catch (error) {
            console.error(error);
            toast.error(i18n.t("toast.error"));
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
            const {data} = await deleteReligion(this.selectedRow.id);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingReligion();
            this.handleClose();
            return data;
        } catch (error) {
            console.log(error);
            toast.warning(i18n.t("toast.error"));
        }
    };

    handleConfirmDeleteMultiple = async () => {
        try {
            const ids = this.selectedDataList.map((item) => item.id);
            await deleteMultipleReligionByIds(ids);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingReligion();
            this.handleClose();
        } catch (error) {
            console.log(error);
            toast.warning(i18n.t("toast.error"));
        }
    };

    handleSelectListDelete = (dataList) => {
        this.selectedDataList = dataList;
    };

    saveReligion = async (data) => {
        try {
            await saveReligion(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingReligion();
        } catch (error) {
            console.error(error);
            toast.error(i18n.t("toast.error"));
        }
    };

    getBank = async (id) => {
        if (id) {
            try {
                const {data} = await getReligionById(id);
                this.selectedRow = data;
                this.openCreateEditPopup = true;
            } catch (error) {
                console.log(error);
                toast.warning(i18n.t("toast.error"));
            }
        } else {
            this.selectedRow = {};
        }
    };

    handleSetSearchObject = (searchObject) => {
        this.searchObject = {...searchObject};
    };

    setOpenCreateEditPopup = (value) => {
        this.openCreateEditPopup = value;
    };

    setSelectedRow = (data) => {
        this.selectedRow = {
            ...data,
        };
    };

    handleOpenFilter = () => {
        this.isOpenFilter = true;
    };

    handleCloseFilter = () => {
        this.isOpenFilter = false;
    };

    handleTogglePopupFilter = () => {
        this.isOpenFilter = !this.isOpenFilter;
    };
}
