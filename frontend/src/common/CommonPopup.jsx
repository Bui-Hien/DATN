import React, {memo} from "react";
import {useTranslation} from "react-i18next";
import {Dialog, DialogActions, DialogContent, DialogTitle, IconButton, makeStyles, Paper,} from "@mui/material";
import Draggable from "react-draggable";
import CloseIcon from "@mui/icons-material/Close";
import PropTypes from "prop-types";

const useStyles = makeStyles({
    root: {
        "& .MuiDialogContent-root": {
            overflow: "auto !important",
        },
    },
});

function CommonPopup({
                          open,
                          onClosePopup,
                          title,
                          size,
                          children,
                          styleTitle = {},
                          noHeader,
                          styleContent,
                          action,
                          noDialogContent,
                          popupId,
                          scroll,
                      }) {
    const { t } = useTranslation();
    const classes = useStyles();

    function PaperComponent(props) {
        return (
            <Draggable
                handle={popupId ? `#${popupId}` : "#globits-draggable-dialog-title"}
                cancel='[class*="MuiDialogContent-root"]'
            >
                <Paper {...props} />
            </Draggable>
        );
    }

    return (
        <Dialog
            className={`dialog-container ${classes.root}`}
            open={open}
            PaperComponent={PaperComponent}
            fullWidth
            scroll={scroll || "paper"}
            maxWidth={size}
            aria-labelledby={popupId || "globits-draggable-dialog-title"}
            onClose={onClosePopup} // Allow closing on ESC and backdrop click
        >
            {!noHeader && (
                <>
                    <DialogTitle
                        id={popupId || "globits-draggable-dialog-title"}
                        style={{ cursor: "move", ...styleTitle }}
                        className="dialog-header"
                    >
                        <span className="text-white">{title}</span>
                    </DialogTitle>
                    <IconButton
                        onClick={onClosePopup}
                        aria-label={t("general.close")}
                        sx={{
                            position: "absolute",
                            right: 0,
                            top: 0,
                            color: "white",
                            padding: "12px",
                        }}
                    >
                        <CloseIcon />
                    </IconButton>
                </>
            )}

            {!noDialogContent ? (
                <DialogContent
                    style={{ overflowY: "auto", maxHeight: "75vh", ...styleContent }}
                >
                    {children}
                </DialogContent>
            ) : (
                children
            )}

            {action && (
                <DialogActions className="dialog-footer p-0 mt-20">{action}</DialogActions>
            )}
        </Dialog>
    );
}

CommonPopup.propTypes = {
    size: PropTypes.oneOf([false, "xs", "sm", "md", "lg", "xl"]),
    open: PropTypes.bool.isRequired,
    onClosePopup: PropTypes.func.isRequired,
    title: PropTypes.string,
    children: PropTypes.node,
    noHeader: PropTypes.bool,
    noDialogContent: PropTypes.bool,
    action: PropTypes.node,
    styleTitle: PropTypes.object,
    popupId: PropTypes.string,
    scroll: PropTypes.oneOf(["paper", "body"]),
};

CommonPopup.defaultProps = {
    size: "lg",
};

export default memo(CommonPopup);
