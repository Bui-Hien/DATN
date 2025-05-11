import React, { memo } from "react";
import { Box, Tabs, Tab, Tooltip, makeStyles } from "@material-ui/core";

// ✅ Tạo custom styles với `makeStyles`
const useStyles = makeStyles(() => ({
  tabRoot: {
    "&.MuiTab-root": {
      minWidth: "auto !important",   // Ghi đè minWidth
      padding: "6px 10px !important", // Ghi đè padding
      marginRight: "8px !important",  // Ghi đè margin
    },
  },
  iconOnly: {
    "&.MuiTab-root": {
      minWidth: "auto !important",
      padding: "8px !important",
      marginRight: "10px !important",
      justifyContent: "center !important", // Ghi đè justifyContent
    },
  },
}));

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    "aria-controls": `simple-tabpanel-${index}`,
  };
}

function TabComponent({
  tabList,
  handleChange,
  value,
  className,
  hideIcon,
  displayMode = "icon-with-text", // ✅ Prop kiểm soát hiển thị
  ...props
}) {
  const classes = useStyles();  // Sử dụng custom styles

  return (
    <Box sx={{ width: "100%" }}>
      <Tabs value={value} onChange={handleChange} aria-label='tabs' textColor='primary' indicatorColor='primary' className={`Mui-tabRootCustom ${className}`} variant='scrollable' scrollButtons='auto'>
        {tabList?.map((tab, index) => {
          const showIcon = displayMode !== "text-only" && !hideIcon && tab?.icon;
          const showLabel = displayMode !== "icon-only";

          return (
            <Tab
              key={index}
              label={showLabel ? tab.label : null}
              icon={
                showIcon ? (
                  <Tooltip title={tab.label} arrow disableHoverListener={displayMode === "icon-with-text"}>
                    <span className="m-0">{tab.icon}</span>
                  </Tooltip>
                ) : null
              }
              iconPosition='start'
              {...a11yProps(index)}
              className={`${classes.tabRoot} ${displayMode === "icon-only" ? classes.iconOnly : ""} ${className}`}
            />
          );
        })}
      </Tabs>
    </Box>
  );
}

export default memo(TabComponent);
