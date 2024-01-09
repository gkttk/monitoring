import * as React from 'react';
import {useState} from 'react';
import Tab from '@mui/material/Tab';
import {TabContext, TabList, TabPanel} from '@mui/lab'
import {UserContent} from "./users/UserContent";
import {useNavigate} from "react-router-dom";
import {LoggedUserType} from "../interfaces/interfaces";
import NotificationContent from "./notifications/NotificationContent";
import ConfigurationContent from "./configurations/ConfigurationContent";
import {Button} from "@mui/material";

export default function ContentPage() {
    const user: LoggedUserType = JSON.parse(localStorage.getItem("user_data")!);
    const [loggedUser] = useState<LoggedUserType>(user);

    const [selectedTab, setSelectedTab] = useState("1");
    let navigate = useNavigate();

    const handleChange = (event: React.SyntheticEvent, newValue: string) => {
        setSelectedTab(newValue);
    };

    function handleLogout() {
        localStorage.removeItem("user_data");
        navigate("/");
    }

    return (<>
            <TabContext value={selectedTab}>
                <TabList onChange={handleChange}>
                    <Tab label="Users" value="1"/>
                    <Tab label="Notifications" value="2"/>
                    {loggedUser.admin && <Tab label="Configurations" value="3"/>}
                </TabList>
                <TabPanel value="1">
                    <UserContent user={loggedUser}/>
                </TabPanel>
                <TabPanel value="2">
                    <NotificationContent user={loggedUser}/>
                </TabPanel>
                {loggedUser.admin && <TabPanel value="3">
                    <ConfigurationContent/>
                </TabPanel>}
            </TabContext>
            <Button variant="contained" color="primary" sx={{marginLeft: "48px"}}
                    onClick={handleLogout}>
                Logout
            </Button>

        </>

    );
}