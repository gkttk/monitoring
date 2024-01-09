import React, {useState} from "react";
import {
    Button,
    FormControl,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    SelectChangeEvent,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow
} from "@mui/material";
import {useApolloClient} from "@apollo/client";
import {NOTIFICATIONS_QUERY} from "../../graphql/queries";
import {NotificationType} from "../../interfaces/interfaces";
import {PuffLoader} from "react-spinners";
import {prepareFilter, sendQuery} from "../../util/utils";
import TableContainer from "@mui/material/TableContainer/TableContainer";
import {useNavigate} from "react-router-dom";

export default function NotificationsTab() {

    const client = useApolloClient();
    const [filter, setFilter] = useState({componentId: "ANY", severity: "ANY"})
    const [loading, setLoading] = useState(false);
    const [notifications, setNotifications] = useState([]);
    const navigate = useNavigate()

    function handleChangeComponentId(event: SelectChangeEvent) {
        const value = event.target.value;
        setFilter({...filter, "componentId": value})
    }

    function handleChangeSeverity(event: SelectChangeEvent) {
        const value = event.target.value;
        setFilter({...filter, "severity": value})
    }

    async function handleSearchNotifications() {

        setLoading(true)
        const preparedFilter = prepareFilter(filter);

        sendQuery(client, NOTIFICATIONS_QUERY, {filter: preparedFilter})
            .then(response => {
                if (response.errors) {
                    console.log(response.errors)
                    navigate("/")
                }else {
                    setNotifications(response.data.notifications)
                }
            })
            .finally(() => setLoading(false))
    }


    if (loading) return <PuffLoader color="#36d7b7" size={120}/>

    return (<>
            <div>
                {/*Component id filter*/}
                <FormControl>
                    <InputLabel id="demo-simple-select-label">Component</InputLabel>
                    <Select sx={{margin: "5px", width: "100px"}}
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            value={filter.componentId}
                            label="Component"
                            onChange={handleChangeComponentId}
                    >
                        <MenuItem value={"ANY"}>Any</MenuItem>
                        <MenuItem value={"1"}>Hardware</MenuItem>
                        <MenuItem value={"2"}>Databases</MenuItem>
                        <MenuItem value={"3"}>Software</MenuItem>
                        <MenuItem value={"4"}>Power Supply</MenuItem>
                        <MenuItem value={"5"}>Processor</MenuItem>
                    </Select>
                </FormControl>

                {/*Severity filter*/}
                <FormControl>
                    <InputLabel id="demo-simple-select-label">Severity</InputLabel>
                    <Select sx={{margin: "5px", width: "100px"}}
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            value={filter.severity}
                            label="Severity"
                            onChange={handleChangeSeverity}
                    >
                        <MenuItem value={"ANY"}>Any</MenuItem>
                        <MenuItem value={"INFO"}>Info</MenuItem>
                        <MenuItem value={"WARNING"}>Warning</MenuItem>
                        <MenuItem value={"ERROR"}>Error</MenuItem>
                    </Select>
                </FormControl>

                <Button sx={{margin: "5px"}} variant="contained" color="primary" onClick={handleSearchNotifications}>
                    Find notifications
                </Button>

                {/*result table*/}
                {notifications && notifications.length !== 0 && <TableContainer component={Paper}>
                    <Table sx={{minWidth: 650}} size="small" aria-label="a dense table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="center">Component name</TableCell>
                                <TableCell align="center">Description</TableCell>
                                <TableCell align="center">Severity</TableCell>
                                <TableCell align="center">Timestamp</TableCell>
                                <TableCell/>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {notifications.map((notification: NotificationType) => (<TableRow
                                    key={notification.id}
                                    sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                >
                                    <TableCell align="center">{notification.componentName}</TableCell>
                                    <TableCell align="center">{notification.description}</TableCell>
                                    <TableCell align="center">{notification.severity}</TableCell>
                                    <TableCell align="center">{notification.timestamp}</TableCell>
                                </TableRow>))}
                        </TableBody>
                    </Table>
                </TableContainer>}

            </div>
        </>);


}