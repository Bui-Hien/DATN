import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./CandidateIndex"));
const ViewCreateOrUpdateComponent = lazy(() => import("./CandidateForm"));

const CandidateRoutes = [
    {
        path: ConstantList.ROOT_PATH + "candidate",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER, SystemRole.ROLE_HR],
    },
    {
        path: ConstantList.ROOT_PATH + "candidate/create",
        exact: true,
        component: ViewCreateOrUpdateComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER, SystemRole.ROLE_HR],
    },
    {
        path: ConstantList.ROOT_PATH + "candidate/:id",
        exact: true,
        component: ViewCreateOrUpdateComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER, SystemRole.ROLE_HR],
    },
];

export default CandidateRoutes;
