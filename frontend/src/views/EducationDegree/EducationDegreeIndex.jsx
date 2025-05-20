import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import EducationDegreeForm from "./EducationDegreeForm";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import EducationDegreeList from "./EducationDegreeList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import EducationDegreeToolbar from "./EducationDegreeToolbar";

function EducationDegreeIndex() {
    const {educationDegreeStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        openCreateEditPopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingEducationDegree,
        resetStore
    } = educationDegreeStore;

    useEffect(() => {
        pagingEducationDegree()

        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Danh mục chung")},
                    {name: t("Bằng cấp")}
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

export default memo(observer(EducationDegreeIndex));
