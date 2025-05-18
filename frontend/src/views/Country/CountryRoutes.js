import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./CountryIndex"));

const CountryRoutes = [
    {
        path: ConstantList.ROOT_PATH + "administration/country",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default CountryRoutes;
