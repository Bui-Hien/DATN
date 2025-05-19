import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./EducationDegreeIndex"));

const EducationDegreeRoutes = [
    {
        path: ConstantList.ROOT_PATH + "administration/education-degree",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
    },
];

export default EducationDegreeRoutes;
