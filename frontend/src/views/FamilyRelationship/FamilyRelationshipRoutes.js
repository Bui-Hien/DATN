import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./FamilyRelationshipIndex"));

const FamilyRelationshipRoutes = [
    {
        path: ConstantList.ROOT_PATH + "family-relationship",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER, SystemRole.ROLE_HR],
    },
];

export default FamilyRelationshipRoutes;
