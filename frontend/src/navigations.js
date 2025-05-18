import * as React from "react";
import {SystemRole} from "./LocalConstants";
import i18next from "i18next";
import {ListAltOutlined} from "@material-ui/icons";

export const navigations = [
    {
        name: i18next.t("administration.title"),
        icon: <ListAltOutlined/>,
        isVisible: true,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
        children: [
            {
                name: i18next.t("country.title"),
                path: "/administration/country",
                isVisible: true,
            }
        ]
    }
]