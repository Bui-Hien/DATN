import React, {memo, useState} from "react";
import {useTranslation} from "react-i18next";
import {useFormikContext} from "formik";
import {observer} from "mobx-react-lite";
import {Button, ButtonGroup, Checkbox, Tooltip} from "@mui/material";
import ChooseSelectedStaff from "../../../common/CommonSelectedStaff/ChooseSelectedStaff";
import CommonTable from "../../../common/CommonTable";
import {getDate} from "../../../LocalFunction";
import {Gender} from "../../../LocalConstants";
import DeleteIcon from '@mui/icons-material/Delete';

function StaffWorkScheduleListTable() {
    const {t} = useTranslation();
    const {values, setFieldValue} = useFormikContext();
    const [selected, setSelected] = useState([]);
    const dataList = values.staffs || [];

    const isSelected = (id) => selected.includes(id);

    const handleChange = (staff) => {
        const exists = selected.includes(staff.id);
        const updated = exists ? selected.filter(id => id !== staff.id) : [...selected, staff.id];
        setSelected(updated);
    };

    const handleSelectAll = () => {
        if (selected.length === dataList.length) {
            setSelected([]);
        } else {
            const allIds = dataList.map(staff => staff.id);
            setSelected(allIds);
        }
    };

    const columns = [
        {
            accessorKey: "action",
            header: () => {
                const allSelected = selected.length === dataList.length;
                return (
                    <div className="flex justify-center">
                        <Tooltip title={allSelected ? t("Bỏ chọn tất cả") : t("Chọn tất cả")}>
                            <Checkbox
                                checked={allSelected}
                                indeterminate={
                                    selected.length > 0 && selected.length < dataList.length
                                }
                                onClick={handleSelectAll}
                                disabled={dataList.length === 0}
                            />
                        </Tooltip>
                    </div>
                );
            },
            Cell: ({row}) => {
                const staff = row.original;
                const checked = isSelected(staff.id);
                return (
                    <div className="flex justify-center">
                        <Tooltip title={checked ? t("Đã chọn") : t("Chọn")}>
                            <Checkbox
                                checked={checked}
                                onClick={() => handleChange(staff)}
                            />
                        </Tooltip>
                    </div>
                );
            },

        },
        {
            accessorKey: "staffCode",
            header: t("Mã nhân viên"),
        },
        {
            accessorKey: "displayName",
            header: t("Họ và tên"),
        },
        {
            accessorKey: "gender",
            header: t("Giới tính"),
            Cell: ({row}) => {
                const value = row.original.gender;
                const name = Gender.getListData().find(i => i.value === value)?.name || "";
                return <span>{name}</span>;
            }
        },
        {
            accessorKey: "birthDate",
            header: t("Ngày sinh"),
            Cell: ({row}) => <span>{getDate(row.original.birthDate)}</span>,
        },
        {
            accessorKey: "phoneNumber",
            header: t("Số điện thoại"),
        },
        {
            accessorKey: "email",
            header: t("Email"),
        },
        {
            accessorKey: "salaryTemplate",
            header: t("Mẫu bảng lương"),
            Cell: ({row}) => <span>{row.original.salaryTemplate?.name || ""}</span>,
        },
    ];

    return (
        <div className="w-full">
            <div className="col-span-12 border-b border-gray-300 pb-2 mb-2 mt-4 font-semibold">
                {t("Nhân viên được phân ca")}
            </div>
            <div className="col-span-12 flex justify-between">
                <div className="font-bold text-gray-700">
                    Danh sách nhân viên<span style={{color: "red"}}> * </span> ({dataList.length} nhân viên được chọn)
                </div>
                <div className="flex gap-2">
                    <ButtonGroup
                        color="container"
                        aria-label="outlined primary button group"
                    >
                        <Button
                            onClick={() => {
                                const filtered = dataList.filter(staff => !selected.includes(staff.id));
                                setFieldValue("staffs", filtered);
                                setSelected([]);
                            }}
                            startIcon={<DeleteIcon/>}
                            disabled={selected.length === 0}
                        >
                            Xoá
                        </Button>
                    </ButtonGroup>
                    <ChooseSelectedStaff
                        title={"Chọn nhân viên phân ca"}
                        labelButton={"Chọn nhân viên phân ca"}
                        name={"staffs"}
                        isOnlyChoose={true}
                        multiline={true}
                        className={"!w-auto"}
                    />
                </div>
            </div>
            <div className="col-span-12">
                <CommonTable
                    data={dataList}
                    columns={columns}
                    nonePagination={true}
                />
            </div>
        </div>
    );
}

export default memo(observer(StaffWorkScheduleListTable));
