import {FieldArray, useFormikContext} from "formik";
import React, {memo} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {Delete} from "@mui/icons-material";
import {Button, ButtonGroup} from "@mui/material";
import CommonTextField from "../../common/form/CommonTextField";
import AddIcon from "@mui/icons-material/Add";
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import CommonCheckBox from "../../common/form/CommonCheckBox";
import {removeVietnameseTones} from "../../LocalFunction";
import {pagingStaff} from "../Staff/StaffService"; // (nếu có để lấy danh sách nhân viên)

function PositionSection() {
    const {t} = useTranslation();
    const {values} = useFormikContext();

    function handleAddNewRow(push) {
        const newItem = {
            code: "",
            name: "",
            description: "",
            staff: null,
            isMain: true,
        };
        push(newItem);
    }

    return (
        <div className="w-full">
            <FieldArray name="positions">
                {({insert, remove, push}) => (
                    <>
                        <div className="mb-4">
                            <ButtonGroup color="container" aria-label="outlined primary button group">
                                <Button onClick={() => handleAddNewRow(push)} startIcon={<AddIcon/>}>
                                    {t("Thêm vị trí")}
                                </Button>
                            </ButtonGroup>
                        </div>

                        <section className="mt-4 overflow-x-auto">
                            <table className="w-full border border-gray-300 table-auto">
                                <thead>
                                <tr className="bg-gray-100">
                                    <th className="border border-gray-300 text-center w-[5%]">{t("STT")}</th>
                                    <th className="border border-gray-300 text-center">{t("Tên vị trí")}<span
                                        style={{color: "red"}}> * </span></th>
                                    <th className="border border-gray-300 text-center">{t("Mã vị trí")}<span
                                        style={{color: "red"}}> * </span></th>
                                    <th className="border border-gray-300 text-center">{t("Mô tả")}</th>
                                    <th className="border border-gray-300 text-center">{t("Nhân viên phụ trách")}</th>
                                    <th className="border border-gray-300 text-center">{t("Vị trí chính")}</th>
                                    <th className="border border-gray-300 text-center">{t("Hành động")}</th>
                                </tr>
                                </thead>
                                <tbody>
                                {values?.positions?.length > 0 ? (
                                    values.positions.map((item, index) => (
                                        <PositionItem
                                            key={index}
                                            index={index}
                                            item={item}
                                            nameSpace={`positions[${index}]`}
                                            remove={() => remove(index)}
                                        />
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan={7} className="text-center py-4 text-gray-500">
                                            {t("Chưa có vị trí nào")}
                                        </td>
                                    </tr>
                                )}
                                </tbody>
                            </table>
                        </section>
                    </>
                )}
            </FieldArray>
        </div>
    );
}

const PositionItem = memo(({index, nameSpace, remove}) => {
    const {t} = useTranslation();
    const withNameSpace = (field) => `${nameSpace}.${field}`;
    const {values, setFieldValue} = useFormikContext();
    return (
        <tr className="border border-gray-300">
            <td className="text-center border border-gray-300">{index + 1}</td>
            <td className="border border-gray-300">
                <CommonTextField
                    name={withNameSpace("name")}
                    onChange={(e) => {
                        const nameValue = e.target.value;
                        const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                        setFieldValue(withNameSpace("name"), nameValue);
                        setFieldValue(withNameSpace("code"), codeValue);
                    }}
                />
            </td>
            <td className="border border-gray-300">
                <CommonTextField
                    name={withNameSpace("code")}
                    onChange={(e) => {
                        const nameValue = e.target.value;
                        const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                        setFieldValue(withNameSpace("code"), codeValue);
                    }}
                />
            </td>
            <td className="border border-gray-300">
                <CommonTextField name={withNameSpace("description")} multiline={4}/>
            </td>
            <td className="border border-gray-300">
                <CommonPagingAutocompleteV2
                    name={withNameSpace("staff")}
                    api={pagingStaff}
                    getOptionLabel={(option) =>
                        option?.staffCode && option?.displayName
                            ? `${option.staffCode} - ${option.displayName}`
                            : option?.staffCode || option?.displayName || ""
                    }
                />
            </td>
            <td className="text-center border border-gray-300 flex justify-center">
                <CommonCheckBox name={withNameSpace("isMain")}/>
            </td>
            <td className="text-center border border-gray-300">
                <span
                    className="text-red-600 cursor-pointer hover:text-red-800"
                    onClick={() => {
                        if (values?.positions?.length === 1) {
                            setFieldValue("positionManager", null);
                        }
                        remove(index);
                    }}
                    title={t("Xóa")}
                >
                    <Delete/>
                </span>
            </td>
        </tr>
    );
});

export default memo(observer(PositionSection));
