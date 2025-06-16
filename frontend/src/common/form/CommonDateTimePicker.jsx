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
        if (!val && val !== 0) return null;

        if (val instanceof Date) {
            return isNaN(val.getTime())
                ? null
                : moment(val).tz("Asia/Ho_Chi_Minh").toDate();
        }

        if (typeof val === "number") {
            const d = new Date(val);
            return isNaN(d.getTime())
                ? null
                : moment(d).tz("Asia/Ho_Chi_Minh").toDate();
        }

        if (typeof val === "string") {
            const m = moment.tz(val, moment.ISO_8601, "Asia/Ho_Chi_Minh");
            if (m.isValid()) return m.toDate();
            const m2 = moment.tz(val, "DD/MM/YYYY", "Asia/Ho_Chi_Minh");
            if (m2.isValid()) return m2.toDate();

            return null;
        }

        return null;
    };


    const [value, setValue] = useState(() => parseToDate(field.value));

    useEffect(() => {
        setValue(parseToDate(field.value));
    }, [field.value]);

    const handleChange = (newValue) => {
        if (readOnly || disabled) return;

        setValue(newValue);

        // ✅ Chuyển sang ISO string có +07:00
        let newDate = newValue instanceof Date && !isNaN(newValue.getTime())
            ? moment(newValue).tz("Asia/Ho_Chi_Minh").format()
            : null;

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

    const [debounceTimer, setDebounceTimer] = useState(null);

    useEffect(() => {
        setValue(parseToDate(field.value));
    }, [field.value]);

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
                    sx: {
                        height: "40px", // chiều cao tổng thể của input
                        "& .MuiInputBase-input": {
                            height: "40px",
                            padding: "0 14px",
                            display: "flex",
                            alignItems: "center",
                        },
                        ...(readOnly
                            ? {
                                color: "rgba(0, 0, 0, 0.87)",
                                backgroundColor: "rgba(0, 0, 0, 0.02)",
                                opacity: 1,
                            }
                            : {}),
                    },
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
        <div className="flex flex-col">
            {label && (
                <label htmlFor={name} className="label-container">
                    {label} {required ? <span style={{color: "red"}}> * </span> : null}
                </label>
            )}
            <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={viLocale}>
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
