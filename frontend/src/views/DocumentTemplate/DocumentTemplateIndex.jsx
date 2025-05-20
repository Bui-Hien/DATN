import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import DocumentTemplateForm from "./DocumentTemplateForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import DocumentTemplateList from "./DocumentTemplateList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import DocumentTemplateToolbar from "./DocumentTemplateToolbar";

function DocumentTemplateIndex() {
    const {documentTemplateStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingDocumentTemplate,
        resetStore
    } = documentTemplateStore;

    useEffect(() => {
        pagingDocumentTemplate()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Nhân viên")},
                    {name: t("Mẫu hồ sơ chung")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <DocumentTemplateToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <DocumentTemplateList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <DocumentTemplateForm/>
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

export default memo(observer(DocumentTemplateIndex));
