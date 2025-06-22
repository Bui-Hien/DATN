import React, {memo, useEffect} from "react";
import ProfileStaffTabs from "./Tab/TabIndex";
import {useLocation} from "react-router-dom";
import {useStore} from "../../stores";
import {observer} from "mobx-react-lite";
import ChangePasswordForm from "../User/ChangePasswordForm";

function ProfileStaff() {
    const {staffStore, userStore} = useStore();
    const location = useLocation();
    useEffect(() => {
        if (location.pathname.includes("/profile")) {
            staffStore.handleSetIsProfile(true);
        } else {
            staffStore.handleSetIsProfile(false);
        }
    }, [location.pathname])
    return (
        <div className="w-full">
            <ProfileStaffTabs/>
            {userStore.openChangePassword && <ChangePasswordForm/>}
        </div>);
}

export default memo(observer(ProfileStaff));
