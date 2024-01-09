import {LoggedUserType} from "../../interfaces/interfaces";
import React, {useState} from "react";
import {TabContext, TabList, TabPanel} from "@mui/lab";
import Tab from "@mui/material/Tab";
import NotificationsTab from "./NotificationsTab";
import SendNotificationTab from "./SendNotificationTab";


interface NotificationContentProps {
    user: LoggedUserType
}

export default function NotificationContent({user}: NotificationContentProps) {

    const [selectedTab, setSelectedTab] = useState("1");
    const handleChange = (event: React.SyntheticEvent, newValue: string) => {
        setSelectedTab(newValue);
    };
    return (
        <>
            <TabContext value={selectedTab}>
                <TabList onChange={handleChange} aria-label="lab API tabs example">
                    <Tab label="Check all notifications" value="1"/>
                    {user.admin && <Tab label="Send a notification" value="2"/>}
                </TabList>
                <TabPanel value="1">
                    <NotificationsTab/>
                </TabPanel>
                <TabPanel value="2">
                    <SendNotificationTab/>
                </TabPanel>
            </TabContext>
        </>
    );
}