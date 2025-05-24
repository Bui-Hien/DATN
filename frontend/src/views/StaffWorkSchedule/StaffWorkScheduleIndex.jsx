import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import StaffWorkScheduleForm from "./StaffWorkScheduleForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import StaffWorkScheduleList from "./StaffWorkScheduleList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import StaffWorkScheduleToolbar from "./StaffWorkScheduleToolbar";
import StaffWorkScheduleFormList from "./StaffWorkScheduleList/StaffWorkScheduleFormList";

function StaffWorkScheduleIndex() {
    const {staffWorkScheduleStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        openCreateEditListPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingStaffWorkSchedule,
        resetStore
    } = staffWorkScheduleStore;

    useEffect(() => {
        pagingStaffWorkSchedule()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Chấm công")},
                    {name: t("Phân ca làm việc")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <StaffWorkScheduleToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <StaffWorkScheduleList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <StaffWorkScheduleForm/>
            )}
            {openCreateEditListPopup && (
                <StaffWorkScheduleFormList/>
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

export default memo(observer(StaffWorkScheduleIndex));
