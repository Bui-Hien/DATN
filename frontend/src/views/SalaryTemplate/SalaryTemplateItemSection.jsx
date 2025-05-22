import {FieldArray, useFormikContext} from "formik";
import React, {memo} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {Delete} from "@mui/icons-material";
import {Button, ButtonGroup} from "@mui/material";
import CommonTextField from "../../common/form/CommonTextField";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import {SalaryItemType} from "../../LocalConstants";
import AddIcon from "@mui/icons-material/Add";
import CommonNumberInput from "../../common/form/CommonNumberInput";
import {removeVietnameseTones} from "../../LocalFunction";

function SalaryTemplateItemSection() {
    const {t} = useTranslation();
    const {values} = useFormikContext();

    function handleAddNewRow(push) {
        const newItem = {
            name: "",
            code: "",
            displayOrder: null,
            salaryItemType: null,
            defaultAmount: null,
            formula: "",
        };
        push(newItem);
    }

    return (
        <div className="w-full">
            <FieldArray name="documentItems">
                {({insert, remove, push}) => (
                    <>
                        <div className="mb-4">
                            <ButtonGroup
                                color="container"
                                aria-label="outlined primary button group"
                            >
                                <Button
                                    onClick={() => handleAddNewRow(push)}
                                    startIcon={<AddIcon/>}
                                >
                                    {t("Thêm phần tử lương")}
                                </Button>
                                <Button
                                    onClick={() => handleAddNewRow(push)}
                                    startIcon={<AddIcon/>}
                                >
                                    {t("Thêm phần tử lương từ hệ thống")}
                                </Button>
                            </ButtonGroup>
                        </div>

                        <section className="mt-4 overflow-x-auto">
                            <table className="w-full border border-gray-300 table-auto">
                                <thead>
                                <tr className="bg-gray-100">
                                    <th className="border border-gray-300 w-[5%] text-center">{t("STT")}</th>
                                    <th className="border border-gray-300 text-center">{t("Tên phần tử")}</th>
                                    <th className="border border-gray-300 text-center">{t("Mã phần tử")}</th>
                                    <th className="border border-gray-300 text-center">{t("Thứ tự hiển thị")}</th>
                                    <th className="border border-gray-300 text-center">{t("Loại phần tử lương")}</th>
                                    <th className="border border-gray-300 text-center">{t("Giá trị / Công thức")}</th>
                                    <th className="border border-gray-300 text-center">{t("Hành động")}</th>
                                </tr>
                                </thead>
                                <tbody>
                                {values?.documentItems?.length > 0 ? (
                                    values.documentItems.map((item, index) => (
                                        <SalaryTemplateItem
                                            key={index}
                                            index={index}
                                            nameSpace={`documentItems[${index}]`}
                                            remove={() => remove(index)}
                                        />
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan={10} className="text-center py-4 text-gray-500">
                                            {t("Chưa có phần tử nào")}
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

const SalaryTemplateItem = memo((props) => {
    const {index, nameSpace, remove, disabled} = props;
    const {t} = useTranslation();

    const withNameSpace = (field) => (field ? `${nameSpace}.${field}` : nameSpace);
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
                    required/>
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
                <CommonNumberInput name={withNameSpace("displayOrder")}/>
            </td>
            <td className="border border-gray-300">
                <CommonSelectInput
                    name={withNameSpace("salaryItemType")}
                    options={SalaryItemType.getListData()}
                />
            </td>
            <td className="border border-gray-300">
                {values.documentItems[index]?.salaryItemType === SalaryItemType.FORMULA.value ? (
                    <CommonTextField name={withNameSpace("formula")}/>
                ) : (
                    <CommonNumberInput name={withNameSpace("defaultAmount")}/>
                )}

            </td>
            {!disabled && (
                <td className="text-center border border-gray-300">
                    <span
                        className="text-red-600 cursor-pointer hover:text-red-800"
                        onClick={remove}
                        title={t("Xóa")}
                    >
                        <Delete/>
                    </span>
                </td>
            )}
        </tr>
    );
});

export default memo(observer(SalaryTemplateItemSection));
