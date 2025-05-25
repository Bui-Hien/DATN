import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import CommonTable from "../../common/CommonTable";
import {ShiftWorkType, ShiftWorkStatus} from "../../LocalConstants";
import {getDate} from "../../LocalFunction";
import {Tooltip} from "@mui/material";

function StaffWorkScheduleSummaryList() {
    const {t} = useTranslation();
    const {staffWorkScheduleSummaryStore} = useStore();

    const {
        totalPages,
        dataList,
        totalElements,
        setPageIndex,
        searchObject,
        setPageSize,
    } = staffWorkScheduleSummaryStore;

    // Base columns for staff info
    const baseColumns = [
        {
            accessorKey: "staffCode",
            header: "Mã nhân viên",
        },
        {
            accessorKey: "displayName",
            header: "Tên nhân viên",
        },
    ];

    // 1. Tạo danh sách tất cả ngày trong tháng 5/2025 (từ 1/5 đến 31/5)
    const allDates = [];
    const startDate = new Date(2025, 4, 1); // Tháng 5 (index 4)
    const endDate = new Date(2025, 4, 31);

    for (let d = new Date(startDate); d <= endDate; d.setDate(d.getDate() + 1)) {
        allDates.push(new Date(d));
    }

    // 2. Tạo dynamic columns cho tất cả ngày
    const dynamicColumns = allDates.map((date) => {
        const dateKey = getDate(date);
        const dayOfWeek = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'][date.getDay()];
        const formattedDate = `${date.getDate()}/${date.getMonth() + 1}`;

        return {
            accessorKey: dateKey,
            header: (
                <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                    <span style={{fontSize: '0.8em'}}>{dayOfWeek}</span>
                    <span>{formattedDate}</span>
                </div>
            ),
            Cell: ({row}) => {
                const shiftWorkStatus = row.getValue(dateKey);

                // Lấy thông tin làm việc cụ thể từ dữ liệu gốc
                const getWorkSchedule = () => {
                    return row.original.staffWorkSchedules?.find(
                        item => getDate(item.workingDate) === dateKey
                    );
                };

                const schedule = getWorkSchedule();

                // Tính số công được tính
                let calculatedWorkingDay = 0;
                if (schedule && schedule.shiftWorkType) {
                    const shiftType = ShiftWorkType.getListData().find(i => i.value === schedule.shiftWorkType);
                    calculatedWorkingDay = shiftType ? (shiftType.calculatedWorkingDay || 0) : 0;
                }
                const workStatus = schedule?.shiftWorkStatus
                    ? ShiftWorkStatus.getListData().find(i => i.value === schedule.shiftWorkStatus)
                    : null;
                // Hiển thị số công thay vì tên trạng thái
                let displayValue = 0;
                if (workStatus?.value === ShiftWorkStatus.WORKED_FULL_HOURS.value) {
                    displayValue = calculatedWorkingDay;
                }
                // Hàm hiển thị thông tin checkin/checkout
                const renderTooltipContent = () => {
                    // Nếu không có schedule thì hiển thị thông báo không có dữ liệu
                    if (!schedule) {
                        return (
                            <div className="p-2 text-sm">
                                <div className="text-gray-500">Ngày làm việc không được phân công</div>
                            </div>
                        );
                    }

                    // Format thời gian checkin/checkout
                    const formatTime = (dateString) => {
                        if (!dateString) return "Chưa có dữ liệu";
                        const date = new Date(dateString);
                        return date.toLocaleTimeString('vi-VN', {
                            hour: '2-digit',
                            minute: '2-digit',
                            hour12: false
                        });
                    };

                    // Kiểm tra xem có dữ liệu check-in/check-out không
                    const hasCheckinData = schedule.checkIn;
                    const hasCheckoutData = schedule.checkOut;

                    // Lấy thông tin loại ca
                    const shiftType = schedule.shiftWorkType
                        ? ShiftWorkType.getListData().find(i => i.value === schedule.shiftWorkType)
                        : null;

                    // Kiểm tra xem có hoàn thành ca làm việc không (status = 5 là hoàn thành)
                    const isCompleted = schedule.shiftWorkStatus === 5;

                    return (
                        <div className="p-2 text-sm min-w-[200px]">
                            <div className="font-medium mb-2 text-gray-800">Chi tiết ca làm:</div>
                            <div className="space-y-1">
                                {/* Loại ca */}
                                {shiftType && (
                                    <div className="flex justify-between">
                                        <span className="text-gray-600">Loại ca:</span>
                                        <span className="font-medium text-gray-800">{shiftType.name}</span>
                                    </div>
                                )}

                                {/* Trạng thái */}
                                {workStatus && (
                                    <div className="flex justify-between">
                                        <span className="text-gray-600">Trạng thái:</span>
                                        <span
                                            className={`font-medium ${isCompleted ? 'text-green-600' : 'text-blue-600'}`}>
                                            {workStatus.name}
                                        </span>
                                    </div>
                                )}

                                {/* Số công được tính */}
                                <div className="flex justify-between">
                                    <span className="text-gray-600">Số công:</span>
                                    <span
                                        className={`font-semibold ${calculatedWorkingDay > 0 ? 'text-green-600' : 'text-gray-400'}`}>
                                        {calculatedWorkingDay} công
                                    </span>
                                </div>

                                {/* Separator */}
                                <div className="border-t border-gray-200 my-2"></div>

                                {/* Check-in time */}
                                <div className="flex justify-between">
                                    <span className="text-gray-600">Check-in:</span>
                                    <span
                                        className={`${hasCheckinData ? 'font-medium text-blue-600' : 'text-gray-400 italic'}`}>
                                        {formatTime(schedule.checkIn)}
                                    </span>
                                </div>

                                {/* Check-out time */}
                                <div className="flex justify-between">
                                    <span className="text-gray-600">Check-out:</span>
                                    <span
                                        className={`${hasCheckoutData ? 'font-medium text-blue-600' : 'text-gray-400 italic'}`}>
                                        {formatTime(schedule.checkOut)}
                                    </span>
                                </div>
                            </div>

                            {/* Thông báo khi chưa có dữ liệu chấm công */}
                            {!hasCheckinData && !hasCheckoutData && (
                                <div className="mt-2 pt-2 border-t border-gray-200">
                                    <span className="text-xs text-amber-600">⚠️ Chưa có dữ liệu chấm công</span>
                                </div>
                            )}

                            {/* Thông báo khi hoàn thành ca */}
                            {isCompleted && (
                                <div className="mt-2 pt-2 border-t border-green-200 bg-green-50 rounded px-2 py-1">
                                    <span className="text-xs text-green-700">✅ Ca làm việc đã hoàn thành</span>
                                </div>
                            )}
                        </div>
                    );
                };

                return (
                    <Tooltip
                        title={renderTooltipContent()}
                        placement="top"
                        arrow
                        enterDelay={300} // Thêm delay để tránh hiển thị tooltip quá nhanh
                        leaveDelay={100}
                        componentsProps={{
                            tooltip: {
                                sx: {
                                    backgroundColor: 'white',
                                    color: 'black',
                                    border: '1px solid #e5e7eb',
                                    borderRadius: '8px',
                                    boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)',
                                    fontSize: '0.875rem',
                                    maxWidth: '300px',
                                    padding: '0'
                                }
                            },
                            arrow: {
                                sx: {
                                    color: 'white',
                                    '&:before': {
                                        border: '1px solid #e5e7eb',
                                    }
                                }
                            }
                        }}
                    >
                        <div className={`
                            flex justify-center items-center 
                            h-full w-full py-2
                            ${schedule ? "hover:bg-amber-50 cursor-pointer" : "bg-gray-100 cursor-default"}
                            rounded
                            relative transition-colors duration-200
                            ${shiftWorkStatus === 5 ? "bg-green-50" : ""} // Highlight ngày đã hoàn thành
                        `}>
                            <span
                                className={`${
                                    !schedule
                                        ? "text-gray-400"
                                        : calculatedWorkingDay > 0
                                            ? "font-semibold text-green-600"
                                            : "text-gray-500"
                                }`}
                            >
                                {displayValue}
                            </span>

                            {/* Indicator cho ngày có checkin/checkout */}
                            {(() => {
                                const hasCheckin = schedule?.checkIn;
                                const hasCheckout = schedule?.checkOut;

                                if (hasCheckin && hasCheckout) {
                                    return <span className="absolute bottom-1 right-1 w-2 h-2 rounded-full bg-green-500"
                                                 title="Đã check-in và check-out"></span>;
                                } else if (hasCheckin) {
                                    return <span className="absolute bottom-1 right-1 w-2 h-2 rounded-full bg-blue-500"
                                                 title="Đã check-in"></span>;
                                } else if (hasCheckout) {
                                    return <span
                                        className="absolute bottom-1 right-1 w-2 h-2 rounded-full bg-orange-500"
                                        title="Chỉ có check-out"></span>;
                                }
                                return null;
                            })()}
                        </div>
                    </Tooltip>
                );
            }
        };
    });

    const columns = [...baseColumns, ...dynamicColumns];

    // 3. Transform data để bao gồm tất cả ngày
    const transformedData = Array.isArray(dataList)
        ? dataList.map(staffItem => {
            const result = {
                staffCode: staffItem.staff?.staffCode || "",
                displayName: staffItem.staff?.displayName || "",
                staffWorkSchedules: staffItem.staffWorkSchedules || [], // Thêm dòng này để truyền dữ liệu gốc
            };

            // Khởi tạo tất cả ngày với giá trị null
            allDates.forEach(date => {
                result[getDate(date)] = null;
            });

            // Gán giá trị cho các ngày có dữ liệu
            if (Array.isArray(staffItem.staffWorkSchedules)) {
                staffItem.staffWorkSchedules.forEach(schedule => {
                    const dateKey = getDate(schedule.workingDate);
                    result[dateKey] = schedule.shiftWorkStatus;
                });
            }

            return result;
        })
        : [];

    return (
        <CommonTable
            data={transformedData}
            columns={columns}
            nonePagination={false}
            totalPages={totalPages}
            enableColumnPinning={true}
            pinnedLeftColumns={['staffCode', 'displayName']}
            pageSize={searchObject.pageSize}
            page={searchObject.pageIndex}
            totalElements={totalElements}
            pageSizeOption={[5, 10, 15]}
            handleChangePage={setPageIndex}
            setRowsPerPage={setPageSize}
        />
    );
}

export default observer(StaffWorkScheduleSummaryList);