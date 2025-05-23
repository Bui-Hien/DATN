import React, {memo} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import {Button, ButtonGroup} from "@mui/material";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SearchIcon from "@mui/icons-material/Search";
import CommonTextField from "../../common/form/CommonTextField";
import {observer} from "mobx-react-lite";
import {useNavigate} from "react-router-dom";

function SalaryResultItemToolbar() {
    const navigate = useNavigate();
    const {t} = useTranslation();
    const {salaryResultItemStore, salaryResultStore} = useStore();

    const {
        pagingSalaryResultItem,
        searchObject,
        handleSetSearchObject,
    } = salaryResultItemStore;

    async function handleFilter(values) {
        const newSearchObject = {
            ...values,
            pageIndex: 1,
        };
        handleSetSearchObject(newSearchObject);
        await pagingSalaryResultItem();
    }

    return (
        <Formik enableReinitialize initialValues={searchObject} onSubmit={handleFilter}>
            {({resetForm, values, setFieldValue, setValues}) => (
                <Form autoComplete="off" className="w-full">
                    <div className="flex flex-wrap items-center justify-between gap-4 mb-4">
                        {/* Left buttons */}
                        <div className="flex space-x-2">
                            <div className="flex justify-end">
                                <ButtonGroup
                                    color="container"
                                    aria-label="outlined primary button group"
                                >
                                    <Button
                                        startIcon={<ArrowBackIcon/>}
                                        onClick={() => navigate(`/salary-result`)}
                                    >
                                        {t("Quay lại")}
                                    </Button>
                                </ButtonGroup>
                            </div>
                        </div>

                        {/* Right search + filter */}
                        <div className="flex items-center gap-2 flex-1 max-w-xl">
                            <CommonTextField
                                name="keyword"
                                placeholder={t("Nhập từ khóa tìm kiếm")}
                                className="flex-grow"
                            />
                            <div className="inline-flex rounded-md shadow-sm" role="group">
                                <ButtonGroup
                                    color="container"
                                    aria-label="outlined primary button group"
                                >
                                    <Button
                                        className="whitespace-nowrap"
                                        type="submit"
                                        startIcon={<SearchIcon/>}
                                    >
                                        {t("general.button.search") || "Tìm kiếm"}
                                    </Button>
                                </ButtonGroup>
                            </div>
                        </div>
                    </div>
                </Form>
            )}
        </Formik>
    );
}

export default memo(observer(SalaryResultItemToolbar));
