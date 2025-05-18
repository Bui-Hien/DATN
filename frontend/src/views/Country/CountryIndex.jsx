import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import CountryToolbar from "./CountryToolbar";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import {Grid} from "@mui/material";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import CommonConfirmationDialog from "../../common/CommonConfirmationDialog";
import CountryForm from "./CountryForm";
import CountryList from "./CountryList";

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
            <div className="index-breadcrumb py-6">
                <CommonBreadcrumb routeSegments={[
                    {name: "Nhân viên"},
                    {name: t("navigation.staff.introduceCost")}
                ]}/>
            </div>
            <Grid className="index-card" container spacing={2}>
                <Grid item xs={12}>
                    <CountryToolbar/>
                </Grid>

                <Grid item xs={12}>
                    <CountryList/>
                </Grid>
            </Grid>

            {openCreateEditPopup && (
                <CountryForm/>
            )}

            {openConfirmDeletePopup && (
                <CommonConfirmationDialog
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
                <CommonConfirmationDialog
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
