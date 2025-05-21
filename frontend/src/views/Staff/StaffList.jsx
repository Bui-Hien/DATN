import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CommonTable from "../../common/CommonTable";
import {AdministrativeUnitLevel} from "../../LocalConstants";
import {useNavigate} from "react-router-dom";

function StaffList() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const {staffStore} = useStore();

    const {
        totalPages,
        handleDelete,
        dataList,
        searchObject,
        totalElements,
        setPageIndex,
        setPageSize,
        handleSelectListDelete
    } = staffStore;


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
                            onClick={() => navigate(`/staff/${row.original.id}`)}
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
            header:
                t("Mã đơn vị hành chính"), // hoặc "Code"
        },
        {
            accessorKey: "name",
            header:
                t("Tên đơn vị hành chính"), // hoặc "Name"
        },
        {
            accessorKey: "level",
            header:
                t("Cấp độ vị hành chính"),
            Cell: ({row}) => {
                const value = row.original.level;
                const name = AdministrativeUnitLevel.getListData().find(i => i.value === value)?.name || "";
                return <span>{name}</span>;
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

export default observer(StaffList);
