import React, {useEffect, useState} from "react";
import {FastField, getIn} from "formik";
import {DatePicker, DateTimePicker, LocalizationProvider, TimePicker,} from "@mui/x-date-pickers";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFns";
import viLocale from "date-fns/locale/vi";
import moment from "moment";

const configDefaultForm = {
    size: "medium",
    variant: "outlined",
    fullWidth: true,
    debounceTime: 200,
    notValueMillisecond: false,
};

const CommonDateTimePicker = (props) => (
    <FastField
        {...props}
        name={props.name}
        shouldUpdate={shouldComponentUpdate}
    >
        {({field, meta, form}) => {
            return (
                <Component
                    {...props}
                    field={field}
                    meta={meta}
                    setFieldValue={form.setFieldValue}
                    formik={form}
                />
            );
        }}
    </FastField>
);

const Component = ({
                       disabled,
                       fullWidth = configDefaultForm.fullWidth,
                       label,
                       name,
                       size = configDefaultForm.size,
                       variant = configDefaultForm.variant,
                       className = "",
                       debounceTime = configDefaultForm.debounceTime,
                       notDelay,
                       field,
                       meta,
                       requiredLabel,
                       required,
                       onChange,
                       readOnly = false,
                       InputProps,
                       InputLabelProps,
                       disablePast = false,
                       disableFuture = false,
                       isDateTimePicker,
                       isDateTimeSecondsPicker, // Note: MUI v5 mặc định không hỗ trợ seconds, cần custom nếu muốn
                       isTimePicker,
                       format = isTimePicker
                           ? "HH:mm"
                           : "dd/MM/yyyy" + (isDateTimeSecondsPicker ? " HH:mm:ss" : isDateTimePicker ? " HH:mm" : ""),
                       minDate,
                       maxDate,
                       minDateMessage = "Ngày không hợp lệ",
                       maxDateMessage = "Ngày không hợp lệ",
                       okLabel = "CHỌN",
                       cancelLabel = "HUỶ",
                       setFieldValue,
                       tabIndex,
                       formik,
                       ...otherProps
                   }) => {
    // Chuyển value từ formik thành Date hoặc null
    // Nếu field.value là số (timestamp) hoặc string hợp lệ, convert sang Date
    const parseToDate = (val) => {
        if (!val) return null;
        if (val instanceof Date) return val;
        if (typeof val === "number") return new Date(val);
        if (typeof val === "string") {
            const m = moment(val, ["DD/MM/YYYY", moment.ISO_8601], true);
            if (m.isValid()) return m.toDate();
            return new Date(val);
        }
        return null;
    };

    const [value, setValue] = useState(parseToDate(field.value));
    const [debounceTimer, setDebounceTimer] = useState(null);

    useEffect(() => {
        setValue(parseToDate(field.value));
    }, [field.value]);

    const handleChange = (newValue) => {
        if (readOnly || disabled) return;

        setValue(newValue);

        // Chuẩn hóa value trả về (timestamp hoặc null)
        let newDate = newValue;
        if (newDate instanceof Date && !isNaN(newDate)) {
            if (!configDefaultForm.notValueMillisecond) {
                newDate = newDate.getTime();
            }
        } else {
            newDate = null;
        }

        if (!notDelay) {
            if (debounceTimer) clearTimeout(debounceTimer);

            setDebounceTimer(
                setTimeout(() => {
                    if (onChange) {
                        onChange(newDate);
                    } else {
                        setFieldValue(name, newDate);
                    }
                }, debounceTime)
            );
        } else {
            if (onChange) {
                onChange(newDate);
            } else {
                setFieldValue(name, newDate);
            }
        }
    };

    const isError = meta?.touched && Boolean(meta?.error);

    // Cấu hình props cho picker
    const pickerProps = {
        ...otherProps,
        value,
        onChange: handleChange,
        disabled: disabled || readOnly,
        inputFormat: format, // inputFormat thay cho format ở MUI v5
        minDate,
        maxDate,
        minDateMessage,
        maxDateMessage,
        showToolbar: true,
        disablePast,
        disableFuture,
        className: `input-container ${className} ${readOnly ? "read-only" : ""}`,
        slotProps: {
            textField: {
                variant,
                size,
                fullWidth,
                error: isError,
                helperText: fullWidth && isError ? meta.error : "",
                InputProps: {
                    ...InputProps,
                    readOnly: readOnly,
                    style: readOnly
                        ? {
                            color: "rgba(0, 0, 0, 0.87)",
                            backgroundColor: "rgba(0, 0, 0, 0.02)",
                            opacity: 1,
                        }
                        : undefined,
                },
                inputProps: {
                    tabIndex,
                    readOnly: readOnly,
                    style: readOnly
                        ? {
                            color: "rgba(0, 0, 0, 0.87)",
                            cursor: "not-allowed",
                            opacity: 1,
                        }
                        : undefined,
                },
                InputLabelProps: {
                    shrink: true,
                    ...InputLabelProps,
                    htmlFor: name,
                },
            },
        },
    };

    return (
        <div className="h-100 flex justify-right align-start flex-column">
            <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={viLocale}>
                {label && (
                    <label htmlFor={name} className="label-container">
                        {label} {required ? <span style={{color: "red"}}> * </span> : null}
                    </label>
                )}

                {isDateTimePicker ? (
                    <DateTimePicker {...pickerProps} />
                ) : isTimePicker ? (
                    <TimePicker {...pickerProps} />
                ) : (
                    <DatePicker {...pickerProps} />
                )}
            </LocalizationProvider>
        </div>
    );
};

// Hàm so sánh để FastField chỉ rerender khi cần thiết
const shouldComponentUpdate = (nextProps, currentProps) => {
    return (
        nextProps.name !== currentProps.name ||
        nextProps.value !== currentProps.value ||
        nextProps.onChange !== currentProps.onChange ||
        nextProps.disablePast !== currentProps.disablePast ||
        nextProps.disableFuture !== currentProps.disableFuture ||
        nextProps.label !== currentProps.label ||
        nextProps.required !== currentProps.required ||
        nextProps.disabled !== currentProps.disabled ||
        nextProps.readOnly !== currentProps.readOnly ||
        nextProps.formik.isSubmitting !== currentProps.formik.isSubmitting ||
        Object.keys(nextProps).length !== Object.keys(currentProps).length ||
        getIn(nextProps.formik.values, currentProps.name) !==
        getIn(currentProps.formik.values, currentProps.name) ||
        getIn(nextProps.formik.errors, currentProps.name) !==
        getIn(currentProps.formik.errors, currentProps.name) ||
        getIn(nextProps.formik.touched, currentProps.name) !==
        getIn(currentProps.formik.touched, currentProps.name)
    );
};

export default React.memo(CommonDateTimePicker);
