import React from "react";
import {useTranslation} from "react-i18next";
import CommonTable from "../../common/CommonTable";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import Icon from "@mui/material/Icon";

function CountryList() {
    const {t} = useTranslation();
    const {countryStore} = useStore();

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
    } = countryStore;

    // Cột tương ứng với DTO: code, name, description
    const columns = [
        // {
        //     accessorKey: "action",
        //     header: t("Hành động"),
        //     Cell: ({ row }) => (
        //         <div className="flex flex-middle justify-center">
        //             <Tooltip title={t("Cập nhật thông tin")} placement="top">
        //                 <IconButton
        //                     size="small"
        //                     onClick={() => handleOpenCreateEdit(row.original.id)}
        //                 >
        //                     <Icon fontSize="small" color="primary">
        //                         edit
        //                     </Icon>
        //                 </IconButton>
        //             </Tooltip>
        //
        //             <Tooltip title={t("Xóa")} placement="top">
        //                 <IconButton
        //                     size="small"
        //                     className="ml-4"
        //                     onClick={() => handleDelete(row.original)}
        //                 >
        //                     <Icon fontSize="small" color="secondary">
        //                         delete
        //                     </Icon>
        //                 </IconButton>
        //             </Tooltip>
        //         </div>
        //     ),
        // },
        {
            accessorKey: "code",
            header: t("Mã quốc gia"), // hoặc "Code"
        },
        {
            accessorKey: "name",
            header: t("Tên quốc gia"), // hoặc "Name"
        },
        {
            accessorKey: "description",
            header: t("Mô tả"), // hoặc "Description"
        },
    ];

    return (
        <CommonTable
            data={dataList} // Dữ liệu lấy từ
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
            handleSelectList={(selected) => {
                handleSelectListDelete(selected);
            }}
        />
    );
}

export default observer(CountryList);
