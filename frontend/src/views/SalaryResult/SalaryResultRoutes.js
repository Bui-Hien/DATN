import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./SalaryResultIndex"));
const ViewComponentSalaryResultItem = lazy(() => import("../SalaryResultItem/SalaryResultItemIndex"));

const SalaryResultRoutes = [
    {
        path: ConstantList.ROOT_PATH + "salary-result",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
    {
        path: ConstantList.ROOT_PATH + "salary-result/:id",
        exact: true,
        component: ViewComponentSalaryResultItem,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default SalaryResultRoutes;
