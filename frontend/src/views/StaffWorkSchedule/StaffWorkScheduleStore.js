import {makeAutoObservable} from "mobx";
import {
    deleteMultipleStaffWorkScheduleByIds,
    deleteStaffWorkSchedule,
    getByStaffAndWorkingDateAndShiftWorkType,
    getListByStaffAndWorkingDate,
    getStaffWorkScheduleById,
    markAttendance,
    pagingStaffWorkSchedule,
    saveListStaffWorkSchedule,
    saveStaffWorkSchedule,
    getStaffMonthScheduleCalendar, deleteMarkAttendanceById, deleteMultipleMarkAttendanceByIds
} from "./StaffWorkScheduleService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "./SearchObject";
import {StaffWorkScheduleObject} from "./StaffWorkSchedule";

export default class StaffWorkScheduleStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    openCreateEditListPopup = false;
    selectedRow = new StaffWorkScheduleObject();
    selectedDataList = [];
    listByStaffAndWorkingDate = [];
    isOpenFilter = false;
    openShiftStatistics = false;
    staffMonthScheduleCalendar = [];
    timeSheetDetail = false

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
        this.listByStaffAndWorkingDate = [];
        this.openShiftStatistics = false;
        this.staffMonthScheduleCalendar = [];
        this.timeSheetDetail = false;
    };

    handleSetTimeSheetDetail = () => {
        this.timeSheetDetail = true;
    }
    pagingStaffWorkSchedule = async () => {
        try {
            const newSearchObject = {
                ...this.searchObject,
                ownerId: this.searchObject.owner?.id,
                owner: null,
                departmentId: this.searchObject.department?.id,
                department: null
            }
            const response = await pagingStaffWorkSchedule(newSearchObject);
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

    pagingStaffMonthScheduleCalendar = async () => {
        try {
            const newSearchObject = {
                ...this.searchObject,
                ownerId: this.searchObject.owner?.id,
                owner: null,
            }
            const response = await getStaffMonthScheduleCalendar(newSearchObject);
            const result = response.data;
            this.staffMonthScheduleCalendar = result.data.content || [];
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
        await this.pagingStaffWorkSchedule();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingStaffWorkSchedule();
    };

    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getStaffWorkScheduleById(id);
                this.selectedRow = {
                    ...new StaffWorkScheduleObject(), ...data.data,
                };
            } else {
                this.selectedRow = new StaffWorkScheduleObject();
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
    handleGetByStaffAndWorkingDateAndShiftWorkType = async (obj) => {
        try {
            if (obj) {
                const {data} = await getByStaffAndWorkingDateAndShiftWorkType(obj);
                return {
                    ...new StaffWorkScheduleObject(),
                    ...data.data,
                    checkIn: data?.data?.checkIn || new Date(),
                    checkOut: data?.data?.checkIn ? new Date() : null
                };

            }
            return null;
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
            return null;
        }
    };


    handleClose = () => {
        this.openConfirmDeletePopup = false;
        this.openCreateEditPopup = false;
        this.openConfirmDeleteListPopup = false;
        this.openCreateEditListPopup = false;
        this.openShiftStatistics = false;
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
            let response = null;
            if (this.timeSheetDetail) {
                const {data} = await deleteMarkAttendanceById(this.selectedRow.id);
                response = data;
            } else {
                const {data} = await deleteStaffWorkSchedule(this.selectedRow.id);
                response = data;
            }
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingStaffWorkSchedule();
            this.handleClose();
            return response;
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
            if (this.timeSheetDetail) {
                await deleteMultipleMarkAttendanceByIds(ids);
            } else {
                await deleteMultipleStaffWorkScheduleByIds(ids);
            }
            this.selectedDataList = [];
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingStaffWorkSchedule();
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

    saveStaffWorkSchedule = async (data) => {
        try {
            await saveStaffWorkSchedule(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingStaffWorkSchedule();
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    getListByStaffAndWorkingDate = async (obj) => {
        try {
            const {data} = await getListByStaffAndWorkingDate(obj);
            this.listByStaffAndWorkingDate = data.data || [];
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    markAttendance = async (data) => {
        try {
            await markAttendance(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingStaffWorkSchedule();
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };
    handleOpenCreateEditListPopup = () => {
        this.openCreateEditListPopup = true;
    };

    saveListStaffWorkSchedule = async (data) => {
        try {
            await saveListStaffWorkSchedule(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingStaffWorkSchedule();
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

    handleSetSelectedRow = (row) => {
        this.selectedRow = {
            ...this.selectedRow,
            ...row
        };
    }
    handleOpenShiftStatistic = async (id) => {
        try {
            if (id) {
                const {data} = await getStaffWorkScheduleById(id);
                this.selectedRow = {
                    ...new StaffWorkScheduleObject(), ...data.data,
                };
            } else {
                this.selectedRow = new StaffWorkScheduleObject();
            }
            this.openShiftStatistics = true;
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    }

}
