import React, {memo} from 'react';
import {useFormikContext} from "formik";
import {useStore} from "../../stores";
import {observer} from "mobx-react-lite";
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import {Button, ButtonGroup, Collapse} from "@mui/material";
import RotateLeftIcon from '@mui/icons-material/RotateLeft';
import {SearchObject} from "./SearchObject";
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import {pagingStaff} from "../Staff/StaffService";
import {pagingPosition} from "../Position/PositionService";

function CandidateFilter() {
    const {candidateStore} = useStore();
    const {
        isOpenFilter,
        handleCloseFilter,
        pagingCandidate,
        handleSetSearchObject,
    } = candidateStore;
    const {resetForm, setFieldValue} = useFormikContext();

    async function handleResetFilter() {
        const newSearchObject = {
            ...JSON.parse(JSON.stringify(new SearchObject())),
        };
        resetForm();
        handleSetSearchObject(newSearchObject)
        await pagingCandidate()
    }

    return (<Collapse in={isOpenFilter} className="filterPopup">
        <div className={"gap-2 grid grid-cols-12"}>
            <div className="grid grid-cols-12 gap-4 col-span-12">
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonPagingAutocompleteV2
                        label={"Người đề xuất tuyển dụng"}
                        name={"proposer"}
                        handleChange={(_, value) => {
                            setFieldValue("proposer", value);
                            setFieldValue("proposerId", value?.id);
                        }}
                        api={pagingStaff}
                        getOptionLabel={(option) =>
                            option?.staffCode && option?.displayName
                                ? `${option.staffCode} - ${option.displayName}`
                                : option?.staffCode || option?.displayName || ""
                        }
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonPagingAutocompleteV2
                        label={"Vị trí tuyển dụng"}
                        name={"position"}
                        handleChange={(_, value) => {
                            setFieldValue("position", value);
                            setFieldValue("positionId", value?.id);
                        }}
                        api={pagingPosition}
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

export default memo(observer(CandidateFilter));