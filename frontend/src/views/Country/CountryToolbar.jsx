import React, {memo} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import CountryFilter from "./CountryFilter";
import {useStore} from "../../stores";
import {Button, ButtonGroup, Grid, Tooltip} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import DeleteIcon from "@mui/icons-material/Delete";
import CommonTextField from "../../common/form/CommonTextField";
import SearchIcon from "@mui/icons-material/Search";
import FilterListIcon from "@mui/icons-material/FilterList";
import {observer} from "mobx-react-lite";

function CountryToolbar() {
    const {countryStore} = useStore();
    const {t} = useTranslation();

    const {
        handleDeleteCountry,
        pagingCountry,
        handleOpenCreateEdit,
        searchObject,
        listOnDelete,
        handleSetSearchObject,
        isOpenFilter,
        handleCloseFilter,
        handleTogglePopupFilter
    } = countryStore;

    async function handleFilter(values) {

        const newSearchObject = {
            ...values,
            pageIndex: 1,
        };
        handleSetSearchObject(newSearchObject);
        await pagingCountry();
    }

    return (
        <Formik
            enableReinitialize
            initialValues={searchObject}
            onSubmit={handleFilter}
        >
            {({resetForm, values, setFieldValue, setValues}) => {
                return (
                    <Form autoComplete="off">
                        <Grid item xs={12}>
                            <Grid container spacing={2}>
                                <Grid item xs={6}>
                                    <ButtonGroup
                                        color="container"
                                        aria-label="outlined primary button group"
                                    >
                                        <Button
                                            startIcon={<AddIcon/>}
                                            onClick={() => handleOpenCreateEdit()}
                                        >
                                            {t("general.button.add")}
                                        </Button>
                                        <Button
                                            fullWidth
                                            startIcon={<DeleteIcon/>}
                                            onClick={handleDeleteCountry}
                                            disabled={listOnDelete?.length <= 0}
                                        >
                                            Xóa
                                        </Button>
                                    </ButtonGroup>
                                </Grid>
                                <Grid item xs={12}>
                                    <div className="flex justify-between align-center">
                                        <Tooltip placement="top" title="Tìm kiếm theo từ khóa...">
                                            {/*<CommonTextField*/}
                                            {/*    placeholder="Tìm kiếm theo từ khóa..."*/}
                                            {/*    name="keyword"*/}
                                            {/*    variant="outlined"*/}
                                            {/*    notDelay*/}
                                            {/*/>*/}
                                        </Tooltip>

                                        <ButtonGroup
                                            className="filterButtonV4"
                                            color="container"
                                            aria-label="outlined primary button group"
                                        >
                                            <Button
                                                startIcon={<SearchIcon className={``}/>}
                                                className="ml-8 d-inline-flex py-2 px-8 btnHrStyle"
                                                type="submit"
                                            >
                                                Tìm kiếm
                                            </Button>
                                            <Button
                                                startIcon={<FilterListIcon
                                                    className={` filterRotateIcon ${isOpenFilter && 'onRotate'}`}/>}
                                                className=" d-inline-flex py-2 px-8 btnHrStyle"
                                                onClick={handleTogglePopupFilter}
                                            >
                                                Bộ lọc
                                            </Button>
                                        </ButtonGroup>
                                    </div>
                                </Grid>
                            </Grid>

                            <CountryFilter
                                isOpenFilter={isOpenFilter}
                                handleFilter={handleFilter}
                                handleCloseFilter={handleCloseFilter}
                            />
                        </Grid>
                    </Form>
                );
            }}
        </Formik>
    );
}

export default memo(observer(CountryToolbar));