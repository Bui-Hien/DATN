import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./StaffMonthScheduleCalendarIndex"));

const StaffMonthScheduleCalendarRoutes = [
    {
        path: ConstantList.ROOT_PATH + "staff-month-schedule-calendar",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default StaffMonthScheduleCalendarRoutes;
