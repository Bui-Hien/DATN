import React, {memo, useEffect, useRef} from "react";
import {useTranslation} from "react-i18next";
import {Dialog, DialogActions, DialogContent, DialogTitle, IconButton, makeStyles, Paper,} from "@mui/material";
import Draggable from "react-draggable";
import CloseIcon from "@mui/icons-material/Close";
import AddCircleOutline from "@mui/icons-material/AddCircleOutline";
import PropTypes from "prop-types";

const useStyles = makeStyles({
    root: {
        "& .MuiDialogContent-root": {
            overflow: "auto !important",
        },
    },
});

function CommonPopupV2({
                            open,
                            onClosePopup = () => {},
                            title,
                            size,
                            children,
                            styleTitle,
                            noHeader,
                            styleContent,
                            action,
                            noDialogContent,
                            popupId,
                            scroll,
                        }) {
    const { t } = useTranslation();
    const classes = useStyles();
    const dialogRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dialogRef.current && !dialogRef.current.contains(event.target)) {
                onClosePopup();
            }
        };

        if (open) {
            document.addEventListener("mousedown", handleClickOutside);
        } else {
            document.removeEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [open, onClosePopup]);

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
            onClose={onClosePopup}
        >
            <div ref={dialogRef}>
                {!noHeader && (
                    <>
                        <DialogTitle
                            className="dialog-header dialog-header-v2"
                            style={{ cursor: "move", ...styleTitle }}
                            id={popupId || "globits-draggable-dialog-title"}
                        >
              <span className="flex flex-middle gap-1" style={{ display: "flex", alignItems: "center", gap: 4 }}>
                <AddCircleOutline />
                  {title}
              </span>
                        </DialogTitle>

                        <IconButton
                            className="p-12"
                            style={{ position: "absolute", right: 0, top: 0 }}
                            onClick={onClosePopup}
                            aria-label={t("general.close")}
                        >
                            <CloseIcon />
                        </IconButton>
                    </>
                )}

                {!noDialogContent ? (
                    <DialogContent style={{ overflowY: "auto", maxHeight: "75vh", ...styleContent }}>
                        {children}
                    </DialogContent>
                ) : (
                    children
                )}

                {action && (
                    <DialogActions className="dialog-footer dialog-footer-v2 p-0 mt-20">{action}</DialogActions>
                )}
            </div>
        </Dialog>
    );
}

CommonPopupV2.propTypes = {
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

CommonPopupV2.defaultProps = {
    size: "lg",
};

export default memo(CommonPopupV2);
