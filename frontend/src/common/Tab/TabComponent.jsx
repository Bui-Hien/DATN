import React, {memo, useEffect} from "react";
import {Badge, Box, makeStyles, Tab, Tabs, Tooltip} from "@mui/material";

const useStyles = makeStyles(() => ({
    tabRoot: {
        "&.MuiTab-root": {
            minWidth: "auto !important",
            padding: "6px 10px !important",
            marginRight: "8px !important",
        },
    },
    iconOnly: {
        "&.MuiTab-root": {
            minWidth: "auto !important",
            padding: "8px !important",
            marginRight: "10px !important",
            justifyContent: "center !important",
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
                          displayMode = "icon-with-text",
                          formik,
                          autoSwitchErrorTab = false,
                          ...props
                      }) {
    const classes = useStyles();

    useEffect(() => {
        if (autoSwitchErrorTab && formik?.submitCount > 0) {
            const firstTabWithError = tabList.findIndex((tab) =>
                tab.fields?.some((field) => formik.errors[field])
            );
            if (firstTabWithError !== -1 && firstTabWithError !== value) {
                handleChange(null, firstTabWithError);
            }
        }
    }, [formik?.submitCount, formik?.errors]);

    const hasErrorInTab = (tab) => {
        if (!formik) return false;
        return tab.fields?.some(
            (field) =>
                formik.errors[field] &&
                (formik.touched[field] || formik.submitCount > 0)
        );
    };

    return (
        <Box sx={{ width: "100%" }}>
            <Tabs
                value={value}
                onChange={handleChange}
                aria-label="tabs"
                textColor="primary"
                indicatorColor="primary"
                className={`Mui-tabRootCustom ${className || ""}`}
                variant="scrollable"
                scrollButtons="auto"
                {...props}
            >
                {tabList?.map((tab, index) => {
                    const showIcon = displayMode !== "text-only" && !hideIcon && tab?.icon;
                    const showLabel = displayMode !== "icon-only";

                    return (
                        <Tab
                            key={index}
                            label={
                                <>
                                    {showLabel ? tab.label : null}
                                    {hasErrorInTab(tab) && (
                                        <Badge
                                            color="error"
                                            variant="dot"
                                            overlap="circular"
                                            sx={{ ml: 1 }}
                                        />
                                    )}
                                </>
                            }
                            icon={
                                showIcon ? (
                                    <Tooltip
                                        title={tab.label}
                                        arrow
                                        disableHoverListener={displayMode === "icon-with-text"}
                                    >
                                        <span className="m-0">{tab.icon}</span>
                                    </Tooltip>
                                ) : null
                            }
                            iconPosition="start"
                            {...a11yProps(index)}
                            className={`${classes.tabRoot} ${
                                displayMode === "icon-only" ? classes.iconOnly : ""
                            } ${className || ""}`}
                        />
                    );
                })}
            </Tabs>
        </Box>
    );
}

export default memo(TabComponent);
