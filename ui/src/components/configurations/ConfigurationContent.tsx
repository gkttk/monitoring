import React, {useState} from "react";
import {TabContext, TabList, TabPanel} from "@mui/lab";
import Tab from "@mui/material/Tab";
import ConfigurationsTab from "./ConfigurationsTab";


export default function ConfigurationContent() {

    const [selectedTab, setSelectedTab] = useState("1");
    const handleChange = (event: React.SyntheticEvent, newValue: string) => {
        setSelectedTab(newValue);
    };
    return (
        <>
            <TabContext value={selectedTab}>
                <TabList onChange={handleChange} aria-label="lab API tabs example">
                    <Tab label="All Configurations" value="1"/>
                </TabList>
                <TabPanel value="1">
                    <ConfigurationsTab/>
                </TabPanel>
            </TabContext>
        </>
    );
}