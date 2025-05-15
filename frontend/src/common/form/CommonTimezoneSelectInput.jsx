import React from "react";
import {MenuItem, TextField} from "@mui/material";
import {useField, useFormikContext} from "formik";

const CommonTimezoneSelectInput = ({
                                        name,
                                        keyValue = "value",
                                        displayValue,
                                        size = "small",
                                        variant = "outlined",
                                        label,
                                        hideNullOption,
                                        readOnly = false,
                                        options = [],
                                        ...otherProps
                                    }) => {
    const { setFieldValue } = useFormikContext();
    const [field, meta] = useField(name);

    const handleChange = (evt) => {
        if (readOnly) return;
        const { value } = evt.target;
        setFieldValue(name, value);
    };

    const configSelectInput = {
        ...field,
        ...otherProps,
        select: true,
        variant,
        size,
        fullWidth: true,
        onChange: otherProps?.handleChange || handleChange,
        disabled: readOnly,
        InputLabelProps: {
            htmlFor: name,
            shrink: true,
        },
        className: `${readOnly ? "read-only" : ""}`,
    };

    if (meta?.touched && meta?.error) {
        configSelectInput.error = true;
        configSelectInput.helperText = meta.error;
    }

    return (
        <>
            {label && (
                <label htmlFor={name} style={{ fontSize: "1rem" }}>
                    {label}
                </label>
            )}
            <TextField {...configSelectInput}>
                {!hideNullOption && (
                    <MenuItem value="">
                        <em>---</em>
                    </MenuItem>
                )}
                {options?.map((item, pos) => (
                    <MenuItem key={pos} value={item[keyValue]}>
                        {item[displayValue || "label"]}
                    </MenuItem>
                ))}
            </TextField>
        </>
    );
};

export default CommonTimezoneSelectInput;
