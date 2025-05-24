import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./StaffWorkScheduleIndex"));

const StaffWorkScheduleRoutes = [
    {
        path: ConstantList.ROOT_PATH + "staff-work-schedule",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default StaffWorkScheduleRoutes;
