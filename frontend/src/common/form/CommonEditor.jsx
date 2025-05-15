import React, { memo, useEffect, useMemo, useRef, useState } from "react";
import { FastField, getIn } from "formik";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { uploadImage } from "app/views/profile/ProfileService";

function CommonEditor(props) {
    const { label, oldStyle } = props;

    return (
        <FastField {...props} name={props?.name} shouldUpdate={shouldComponentUpdate}>
            {({ field, form }) => (
                <>
                    {label && (
                        <label
                            htmlFor={props?.name}
                            className={oldStyle ? "old-label" : "label-container"}
                        >
                            {label} {props?.validate ? <span style={{ color: "red" }}> * </span> : null}
                        </label>
                    )}
                    <MyComponent {...props} field={field} setFieldValue={form.setFieldValue} />
                </>
            )}
        </FastField>
    );
}

function MyComponent({ disabled, field, name, setFieldValue, placeholder, readOnly, oldStyle = false }) {
    const quillRef = useRef();
    const [data, setData] = useState(field.value || "");

    useEffect(() => {
        setData(field.value || "");
    }, [field.value]);

    const handleChange = (value) => {
        setData(value);
        setFieldValue(name, value);
    };

    const imageHandler = () => {
        const editor = quillRef.current.getEditor();
        const input = document.createElement("input");
        input.setAttribute("type", "file");
        input.setAttribute("accept", "image/*");
        input.click();

        input.onchange = async () => {
            const file = input.files[0];
            if (/^image\//.test(file.type)) {
                try {
                    const formData = new FormData();
                    formData.append("image", file);
                    const res = await uploadImage(formData);
                    const url = res?.data?.url;
                    const range = editor.getSelection(true);
                    editor.insertEmbed(range.index, "image", url);
                    editor.setSelection(range.index + 1);
                } catch (error) {
                    console.error("Upload image failed", error);
                }
            } else {
                console.warn("You could only upload images.");
            }
        };
    };

    const modules = useMemo(() => {
        if (readOnly || disabled) {
            return {
                toolbar: false,
                clipboard: {
                    matchVisual: false,
                },
            };
        }
        return {
            clipboard: {
                matchVisual: false,
            },
            toolbar: {
                container: [
                    [{ header: [1, 2, 3, 4, 5, 6, false] }],
                    ["bold", "italic", "underline", "strike"],
                    [{ list: "ordered" }, { list: "bullet" }, { indent: "-1" }, { indent: "+1" }],
                    ["image", "link"],
                    [
                        {
                            color: [
                                "#000000",
                                "#e60000",
                                "#ff9900",
                                "#ffff00",
                                "#008a00",
                                "#0066cc",
                                "#9933ff",
                                "#ffffff",
                                "#facccc",
                                "#ffebcc",
                                "#ffffcc",
                                "#cce8cc",
                                "#cce0f5",
                                "#ebd6ff",
                                "#bbbbbb",
                                "#f06666",
                                "#ffc266",
                                "#ffff66",
                                "#66b966",
                                "#66a3e0",
                                "#c285ff",
                                "#888888",
                                "#a10000",
                                "#b26b00",
                                "#b2b200",
                                "#006100",
                                "#0047b2",
                                "#6b24b2",
                                "#444444",
                                "#5c0000",
                                "#663d00",
                                "#666600",
                                "#003700",
                                "#002966",
                                "#3d1466",
                            ],
                        },
                    ],
                ],
                handlers: {
                    image: imageHandler,
                },
            },
        };
    }, [readOnly, disabled]);

    return (
        <ReactQuill
            ref={quillRef}
            theme="snow"
            value={data}
            onChange={handleChange}
            placeholder={placeholder}
            readOnly={readOnly || disabled}
            modules={modules}
            className={`bg-white ${oldStyle ? "" : "editor-container"} ${
                readOnly || disabled ? "read-only" : ""
            }`}
        />
    );
}

export default memo(CommonEditor);

const shouldComponentUpdate = (nextProps, currentProps) => {
    return (
        nextProps.name !== currentProps.name ||
        nextProps.value !== currentProps.value ||
        nextProps.onChange !== currentProps.onChange ||
        nextProps.label !== currentProps.label ||
        nextProps.oldStyle !== currentProps.oldStyle ||
        nextProps.required !== currentProps.required ||
        nextProps.disabled !== currentProps.disabled ||
        nextProps.readOnly !== currentProps.readOnly ||
        nextProps.className !== currentProps.className ||
        nextProps.formik.isSubmitting !== currentProps.formik.isSubmitting ||
        Object.keys(nextProps).length !== Object.keys(currentProps).length ||
        getIn(nextProps.formik.values, currentProps.name) !== getIn(currentProps.formik.values, currentProps.name) ||
        getIn(nextProps.formik.errors, currentProps.name) !== getIn(currentProps.formik.errors, currentProps.name) ||
        getIn(nextProps.formik.touched, currentProps.name) !== getIn(currentProps.formik.touched, currentProps.name)
    );
};
