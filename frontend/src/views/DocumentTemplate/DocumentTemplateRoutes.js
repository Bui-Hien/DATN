import {lazy} from "react";
import ConstantList from "../../appConfig";
import {SystemRole} from "../../LocalConstants";

const ViewComponent = lazy(() => import("./DocumentTemplateIndex"));

const DocumentTemplateRoutes = [
    {
        path: ConstantList.ROOT_PATH + "document-template",
        exact: true,
        component: ViewComponent,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER, SystemRole.ROLE_HR],
    },
];

export default DocumentTemplateRoutes;
