import {makeAutoObservable} from "mobx";
import {
    pagingEthnics,
    getEthnicsById,
    saveEthnics,
    deleteEthnics,
    deleteMultipleEthnicsByIds,
} from "./EthnicsService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "./SearchObject";
import {EthnicsObject} from "./Ethnics";

export default class EthnicsStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    selectedRow = new EthnicsObject();
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

    pagingEthnics = async () => {
        try {
            const response = await pagingEthnics(this.searchObject);
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
        await this.pagingEthnics();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingEthnics();
    };

    handleChangePage = async (event, newPage) => {
        await this.setPageIndex(newPage);
    };

    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getEthnicsById(id);
                this.selectedRow = {
                    ...new EthnicsObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new EthnicsObject();
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
            const {data} = await deleteEthnics(this.selectedRow.id);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingEthnics();
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
            await deleteMultipleEthnicsByIds(ids);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingEthnics();
            this.handleClose();
        } catch (error) {
            console.log(error);
            toast.warning(i18n.t("toast.error"));
        }
    };

    handleSelectListDelete = (dataList) => {
        this.selectedDataList = dataList;
    };

    saveEthnics = async (data) => {
        try {
            await saveEthnics(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingEthnics();
        } catch (error) {
            console.error(error);
            toast.error(i18n.t("toast.error"));
        }
    };

    getBank = async (id) => {
        if (id) {
            try {
                const {data} = await getEthnicsById(id);
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
