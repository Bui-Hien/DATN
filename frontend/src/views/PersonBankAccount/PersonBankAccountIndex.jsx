import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import PersonBankAccountForm from "./PersonBankAccountForm";
import PersonBankAccountList from "./PersonBankAccountList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import PersonBankAccountToolbar from "./PersonBankAccountToolbar";

function PersonBankAccountIndex() {
    const {personBankAccountStore, staffStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingPersonBankAccount,
        handleSetSearchObject,
        searchObject,
        resetStore
    } = personBankAccountStore;

    useEffect(() => {
        handleSetSearchObject({
            ...searchObject,
            ownerId: staffStore.selectedRow?.id
        })
        pagingPersonBankAccount()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <PersonBankAccountToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <PersonBankAccountList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <PersonBankAccountForm/>
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

export default memo(observer(PersonBankAccountIndex));
