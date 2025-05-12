import * as React from 'react';
import {styled} from '@mui/material/styles';
import Box from '@mui/material/Box';
import MuiDrawer from '@mui/material/Drawer';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import CssBaseline from '@mui/material/CssBaseline';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import {useTranslation} from 'react-i18next';
import {Route, Routes, useLocation, useNavigate} from "react-router-dom";
import ExpandMore from "@mui/icons-material/ExpandMore";
import ChevronRight from "@mui/icons-material/ChevronRight";
import TreeIcon from "@mui/icons-material/Schema";
import {useEffect} from "react";
import {Tooltip} from "@mui/material";

const drawerWidth = 240;
const AUTH_ROLES = ["admin"];

// Màu sắc theo yêu cầu
const COLORS = {
    rootActiveBg: '#ef4444',    // Đỏ - cho cha cấp 1
    parentActiveBg: '#3b82f6', // Xanh - cho cha từ cấp 2
    itemActiveBg: '#fef3c7',   // Vàng - cho item được chọn
    activeText: 'white',       // Chữ trắng cho cha được active
};

// Navigation data
const NAV_ITEMS = [
    {
        name: "Tổ chức",
        icon: <TreeIcon/>,
        isVisible: true,
        auth: AUTH_ROLES,
        children: [
            {
                name: "Cây tổ chức",
                path: "/organization/tree",
                isVisible: true,
                auth: AUTH_ROLES
            },
            {
                name: "Yêu cầu định biên",
                path: "/organization/hr-resource-plan",
                isVisible: true,
                auth: AUTH_ROLES,
                children: [
                    {
                        name: "Cây tổ chức cấp 2",
                        path: "/organization/hr-resource-plan/tree",
                        isVisible: true,
                        auth: AUTH_ROLES
                    },
                    {
                        name: "Yêu cầu định biên cấp 2",
                        path: "/organization/hr-resource-plan/plan",
                        isVisible: true,
                        auth: AUTH_ROLES,
                        children: [
                            {
                                name: "Cây tổ chức cấp 3",
                                isVisible: true,
                                auth: AUTH_ROLES,
                                children: [
                                    {
                                        name: "Cây tổ chức cấp 3",
                                        path: "/organization/hr-resource-plan/plan/tree",
                                        isVisible: true,
                                        auth: AUTH_ROLES
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ],
    },
    {
        name: "Yêu cầu định biên",
        isVisible: true,
        auth: AUTH_ROLES,
        icon: <TreeIcon/>,

        children: [
            {
                name: "Cây tổ chức cấp 2",
                path: "/organization/hr-resource-plan/tree",
                isVisible: true,
                auth: AUTH_ROLES
            },
            {
                name: "Yêu cầu định biên cấp 2",
                path: "/organization/hr-resource-plan/plan",
                isVisible: true,
                auth: AUTH_ROLES,
                children: [
                    {
                        name: "Cây tổ chức cấp 3",
                        isVisible: true,
                        auth: AUTH_ROLES,
                        children: [
                            {
                                name: "Cây tổ chức cấp 3",
                                path: "/organization/hr-resource-plan/plan/tree",
                                isVisible: true,
                                auth: AUTH_ROLES
                            }
                        ]
                    }
                ]
            }
        ]
    }
];
const openedMixin = (theme) => ({
    width: drawerWidth,
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
    }),
    overflowX: 'hidden',
});

const closedMixin = (theme) => ({
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: `calc(${theme.spacing(7)} + 1px)`,
    [theme.breakpoints.up('sm')]: {
        width: `calc(${theme.spacing(8)} + 1px)`,
    },
});

const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
})(({theme}) => ({
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    variants: [
        {
            props: ({open}) => open,
            style: {
                marginLeft: drawerWidth,
                width: `calc(100% - ${drawerWidth}px)`,
                transition: theme.transitions.create(['width', 'margin'], {
                    easing: theme.transitions.easing.sharp,
                    duration: theme.transitions.duration.enteringScreen,
                }),
            },
        },
    ],
}));

const Drawer = styled(MuiDrawer, {shouldForwardProp: (prop) => prop !== 'open'})(
    ({theme}) => ({
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
        boxSizing: 'border-box',
        variants: [
            {
                props: ({open}) => open,
                style: {
                    ...openedMixin(theme),
                    '& .MuiDrawer-paper': openedMixin(theme),
                },
            },
            {
                props: ({open}) => !open,
                style: {
                    ...closedMixin(theme),
                    '& .MuiDrawer-paper': closedMixin(theme),
                },
            },
        ],
    }),
);
const DrawerHeader = styled('div')(({theme}) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
}));
// Page Component
const Page = ({title}) => (
    <Typography variant="h4" sx={{mt: 2}}>
        {title}
    </Typography>
);

export default function MiniDrawer() {
    const navigate = useNavigate();
    const location = useLocation();
    const [open, setOpen] = React.useState(true);
    const [expandedItems, setExpandedItems] = React.useState({});

    // Hàm xử lý đóng/mở menu con
    const toggleExpand = (itemPath) => {
        setExpandedItems(prev => ({
            ...prev,
            [itemPath]: !prev[itemPath]
        }));
    };
    useEffect(() => {
        if (!open) {
            setExpandedItems({})
        }
    }, [open]);
    // Hàm kiểm tra xem item có con đang được chọn không
    const hasActiveChild = (item, currentPath) => {
        if (!item.children) return false;
        return item.children.some(child =>
            child.path === currentPath ||
            (child.children && hasActiveChild(child, currentPath))
        );
    };
    // Render navigation items
    const renderNavItems = (items, level = 0, parentPath = '') => {
        return items
            .filter(item => item.isVisible && item.auth?.some(role => AUTH_ROLES.includes(role)))
            .map((item) => {
                const itemPath = parentPath ? `${parentPath}/${item.name}` : item.name;
                const isActive = location.pathname === item.path;
                const hasActiveChildren = hasActiveChild(item, location.pathname);
                const isExpanded = expandedItems[itemPath]; // Chỉ phụ thuộc vào expandedItems

                // Xác định màu nền theo cấp
                let bgColor = 'transparent';
                if (isActive) {
                    bgColor = COLORS.itemActiveBg; // Vàng cho item được chọn
                } else if (hasActiveChildren) {
                    bgColor = level === 0 ? COLORS.rootActiveBg : COLORS.parentActiveBg;
                }

                const textColor = hasActiveChildren ? COLORS.activeText : 'inherit';

                if (item.children?.length) {
                    const visibleChildren = item.children.filter(
                        child => child.isVisible && child.auth?.some(role => AUTH_ROLES.includes(role))
                    );

                    if (visibleChildren.length === 0) return null;

                    return (
                        <Box key={`${item.name}-${level}`}>
                            <Tooltip title={!open &&item.name}>
                                <ListItemButton
                                    sx={{
                                        paddingLeft: open ? `${5 + (level * 5)}px` : '5px',
                                        backgroundColor: bgColor,
                                        color: textColor,
                                        '&:hover': {
                                            backgroundColor: bgColor !== 'transparent' ? bgColor : undefined,
                                        }
                                    }}
                                    className={`flex !justify-center h-10 ${!open && '!p-0'}`}
                                    onClick={() => {
                                        toggleExpand(itemPath);
                                        if (item.path) {
                                            navigate(visibleChildren[0]?.path || '#');
                                        }
                                        setOpen(true);
                                    }}
                                >
                                    <ListItemIcon sx={{color: textColor}} className={"px-2 !min-w-0"}>
                                        {item.icon}
                                    </ListItemIcon>
                                    <ListItemText
                                        primary={item.name}
                                        sx={{opacity: open ? 1 : 0}}
                                        className={`${!open && 'hidden'}`}
                                    />
                                    {open && (
                                        isExpanded ? <ExpandMore/> : <ChevronRight/>
                                    )}
                                </ListItemButton>
                            </Tooltip>
                            {isExpanded && renderNavItems(visibleChildren, level + 1, itemPath)}
                        </Box>
                    );
                }

                return (
                    <ListItem
                        key={`${item.name}-${level}`}
                        disablePadding
                        sx={{backgroundColor: isActive ? COLORS.itemActiveBg : 'transparent'}}
                    >
                        <Tooltip title={!open &&item.name}>
                            <ListItemButton
                                sx={{
                                    paddingLeft: open ? `${5 + (level * 5)}px` : '5px',
                                    color: isActive ? 'inherit' : textColor,
                                }}
                                onClick={() => {
                                    navigate(item.path)
                                    setOpen(true);
                                }}
                                className={"flex justify-center h-10"}
                            >
                                <ListItemIcon sx={{color: isActive ? 'inherit' : textColor}}
                                              className={"px-2 !min-w-0"}>
                                    {item.icon}
                                </ListItemIcon>
                                <ListItemText
                                    primary={item.name}
                                    sx={{opacity: open ? 1 : 0}}
                                />
                            </ListItemButton>
                        </Tooltip>
                    </ListItem>
                );
            });
    };
    // Generate routes từ NAV_ITEMS
    const generateRoutes = (items) => {
        return items.flatMap(item => {
            const routes = [];
            if (item.path) {
                routes.push(
                    <Route
                        key={item.path}
                        path={item.path}
                        element={<Page title={item.name}/>}
                    />
                );
            }
            if (item.children?.length) {
                routes.push(...generateRoutes(item.children));
            }
            return routes;
        });
    };
    return (
        <Box sx={{display: 'flex'}}>
            <CssBaseline/>
            <AppBar position="fixed" className="!bg-slate-800">
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        onClick={() => setOpen(!open)}
                        edge="start"
                        sx={[
                            {
                                marginRight: 5,
                            },
                        ]}
                    >
                        <MenuIcon/>
                    </IconButton>
                    <Typography variant="h6" noWrap component="div">
                        Mini variant drawer
                    </Typography>
                </Toolbar>
            </AppBar>
            <Drawer variant="permanent" open={open}>
                <DrawerHeader/>
                <List className={"!py-0"}>
                    {renderNavItems(NAV_ITEMS)}
                </List>
            </Drawer>
            <Box component="main" sx={{flexGrow: 1}}>
                <DrawerHeader/>
                <Box className={"px-6"}>
                    <Routes>
                        {generateRoutes(NAV_ITEMS)}
                        <Route path="*" element={<Page title="404 - Not Found"/>}/>
                    </Routes>
                </Box>
            </Box>
        </Box>
    );
}
