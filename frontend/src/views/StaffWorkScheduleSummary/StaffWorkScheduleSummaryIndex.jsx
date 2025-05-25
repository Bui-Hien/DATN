import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import StaffWorkScheduleSummaryList from "./StaffWorkScheduleSummaryList";
import StaffWorkScheduleSummaryToolbar from "./StaffWorkScheduleSummaryToolbar";

function StaffWorkScheduleSummaryIndex() {
    const {staffWorkScheduleSummaryStore} = useStore();
    const {t} = useTranslation();

    const {
        getScheduleSummary,
        resetStore
    } = staffWorkScheduleSummaryStore;

    useEffect(() => {
        getScheduleSummary()
        return resetStore;
    }, []);
    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Chấm công")},
                    {name: t("Thống kê công")}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <StaffWorkScheduleSummaryToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <StaffWorkScheduleSummaryList/>
                </div>
            </div>
        </div>
    );
}

export default memo(observer(StaffWorkScheduleSummaryIndex));
