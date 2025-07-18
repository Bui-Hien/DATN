import * as React from 'react';
import {memo} from 'react';
import Box from '@mui/material/Box';
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
import {Navigate, Route, Routes, useLocation, useNavigate} from "react-router-dom";
import ExpandMore from "@mui/icons-material/ExpandMore";
import ChevronRight from "@mui/icons-material/ChevronRight";
import {Tooltip} from "@mui/material";
import {AppBar, Drawer, DrawerHeader} from "./styledComponents";
import {useStore} from "../../stores";
import PropTypes from "prop-types";
import {observer} from "mobx-react-lite";
import {navigations} from "../../navigations";
import NotFound from "./NotFound";
import {HOME_PAGE, LOGIN_PAGE} from "../../appConfig";
import LogoutIcon from '@mui/icons-material/Logout';
import AlertDialog from "../CommonConfirmationDialog";

const COLORS = {
    rootActiveBg: '#ef4444',    // Cho cha cấp 1
    parentActiveBg: '#3b82f6', // Cho cha từ cấp 2
    itemActiveBg: '#269066',   // Cho item được chọn
    activeText: 'white',       // Chữ trắng cho cha được active
};

function AppLayout({routes}) {
    const navigate = useNavigate();
    const location = useLocation();
    const [open, setOpen] = React.useState(true);
    const [expandedItems, setExpandedItems] = React.useState({});
    const {authStore} = useStore();
    const {
        roles,
        currentUser
    } = authStore;
    const handleLogout = () => {
        localStorage.removeItem("access_token");
        localStorage.removeItem("refresh_token");
        setShouldOpenLogout(false)
        navigate(LOGIN_PAGE)
    };

    const [shouldOpenLogout, setShouldOpenLogout] = React.useState(false);
    // Hàm xử lý đóng/mở menu con
    const toggleExpand = (itemPath) => {
        setExpandedItems(prev => ({
            ...prev,
            [itemPath]: !prev[itemPath]
        }));
    };
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
            .filter(item => item.isVisible && ((!item.auth || item.auth.some(role => roles?.includes(role)))
            ))
            ?.map((item) => {
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
                        child => child.isVisible && (!child.auth || child.auth.some(role => roles?.includes(role)))
                    );

                    if (visibleChildren.length === 0) return null;
                    const shouldRenderChildren = open && isExpanded;

                    return (
                        <Box key={`${item.name}-${level}`}>
                            <Tooltip title={!open && item.name}>
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
                                        className={`${!open && 'hidden'} text-white`}
                                    />
                                    {open && (
                                        isExpanded ? <ExpandMore className={"text-white"}/> :
                                            <ChevronRight className={"text-white"}/>
                                    )}
                                </ListItemButton>
                            </Tooltip>
                            {shouldRenderChildren && renderNavItems(visibleChildren, level + 1, itemPath)}
                        </Box>
                    );
                }

                return (
                    <ListItem
                        key={`${item.name}-${level}`}
                        disablePadding
                        sx={{backgroundColor: isActive ? COLORS.itemActiveBg : 'transparent'}}
                    >
                        <Tooltip title={!open && item.name}>
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
                                    className={" text-white"}
                                />
                            </ListItemButton>
                        </Tooltip>
                    </ListItem>
                );
            });
    };
    return (
        <Box sx={{display: "flex", height: "100vh", overflow: "hidden"}}>
            <CssBaseline/>
            {/* AppBar */}
            <AppBar position="fixed" className="!bg-slate-700">
                <Toolbar className={"flex justify-between"}>
                    <div className="flex items-center">
                        <IconButton
                            color="inherit"
                            aria-label="open drawer"
                            onClick={() => setOpen(!open)}
                            edge="start"
                        >
                            <MenuIcon/>
                        </IconButton>
                        <Typography variant="h6" noWrap component="div">
                            TLU
                        </Typography>
                    </div>
                    <div className="">
                        <IconButton
                            color="inherit"
                            aria-label="open drawer"
                            onClick={() => {
                                setShouldOpenLogout(true)
                            }}
                            edge="start"
                        >
                            <LogoutIcon/>
                        </IconButton>
                    </div>
                </Toolbar>
            </AppBar>

            {/* Drawer */}
            <Drawer
                variant="permanent"
                open={open}
                className="h-screen !bg-slate-700"
                sx={{
                    '& .MuiDrawer-paper': {
                        position: 'relative',
                        whiteSpace: 'nowrap',
                        width: open ? 240 : 56,
                        transition: 'width 0.3s',
                        overflowX: 'hidden',
                        boxSizing: 'border-box',
                    }
                }}
            >
                <DrawerHeader/>
                <List className="!py-0">{renderNavItems(navigations)}</List>
            </Drawer>

            {/* Main Content */}
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    overflow: 'auto',
                    height: '100vh',
                    display: 'flex',
                    flexDirection: 'column',
                }}
            >
                <DrawerHeader/>
                <Box
                    className="!px-4"
                    sx={{
                        flexGrow: 1,
                        overflowY: 'auto',
                        minHeight: 0, // Quan trọng để scroll hoạt động
                    }}
                >
                    <Routes>
                        <Route path="/" element={<Navigate to={HOME_PAGE} replace/>}/>
                        {routes?.map((item, index) => {
                            const hasAccess = !item.auth || item.auth.some(role => roles.includes(role));
                            if (hasAccess) {
                                return (
                                    <Route
                                        key={index}
                                        path={item.path}
                                        element={React.createElement(item.component)}
                                    />
                                );
                            }
                            return null;
                        })}
                        <Route path="*" element={<NotFound/>}/>
                    </Routes>
                </Box>
            </Box>
            {shouldOpenLogout && (
                <AlertDialog
                    open={shouldOpenLogout}
                    onConfirmDialogClose={() => setShouldOpenLogout(false)}
                    onYesClick={handleLogout}
                    title={"Bạn có chắc muốn đăng xuất không?"}
                    text={`Đăng xuất tài khoản ${currentUser?.username}`}
                    agree={"Đăng xuất"}
                    cancel={"Hủy"}
                />
            )}
        </Box>
    );
}

export default memo(observer(AppLayout));


AppLayout.propTypes = {
    routes: PropTypes.arrayOf(
        PropTypes.shape({
            path: PropTypes.string.isRequired,
            exact: PropTypes.bool,
            component: PropTypes.elementType.isRequired,
            auth: PropTypes.arrayOf(PropTypes.string),
        })
    ).isRequired,
};