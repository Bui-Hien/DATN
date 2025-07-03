import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./ProfessionIndex"));

const ProfessionRoutes = [
    {
        path: ConstantList.ROOT_PATH + "administration/profession",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN],
    },
];

export default ProfessionRoutes;
