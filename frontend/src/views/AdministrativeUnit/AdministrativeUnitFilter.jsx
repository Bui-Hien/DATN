import React, {memo} from 'react';
import {useFormikContext} from "formik";
import {useStore} from "../../stores";
import {observer} from "mobx-react-lite";
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import {Button, ButtonGroup, Collapse} from "@mui/material";
import RotateLeftIcon from '@mui/icons-material/RotateLeft';
import {SearchObject} from "./SearchObject";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import {AdministrativeUnitLevel} from "../../LocalConstants";
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import {pagingAdministrativeUnit as ApiPagingAdministrativeUnit} from "./AdministrativeUnitService";

function AdministrativeUnitFilter() {
    const {administrativeUnitStore} = useStore();
    const {
        isOpenFilter,
        handleCloseFilter,
        pagingTreeAdministrativeUnit,
        handleSetSearchObject,
    } = administrativeUnitStore;
    const {resetForm, setFieldValue, values} = useFormikContext();

    async function handleResetFilter() {
        const newSearchObject = {
            ...JSON.parse(JSON.stringify(new SearchObject())),
        };
        resetForm();
        handleSetSearchObject(newSearchObject)
        await pagingTreeAdministrativeUnit()
    }

    return (<Collapse in={isOpenFilter} className="filterPopup">
        <div className={"gap-2 grid grid-cols-12"}>
            <div className="grid grid-cols-12 gap-4 col-span-12">
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonSelectInput
                        label={"Cấp độ"}
                        name={"level"}
                        keyValue='value'
                        options={AdministrativeUnitLevel.getListData()}
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonPagingAutocompleteV2
                        label={"Tỉnh/Thành phố"}
                        name={"province"}
                        searchObject={{level: AdministrativeUnitLevel.PROVINCE.value}}
                        handleChange={(_, value) => {
                            setFieldValue("province", value);
                            setFieldValue("district", null);
                            setFieldValue("ward", null);
                        }}
                        api={ApiPagingAdministrativeUnit}
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonPagingAutocompleteV2
                        label={"Quận/Huyện"}
                        name={"district"}
                        searchObject={{
                            level: AdministrativeUnitLevel.DISTRICT.value, provinceId: values?.province?.id
                        }}
                        handleChange={(_, value) => {
                            setFieldValue("district", value);
                            setFieldValue("ward", null);
                        }}
                        api={ApiPagingAdministrativeUnit}
                        disabled={!values?.province?.id}
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonPagingAutocompleteV2
                        label={"Xã/Phường"}
                        name={"ward"}
                        searchObject={{
                            level: AdministrativeUnitLevel.WARD.value, districtId: values?.district?.id
                        }}
                        api={ApiPagingAdministrativeUnit}
                        disabled={!values?.district?.id}
                    />
                </div>
            </div>

            <div className="col-span-12 border-t border-gray-300">
                <div className="mt-4 flex justify-end">
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
    </Collapse>);
}

export default memo(observer(AdministrativeUnitFilter));