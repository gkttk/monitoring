import React, {useState} from "react";
import {TabContext, TabList, TabPanel} from "@mui/lab";
import Tab from "@mui/material/Tab";
import UsersTab from "./UsersTab";
import UserByIDTab from "./UserByIDTab";
import {LoggedUserType} from "../../interfaces/interfaces";


interface UserContentProps {
    user: LoggedUserType
}

export function UserContent({user}: UserContentProps) {

    const [selectedTab, setSelectedTab] = useState("1");
    const handleChange = (event: React.SyntheticEvent, newValue: string) => {
        setSelectedTab(newValue);
    };
    return (
        <>
            <TabContext value={selectedTab}>
                <TabList onChange={handleChange} aria-label="lab API tabs example">
                    <Tab label="User information" value="1"/>
                    {user.admin && <Tab label="Check all users" value="2"/>}
                    {user.admin && <Tab label="Find user by ID" value="3"/>}
                </TabList>
                <TabPanel value="1">
                    {user &&
                        <h1>
                            Hello, {user.login}, your role is {user.admin ? "admin" : "user"}
                        </h1>
                    }
                </TabPanel>
                <TabPanel value="2">
                    <UsersTab/>
                </TabPanel>
                <TabPanel value="3">
                    <UserByIDTab/>
                </TabPanel>
            </TabContext>
        </>
    )


}