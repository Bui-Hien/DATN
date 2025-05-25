import React, {memo} from "react";
import ProfileStaffTabs from "./Tab/TabIndex";
import {useLocation} from "react-router-dom";
import {useStore} from "../../stores";

function ProfileStaff() {
    const {staffStore} = useStore();
    const location = useLocation();
    const isProfile = location.pathname === "/profile"
    if (isProfile) {
        staffStore.handleSetIsProfile();
    }
    return (
        <div className="w-full">
            <ProfileStaffTabs/>
        </div>
    );
}

export default memo(ProfileStaff);
