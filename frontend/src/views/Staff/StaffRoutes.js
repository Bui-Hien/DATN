import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./StaffIndex"));
const ViewProfileStaffComponent = lazy(() => import("./ProfileStaff"));

const StaffRoutes = [
    {
        path: ConstantList.ROOT_PATH + "staff",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
    {
        path: ConstantList.ROOT_PATH + "staff/:id",
        exact: true,
        component: ViewProfileStaffComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
    {
        path: ConstantList.ROOT_PATH + "profile",
        exact: true,
        component: ViewProfileStaffComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default StaffRoutes;
