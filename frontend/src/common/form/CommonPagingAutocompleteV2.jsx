import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import {FastField, getIn} from "formik";
import {isEqual} from "lodash";
import React, {useEffect, useState} from "react";
import {makeStyles} from "@mui/styles";

const PAGE_SIZE = 20;

const useStyles = makeStyles(() => ({
    container: {
        "& .MuiAutocomplete-inputRoot": {
            paddingTop: "0px !important",
            paddingBottom: "0px !important",
        },
    },
    autoHeight: {
        "& > div": {
            height: "auto !important",
        },
    },
}));

const CommonPagingAutocompleteV2 = (props) => {
    return (
        <FastField {...props} name={props.name} shouldUpdate={shouldComponentUpdate}>
            {({field, meta, form}) => (
                <MyPagingAutocomplete {...props} field={field} meta={meta} setFieldValue={form.setFieldValue}/>
            )}
        </FastField>
    );
};

function MyPagingAutocomplete({
                                  api,
                                  name,
                                  searchObject,
                                  allowLoadOptions = true,
                                  clearOptionOnClose,
                                  handleChange,
                                  field,
                                  meta,
                                  setFieldValue,
                                  label,
                                  oldStyle = false,
                                  required,
                                  disabled = false,
                                  getOptionDisabled,
                                  readOnly = false,
                                  ...otherProps
                              }) {
    const [page, setPage] = useState(1);
    const [options, setOptions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [keyword, setKeyword] = useState("");
    const [firstLoading, setFirstLoading] = useState(true);
    const [totalPage, setTotalPage] = useState(1);
    const [open, setOpen] = useState(false);
    const [t, setT] = useState();
    const [typing, setTyping] = useState(false);

    const classes = useStyles();

    useEffect(() => {
        if (loading && allowLoadOptions) {
            loadMoreResults();
        }
    }, [page, loading]);

    useEffect(() => {
        if (open && allowLoadOptions) {
            getData();
        }
    }, [keyword, open, searchObject]);

    const getData = () => {
        let newPage = 1;
        setPage(newPage);
        api({
            ...searchObject,
            pageIndex: newPage,
            pageSize: PAGE_SIZE,
            keyword,
        }).then((response) => {
            const result = response?.data;
            if (result && result.data?.content) {
                setOptions([...result.data.content]);
                setTotalPage(result.data.totalPages);
            } else {
                setOptions([]);
            }
        });
    };

    const loadMoreResults = () => {
        const nextPage = page + 1;
        setPage(nextPage);
        api({
            ...searchObject,
            pageIndex: nextPage,
            pageSize: PAGE_SIZE,
            keyword,
        }).then((response) => {
            const result = response?.data;
            if (result && result.data?.content) {
                setOptions((prev) => [...prev, ...result.data.content]);
                setTotalPage(result.data.totalPages);
            }
        });

    };

    const handleScroll = (event) => {
        const listboxNode = event.currentTarget;
        const position = listboxNode.scrollTop + listboxNode.clientHeight;
        if (listboxNode.scrollHeight - position <= 8 && page < totalPage) {
            loadMoreResults();
        }
    };

    const onOpen = () => {
        if (readOnly) return;
        setOpen(true);
        if (firstLoading && allowLoadOptions) {
            getData();
        }
        setFirstLoading(false);
    };

    const onClose = () => {
        setOpen(false);
        setKeyword(null);
        if (clearOptionOnClose) {
            setOptions([]);
            setTotalPage(1);
        }
    };

    const handleChangeText = (value) => {
        if (readOnly) return;
        setTyping(true);
        if (t) clearTimeout(t);
        setT(
            setTimeout(() => {
                setKeyword(value || null);
                setTyping(false);
            }, 500)
        );
    };

    const defaultHandleChange = (_, value) => {
        if (readOnly) return;
        setFieldValue(name, value ? value : null);
    };

    const defaultGetOptionLabel = (option) =>
        option[otherProps?.displayName || "name"] || "";

    return (
        <Autocomplete
            {...field}
            {...otherProps}
            id={name}
            options={options}
            loading={loading && !readOnly}
            disabled={disabled || readOnly}
            onOpen={onOpen}
            open={readOnly ? false : open}
            onClose={onClose}
            className={`${oldStyle ? "" : "input-container"} ${classes.container} ${readOnly ? "read-only" : ""}`}
            onChange={handleChange || defaultHandleChange}
            getOptionLabel={otherProps?.getOptionLabel || defaultGetOptionLabel}
            getOptionSelected={(option, value) => option?.id === value?.id}
            getOptionDisabled={readOnly ? () => true : getOptionDisabled}
            noOptionsText="Không có dữ liệu"
            onKeyDown={(event) => {
                if (event.key === "Enter") {
                    event.stopPropagation();
                    event.preventDefault();
                }
            }}
            onInputChange={(event) => {
                if (!readOnly) {
                    handleChangeText(event?.target?.value);
                }
            }}
            renderInput={(params) => (
                <>
                    {label && (
                        <label htmlFor={name} className={`${oldStyle ? "old-label" : "label-container"}`}>
                            {label}
                            {required && <span style={{color: "red"}}> * </span>}
                        </label>
                    )}
                    <TextField
                        {...params}
                        variant={otherProps?.variant || "outlined"}
                        inputProps={{
                            ...params.inputProps,
                            autoComplete: "off",
                            readOnly: readOnly,
                            style: readOnly
                                ? {
                                    ...params.inputProps.style,
                                    color: "rgba(0, 0, 0, 0.87)",
                                    cursor: "text",
                                    opacity: 1,
                                }
                                : params.inputProps.style,
                        }}
                        InputProps={{
                            ...params.InputProps,
                            readOnly: readOnly,
                            style: readOnly
                                ? {
                                    ...params.InputProps.style,
                                    color: "rgba(0, 0, 0, 0.87)",
                                    backgroundColor: "rgba(0, 0, 0, 0.02)",
                                    opacity: 1,
                                }
                                : params.InputProps.style,
                        }}
                        className={`${classes.autoHeight} ${readOnly ? "read-only" : ""}`}
                        error={meta && meta.touched && meta.error}
                        helperText={meta && meta.touched && meta.error ? meta.error : ""}
                    />
                </>
            )}
            ListboxProps={{
                onScroll: handleScroll,
            }}
        />
    );
}

const shouldComponentUpdate = (nextProps, currentProps) => {
    return (
        nextProps.name !== currentProps.name ||
        nextProps.value !== currentProps.value ||
        nextProps.handleChange !== currentProps.handleChange ||
        nextProps.label !== currentProps.label ||
        nextProps.required !== currentProps.required ||
        nextProps.api !== currentProps.api ||
        nextProps.disabled !== currentProps.disabled ||
        nextProps.readOnly !== currentProps.readOnly ||
        nextProps.getOptionDisabled !== currentProps.getOptionDisabled ||
        !isEqual(nextProps.searchObject, currentProps.searchObject) ||
        nextProps.formik.isSubmitting !== currentProps.formik.isSubmitting ||
        Object.keys(nextProps).length !== Object.keys(currentProps).length ||
        getIn(nextProps.formik.values, currentProps.name) !== getIn(currentProps.formik.values, currentProps.name) ||
        getIn(nextProps.formik.errors, currentProps.name) !== getIn(currentProps.formik.errors, currentProps.name) ||
        getIn(nextProps.formik.touched, currentProps.name) !== getIn(currentProps.formik.touched, currentProps.name)
    );
};

export default React.memo(CommonPagingAutocompleteV2);
