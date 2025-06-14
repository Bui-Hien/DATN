import React, {memo, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import CommonPopupV2 from "../../common/CommonPopupV2";
import {Button, DialogActions, DialogContent} from "@mui/material";
import {observer} from "mobx-react-lite";
import CloseIcon from "@mui/icons-material/Close";
import {ShiftWorkStatus, ShiftWorkType} from "../../LocalConstants";
import {getDate, getDateTime} from "../../LocalFunction";

function StaffShiftStatistics() {
    const {t} = useTranslation();
    const {staffWorkScheduleStore} = useStore();
    const [shiftWorkType, setShiftWorkType] = useState(ShiftWorkType.getListData());
    const [shiftWorkStatus, setShiftWorkStatus] = useState(ShiftWorkStatus.getListData());
    const {
        handleClose,
        selectedRow,
        openShiftStatistics,
    } = staffWorkScheduleStore;

    useEffect(() => {
        const shiftWorkType = ShiftWorkType.getListData().find(i => i.value === selectedRow?.shiftWorkType);
        const shiftWorkStatus = ShiftWorkStatus.getListData().find(i => i.value === selectedRow?.shiftWorkStatus);
        setShiftWorkType(shiftWorkType);
        setShiftWorkStatus(shiftWorkStatus);
    }, [selectedRow?.id])

    const calculateEarlyLateHour = (actualTime, expectedTime, type = 'early') => {
        if (!actualTime || !expectedTime) return 0;

        const actual = new Date(actualTime);
        const expected = new Date(expectedTime);

        if (isNaN(actual.getTime()) || isNaN(expected.getTime())) return 0;

        const actualHours = actual.getHours() + actual.getMinutes() / 60;
        const expectedHours = expected.getHours() + expected.getMinutes() / 60;
        const diff = actualHours - expectedHours;

        if (type === 'early' && diff < 0) return +Math.abs(diff).toFixed(1);
        if (type === 'late' && diff > 0) return +Math.abs(diff).toFixed(1);

        return 0;
    };

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openShiftStatistics}
            noDialogContent
            title={"Chi tiết ca làm việc"}
            onClosePopup={handleClose}
            noIcon={true}
        >
            <>
                <DialogContent className="p-6">
                    <div>
                        <span className="text-lg font-semibold block mb-4">Thông tin ca làm việc</span>
                        <table className="table-auto w-full border border-gray-300">
                            <tbody>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Ngày làm việc</td>
                                <td className="border px-4 py-2">{getDate(selectedRow?.workingDate)}</td>
                            </tr>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Người phân ca</td>
                                <td className="border px-4 py-2">{selectedRow?.coordinator?.displayName}</td>
                            </tr>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Thời gian checkin</td>
                                <td className="border px-4 py-2">
                                    {getDateTime(selectedRow?.checkIn)}
                                </td>
                            </tr>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Thời gian checkout</td>
                                <td className="border px-4 py-2">
                                    {getDateTime(selectedRow?.checkOut)}
                                </td>
                            </tr>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Số giờ đi sớm</td>
                                <td className="border px-4 py-2">
                                    {calculateEarlyLateHour(selectedRow?.checkIn, shiftWorkType.startTime, 'early')} {" giờ"}
                                </td>
                            </tr>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Số giờ về sớm</td>
                                <td className="border px-4 py-2">
                                    {calculateEarlyLateHour(selectedRow?.checkOut, shiftWorkType.endTime, 'early')} {" giờ"}
                                </td>
                            </tr>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Số giờ đi muộn</td>
                                <td className="border px-4 py-2">
                                    {calculateEarlyLateHour(selectedRow?.checkIn, shiftWorkType.startTime, 'late')} {" giờ"}
                                </td>
                            </tr>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Số giờ về muộn</td>
                                <td className="border px-4 py-2">
                                    {calculateEarlyLateHour(selectedRow?.checkOut, shiftWorkType.endTime, 'late')} {" giờ"}
                                </td>
                            </tr>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Số công được tính</td>
                                <td className="border px-4 py-2">{shiftWorkType.calculatedWorkingDay}</td>
                            </tr>
                            <tr>
                                <td className="border px-4 py-2 font-medium">Trạng thái ca làm việc</td>
                                <td className="border px-4 py-2">{shiftWorkStatus.name}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </DialogContent>
                <DialogActions className="px-6 pb-4">
                    <div className="flex justify-end w-full">
                        <Button
                            variant="outlined"
                            color="secondary"
                            onClick={handleClose}
                            className="rounded-lg px-4 py-2 !mr-2 !bg-red-500"
                            startIcon={<CloseIcon/>}
                        >
                            {t("general.button.close")}
                        </Button>
                    </div>
                </DialogActions>
            </>

        </CommonPopupV2>
    );
}

export default memo(observer(StaffShiftStatistics));
