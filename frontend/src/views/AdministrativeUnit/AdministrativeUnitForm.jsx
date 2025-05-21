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
import CommonSelectInput from "../../common/form/CommonSelectInput";
import {AdministrativeUnitLevel} from "../../LocalConstants";
import {pagingAdministrativeUnit} from "./AdministrativeUnitService";
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import {removeVietnameseTones} from "../../LocalFunction";

function AdministrativeUnitForm(props) {
    const {t} = useTranslation();
    const {administrativeUnitStore} = useStore();

    const {
        handleClose,
        saveAdministrativeUnit,
        selectedRow,
        openCreateEditPopup,
    } = administrativeUnitStore;

    const validationSchema = Yup.object({
        level: Yup.number().required(t("validation.required")),
        code: Yup.string().trim().required(t("validation.required")),
        name: Yup.string().trim().nullable().required(t("validation.required")),
        parent: Yup.object().nullable(),
    });


    async function handleSaveForm(values) {
        await saveAdministrativeUnit(values);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Đơn vị hành chính")}
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
                                        <CommonSelectInput
                                            label={"Cấp độ"}
                                            name={"level"}
                                            keyValue='value'
                                            options={AdministrativeUnitLevel.getListData()}
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Tên đơn vị hành chính"
                                            name="name"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("name", nameValue);
                                                setFieldValue("code", codeValue);
                                            }}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mã đơn vị hành chính"
                                            name="code"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("code", codeValue);
                                            }}
                                            required/>
                                    </div>
                                    {values?.level !== AdministrativeUnitLevel.PROVINCE.value && (
                                        <div className="col-span-12">
                                            <CommonPagingAutocompleteV2
                                                label={"Trực thuộc đơn vị"}
                                                name={"parent"}
                                                searchObject={values?.level ? {level: values.level - 1} : null}
                                                api={pagingAdministrativeUnit}
                                                disabled={!values?.level}
                                            />
                                        </div>
                                    )}
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

export default memo(observer(AdministrativeUnitForm));
