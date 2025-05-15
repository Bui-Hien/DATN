import React from "react";
import {Dialog, DialogContent, DialogTitle, Grid, Icon, IconButton, MenuItem, TextField,} from "@mui/material"; // <-- MUI v5 import
import {useTranslation} from "react-i18next";

export default function PaginationOptionPopup(props) {
  const { t } = useTranslation();
  const {
    open,
    handleClose,
    setRowsPerPage,
    pageSizeOption,
    totalPages,
    handleChangePage,
    page,
  } = props;

  const [pageIndex, setPageIndex] = React.useState(page);

  const handleGo = (event) => {
    if (pageIndex < 1 || pageIndex > totalPages) {
      alert("Hãy nhập số từ 1 đến " + totalPages);
      return;
    }
    handleChangePage(event, Number(pageIndex));
  };

  const [pageSize, setPageSize] = React.useState(props.pageSize);

  const handleChange = (event) => {
    setRowsPerPage(event);
    setPageSize(event.target.value);
  };

  return (
      <Dialog className="dialog-container" open={open} fullWidth maxWidth="sm">
        <DialogTitle
            className="dialog-header"
            style={{ cursor: "move" }}
            id="draggable-dialog-title"
        >
          <span className="mb-20">{t("pagination.tool")}</span>
          <IconButton
              style={{ position: "absolute", right: 10, top: 10 }}
              onClick={() => handleClose()}
          >
            <Icon title={t("general.close")}>close</Icon>
          </IconButton>
        </DialogTitle>
        <DialogContent className="o-hidden" sx={{ pb: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <div>
                <p>{t("general.rowsPerPage")}</p>
                <TextField select value={pageSize} onChange={handleChange} fullWidth>
                  {pageSizeOption.map((option, index) => (
                      <MenuItem key={index} value={option}>
                        {option}
                      </MenuItem>
                  ))}
                </TextField>
              </div>
            </Grid>
            <Grid item xs={12}>
              <div>
                <p>{t("general.gotoPage")}</p>
                <TextField
                    type="number"
                    fullWidth
                    name="pageIndex"
                    value={pageIndex}
                    onChange={(e) => setPageIndex(e.target.value)}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        handleGo(e);
                      }
                    }}
                />
              </div>
            </Grid>
          </Grid>
        </DialogContent>
      </Dialog>
  );
}
