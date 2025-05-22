import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import SalaryTemplateForm from "./SalaryTemplateForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import SalaryTemplateList from "./SalaryTemplateList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import SalaryTemplateToolbar from "./SalaryTemplateToolbar";

function SalaryTemplateIndex() {
    const {salaryTemplateStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingSalaryTemplate,
        resetStore
    } = salaryTemplateStore;

    useEffect(() => {
        pagingSalaryTemplate()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Lương")},
                    {name: t("Mẫu bảng lương")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <SalaryTemplateToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <SalaryTemplateList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <SalaryTemplateForm/>
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

export default memo(observer(SalaryTemplateIndex));
