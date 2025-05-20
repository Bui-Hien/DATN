import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import DepartmentForm from "./DepartmentForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import DepartmentList from "./DepartmentList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import DepartmentToolbar from "./DepartmentToolbar";

function DepartmentIndex() {
    const {departmentStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingTreeDepartment,
        resetStore
    } = departmentStore;

    useEffect(() => {
        pagingTreeDepartment()
        return resetStore
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Cơ cấu tổ chức")},
                    {name: t("Phòng ban")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <DepartmentToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <DepartmentList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <DepartmentForm/>
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

export default memo(observer(DepartmentIndex));
