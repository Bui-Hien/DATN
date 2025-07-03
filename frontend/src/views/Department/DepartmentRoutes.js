import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./DepartmentIndex"));

const DepartmentRoutes = [
    {
        path: ConstantList.ROOT_PATH + "organization/department",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN],
    },
];

export default DepartmentRoutes;
