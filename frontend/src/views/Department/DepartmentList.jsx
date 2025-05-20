import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CommonTable from "../../common/CommonTable";
import CheckIcon from '@mui/icons-material/Check';

function DepartmentList() {
    const {t} = useTranslation();
    const {departmentStore} = useStore();

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
    } = departmentStore;


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
            accessorKey: "code",
            header: t("Mã phòng ban"),
        },
        {
            accessorKey: "name",
            header: t("Tên phòng ban"),
        },
        {
            accessorKey: "nameParent",
            header: t("Tên phòng ban cha"),
            Cell: ({row}) => {
                const name = row.original?.parent?.name;
                return <span>{name}</span>;
            }
        },
        {
            accessorKey: "positionManager",
            header: t("Phòng ban đã có quản lý"),
            Cell: ({row}) => {
                const isPositionManager = row.original?.positionManager?.id;
                return <span>{isPositionManager ? <CheckIcon fontSize="small" style={{color: "green"}}/> : ""}</span>;
            }
        },
        {
            accessorKey: "description",
            header: t("Mô tả phòng ban"),
        },

    ];

    return (
        <CommonTable
            colParent={true}
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

export default observer(DepartmentList);
