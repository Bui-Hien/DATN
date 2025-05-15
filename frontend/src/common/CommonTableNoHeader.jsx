import React from "react";
import MaterialTable from "material-table";
import CommonPagination from "./CommonPagination";
import {useTranslation} from "react-i18next";
import {makeStyles} from "@mui/material";

const useStyles = makeStyles(() => ({
  globitsTableWraper: {
    '& td': {
      borderBottom: 'unset !important',
    },
  },
}));

export default function GlobitsTable(props) {
  const classes = useStyles();
  const { t } = useTranslation();

  const {
    data,
    columns,
    totalPages,
    handleChangePage,
    setRowsPerPage,
    pageSize,
    pageSizeOption,
    totalElements,
    page,
    selection,
    handleSelectList,
    maxWidth,
    fontSize,
    paddingRight,
    paddingLeft,
  } = props;

  return (
      <div className={classes.globitsTableWraper}>
        <MaterialTable
            data={data}
            columns={columns}
            parentChildData={(row, rows) => {
              if (row.parentId) {
                return rows.find((a) => a.id === row.parentId);
              }
              return null;
            }}
            options={{
              selection: Boolean(selection),
              actionsColumnIndex: -1,
              paging: false,
              search: false,
              toolbar: false,
              headerStyle: {
                color: "#000",
                paddingLeft: paddingLeft || "unset",
                paddingRight: paddingRight || "unset",
                position: "sticky",
                maxWidth: maxWidth || "auto",
                fontSize: fontSize || "auto",
                textAlign: "center",
              },
              rowStyle: (rowData, index) => ({
                backgroundColor: index % 2 === 1 ? "#f8f9fa" : "#ffffff",
              }),
            }}
            onSelectionChange={(rows) => {
              if (handleSelectList) {
                handleSelectList(rows);
              }
            }}
            localization={{
              body: {
                emptyDataSourceMessage: t("general.emptyDataMessageTable"),
              },
            }}
        />
        <CommonPagination
            totalPages={totalPages}
            handleChangePage={handleChangePage}
            setRowsPerPage={setRowsPerPage}
            pageSize={pageSize}
            pageSizeOption={pageSizeOption}
            totalElements={totalElements}
            page={page}
        />
      </div>
  );
}
