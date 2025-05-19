import * as React from 'react';
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle
} from '@mui/material';

export default function AlertDialog(props) {
    const {
        open,
        onConfirmDialogClose,
        text,
        title,
        agree,
        cancel,
        onYesClick,
        handleAfterConfirm
    } = props;

    const handleAgree = async () => {
        if (typeof onYesClick === 'function') {
            await onYesClick();
        }
        if (typeof handleAfterConfirm === 'function') {
            handleAfterConfirm();
        }
        onConfirmDialogClose();
    };

    return (
        <Dialog
            open={open}
            onClose={onConfirmDialogClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <DialogTitle id="alert-dialog-title">
                {title || 'Xác nhận'}
            </DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    {text || 'Bạn có chắc chắn muốn thực hiện hành động này?'}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={onConfirmDialogClose}>
                    {cancel || 'Hủy'}
                </Button>
                <Button onClick={handleAgree} autoFocus>
                    {agree || 'Đồng ý'}
                </Button>
            </DialogActions>
        </Dialog>
    );
}
