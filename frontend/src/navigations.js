import {SystemRole} from "./LocalConstants";
import i18next from "i18next";

import ListAltIcon from '@mui/icons-material/ListAlt';
import AccountTreeIcon from '@mui/icons-material/AccountTree';
import GroupIcon from '@mui/icons-material/Group';
import EventNoteIcon from '@mui/icons-material/EventNote';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import DomainAddIcon from '@mui/icons-material/DomainAdd';
import PersonIcon from '@mui/icons-material/Person';

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
        name: i18next.t("Tuyển dụng"),
        icon: <DomainAddIcon className={iconStyle}/>,
        isVisible: true,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER],
        children: [
            {
                name: i18next.t("Yêu cầu tuyển dụng"),
                path: "/staff",
                isVisible: true,
            },
            {
                name: i18next.t("Kế hoạch tuyển dụng"),
                path: "/document-template",
                isVisible: true,
            },
            {
                name: i18next.t("Hồ so ứng viên"),
                path: "/family-relationship",
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
                name: i18next.t("Phân ca làm việc"),
                path: "/staff-work-schedule",
                isVisible: true,
            },
            {
                name: i18next.t("Chấm công"),
                path: "/time-sheet-detail",
                isVisible: true,
            },
            {
                name: i18next.t("Thống kê công"),
                path: "/staff-work-schedule-summary",
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
            {
                name: i18next.t("Bảng lương"),
                path: "/salary-result",
                isVisible: true,
            },
        ]
    },
    {
        name: i18next.t("Cá nhân"),
        icon: <PersonIcon className={iconStyle}/>,
        isVisible: true,
        auth: [SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER, SystemRole.ROLE_USER],
        children: [
            {
                name: i18next.t("Hồ sơ cá nhân"),
                path: "/profile",
                isVisible: true,
            },
            {
                name: i18next.t("Lịch làm việc"),
                path: "staff-month-schedule-calendar",
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
                name: i18next.t("Quản trị tài khoản"),
                path: "/administration/country",
                isVisible: true,
            },
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
