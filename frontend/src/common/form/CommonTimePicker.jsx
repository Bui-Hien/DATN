import React from "react";
import {useField, useFormikContext} from "formik";
import {LocalizationProvider, TimePicker} from "@mui/x-date-pickers";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFns";

const CommonTimePicker = ({
                               disablePast,
                               disableFuture,
                               name,
                               size = "small",
                               format = "HH:mm",
                               label,
                               required,
                               readOnly = false,
                               isTimePicker,
                               ...otherProps
                           }) => {
    const {setFieldValue} = useFormikContext();
    const [field, meta] = useField(name);

    const onChange = (value) => {
        if (!readOnly) {
            setFieldValue(name, value);
        }
    };

    const showError = Boolean(meta.touched && meta.error);

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            {label && (
                <label htmlFor={name} style={{fontSize: "1rem"}}>
                    {label} {required && <span style={{color: "red"}}> * </span>}
                </label>
            )}

            <TimePicker
                {...otherProps}
                value={field.value || otherProps.value || null}
                onChange={otherProps.onChange || onChange}
                disabled={readOnly}
                ampm={false}
                disablePast={disablePast}
                disableFuture={disableFuture}
                slotProps={{
                    textField: {
                        fullWidth: true,
                        size,
                        id: name,
                        error: showError,
                        helperText: showError ? meta.error : "",
                        InputLabelProps: {
                            shrink: true,
                        },
                        inputProps: {
                            readOnly,
                        },
                    },
                }}
            />
        </LocalizationProvider>
    );
};

export default CommonTimePicker;
