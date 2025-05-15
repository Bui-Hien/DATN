import TreeIcon from "@mui/icons-material/Schema";
import * as React from "react";

export const AUTH_ROLES = ["admin"];


export const NAV_ITEMS = [
    {
        name: "Tổ chức",
        icon: <TreeIcon />,
        isVisible: true,
        auth: AUTH_ROLES,
        children: [
            {
                name: "Cây tổ chức",
                path: "/organization/tree",
                isVisible: true,
                icon: <TreeIcon />,
                auth: AUTH_ROLES
            },
            {
                name: "Yêu cầu định biên",
                path: "/organization/hr-resource-plan",
                isVisible: true,
                icon: <TreeIcon />,
                auth: AUTH_ROLES,
                children: [
                    {
                        name: "Cây tổ chức cấp 2",
                        path: "/organization/hr-resource-plan/tree",
                        isVisible: true,
                        icon: <TreeIcon />,
                        auth: AUTH_ROLES
                    },
                    {
                        name: "Yêu cầu định biên cấp 2",
                        path: "/organization/hr-resource-plan/plan",
                        isVisible: true,
                        icon: <TreeIcon />,
                        auth: AUTH_ROLES,
                        children: [
                            {
                                name: "Cây tổ chức cấp 3",
                                path: "/organization/hr-resource-plan/plan/tree",
                                isVisible: true,
                                icon: <TreeIcon />,
                                auth: AUTH_ROLES
                            }
                        ]
                    }
                ]
            }
        ]
    }
]