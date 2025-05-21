import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import DepartmentIpForm from "./DepartmentIpForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import AlertDialog from "../../common/CommonConfirmationDialog";
import DepartmentIpToolbar from "./DepartmentIpToolbar";
import DepartmentIpList from "./DepartmentIpList";

function DepartmentIpIndex() {
    const {departmentIpStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingDepartmentIp,
        resetStore
    } = departmentIpStore;

    useEffect(() => {
        pagingDepartmentIp()
        return resetStore
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Chấm công")},
                    {name: t("White list")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <DepartmentIpToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <DepartmentIpList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <DepartmentIpForm/>
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

export default memo(observer(DepartmentIpIndex));
