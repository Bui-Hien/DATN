import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./SalaryTemplateIndex"));

const SalaryTemplateRoutes = [
    {
        path: ConstantList.ROOT_PATH + "salary-template",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default SalaryTemplateRoutes;
