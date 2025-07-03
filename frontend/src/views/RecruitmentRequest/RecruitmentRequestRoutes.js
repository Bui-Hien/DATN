import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./RecruitmentRequestIndex"));

const RecruitmentRequestRoutes = [
    {
        path: ConstantList.ROOT_PATH + "recruitment-request",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER, SystemRole.ROLE_HR],
    },
];

export default RecruitmentRequestRoutes;
