import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import TimeSheetDetailForm from "./TimeSheetDetailForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import TimeSheetDetailList from "./TimeSheetDetailList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import TimeSheetDetailToolbar from "./TimeSheetDetailToolbar";

function TimeSheetDetailIndex() {
    const {staffWorkScheduleStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        handleSetSearchObject,
        searchObject,
        pagingStaffWorkSchedule,
        resetStore,
        handleSetTimeSheetDetail
    } = staffWorkScheduleStore;

    useEffect(() => {
        handleSetTimeSheetDetail()
        handleSetSearchObject({
            ...searchObject,
            timeSheetDetail: true
        })
        pagingStaffWorkSchedule()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Chấm công")},
                    {name: t("Chấm công")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <TimeSheetDetailToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <TimeSheetDetailList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <TimeSheetDetailForm/>
            )}

            {openConfirmDeletePopup && (
                <AlertDialog
                    open={openConfirmDeletePopup}
                    onConfirmDialogClose={handleClose}
                    onYesClick={handleConfirmDelete}
                    title={t("confirm_dialog.delete.title")}
                    text={t("confirm_dialog.delete.text")}
                    agree={t("confirm_dialog.delete.agree")}
                    cancel={t("confirm_dialog.delete.cancel")}
                />
            )}

            {openConfirmDeleteListPopup && (
                <AlertDialog
                    open={openConfirmDeleteListPopup}
                    onConfirmDialogClose={handleClose}
                    onYesClick={handleConfirmDeleteMultiple}
                    title={t("confirm_dialog.delete_list.title")}
                    text={t("confirm_dialog.delete_list.text")}
                    agree={t("confirm_dialog.delete_list.agree")}
                    cancel={t("confirm_dialog.delete_list.cancel")}
                />
            )}
        </div>
    );
}

export default memo(observer(TimeSheetDetailIndex));
