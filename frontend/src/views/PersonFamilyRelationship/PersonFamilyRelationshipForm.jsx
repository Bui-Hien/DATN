import React, {memo} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import * as Yup from "yup";
import CommonPopupV2 from "../../common/CommonPopupV2";
import {Button, DialogActions, DialogContent} from "@mui/material";
import CommonTextField from "../../common/form/CommonTextField";
import {observer} from "mobx-react-lite";
import SaveIcon from '@mui/icons-material/Save';
import CloseIcon from "@mui/icons-material/Close";
import {removeVietnameseTones} from "../../LocalFunction";
import CommonDateTimePicker from "../../common/form/CommonDateTimePicker";
import {pagingReligion} from "../Religion/ReligionService";
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import {pagingFamilyRelationship} from "../FamilyRelationship/FamilyRelationshipService";
import {pagingProfession} from "../Profession/ProfessionService";

function PersonFamilyRelationshipForm(props) {
    const {t} = useTranslation();
    const {personFamilyRelationshipStore, staffStore} = useStore();

    const {
        handleClose,
        savePersonFamilyRelationship,
        selectedRow,
        openCreateEditPopup,
    } = personFamilyRelationshipStore;


    const validationSchema = Yup.object({
        familyRelationship: Yup.object().nullable().required(t("validation.required")),
        fullName: Yup.string().nullable().required(t("validation.required")),
        address: Yup.string().nullable(),
        birthDate: Yup.date().nullable(),
        profession: Yup.object().nullable(),
    });


    async function handleSaveForm(values) {
        if (!staffStore.selectedRow?.id) return
        const newValues = {
            ...values,
            person: staffStore.selectedRow
        }
        await savePersonFamilyRelationship(newValues);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Quan hệ nhân thân")}
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
                                        <CommonTextField
                                            label="Họ và tên"
                                            name="fullName"
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <CommonDateTimePicker
                                            label={t("Ngày sinh")}
                                            name="birthDate"
                                            disableFuture
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonPagingAutocompleteV2
                                            label={t("Loại quan hệ")}
                                            name="familyRelationship"
                                            api={pagingFamilyRelationship}
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonPagingAutocompleteV2
                                            label="Nghề nghiệp"
                                            name="profession"
                                            api={pagingProfession}
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Địa chỉ"
                                            name="address"
                                            multiline
                                            rows={3}
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

export default memo(observer(PersonFamilyRelationshipForm));
