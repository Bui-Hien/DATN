import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./AdministrativeUnitIndex"));

const AdministrativeUnitRoutes = [
    {
        path: ConstantList.ROOT_PATH + "administration/administrative-unit",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN],
    },
];

export default AdministrativeUnitRoutes;
