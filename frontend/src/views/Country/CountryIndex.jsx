import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import CountryForm from "./CountryForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import CountryList from "./CountryList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import CountryToolbar from "./CountryToolbar";

function CountryIndex() {
    const {countryStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingCountry,
    } = countryStore;

    useEffect(() => {
        pagingCountry()
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Danh mục chung")},
                    {name: t("Quốc gia")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <CountryToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <CountryList/>
                </div>
            </div>

            {openCreateEditPopup && (
                <CountryForm/>
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

export default memo(observer(CountryIndex));
