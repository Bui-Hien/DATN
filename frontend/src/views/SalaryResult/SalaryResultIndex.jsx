import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import SalaryResultForm from "./SalaryResultForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import SalaryResultList from "./SalaryResultList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import SalaryResultToolbar from "./SalaryResultToolbar";

function SalaryResultIndex() {
    const {salaryResultStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingSalaryResult,
        resetStore
    } = salaryResultStore;

    useEffect(() => {
        pagingSalaryResult()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Lương")},
                    {name: t("Bảng lương")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <SalaryResultToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <SalaryResultList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <SalaryResultForm/>
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

export default memo(observer(SalaryResultIndex));
