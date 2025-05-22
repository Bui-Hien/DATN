import * as React from 'react';
import {memo} from 'react';
import PropTypes from 'prop-types';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import Tooltip from '@mui/material/Tooltip';
import AssignmentIndIcon from '@mui/icons-material/AssignmentInd';
import FamilyRestroomIcon from '@mui/icons-material/FamilyRestroom';
import PersonalInformation from "./PersonalInformation";
import PersonFamilyRelationshipIndex from "../../PersonFamilyRelationship/PersonFamilyRelationshipIndex";
import CommonBreadcrumb from "../../../common/CommonBreadcrumb";
import i18next from "i18next";
import {useStore} from "../../../stores";
import {observer} from "mobx-react-lite";
import StaffDocumentItemIndex from "../../StaffDocumentItem/StaffDocumentItemIndex";
import SchoolIcon from '@mui/icons-material/School';
import PersonBankAccountIndex from "../../PersonBankAccount/PersonBankAccountIndex";
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import FolderIcon from '@mui/icons-material/Folder';
import CertificateIndex from "../../Certificate/CertificateIndex";
import StaffLabourAgreementIndex from "../../StaffLabourAgreement/StaffLabourAgreementIndex";
import AssignmentIcon from '@mui/icons-material/Assignment';
// Component hiển thị từng tab panel
function CustomTabPanel(props) {
    const {children, value, index, ...other} = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`simple-tabpanel-${index}`}
            aria-labelledby={`simple-tab-${index}`}
            {...other}
        >
            {value === index && <Box>{children}</Box>}
        </div>
    );
}

CustomTabPanel.propTypes = {
    children: PropTypes.node,
    index: PropTypes.number.isRequired,
    value: PropTypes.number.isRequired,
};

// Props hỗ trợ accessibility
function a11yProps(index) {
    return {
        id: `simple-tab-${index}`,
        'aria-controls': `simple-tabpanel-${index}`,
    };
}

function ProfileStaffTabs() {
    const [value, setValue] = React.useState(0);
    const {staffStore} = useStore();

    const {selectedRow} = staffStore;
    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    const listTab = [
        {
            title: "Thông tin cá nhân",
            icon: <AssignmentIndIcon fontSize="small"/>,
            component: <PersonalInformation/>,
        },
        {
            title: "Quan hệ nhân thân",
            icon: <FamilyRestroomIcon fontSize="small"/>,
            component: <PersonFamilyRelationshipIndex/>,
        },
        {
            title: "Chứng chỉ",
            icon: <SchoolIcon fontSize="small"/>,
            component: <CertificateIndex/>,
        },
        {
            title: "Tài khoản cá nhân",
            icon: <AccountBalanceIcon fontSize="small"/>,
            component: <PersonBankAccountIndex/>,
        },
        {
            title: "Hồ sơ cá nhân",
            icon: <FolderIcon fontSize="small"/>,
            component: <StaffDocumentItemIndex/>,
        },
        {
            title: "Hợp đồng",
            icon: <AssignmentIcon fontSize="small"/>,
            component: <StaffLabourAgreementIndex/>,
        }
    ];

    return (
        <Box className="w-full !p-0">
            <Box sx={{borderBottom: 1, borderColor: 'divider'}}>
                <CommonBreadcrumb
                    routeSegments={[
                        {name: i18next.t("Nhân viên")},
                        {name: i18next.t(`${selectedRow?.displayName} - ${listTab[value]?.title}`)}
                    ]}
                />
                <Box sx={{borderBottom: 1, borderColor: 'divider'}}/>
                <Tabs value={value} onChange={handleChange} aria-label="icon tabs with tooltip">
                    {listTab.map((tab, index) => (
                        <Tooltip title={tab.title} arrow key={index}>
                            <Tab
                                className="!p-3 !min-w-0"
                                icon={tab.icon}
                                {...a11yProps(index)}
                            />
                        </Tooltip>
                    ))}
                </Tabs>
            </Box>
            <div className={"pt-5"}></div>
            {listTab.map((tab, index) => (
                <CustomTabPanel value={value} index={index} key={index}>
                    {tab.component}
                </CustomTabPanel>
            ))}
        </Box>
    );
}

export default memo(observer(ProfileStaffTabs));
