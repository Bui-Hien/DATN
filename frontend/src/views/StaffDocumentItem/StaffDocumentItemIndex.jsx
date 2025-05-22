import {memo, useEffect} from "react";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import StaffDocumentItemForm from "./StaffDocumentItemForm";

function StaffDocumentItemIndex() {
    const {staffDocumentItemStore, staffStore} = useStore();

    const {
        getStaffDocumentItemByDocumentTemplate,
        handleSetStaffId,
        resetStore
    } = staffDocumentItemStore;

    useEffect(() => {
        handleSetStaffId(staffStore.selectedRow?.id)
        getStaffDocumentItemByDocumentTemplate()
        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <StaffDocumentItemForm/>
                </div>
            </div>
        </div>
    );
}

export default memo(observer(StaffDocumentItemIndex));
