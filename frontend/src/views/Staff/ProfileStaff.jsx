import React, {memo} from "react";
import ProfileStaffTabs from "./Tab/TabIndex";

function ProfileStaff() {
    return (
        <div className="w-full">
            <ProfileStaffTabs/>
        </div>
    );
}

export default memo(ProfileStaff);
