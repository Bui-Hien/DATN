import {SystemRole} from "./LocalConstants";
import i18next from "i18next";
import ListAltIcon from '@mui/icons-material/ListAlt';
import ConstantList from "./appConfig";

export const navigations = [
    {
        name: i18next.t("Danh mục chung"),
        icon: <ListAltIcon/>,
        isVisible: true,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
        children: [
            {
                name: i18next.t("Quốc gia"),
                path: "/administration/country",
                isVisible: true,
            },
            {
                name: i18next.t("Ngân hàng"),
                path: "/administration/bank",
                isVisible: true,
            },
            {
                name: i18next.t("Bằng cấp"),
                path: "/administration/education-degree",
                isVisible: true,
            },
            {
                name: i18next.t("Dân tộc"),
                path: "/administration/ethnics",
                isVisible: true,
            },
            {
                name: i18next.t("Quan hệ nhân thân"),
                path: "/administration/family-relationship",
                isVisible: true,
            },
            {
                name: i18next.t("Nghề nghiệp"),
                path: "/administration/profession",
                isVisible: true,
            },
            {
                name: i18next.t("Tôn giáo"),
                path: "/administration/religion",
                isVisible: true,
            }
        ]
    }
]
