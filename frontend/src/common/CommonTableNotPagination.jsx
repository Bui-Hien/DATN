import React from "react";
import MaterialTable from "material-table";
import {useTranslation} from "react-i18next";
import {makeStyles} from "@mui/material";

const useStyles = makeStyles(() => ({
  globitsTableWraper: {
    maxHeight: "700px",
    overflowY: "auto",
    borderColor: "none !important",
    ["@media (max-width:420px)"]: {
      maxHeight: "400px",
      overflowY: "auto",
    },
  },
}));

export default function CommonTableNotPagination(props) {
  const classes = useStyles();
  const { t } = useTranslation();
  const { data, columns, selection, handleSelectList, maxWidth, title } = props;

  return (
      <>
        <div className={classes.globitsTableWraper}>
          <MaterialTable
              data={data}
              columns={columns}
              parentChildData={(row, rows) => {
                if (row.parentId) {
                  return rows.find((a) => a.id === row.parentId) || null;
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
                  maxHeight: "700px",
                  backgroundColor: "#01c0c8",
                  color: "#fff",
                  paddingLeft: !selection ? "5px" : "unset",
                  paddingRight: !selection ? "5px" : "unset",
                  position: "sticky",
                  maxWidth: maxWidth || "auto",
                  textAlign: "center",
                },
                rowStyle: (rowData, index) => ({
                  backgroundColor: index % 2 === 1 ? "#f8f9fa" : "#ffffff",
                }),
              }}
              onSelectionChange={(rows) => {
                if (handleSelectList) handleSelectList(rows);
              }}
              localization={{
                body: {
                  emptyDataSourceMessage: t("general.emptyDataMessageTable"),
                },
              }}
          />
        </div>
      </>
  );
}
