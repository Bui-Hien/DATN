import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import SalaryPeriodForm from "./SalaryPeriodForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import SalaryPeriodList from "./SalaryPeriodList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import SalaryPeriodToolbar from "./SalaryPeriodToolbar";

function SalaryPeriodIndex() {
    const {salaryPeriodStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingSalaryPeriod,
        resetStore
    } = salaryPeriodStore;

    useEffect(() => {
        pagingSalaryPeriod()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Lương")},
                    {name: t("Kỳ lương")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <SalaryPeriodToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <SalaryPeriodList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <SalaryPeriodForm/>
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

export default memo(observer(SalaryPeriodIndex));
