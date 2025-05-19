import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./EthnicsIndex"));

const EthnicsRoutes = [
    {
        path: ConstantList.ROOT_PATH + "administration/ethnics",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default EthnicsRoutes;
