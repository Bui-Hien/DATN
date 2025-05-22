import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import EducationDegreeForm from "./CertificateForm";
import EducationDegreeList from "./CertificateList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import EducationDegreeToolbar from "./CertificateToolbar";

function CertificateIndex() {
    const {certificateStore, staffStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingCertificate,
        handleSetSearchObject,
        searchObject,
        resetStore
    } = certificateStore;

    useEffect(() => {
        handleSetSearchObject({
            ...searchObject,
            ownerId: staffStore.selectedRow?.id
        })
        pagingCertificate()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <EducationDegreeToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <EducationDegreeList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <EducationDegreeForm/>
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

export default memo(observer(CertificateIndex));
