import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./TimeSheetDetailIndex"));

const TimeSheetDetailRoutes = [
    {
        path: ConstantList.ROOT_PATH + "time-sheet-detail",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER, SystemRole.ROLE_HR],
    },
];

export default TimeSheetDetailRoutes;
