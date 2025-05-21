import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./DepartmentIpIndex"));

const DepartmentIpRoutes = [
    {
        path: ConstantList.ROOT_PATH + "schedule/department-ip",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default DepartmentIpRoutes;
