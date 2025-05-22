import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import StaffLabourAgreementForm from "./StaffLabourAgreementForm";
import StaffLabourAgreementList from "./StaffLabourAgreementList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import StaffLabourAgreementToolbar from "./StaffLabourAgreementToolbar";

function StaffLabourAgreementIndex() {
    const {staffLabourAgreementStore, staffStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingStaffLabourAgreement,
        handleSetSearchObject,
        searchObject,
        resetStore
    } = staffLabourAgreementStore;

    useEffect(() => {
        handleSetSearchObject({
            ...searchObject,
            ownerId: staffStore.selectedRow?.id
        })
        pagingStaffLabourAgreement()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <StaffLabourAgreementToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <StaffLabourAgreementList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <StaffLabourAgreementForm/>
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

export default memo(observer(StaffLabourAgreementIndex));
