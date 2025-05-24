import React, {memo} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import * as Yup from "yup";
import CommonPopupV2 from "../../common/CommonPopupV2";
import {Button, DialogActions, DialogContent} from "@mui/material";
import {observer} from "mobx-react-lite";
import SaveIcon from '@mui/icons-material/Save';
import CloseIcon from "@mui/icons-material/Close";
import {ShiftWorkType} from "../../LocalConstants";
import CommonDateTimePicker from "../../common/form/CommonDateTimePicker";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import ChooseSelectedStaff from "../../common/CommonSelectedStaff/ChooseSelectedStaff";

function StaffWorkScheduleForm(props) {
    const {t} = useTranslation();
    const {staffWorkScheduleStore} = useStore();

    const {
        handleClose,
        saveStaffWorkSchedule,
        selectedRow,
        openCreateEditPopup,
    } = staffWorkScheduleStore;

    const validationSchema = Yup.object({
        shiftWorkType: Yup.number().required(t("validation.required")),
        staff: Yup.object().required(t("validation.required")),
        workingDate: Yup.date().required(t("validation.required")),
    });


    async function handleSaveForm(values) {
        await saveStaffWorkSchedule(values);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Ca làm việc")}
            onClosePopup={handleClose}
            isCreate={!selectedRow?.id}
        >
            <Formik
                validationSchema={validationSchema}
                enableReinitialize
                initialValues={selectedRow}
                onSubmit={handleSaveForm}
            >
                {({isSubmitting, values, setFieldValue, initialValues}) => {
                    return (
                        <Form autoComplete="off">
                            <DialogContent className="p-6">
                                <div className={"grid grid-cols-12 gap-2"}>
                                    <div className="col-span-12">
                                        <ChooseSelectedStaff
                                            name={"staff"}
                                            multiline={false}
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonSelectInput
                                            label={t("Ca làm việc")}
                                            name="shiftWorkType"
                                            options={ShiftWorkType.getListData()}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonDateTimePicker
                                            label={t("Ngày làm việc")}
                                            name="workingDate"
                                            required
                                        />
                                    </div>
                                </div>
                            </DialogContent>

                            <DialogActions className="px-6 pb-4">
                                <div className="flex justify-end w-full">
                                    <Button
                                        variant="outlined"
                                        color="secondary"
                                        disabled={isSubmitting}
                                        onClick={handleClose}
                                        className="rounded-lg px-4 py-2 !mr-2 !bg-red-500"
                                        startIcon={<CloseIcon/>}
                                    >
                                        {t("general.button.close")}
                                    </Button>
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        type="submit"
                                        disabled={isSubmitting}
                                        className="rounded-lg px-4 py-2"
                                        startIcon={<SaveIcon/>}
                                    >
                                        {t("general.button.save")}
                                    </Button>
                                </div>
                            </DialogActions>
                        </Form>
                    );
                }
                }
            </Formik>
        </CommonPopupV2>
    );
}

export default memo(observer(StaffWorkScheduleForm));
