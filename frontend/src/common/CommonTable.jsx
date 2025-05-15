import React, { useMemo } from "react";
import PropTypes from "prop-types";
import MaterialTable from "material-table";
import { useTranslation } from "react-i18next";
import CommonPagination from "./CommonPagination";
import {makeStyles} from "@mui/material";

const useStyles = makeStyles(() => ({
  globitsTableWraper: {
    width: "100%",
    "& td": {
      border: "1px solid #ccc",
      paddingLeft: "4px",
    },
    "& th": {
      border: "1px solid #ccc",
      fontWeight: "600",
      color: "#000000",
    },
    border: "0 !important",
    overflow: "hidden",
    backgroundColor: "white",

    "& .MuiCheckbox-root": {
      display: "flex",
      justifyContent: "center",
      margin: 0,
    },

    "& .MuiPaper-elevation2": {
      boxShadow: "none",
    },

    "& .mat-mdc-row:hover": {
      backgroundColor: "red",
    },
  },
}));

function CommonTable(props) {
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
    nonePagination,
    maxHeight,
    colParent = false,
    specialStyleForLastRow,
    defaultExpanded = false,
    rowStyle: propRowStyle,
  } = props;

  const internalRowStyle = useMemo(() => {
    return (rowData, index) => {
      if (!data || !Array.isArray(data)) {
        return {};
      }
      const isLastRow = index === data.length - 1;
      if (specialStyleForLastRow && isLastRow) {
        return {
          backgroundColor: "#fffacd",
          fontWeight: "bold",
          textAlign: "center",
          color: "#000",
          border: "1px solid #000",
        };
      }
      return {
        backgroundColor: !(index % 2 === 1) ? "#fbfcfd" : "#ffffff",
        textAlign: "center",
        color: "red",
      };
    };
  }, [data, specialStyleForLastRow]);

  const filterCellStyle = useMemo(() => {
    return () => ({});
  }, []);

  const rowStyle = propRowStyle || internalRowStyle;

  return (
      <div className={classes.globitsTableWraper}>
        <MaterialTable
            data={data}
            columns={columns}
            style={{
              borderRadius: "10px",
              maxWidth: maxWidth || "auto",
            }}
            parentChildData={
              colParent
                  ? (row, rows) => {
                    if (row?.parentId) {
                      return rows.find((a) => a?.id === row?.parentId);
                    }
                    return null;
                  }
                  : undefined
            }
            options={{
              selection: Boolean(selection),
              sorting: false,
              actionsColumnIndex: -1,
              paging: false,
              search: false,
              toolbar: false,
              draggable: false,
              maxBodyHeight: maxHeight || "unset",
              headerStyle: {
                color: "#000",
                paddingLeft: "4px",
                paddingRight: !selection ? "4px" : "unset",
                paddingTop: "8px",
                paddingBottom: "8px",
                fontSize: "14px",
                maxWidth: maxWidth || "auto",
                textAlign: "center",
              },
              rowStyle,
              filterCellStyle,
              defaultExpanded,
            }}
            onSelectionChange={(rows) => {
              if (handleSelectList) {
                handleSelectList(rows);
              }
            }}
            localization={{
              body: {
                emptyDataSourceMessage: `${t("general.emptyDataMessageTable")}`,
              },
            }}
        />

        {!nonePagination && (
            <CommonPagination
                totalPages={totalPages}
                handleChangePage={handleChangePage}
                setRowsPerPage={setRowsPerPage}
                pageSize={pageSize}
                pageSizeOption={pageSizeOption}
                totalElements={totalElements}
                page={page}
            />
        )}
      </div>
  );
}

CommonTable.propTypes = {
  ...CommonPagination.propTypes,
  data: PropTypes.array.isRequired,
  columns: PropTypes.arrayOf(
      PropTypes.shape({
        field: PropTypes.string,
        title: PropTypes.string,
        minWidth: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
        render: PropTypes.func,
        cellStyle: PropTypes.object,
        headerStyle: PropTypes.object,
        align: PropTypes.string,
      })
  ).isRequired,
  selection: PropTypes.bool,
  handleSelectList: PropTypes.func,
  maxWidth: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  maxHeight: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  nonePagination: PropTypes.bool,
  defaultExpanded: PropTypes.bool,
  rowStyle: PropTypes.func,
};

CommonTable.defaultProps = {
  data: [],
};

export default CommonTable;
