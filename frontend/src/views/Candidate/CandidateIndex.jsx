import * as React from "react";
import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import CandidateList from "./CandidateList";
import AlertDialog from "../../common/CommonConfirmationDialog";
import CandidateToolbar from "./CandidateToolbar";
import Tab from "@mui/material/Tab";
import Tabs from "@mui/material/Tabs";
import {CandidateStatus} from "../../LocalConstants";

function CandidateIndex() {
    const {candidateStore} = useStore();
    const {t} = useTranslation();

    const {
        openConfirmDeleteListPopup,
        openConfirmDeletePopup,
        handleClose,
        handleConfirmDelete,
        handleConfirmDeleteMultiple,
        pagingCandidate,
        resetStore,
        setCurrentTab,
        currentTab
    } = candidateStore;

    useEffect(() => {
        pagingCandidate()

        return resetStore;
    }, []);

    const handleChange = (event, newValue) => {
        setCurrentTab(newValue);

    };
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Tuyển dụng")},
                    {name: t("Hồ sơ ứng viên")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <CandidateToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <Tabs value={currentTab} onChange={handleChange} aria-label="icon tabs with tooltip"
                          sx={{
                              borderBottom: 1,
                              borderColor: 'divider',
                              mb: 2,
                              '.MuiTab-root': {
                                  minWidth: 'unset',
                                  textTransform: 'none',
                                  padding: '8px 16px',
                                  fontWeight: 500,
                                  color: 'text.secondary',
                              },
                              '.Mui-selected': {
                                  color: 'primary.main',
                                  fontWeight: 600,
                                  borderBottom: '2px solid',
                                  borderColor: 'primary.main',
                              },
                          }}
                    >>
                        {CandidateStatus.getListData().map((tab, index) => (
                            <Tab
                                key={index}
                                label={tab.name}
                                className="!p-3 !min-w-0"
                                {...a11yProps(index)}
                            />
                        ))}
                    </Tabs>
                    <CandidateList/>
                </div>
            </div>

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

// Props hỗ trợ accessibility
function a11yProps(index) {
    return {
        id: `simple-tab-${index}`,
        'aria-controls': `simple-tabpanel-${index}`,
    };
}

export default memo(observer(CandidateIndex));
