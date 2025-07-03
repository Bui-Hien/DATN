import {memo, useEffect} from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import CommonBreadcrumb from "../../common/CommonBreadcrumb";
import {useParams} from "react-router-dom";
import SalaryResultItemList from "./SalaryResultItemList";
import SalaryResultItemToolbar from "./SalaryResultItemToolbar";

function SalaryResultItemIndex() {
    const {t} = useTranslation();
    const {id} = useParams();
    const {salaryResultItemStore, salaryResultStore} = useStore();

    const {
        pagingSalaryResultItem,
        searchObject,
        handleSetSearchObject,
        resetStore,
    } = salaryResultItemStore;
    const {selectedRow: salaryResult} = salaryResultStore;

    useEffect(() => {
        if (!id) return;
        handleSetSearchObject({
            ...searchObject,
            ownerId: id
        })
        pagingSalaryResultItem()
        return resetStore;
    }, []);

    return (
        <div className="content-index">
            <div className="">
                <CommonBreadcrumb routeSegments={[
                    {name: t("Lương")},
                    {name: t("Bảng lương")},
                    {name: t(`Chi tiết bảng lương - ${salaryResult?.name}`)}
                ]}/>
            </div>
            <div className="index-card grid grid-cols-12">
                <div className={"col-span-12"}>
                    <SalaryResultItemToolbar/>
                </div>

                <div className={"col-span-12"}>
                    <SalaryResultItemList/>
                </div>
            </div>

        </div>
    );
}

export default memo(observer(SalaryResultItemIndex));
