import React, {memo, useEffect} from "react";
import {FieldArray, useFormikContext} from "formik";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import EditIcon from '@mui/icons-material/Edit';
import {Add as AddIcon, Delete} from "@mui/icons-material";
import {Button, ButtonGroup, Tooltip} from "@mui/material";

import CommonTextField from "../../common/form/CommonTextField";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import CommonNumberInput from "../../common/form/CommonNumberInput";
import CommonCheckBox from "../../common/form/CommonCheckBox";

import {SalaryItemType, SalaryTemplateItemSystem} from "../../LocalConstants";
import {removeVietnameseTones} from "../../LocalFunction";
import FormulaEditor from "../../common/form/FormulaEditor";

function SalaryTemplateItemSection() {
    const {t} = useTranslation();
    const {values, setFieldValue} = useFormikContext();

    const handleAddNewRow = (push) => {
        const newItem = {
            name: "", code: "", displayOrder: null, salaryItemType: null, defaultAmount: null, formula: "",
        };
        push(newItem);
    };

    const handleRemoveRow = (indexToRemove, remove) => {
        const itemToRemove = values.templateItems[indexToRemove];
        const code = itemToRemove?.code;

        remove(indexToRemove);

        // Nếu phần tử hệ thống bị xóa thì bỏ check tương ứng
        if (code === SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS.code) {
            setFieldValue(SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS.code, false);
        } else if (code === SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS.code) {
            setFieldValue(SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS.code, false);
        } else if (code === SalaryTemplateItemSystem.BASIC_SALARY.code) {
            setFieldValue(SalaryTemplateItemSystem.BASIC_SALARY.code, false);
        }
    };

    useEffect(() => {
        const templateItems = values?.templateItems;
        templateItems.forEach(templateItem => {
            if (templateItem?.code === SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS.code) {
                setFieldValue(SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS.code, true);
            } else if (templateItem?.code === SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS.code) {
                setFieldValue(SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS.code, true);
            } else if (templateItem?.code === SalaryTemplateItemSystem.BASIC_SALARY.code) {
                setFieldValue(SalaryTemplateItemSystem.BASIC_SALARY.code, true);
            }
        })
    }, [])
    return (<div className="w-full">
        <FieldArray name="templateItems">
            {({insert, remove, push}) => (<>
                <div className="mb-4 flex gap-4 items-center">
                    <ButtonGroup color="container" aria-label="outlined primary button group">
                        <Button onClick={() => handleAddNewRow(push)} startIcon={<AddIcon/>}>
                            {t("Thêm phần tử lương")}
                        </Button>
                    </ButtonGroup>

                    {/* Các checkbox điều khiển phần tử hệ thống */}
                    <CommonCheckBox
                        label={t("Số ngày công thực tế")}
                        name={SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS.code}
                        handleChange={(_, value) => {
                            setFieldValue(SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS.code, value);
                            const currentItems = values.templateItems || [];
                            const existsIndex = currentItems.findIndex((item) => item?.code === SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS.code);
                            if (value) {
                                if (existsIndex === -1) {
                                    push(SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS);
                                }
                            } else {
                                if (existsIndex !== -1) {
                                    remove(existsIndex);
                                }
                            }
                        }}
                    />

                    <CommonCheckBox
                        label={t("Số ngày công tiêu chuẩn")}
                        name={SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS.code}
                        handleChange={(_, value) => {
                            setFieldValue(SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS.code, value);
                            const currentItems = values.templateItems || [];
                            const existsIndex = currentItems.findIndex((item) => item?.code === SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS.code);
                            if (value) {
                                if (existsIndex === -1) {
                                    push(SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS);
                                }
                            } else {
                                if (existsIndex !== -1) {
                                    remove(existsIndex);
                                }
                            }
                        }}
                    />

                    <CommonCheckBox
                        label={t("Lương cơ bản")}
                        name={SalaryTemplateItemSystem.BASIC_SALARY.code}
                        handleChange={(_, value) => {
                            setFieldValue(SalaryTemplateItemSystem.BASIC_SALARY.code, value);
                            const currentItems = values.templateItems || [];
                            const existsIndex = currentItems.findIndex((item) => item?.code === SalaryTemplateItemSystem.BASIC_SALARY.code);
                            if (value) {
                                if (existsIndex === -1) {
                                    push(SalaryTemplateItemSystem.BASIC_SALARY);
                                }
                            } else {
                                if (existsIndex !== -1) {
                                    remove(existsIndex);
                                }
                            }
                        }}
                    />
                </div>

                <section className="mt-4 overflow-x-auto">
                    <table className="w-full border border-gray-300 table-auto">
                        <thead>
                        <tr className="bg-gray-100">
                            <th className="border border-gray-300 w-[85px] text-center">{t("Thứ tự hiển thị")}</th>
                            <th className="border border-gray-300 w-[15%] text-center">{t("Tên phần tử")}</th>
                            <th className="border border-gray-300 w-[15%] text-center">{t("Mã phần tử")}</th>
                            <th className="border border-gray-300 w-[15%] text-center">{t("Loại phần tử lương")}</th>
                            <th className="border border-gray-300 text-center">{t("Giá trị / Công thức")}</th>
                            <th className="border border-gray-300 w-[85px] text-center">{t("Hành động")}</th>
                        </tr>

                        </thead>
                        <tbody>
                        {values?.templateItems?.length > 0 ? (values.templateItems.map((item, index) => (
                            <SalaryTemplateItem
                                key={index}
                                index={index}
                                nameSpace={`templateItems[${index}]`}
                                remove={() => handleRemoveRow(index, remove)}
                            />))) : (<tr>
                            <td colSpan={7} className="text-center py-4 text-gray-500">
                                {t("Chưa có phần tử nào")}
                            </td>
                        </tr>)}
                        </tbody>
                    </table>
                </section>
            </>)}
        </FieldArray>
    </div>);
}

