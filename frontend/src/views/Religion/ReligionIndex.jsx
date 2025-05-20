import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import EducationDegreeForm from "./ReligionForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import EducationDegreeList from "./ReligionList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import EducationDegreeToolbar from "./ReligionToolbar";

function ReligionIndex() {
    const {religionStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingReligion,
        resetStore
    } = religionStore;

    useEffect(() => {
        pagingReligion()
        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Danh mục chung")},
                    {name: t("Tôn giáo")}
                ]}/>
            </div>
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

export default memo(observer(ReligionIndex));
