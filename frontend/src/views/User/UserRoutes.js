import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./UserIndex"));

const UserRoutes = [
    {
        path: ConstantList.ROOT_PATH + "system-user",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN],
    },
];

export default UserRoutes;
