import {SystemRole} from "./LocalConstants";
import i18next from "i18next";

import ListAltIcon from '@mui/icons-material/ListAlt';
import AccountTreeIcon from '@mui/icons-material/AccountTree';
import GroupIcon from '@mui/icons-material/Group';
import EventNoteIcon from '@mui/icons-material/EventNote';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';

const iconStyle = "!size-6 !text-white"; // 👈 style dùng chung

export const navigations = [
    {
        name: i18next.t("Cơ cấu tổ chức"),
        icon: <AccountTreeIcon className={iconStyle}/>,
        isVisible: true,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
        children: [
            {
                name: i18next.t("Phòng ban"),
                path: "/organization/department",
                isVisible: true,
            },
        ]
    },
    {
        name: i18next.t("Nhân viên"),
        icon: <GroupIcon className={iconStyle}/>,
        isVisible: true,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
        children: [
            {
                name: i18next.t("Hồ sơ nhân viên"),
                path: "/staff",
                isVisible: true,
            },
            {
                name: i18next.t("Mẫu hồ sơ chung"),
                path: "/document-template",
                isVisible: true,
            },
            {
                name: i18next.t("Quan hệ nhân thân"),
                path: "/family-relationship",
                isVisible: true,
            },
        ]
    },
    {
        name: i18next.t("Chấm công"),
        icon: <EventNoteIcon className={iconStyle}/>,
        isVisible: true,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
        children: [
            {
                name: i18next.t("White list"),
                path: "/schedule/department-ip",
                isVisible: true,
            },
        ]
    },
    {
        name: i18next.t("Lương"),
        icon: <AttachMoneyIcon className={iconStyle}/>,
        isVisible: true,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
        children: [
            {
                name: i18next.t("Mẫu bảng lương"),
                path: "/salary-template",
                isVisible: true,
            },
            {
                name: i18next.t("Kỳ lương"),
                path: "/salary-period",
                isVisible: true,
            },
        ]
    },
    {
        name: i18next.t("Danh mục chung"),
        icon: <ListAltIcon className={iconStyle}/>,
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
                name: i18next.t("Dân tộc"),
                path: "/administration/ethnics",
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
            },
            {
                name: i18next.t("Đơn vị hành chính"),
                path: "/administration/administrative-unit",
                isVisible: true,
            }
        ]
    }
]
