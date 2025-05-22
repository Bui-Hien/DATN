import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CommonTable from "../../common/CommonTable";
import {getDate} from "../../LocalFunction";

function PersonFamilyRelationshipList() {
    const {t} = useTranslation();
    const {personFamilyRelationshipStore} = useStore();

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
    } = personFamilyRelationshipStore;


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
            accessorKey: "fullName",
            header:
                t("Họ và tên"),
        },
        {
            accessorKey: "familyRelationship.name",
            header:
                t("Loại quan hệ"),
        },
        {
            accessorKey: "address",
            header:
                t("Địa chỉ"),
        },
        {
            accessorKey: "birthDate",
            header:
                t("Ngày sinh"),
            Cell: ({row}) => <span>{getDate(row.original.birthDate)}</span>
        },
        {
            accessorKey: "profession.name",
            header:
                t("Nghề nghiệp"),
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

export default observer(PersonFamilyRelationshipList);
