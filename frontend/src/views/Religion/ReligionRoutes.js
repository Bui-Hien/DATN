import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./ReligionIndex"));

const ReligionRoutes = [
    {
        path: ConstantList.ROOT_PATH + "administration/religion",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default ReligionRoutes;
