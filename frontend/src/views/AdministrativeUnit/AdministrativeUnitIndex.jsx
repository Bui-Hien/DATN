import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import AdministrativeUnitForm from "./AdministrativeUnitForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import AdministrativeUnitList from "./AdministrativeUnitList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import AdministrativeUnitToolbar from "./AdministrativeUnitToolbar";

function AdministrativeUnitIndex() {
    const {administrativeUnitStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingTreeAdministrativeUnit,
        resetStore
    } = administrativeUnitStore;

    useEffect(() => {
        pagingTreeAdministrativeUnit()
        return resetStore
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Danh mục chung")},
                    {name: t("Đơn vị hành chính")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <AdministrativeUnitToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <AdministrativeUnitList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <AdministrativeUnitForm/>
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

export default memo(observer(AdministrativeUnitIndex));
