import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CommonTable from "../../common/CommonTable";
import {useNavigate} from "react-router-dom";
import VisibilityIcon from '@mui/icons-material/Visibility';
function SalaryResultList() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const {salaryResultStore} = useStore();

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
    } = salaryResultStore;


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
                    <Tooltip title={t("Xem chi tiết bảng lương")} placement="top">
                        <VisibilityIcon
                            className="cursor-pointer ml-4"
                            onClick={() => navigate(`/salary-result/${row.original.id}`)}
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
            accessorKey: "name",
            header:
                t("Tên bảng lương"),
        },
        {
            accessorKey: "salaryPeriod",
            header:
                t("Tên kỳ lương được tính"),
            Cell: ({row}) => {
                const value = row.original?.salaryPeriod?.name || "";
                return <span>{value}</span>;
            }
        },
        {
            accessorKey: "salaryTemplate",
            header:
                t("Mẫu bảng lương sử dụng"),
            Cell: ({row}) => {
                const value = row.original?.salaryTemplate?.name || "";
                return <span>{value}</span>;
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

export default observer(SalaryResultList);
