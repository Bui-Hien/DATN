export const navigations = [
    {
        name: "navigation.organization.title",
        // icon: "supervisor_account",
        color: "#007fff",
        icon: "account_tree",
        // auth: [ROLE_ADMIN, HR_MANAGER, HR_STAFF_VIEW, IS_POSITION_MANAGER],
        isVisible: true,
        children: [
            {
                name: "navigation.organization.organizationalChart",
                icon: "",
                // auth: [ROLE_ADMIN, HR_MANAGER, HR_STAFF_VIEW],
                // path: ConstantList.ROOT_PATH + "organization/diagram",
                isVisible: true,
            },
            {
                name: "Cây tổ chức",
                // icon: "",
                // auth: [ROLE_ADMIN, HR_MANAGER, HR_STAFF_VIEW],
                // path: ConstantList.ROOT_PATH + "organization/tree",
                isVisible: true,
            },
            {
                name: "navigation.category.staff.listPositions",
                icon: "",
                // auth: [ROLE_ADMIN, HR_MANAGER, HR_STAFF_VIEW],
                className: "btn-link-black",
                title: "Quản lý danh mục vị trí",
                // path: ConstantList.ROOT_PATH + "category/staff/position",
                isVisible: true,
            },

            {
                name: "Yêu cầu định biên",
                // isVisible: true,
                // auth: [ROLE_ADMIN, HR_MANAGER, HR_STAFF_VIEW, IS_POSITION_MANAGER],
                // path: ConstantList.ROOT_PATH + "organization/hr-resource-plan",
                className: "btn-link-black",
                title: "Yêu cầu định biên",
            }
        ]
    }
]