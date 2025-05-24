import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CommonTable from "../../common/CommonTable";
import {ShiftWorkType} from "../../LocalConstants";
import {getDate} from "../../LocalFunction";

function StaffWorkScheduleList() {
    const {t} = useTranslation();
    const {staffWorkScheduleStore} = useStore();

    const {
        handleOpenCreateEdit,
        totalPages,
        handleDelete,
        dataList,
        searchObject,
        totalElements,
        setPageIndex,
        setPageSize,
        handleSelectListDelete
    } = staffWorkScheduleStore;


    const columns = [
        {
            accessorKey: "action",
            header: t("Hành động"),
            Cell: ({row}) => (
                <div className="flex flex-middle justify-center">
                    <Tooltip
                        className={"cursor-pointer"}
                        title={t("Cập nhật thông tin")}
                        placement="top">
                        <EditIcon
                            onClick={() => handleOpenCreateEdit(row.original.id)}
                        />
                    </Tooltip>

                    <Tooltip title={t("Xóa")} placement="top">
                        <DeleteIcon
                            className="cursor-pointer ml-4"
                            onClick={() => handleDelete(row.original)}
                        />
                    </Tooltip>
                </div>
            ),
        },
        {
            accessorKey: "staff",
            header:
                t("Nhân viên được phân ca"),
            Cell: ({row}) => {
                const value = row.original.staff;
                const staffCode = value?.staffCode || "";
                const displayName = value?.displayName || "";
                return <span>{displayName + " - " + staffCode}</span>;
            }
        },
        {
            accessorKey: "shiftWorkType",
            header:
                t("Ca làm việc"),
            Cell: ({row}) => {
                const value = row.original.shiftWorkType;
                const name = ShiftWorkType.getListData().find(i => i.value === value)?.name || "";
                return <span>{name}</span>;
            }
        },
        {
            accessorKey: "workingDate",
            header:
                t("Ngày làm việc"),
            Cell: ({row}) => {
                const value = row.original.workingDate;
                return <span>{getDate(value)}</span>;
            }
        },
        {
            accessorKey: "coordinator",
            header:
                t("Người phân ca"),
            Cell: ({row}) => {
                const value = row.original.coordinator;
                const staffCode = value?.staffCode || "";
                const displayName = value?.displayName || "";
                return <span>{displayName + " - " + staffCode}</span>;
            }
        },
    ];

    return (
        <CommonTable
            data={dataList}
            columns={columns}
            selection={true}
            nonePagination={false}
            totalPages={totalPages}
            pageSize={searchObject.pageSize}
            page={searchObject.pageIndex}
            totalElements={totalElements}
            pageSizeOption={[5, 10, 15]}
            handleChangePage={setPageIndex}
            setRowsPerPage={setPageSize}
            handleSelectList={handleSelectListDelete}
        />
    );
}

export default observer(StaffWorkScheduleList);
