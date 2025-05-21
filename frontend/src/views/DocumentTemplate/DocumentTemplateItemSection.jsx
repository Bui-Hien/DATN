import {FieldArray, useFormikContext} from "formik";
import React, {memo} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {Delete} from "@mui/icons-material";
import {Button, ButtonGroup} from "@mui/material";
import CommonTextField from "../../common/form/CommonTextField";
import CommonNumberInput from "../../common/form/CommonNumberInput";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import {DocumentItemRequired} from "../../LocalConstants";
import AddIcon from "@mui/icons-material/Add";

function DocumentTemplateItemSection() {
    const {t} = useTranslation();
    const {values} = useFormikContext();

    function handleAddNewRow(push) {
        const newItem = {
            name: "",
            description: "",
            displayOrder: null,
            isRequired: false,
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
                                    {t("Thêm mới tài liệu")}
                                </Button>
                            </ButtonGroup>
                        </div>

                        <section className="mt-4 overflow-x-auto">
                            <table className="w-full border border-gray-300 table-auto">
                                <thead>
                                <tr className="bg-gray-100">
                                    <th className="border border-gray-300 w-[5%] text-center">{t("STT")}</th>
                                    <th className="border border-gray-300 w-[25%] text-center">{t("Tên tài liệu")}<span
                                        style={{color: "red"}}> * </span></th>
                                    <th className="border border-gray-300 w-[30%] text-center">{t("Mô tả tài liệu")}</th>
                                    <th className="border border-gray-300 w-[10%] text-center">{t("Thứ tự hiển thị")}<span
                                        style={{color: "red"}}> * </span></th>
                                    <th className="border border-gray-300 w-[15%] text-center">{t("Cần phải nộp")}<span
                                        style={{color: "red"}}> * </span></th>
                                    <th className="border border-gray-300 w-[10%] text-center">{t("Hành động")}</th>
                                </tr>
                                </thead>
                                <tbody>
                                {values?.documentItems?.length > 0 ? (
                                    values.documentItems.map((order, index) => (
                                        <HrDocumentItem
                                            key={index}
                                            index={index}
                                            order={order}
                                            hrDocumentItems={values.documentItems}
                                            nameSpace={`documentItems[${index}]`}
                                            remove={() => remove(index)}
                                            push={() => push(index)}
                                        />
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan={7} className="text-center py-4 text-gray-500">
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

const HrDocumentItem = memo((props) => {
    const {index, nameSpace, remove, disabled} = props;
    const {t} = useTranslation();

    const withNameSpace = (field) => (field ? `${nameSpace}.${field}` : nameSpace);

    return (
        <tr className="border border-gray-300">
            <td className="text-center border border-gray-300">{index + 1}</td>
            <td className="border border-gray-300">
                <CommonTextField name={withNameSpace("name")}/>
            </td>
            <td className="border border-gray-300">
                <CommonTextField name={withNameSpace("description")}/>
            </td>
            <td className="border border-gray-300">
                <CommonNumberInput name={withNameSpace("displayOrder")}/>
            </td>
            <td className="border border-gray-300">
                <CommonSelectInput
                    hideNullOption
                    name={withNameSpace("isRequired")}
                    keyValue="value"
                    displayvalue="name"
                    options={DocumentItemRequired.getListData()}
                />
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

export default memo(observer(DocumentTemplateItemSection));
