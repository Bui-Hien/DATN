import React, {memo} from 'react';
import {useTranslation} from 'react-i18next';
import {useFormikContext} from "formik";
import {useStore} from "../../stores";
import {observer} from "mobx-react-lite";
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import {Button, ButtonGroup, Collapse, Grid} from "@mui/material";
import RotateLeftIcon from '@mui/icons-material/RotateLeft';

function CountryFilter() {
    const {countryStore} = useStore();
    const {
        isOpenFilter,
        handleFilter,
        handleCloseFilter
    } = countryStore;

    function handleResetFilter() {
        const newSearchObject = {
            ...JSON.parse(JSON.stringify({})),
        };
        handleFilter(newSearchObject);
    }

    return (
        <Collapse in={isOpenFilter} className="filterPopup">
            <div className="flex flex-column">
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <div className="filterContent pt-8">
                            <Grid container spacing={2} className={"flex flex-end"}>
                                <Grid item xs={12}>
                                    <p className='m-0 p-0 borderThrough2'>Nhân viên giới thiệu</p>
                                </Grid>
                                <Grid item xs={12}>
                                    <Grid
                                        container
                                        spacing={2}
                                        //   className=' flex-end'
                                    >
                                        {/*<Grid item xs={12} sm={6} md={4} lg={3}>*/}
                                        {/*    <GlobitsPagingAutocompleteV2*/}
                                        {/*        name='organization'*/}
                                        {/*        label='Đơn vị'*/}
                                        {/*        api={pagingAllOrg}*/}
                                        {/*    />*/}
                                        {/*</Grid>*/}

                                        {/*<Grid item xs={12} sm={6} md={4} lg={3}>*/}
                                        {/*    <GlobitsPagingAutocompleteV2*/}
                                        {/*        label={"Phòng ban"}*/}
                                        {/*        name='department'*/}
                                        {/*        api={pagingHasPermissionDepartments}*/}
                                        {/*        searchObject={{*/}
                                        {/*            pageIndex: 1,*/}
                                        {/*            pageSize: 9999,*/}
                                        {/*            keyword: "",*/}
                                        {/*            organizationId: values?.organization?.id,*/}
                                        {/*        }}*/}
                                        {/*        getOptionLabel={(option) =>*/}
                                        {/*            [option?.name, option?.code].filter(Boolean).join(" - ") || ""*/}
                                        {/*        }*/}
                                        {/*    />*/}
                                        {/*</Grid>*/}

                                        {/*<Grid item xs={12} sm={6} md={4} lg={3}>*/}
                                        {/*    <GlobitsPagingAutocompleteV2*/}
                                        {/*        name='positionTitle'*/}
                                        {/*        label='Chức danh'*/}
                                        {/*        api={pagingPositionTitle}*/}
                                        {/*        searchObject={{*/}
                                        {/*            departmentId: values?.department?.id,*/}
                                        {/*        }}*/}
                                        {/*    />*/}
                                        {/*</Grid>*/}
                                        {/*<Grid item xs={12} sm={6} md={4} lg={3}>*/}
                                        {/*    <GlobitsPagingAutocompleteV2*/}
                                        {/*        label="Nhân viên"*/}
                                        {/*        name="staff"*/}
                                        {/*        api={pagingStaff}*/}
                                        {/*        getOptionLabel={(option) =>*/}
                                        {/*            [option?.displayName, option?.staffCode].filter(Boolean).join(' - ') || ''*/}
                                        {/*        }*/}
                                        {/*    />*/}
                                        {/*</Grid>*/}
                                    </Grid>
                                </Grid>
                                <Grid item xs={12}>
                                    <p className='m-0 p-0 borderThrough2'>Nhân viên được giới thiệu</p>
                                </Grid>
                                <Grid item xs={12}>
                                    <Grid
                                        container
                                        spacing={2}
                                        //   className=' flex-end'
                                    >
                                        {/*<Grid item xs={12} sm={6} md={4} lg={3}>*/}
                                        {/*    <GlobitsPagingAutocompleteV2*/}
                                        {/*        name='introStaffOrganization'*/}
                                        {/*        label='Đơn vị'*/}
                                        {/*        api={pagingAllOrg}*/}
                                        {/*    />*/}
                                        {/*</Grid>*/}
                                        {/*<Grid item xs={12} sm={6} md={4} lg={3}>*/}
                                        {/*    <GlobitsPagingAutocompleteV2*/}
                                        {/*        label={"Phòng ban"}*/}
                                        {/*        name='introStaffDepartment'*/}
                                        {/*        api={pagingHasPermissionDepartments}*/}
                                        {/*        searchObject={{*/}
                                        {/*            pageIndex: 1,*/}
                                        {/*            pageSize: 9999,*/}
                                        {/*            keyword: "",*/}
                                        {/*            organizationId: values?.introStaffOrganization?.id,*/}
                                        {/*        }}*/}
                                        {/*        getOptionLabel={(option) =>*/}
                                        {/*            [option?.name, option?.code].filter(Boolean).join(" - ") || ""*/}
                                        {/*        }*/}
                                        {/*    />*/}
                                        {/*</Grid>*/}
                                        {/*<Grid item xs={12} sm={6} md={4} lg={3}>*/}
                                        {/*    <GlobitsPagingAutocompleteV2*/}
                                        {/*        name='introStaffPositionTitle'*/}
                                        {/*        label='Chức danh'*/}
                                        {/*        api={pagingPositionTitle}*/}
                                        {/*        searchObject={{*/}
                                        {/*            departmentId: values?.introStaffDepartment?.id,*/}
                                        {/*        }}*/}
                                        {/*    />*/}
                                        {/*</Grid>*/}
                                    </Grid>
                                </Grid>
                            </Grid>
                            <div className="py-8 mt-12 border-bottom-fade border-top-fade">
                                <div className="flex justify-end">
                                    <ButtonGroup
                                        color="container"
                                        aria-label="outlined primary button group"
                                    >
                                        <Button
                                            onClick={handleResetFilter}
                                            startIcon={<RotateLeftIcon/>}
                                        >
                                            Đặt lại
                                        </Button>
                                        <Button
                                            type="button"
                                            onClick={handleCloseFilter}
                                            startIcon={<HighlightOffIcon/>}
                                        >
                                            Đóng bộ lọc
                                        </Button>
                                    </ButtonGroup>
                                </div>
                            </div>
                        </div>
                    </Grid>
                </Grid>
            </div>
        </Collapse>
    );
}

export default memo(observer(CountryFilter));