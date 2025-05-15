import React, {memo, useState} from 'react';
import {Table, TableBody, TableCell, TableHead, TableRow} from '@mui/material';
import {Form, Formik} from 'formik';
import KeyboardArrowLeftIcon from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';
import {formatDate, getDay, getFullYear, getMonth} from "../../LocalFunction";
import {LIST_MONTH, LIST_YEAR, ListWeeks} from "../../LocalConstants";
import * as i18n from "i18next";
import GlobitsSelectInput from "../form/CommonSelectInput";

function CalendarMonth({ defaultDate, onClickDate, renderDate, renderButtonView, onChangeDate }) {
    const [currentMonth, setCurrentMonth] = useState(defaultDate ? defaultDate : new Date());
    const firstDayOfMonth = new Date(getFullYear(currentMonth), getMonth(currentMonth));

    function handleChangeDateOffRange(date, classDateOffRange) {
        if (onClickDate) {
            onClickDate(date);
        }
        if (classDateOffRange) {
            onChangeDate(date);
            setCurrentMonth(date);
        }
    }

    return (
        <div className="calendar-month">
            <Formik
                enableReinitialize
                initialValues={{
                    month: getMonth(currentMonth) + 1,
                    year: getFullYear(currentMonth)
                }}
            >
                {({ values, setValues }) => {
                    const handleChangeMonth = (date, isChangeToday) => {
                        setCurrentMonth(date);
                        setValues({
                            month: getMonth(date) + 1,
                            year: getFullYear(date)
                        });

                        if (onChangeDate) {
                            onChangeDate(date);
                        }
                        if (isChangeToday) {
                            onClickDate(date);
                        }
                    };

                    return (
                        <Form className="calendar-form flex space-between">
                            <div className="btn-group">
                                <button
                                    type="button"
                                    onClick={() =>
                                        handleChangeMonth(new Date(getFullYear(currentMonth), values.month - 2))
                                    }
                                    className="btn-calendar"
                                >
                                    <KeyboardArrowLeftIcon />
                                </button>
                                <button
                                    type="button"
                                    onClick={() =>
                                        handleChangeMonth(new Date(getFullYear(currentMonth), values.month))
                                    }
                                    className="btn-calendar"
                                >
                                    <KeyboardArrowRightIcon />
                                </button>
                                <button
                                    type="button"
                                    onClick={() => handleChangeMonth(new Date(), true)}
                                    className="btn-calendar ml-10"
                                >
                                    Today
                                </button>
                            </div>

                            <div className="flex items-center gap-1">
                                <div style={{ width: 120 }}>
                                    <GlobitsSelectInput
                                        name="month"
                                        options={LIST_MONTH}
                                        handleChange={({ target }) =>
                                            handleChangeMonth(new Date(getFullYear(currentMonth), target.value - 1))
                                        }
                                    />
                                </div>
                                <div style={{ width: 130 }}>
                                    <GlobitsSelectInput
                                        name="year"
                                        options={LIST_YEAR}
                                        handleChange={({ target }) =>
                                            handleChangeMonth(
                                                new Date(currentMonth).setFullYear(target.value)
                                            )
                                        }
                                    />
                                </div>
                            </div>

                            {renderButtonView ? renderButtonView() : <span></span>}
                        </Form>
                    );
                }}
            </Formik>

            <Table className="mt-10">
                <TableHead>
                    <TableRow>
                        {ListWeeks.map((item, index) => (
                            <TableCell className="weeks-header" key={index}>
                                {i18n.t(item.name)}
                            </TableCell>
                        ))}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {Array.from({ length: 6 }).map((_, index) => {
                        const day = getDay(firstDayOfMonth);
                        const startDate = new Date(firstDayOfMonth).setDate(
                            2 - (day === 0 ? 7 : day) + index * 7
                        );

                        if (index >= 5 && getMonth(startDate) !== getMonth(currentMonth)) {
                            return <React.Fragment key={index}></React.Fragment>;
                        }

                        return (
                            <TableRow key={index}>
                                {Array.from({ length: 7 }).map((_, i) => {
                                    const date = new Date(startDate);
                                    date.setDate(date.getDate() + i);

                                    const classDateToDay =
                                        new Date().toDateString() === date.toDateString() ? 'today-date' : '';
                                    const classDateOffRange =
                                        getMonth(currentMonth) !== getMonth(date) ? 'date-off-range' : '';

                                    return (
                                        <TableCell
                                            key={i}
                                            className={['date', classDateToDay, classDateOffRange].join(' ')}
                                            onClick={() => handleChangeDateOffRange(date, classDateOffRange)}
                                        >
                                            <p>{formatDate('DD', date)}</p>
                                            {!classDateOffRange && renderDate ? renderDate(date) : null}
                                        </TableCell>
                                    );
                                })}
                            </TableRow>
                        );
                    })}
                </TableBody>
            </Table>
        </div>
    );
}

export default memo(CalendarMonth);
