import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./SalaryPeriodIndex"));

const SalaryPeriodRoutes = [
    {
        path: ConstantList.ROOT_PATH + "salary-period",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default SalaryPeriodRoutes;
