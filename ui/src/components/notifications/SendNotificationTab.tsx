import {
    FormControl, InputLabel, MenuItem, Paper, Select, Table, TableBody, TableCell, TableHead, TableRow
} from "@mui/material";
import React, {useState} from "react";
import {useApolloClient} from "@apollo/client";
import Textarea from "@mui/joy/Textarea";
import {Button} from "@mui/joy";
import {CREATE_NOTIFICATION_MUTATION} from "../../graphql/queries";
import {PuffLoader} from "react-spinners";
import {NotificationType} from "../../interfaces/interfaces";
import TableContainer from "@mui/material/TableContainer/TableContainer";
import {sendMutate} from "../../util/utils";
import {useNavigate} from "react-router-dom";

export default function SendNotificationTab() {

    const client = useApolloClient();
    const [loading, setLoading] = useState(false);
    const [createdNotification, setCreatedNotification] = useState<NotificationType>()
    const [notification, setNotification] = useState({componentName: "Hardware", description: "", severity: "INFO"})
    const navigate = useNavigate()

    async function handleNotificationCreation() {
        setLoading(true)
        setCreatedNotification(undefined)

        sendMutate(client, CREATE_NOTIFICATION_MUTATION, {notification: notification})
            .then(response => {
                setCreatedNotification(response.data.createNotification)
            })
            .catch(error => {
                console.log(error)
                navigate("/")
            })
            .finally(() => setLoading(false))
    }

    if (loading) return <PuffLoader color="#36d7b7" size={120}/>

    return (<>
        <div>
            <FormControl sx={{margin: "5px"}}>
                <InputLabel id="demo-simple-select-label">Component</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={notification.componentName}
                    label="Component"
                    onChange={event => setNotification({...notification, "componentName": event.target.value})}
                >
                    <MenuItem value={"Hardware"}>Hardware</MenuItem>
                    <MenuItem value={"Databases"}>Databases</MenuItem>
                    <MenuItem value={"Software"}>Software</MenuItem>
                    <MenuItem value={"Power Supply"}>Power Supply</MenuItem>
                    <MenuItem value={"Processor"}>Processor</MenuItem>
                </Select>
            </FormControl>

            <FormControl sx={{margin: "5px"}}>
                <InputLabel id="demo-simple-select-label">Severity</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={notification.severity}
                    label="Severity"
                    onChange={event => setNotification({...notification, "severity": event.target.value})}
                >
                    <MenuItem value={"INFO"}>Info</MenuItem>
                    <MenuItem value={"WARNING"}>Warning</MenuItem>
                    <MenuItem value={"ERROR"}>Error</MenuItem>
                </Select>
            </FormControl>

            <Textarea sx={{margin: "5px", marginBottom: "10px"}}
                      onChange={event => setNotification({...notification, "description": event.target.value})}
                      minRows={2}
                      placeholder="Notification description"
                      size="sm"
                      variant="soft"
            />

            {createdNotification && <TableContainer component={Paper}>
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
                        <TableRow
                            key={createdNotification.id}
                            sx={{'&:last-child td, &:last-child th': {border: 0}}}
                        >
                            <TableCell align="center">{createdNotification.componentName}</TableCell>
                            <TableCell align="center">{createdNotification.description}</TableCell>
                            <TableCell align="center">{createdNotification.severity}</TableCell>
                            <TableCell align="center">{createdNotification.timestamp}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>}

            <Button sx={{margin: "5px"}}
                    color="primary"
                    onClick={handleNotificationCreation}
                    size="md"
                    variant="solid"
            > Send the notification </Button>
        </div>
    </>);


}