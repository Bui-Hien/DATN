import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./StaffWorkScheduleSummaryIndex"));

const StaffWorkScheduleSummaryRoutes = [
    {
        path: ConstantList.ROOT_PATH + "staff-work-schedule-summary",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default StaffWorkScheduleSummaryRoutes;
