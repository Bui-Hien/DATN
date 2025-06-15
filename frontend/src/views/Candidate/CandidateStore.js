import {makeAutoObservable} from "mobx";
import {
    deleteCandidate,
    deleteMultipleCandidateByIds,
    getCandidateById,
    pagingCandidate,
    preScreened,
    saveCandidate,
    updateStatus,
} from "./CandidateService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "./SearchObject";
import {CandidateObject} from "./Candidate";
import {CandidateStatus} from "../../LocalConstants";

export default class CandidateStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    selectedRow = new CandidateObject();
    selectedDataList = [];
    isOpenFilter = false;
    currentTab = CandidateStatus.ALL.value;

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
        this.currentTab = CandidateStatus.ALL.value;
    };
    setCurrentTab = async (index) => {
        this.currentTab = index;
        this.handleSetSearchObject({
            ...this.searchObject,
            candidateStatus: index === CandidateStatus.ALL.value ? null : index,
        })
        await this.pagingCandidate();
    }
    pagingCandidate = async () => {
        try {
            const response = await pagingCandidate(this.searchObject);
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
        await this.pagingCandidate();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingCandidate();
    };

    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getCandidateById(id);
                this.selectedRow = {
                    ...new CandidateObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new CandidateObject();
            }
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
            const {data} = await deleteCandidate(this.selectedRow.id);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingCandidate();
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
            await deleteMultipleCandidateByIds(ids);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingCandidate();
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

    handleUpdateStatusList = async (status) => {
        try {
            const ids = this.selectedDataList.map((item) => item.id);
            const newValue = {
                candidates: ids,
                candidateStatus: status
            }
            await updateStatus(newValue);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.update_success"));

            await this.setCurrentTab(status);
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
    handleUpdatePreScreenedList = async () => {
        try {
            const ids = this.selectedDataList.map((item) => item.id);
            const newValue = {
                candidates: ids,
            }
            await preScreened(newValue);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.update_success"));
            await this.setCurrentTab(CandidateStatus.PRE_SCREENED.value);
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

    saveCandidate = async (data) => {
        try {
            const {data: response} = await saveCandidate(data);
            toast.success(i18n.t("toast.save_success"));
            return response;
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
