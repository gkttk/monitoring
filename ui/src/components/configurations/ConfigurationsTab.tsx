import {useApolloClient, useQuery} from "@apollo/client";
import {CONFIGS_QUERY, UPDATE_CONFIGURATION_MUTATION} from "../../graphql/queries";
import React, {useEffect, useState} from "react";
import {PuffLoader} from "react-spinners";
import {ConfigurationType} from "../../interfaces/interfaces";
import TableContainer from "@mui/material/TableContainer/TableContainer";
import {Button, Paper, Table, TableBody, TableCell, TableHead, TableRow, TextField} from "@mui/material";
import {isProvidedSeverityValidEnum, sendMutate, validateEmail} from "../../util/utils";
import {useNavigate} from "react-router-dom";


interface TextFieldState {
    id: string
    key: string
    error: boolean,
    helperText: string,
    textForUpdate: string
}


export default function ConfigurationsTab() {

    const {data, loading, error, refetch} = useQuery(CONFIGS_QUERY)
    const [mutationLoading, setMutationLoading] = useState(false);
    const [textFieldStates, setTextFieldStates] = useState<TextFieldState[]>([])
    const client = useApolloClient();
    const navigate = useNavigate();

    useEffect(() => {
        data && data.configurations && initTextFieldsState()
    }, [data])

    function handleUpdateTextField(newValue: string, id: string) {
        const state = getTextFieldStateByID(id)
        state && (state.textForUpdate = newValue)
    }

    function updateTextFieldStatesWithNewError(states: TextFieldState[], configID: string, helperText: string) {
        return states.map(state => state.id === configID ? {...state, error: true, helperText: helperText} : {
            ...state, error: false, helperText: ""
        });
    }

    function isTextFieldValid(currentState: TextFieldState) {
        const text = currentState.textForUpdate
        if (!text.length) {
            setTextFieldStates(updateTextFieldStatesWithNewError(textFieldStates, currentState.id, "The new value should be provided"))
            return false;
        }

        if (currentState.key === "notification_email" && !validateEmail(text)) {
            setTextFieldStates(updateTextFieldStatesWithNewError(textFieldStates, currentState.id, "You should provide a valid email"))
            return false;
        }

        if (currentState.key === "severity_email_level" && !isProvidedSeverityValidEnum(text)) {
            setTextFieldStates(updateTextFieldStatesWithNewError(textFieldStates, currentState.id, "For this key you should provide any of INFO, WARNING or ERROR value"))

            return false;
        }

        if (currentState.key === "severity_email_threshold" && (!Number.isInteger(Number(text)) || Number(text) <= 0)) {
            setTextFieldStates(updateTextFieldStatesWithNewError(textFieldStates, currentState.id, "Threshold must be a number and greater than 0"))
            return false;
        }

        return true;
    }

    function initTextFieldsState() {

        if (data && data.configurations) {
            const result = data.configurations.map((config: ConfigurationType) => {
                return {id: config.id, error: false, helperText: "", textForUpdate: "", key: config.key}
            })

            setTextFieldStates(result)
        }
    }

    async function handleClick(configID: string) {

        const state = getTextFieldStateByID(configID)

        if (state && isTextFieldValid(state)) {

            if (state) {
                setMutationLoading(true)
                sendMutate(client, UPDATE_CONFIGURATION_MUTATION, {id: state.id, newValue: state.textForUpdate})
                    .then(() => refetch())
                    .catch(error => {
                        console.log(error)
                        navigate("/")
                    })
                    .finally(() => setMutationLoading(false))
            }
        }
    }

    function getTextFieldStateByID(id: string) {
        return textFieldStates.find(state => state.id === id);
    }

    if (loading || mutationLoading) return <PuffLoader color="#36d7b7" size={120}/>
    if (error) return <pre>{error.message}</pre>
    console.log(textFieldStates)
    return (<>
        <h1>Configurations</h1>

        <Button variant="contained" color="primary" sx={{margin: "5px"}}
                onClick={() => refetch().catch(error => {
                    console.log(error)
                    navigate("/")
                })}>
            Refresh configs
        </Button>

        {data && <TableContainer component={Paper} sx={{margin: "5px"}}>
            <Table sx={{minWidth: 650}} size="small" aria-label="a dense table">
                <TableHead>
                    <TableRow>
                        <TableCell align="center">Key</TableCell>
                        <TableCell align="center">Value</TableCell>
                        <TableCell align="center">New value</TableCell>
                        <TableCell/>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {data.configurations.map((configuration: ConfigurationType) => (<TableRow
                        key={configuration.id}
                        sx={{'&:last-child td, &:last-child th': {border: 0}}}
                    >
                        <TableCell align="center">{configuration.key}</TableCell>
                        <TableCell align="center">
                            <TextField sx={{display: "flex"}}
                                       size={"small"}
                                       id="outlined-basic"
                                       label={configuration.value}
                                       variant="outlined"
                                       disabled>

                            </TextField>
                        </TableCell>
                        <TableCell align="center">
                            <TextField sx={{display: "flex"}}
                                       id="outlined-basic"
                                       variant="outlined"
                                       size={"small"}
                                       helperText={getTextFieldStateByID(configuration.id)?.helperText}
                                       error={getTextFieldStateByID(configuration.id)?.error}
                                       onChange={(event) => handleUpdateTextField(event.target.value, configuration.id)}
                            >
                            </TextField>
                        </TableCell>
                        <TableCell align="right">
                            <Button variant="contained" color="primary"
                                    onClick={() => handleClick(configuration.id)}>
                                Change value
                            </Button>
                        </TableCell>

                    </TableRow>))}
                </TableBody>
            </Table>
        </TableContainer>

        }
    </>);


}