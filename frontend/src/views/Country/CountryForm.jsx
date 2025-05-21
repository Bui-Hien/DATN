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

function CountryForm(props) {
    const {t} = useTranslation();
    const {countryStore} = useStore();

    const {
        handleClose,
        saveCountry,
        selectedRow,
        openCreateEditPopup,
    } = countryStore;

    const validationSchema = Yup.object({
        code: Yup.string()
            .trim()
            .required(t("validation.required")),
        name: Yup.string().trim().nullable().required(t("validation.required")),
        description: Yup.string().nullable(),
    });


    async function handleSaveForm(values) {
        await saveCountry(values);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Quốc gia")}
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
                                            label="Tên quốc gia"
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
                                            label="Mã quốc gia"
                                            name="code"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("code", codeValue);
                                            }}
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mô tả"
                                            name="description"
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

export default memo(observer(CountryForm));
