import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import RecruitmentRequestForm from "./RecruitmentRequestForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import RecruitmentRequestList from "./RecruitmentRequestList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import RecruitmentRequestToolbar from "./RecruitmentRequestToolbar";

function RecruitmentRequestIndex() {
    const {recruitmentRequestStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingRecruitmentRequest,
        resetStore
    } = recruitmentRequestStore;

    useEffect(() => {
        pagingRecruitmentRequest()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Tuyển dụng")},
                    {name: t("Yêu cầu tuyển dụng")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <RecruitmentRequestToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <RecruitmentRequestList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <RecruitmentRequestForm/>
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

export default memo(observer(RecruitmentRequestIndex));