const SalaryTemplateItem = memo(({index, nameSpace, remove, disabled}) => {
    const {t} = useTranslation();
    const {values, setFieldValue} = useFormikContext();

    const withNameSpace = (field) => (field ? `${nameSpace}.${field}` : nameSpace);
    const item = values.templateItems[index];
    const isSystemType = item?.salaryItemType === SalaryItemType.SYSTEM.value;
    const [open, setOpen] = React.useState(false);

    return (<tr className="border border-gray-300">
        <td className="border border-gray-300">
            <CommonNumberInput name={withNameSpace("displayOrder")}/>
        </td>
        <td className="border border-gray-300">
            <CommonTextField
                name={withNameSpace("name")}
                onChange={(e) => {
                    const nameValue = e.target.value;
                    const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                    setFieldValue(withNameSpace("name"), nameValue);
                    setFieldValue(withNameSpace("code"), codeValue);
                }}
                required
                disabled={isSystemType}
            />
        </td>

        <td className="border border-gray-300">
            <CommonTextField
                name={withNameSpace("code")}
                onChange={(e) => {
                    const codeValue = removeVietnameseTones(e.target.value).toUpperCase().replace(/\s+/g, "_");
                    setFieldValue(withNameSpace("code"), codeValue);
                }}
                disabled={isSystemType}
            />
        </td>

        <td className="border border-gray-300">
            {isSystemType ? (<CommonSelectInput
                name={withNameSpace("salaryItemType")}
                options={SalaryItemType.getListData()}
                disabled={isSystemType}
            />) : (<CommonSelectInput
                name={withNameSpace("salaryItemType")}
                options={SalaryItemType.getListData().filter((item) => item.value !== SalaryItemType.SYSTEM.value)}
                disabled={isSystemType}
            />)}
        </td>

        <td className="border border-gray-300">
            {item?.salaryItemType === SalaryItemType.FORMULA.value ? (
                <div className="flex items-center space-x-2">
                    <div className="flex-1">
                        <CommonTextField name={withNameSpace("formula")} disabled fullWidth/>
                    </div>
                    <Tooltip title="Nhập công thức"
                             className={"!max-w-10 !p-0 flex"}
                    >
                        <Button
                            onClick={() => setOpen(true)}
                            startIcon={<EditIcon/>}
                            className="!m-0 h-full"
                        />
                    </Tooltip>
                    <FormulaEditor
                        name={withNameSpace("formula")}
                        label="Công thức"
                        variables={values.templateItems?.map(item => item.code)}
                        required={true}
                        open={open}
                        onClosePopup={() => setOpen(false)}
                    />
                </div>
            ) : (
                <CommonNumberInput name={withNameSpace("defaultAmount")} disabled={isSystemType}/>
            )}
        </td>

        {
            !disabled && (<td className="text-center border border-gray-300">
          <span
              className="text-red-600 cursor-pointer hover:text-red-800"
              onClick={remove}
              title={t("Xóa")}
          >
            <Delete/>
          </span>
            </td>)
        }
    </tr>);
});

export default memo(observer(SalaryTemplateItemSection));
