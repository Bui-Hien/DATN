import React, {memo, useEffect, useMemo, useState} from 'react';
import {Calendar, momentLocalizer, Views} from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import {Button, ButtonGroup} from "@mui/material";
import AccessTimeIcon from "@mui/icons-material/AccessTime";
import {t} from "i18next";
import {useStore} from "../../stores";
import TimeSheetDetailForm from "../TimeSheetDetail/TimeSheetDetailForm";
import {observer} from "mobx-react-lite";
import {Months, ShiftWorkStatus, ShiftWorkType} from "../../LocalConstants";
import VisibilityIcon from '@mui/icons-material/Visibility';
import IconButton from "@mui/material/IconButton";
import StaffShiftStatistics from "./StaffShiftStatistics";
import {getEndMonth, getStartMonth, Years} from "../../LocalFunction";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import {Form, Formik} from "formik";
import 'moment/locale/vi';
moment.locale('vi');
const localizer = momentLocalizer(moment);

const WorkScheduleCalendar = () => {
    const [currentDate, setCurrentDate] = useState(new Date());
    const {staffWorkScheduleStore, staffStore} = useStore();
    const {
        openCreateEditPopup,
        handleOpenCreateEdit,
        openShiftStatistics,
        handleOpenShiftStatistic,
        staffMonthScheduleCalendar,
        pagingStaffMonthScheduleCalendar,
        resetStore,
        handleSetSearchObject,
        searchObject
    } = staffWorkScheduleStore;

    const {getCurrentStaff, selectedRow: currentStaff, resetStore: resetStaffStore} = staffStore;

    const events = useMemo(() => {
        const eventList = [];

        // Add null check and ensure staffWorkSchedules exists
        const scheduleData = staffMonthScheduleCalendar?.[0];
        if (!scheduleData?.staffWorkSchedules) {
            return eventList;
        }

        Object.entries(scheduleData.staffWorkSchedules).forEach(([dateKey, shifts]) => {
            const workDate = new Date(dateKey.split('T')[0]); // Get date part only

            shifts.forEach((shift, index) => {
                // Create event for each shift
                eventList.push({
                    id: shift.id,
                    start: new Date(workDate.getFullYear(), workDate.getMonth(), workDate.getDate(), 8 + (index * 4), 0), // Stagger times for display
                    end: new Date(workDate.getFullYear(), workDate.getMonth(), workDate.getDate(), 12 + (index * 4), 0),
                    resource: {
                        shift: shift
                    }
                });
            });
        });

        return eventList;
    }, [staffMonthScheduleCalendar]);

    const handleNavigate = (date) => {
        setCurrentDate(date);
    };

    const handleToday = () => {
        setCurrentDate(new Date());
    };

    const handleNext = () => {
        const today = new Date();
        const isMaxMonth = currentDate.getFullYear() >= today.getFullYear() && currentDate.getMonth() >= 11; // tháng 12 là 11

        if (isMaxMonth) return;

        setCurrentDate(prevDate => {
            const nextMonth = new Date(prevDate);
            nextMonth.setMonth(nextMonth.getMonth() + 1);
            return nextMonth;
        });
    };

    const handleBack = () => {
        const isMinMonth = currentDate.getFullYear() === 2020 && currentDate.getMonth() === 0;

        if (isMinMonth || currentDate.getFullYear() < 2020) return;

        setCurrentDate(prevDate => {
            const prevMonth = new Date(prevDate);
            prevMonth.setMonth(prevMonth.getMonth() - 1);
            return prevMonth;
        });
    };
    useEffect(() => {
        getCurrentStaff();

        return () => {
            resetStore();
            resetStaffStore();
        };
    }, []);

    const getValueForm = async (fromDate, toDate) => {
        if (!currentStaff?.id) return;

        const searchParams = {
            ...searchObject,
            ownerId: currentStaff.id,
            fromDate: fromDate,
            toDate: toDate,
        };

        handleSetSearchObject(searchParams);

        await pagingStaffMonthScheduleCalendar();
    }
    useEffect(() => {
        getValueForm(getStartMonth(currentDate), getEndMonth(currentDate));
    }, [currentDate, currentStaff?.id, openCreateEditPopup]);

    const handleSubmitForm = (values) => {
        const {month, year} = values;

        const newValue = new Date(year, month - 1);

        getValueForm(getStartMonth(newValue), getEndMonth(newValue));
    }

    const EventComponent = ({event}) => {
        const shiftWorkType = ShiftWorkType.getListData().find(i => i.value === event.resource.shift.shiftWorkType);
        const shiftWorkStatus = ShiftWorkStatus.getListData().find(i => i.value === event.resource.shift.shiftWorkStatus);

        return (
            <div
                className={`
                    rounded-md
                    p-1
                    text-xs
                    font-medium
                    text-gray-800
                    border
                    shadow-sm
                    cursor-pointer
                    hover:shadow-md
                    transition-shadow
                    mb-1
                `}
                style={{
                    backgroundColor: shiftWorkStatus?.color,
                    borderColor: shiftWorkStatus?.color,
                }}
            >
                <div className="flex flex-col">
                    <div className="flex justify-between">
                        <span className="block truncate text-xs font-semibold">
                            {shiftWorkType?.name || 'Không xác định'}
                        </span>
                        <IconButton className={"!p-0"}
                                    onClick={() => handleOpenShiftStatistic(event.resource.shift?.id)}
                        >
                            <VisibilityIcon
                                fontSize={"small"}
                            />
                        </IconButton>
                    </div>
                    <span className="block text-xs opacity-75">
                        {shiftWorkType?.calculatedWorkingDay || 0} công
                    </span>
                </div>
            </div>
        );
    };


    return (
        <div className="p-6 bg-white">
            <div className="grid grid-cols-12 gap-4">
                <div className="col-span-4">
                    <h1 className="text-2xl font-bold text-gray-800 mb-2">
                        Lịch Làm Việc - {currentStaff?.displayName}
                    </h1>
                    <p className="text-gray-600">Mã NV: {currentStaff?.staffCode}</p>
                </div>
                <div className="col-span-4 flex justify-center items-center">
                    <ButtonGroup
                        color="container"
                        aria-label="outlined primary button group"
                    >
                        <Button
                            onClick={() => {
                                handleOpenCreateEdit(null)
                            }}
                            startIcon={<AccessTimeIcon/>}
                        >
                            {t("Chấm công")}
                        </Button>
                    </ButtonGroup>
                </div>
            </div>

            {/* Custom Navigation */}
            <div className="mb-4 justify-between items-center grid grid-cols-2">
                <div className="flex gap-2 col-span-1">
                    <button
                        onClick={handleToday}
                        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors"
                    >
                        Hôm nay
                    </button>
                    <button
                        onClick={handleBack}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 transition-colors"
                    >
                        ←
                    </button>
                    <button
                        onClick={handleNext}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 transition-colors"
                    >
                        →
                    </button>
                </div>
                <div className="flex gap-2 justify-end">
                    <Formik
                        enableReinitialize
                        initialValues={{
                            month: (new Date()).getMonth() + 1,
                            year: (new Date()).getFullYear()
                        }}
                        onSubmit={handleSubmitForm}
                    >
                        {({isSubmitting, values, setFieldValue}) => {
                            // eslint-disable-next-line react-hooks/rules-of-hooks
                            useEffect(() => {
                                setFieldValue("month", currentDate.getMonth() + 1);
                                setFieldValue("year", currentDate.getFullYear());
                            }, [currentDate, setFieldValue]);
                            return (
                                <Form autoComplete="off">
                                    <div className="flex justify-end gap-2">
                                        <div>
                                            <CommonSelectInput
                                                label={"Tháng"}
                                                name="month"
                                                options={Months.getListData()}
                                                required
                                                hideNullOption
                                                handleChange={(evt) => {
                                                    const {value} = evt.target;
                                                    setFieldValue("month", value);
                                                    handleSubmitForm({...values, month: value}); // dùng value mới
                                                }}
                                            />
                                        </div>
                                        <div>
                                            <CommonSelectInput
                                                label={"Năm"}
                                                name="year"
                                                options={Years.getListData()}
                                                required
                                                hideNullOption
                                                handleChange={(evt) => {
                                                    const {value} = evt.target;
                                                    setFieldValue("year", value);
                                                    handleSubmitForm({...values, year: value}); // dùng value mới
                                                }}
                                            />
                                        </div>
                                    </div>
                                </Form>
                            );
                        }}
                    </Formik>
                </div>
            </div>

            {/* Calendar */}
            <div style={{height: '600px'}} className="border rounded-lg overflow-hidden">
                <Calendar
                    culture="vi"
                    localizer={localizer}
                    events={events}
                    startAccessor="start"
                    endAccessor="end"
                    views={[Views.MONTH]}
                    defaultView={Views.MONTH}
                    view={Views.MONTH}
                    date={currentDate}
                    onNavigate={handleNavigate}
                    components={{
                        event: EventComponent
                    }}
                    eventPropGetter={(event) => ({
                        style: {
                            backgroundColor: 'transparent',
                            border: 'none',
                            borderRadius: '4px',
                        }
                    })}
                    dayPropGetter={(date) => {
                        const today = new Date();
                        const isToday = date.toDateString() === today.toDateString();
                        return {
                            style: {
                                backgroundColor: isToday ? '#e3f2fd' : 'transparent',
                            }
                        };
                    }}
                    toolbar={false}
                    popup
                    showMultiDayTimes
                />
            </div>
            {openCreateEditPopup && <TimeSheetDetailForm isPerson={true}/>}
            {openShiftStatistics && <StaffShiftStatistics/>}
        </div>

    );
};

export default memo(observer(WorkScheduleCalendar));