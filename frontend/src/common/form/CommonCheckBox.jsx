import React from "react";
import { Checkbox, FormControlLabel, FormGroup } from "@mui/material";
import { useField } from "formik";
import { styled } from "@mui/material/styles";

const CheckBoxLabel = styled(FormControlLabel)(({ theme }) => ({
    margin: 0,
    "& .MuiIconButton-root": {
        padding: 8,
    },
    "&.read-only": {
        // Nếu cần style khi readonly thì thêm vào đây
        // Ví dụ: opacity: 0.6, pointerEvents: 'none',
    },
}));

const AlignCenterWrapper = styled(FormGroup)({
    display: "flex",
    alignItems: "flex-start",
    justifyContent: "center",
    height: "100%",
});

const CommonCheckBox = ({
                             name,
                             label,
                             style,
                             alignCenter = true,
                             readOnly = false,
                             handleChange: externalHandleChange,
                             ...otherProps
                         }) => {
    const [field, meta] = useField(name);

    const handleExternalChange = (evt) => {
        if (readOnly) return;
        if (externalHandleChange) {
            externalHandleChange(evt, evt.target.checked);
        }
    };

    const handleInternalChange = (evt) => {
        if (readOnly) return;
        field.onChange(evt);
    };

    const configCheckBox = {
        ...field,
        ...otherProps,
        checked: Boolean(field.value),
        onChange: externalHandleChange ? handleExternalChange : handleInternalChange,
        className: readOnly ? "read-only" : "",
    };

    if (meta && meta.touched && meta.error) {
        configCheckBox.error = true;
        configCheckBox.helperText = meta.error;
    }

    return alignCenter ? (
        <AlignCenterWrapper>
            <CheckBoxLabel
                className={readOnly ? "read-only" : ""}
                style={style}
                control={<Checkbox {...configCheckBox} />}
                label={label}
            />
        </AlignCenterWrapper>
    ) : (
        <FormGroup>
            <CheckBoxLabel
                className={readOnly ? "read-only" : ""}
                style={style}
                control={<Checkbox {...configCheckBox} />}
                label={label}
            />
        </FormGroup>
    );
};

export default CommonCheckBox;
