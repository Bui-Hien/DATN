import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import CandidateWorkingExperienceForm from "./CandidateWorkingExperienceForm";
import AlertDialog from "../../common/CommonConfirmationDialog";
import CandidateWorkingExperienceToolbar from "./CandidateWorkingExperienceToolbar";
import CandidateWorkingExperienceList from "./CandidateWorkingExperienceList";

function CandidateWorkingExperienceIndex() {
    const {candidateWorkingExperienceStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingCandidateWorkingExperience,
        resetStore,
        searchObject,
        handleSetSearchObject,
    } = candidateWorkingExperienceStore;

    const innitSearchObject = async () => {
        handleSetSearchObject({
            ...searchObject,
            owner: "id candidate",
        })
        await pagingCandidateWorkingExperience()
    }

    useEffect(() => {
        innitSearchObject();
        return resetStore
    }, []);
    return (
        <div className="content-index">
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <CandidateWorkingExperienceToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <CandidateWorkingExperienceList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <CandidateWorkingExperienceForm/>
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

export default memo(observer(CandidateWorkingExperienceIndex));
