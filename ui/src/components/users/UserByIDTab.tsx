import {PuffLoader} from "react-spinners";
import {USER_BY_ID_QUERY} from "../../graphql/queries";
import {useApolloClient} from "@apollo/client";
import React, {useState} from "react";
import {UserType} from "../../interfaces/interfaces";
import {Button, TextField} from "@mui/material";
import {sendQuery} from "../../util/utils";
import {useNavigate} from "react-router-dom";


export default function UserByIDTab() {

    const [searchID, setSearchID] = useState("");
    const [foundUser, setFoundUser] = useState<UserType>();
    const [loading, setLoading] = useState(false);
    const [errorText, setErrorText] = useState("");
    const navigate = useNavigate()
    const client = useApolloClient();

    function isUserIdValid(value: string) {
        return value.length > 0 && !Number.isNaN(value) && Number(value) > 0;
    }

    async function handleSearch() {

        if (isUserIdValid(searchID)) {
            setErrorText("")
            setLoading(true)
            sendQuery(client, USER_BY_ID_QUERY, {id: searchID})
                .then(response => {
                    if(response.errors){
                        console.log(response.errors)
                        navigate("/")
                    } else {
                        setFoundUser(response.data.userById)
                    }

                })
                .finally(() => setLoading(false))
        } else {
            setErrorText("Provided ID must be a number greater than 0")
        }
    }

    function handleTextChange(event: React.ChangeEvent<HTMLInputElement>) {
        const value = event.target.value;
        setSearchID(value);
    }

    if (loading) return <PuffLoader color="#36d7b7" size={120}/>

    return (<>
        <div className="defaultDivContainer">
            <h1>User by ID</h1>

            <TextField sx={{margin: "5px"}}
                       id="outlined-basic"
                       label={"User ID"}
                       variant="outlined"
                       onChange={handleTextChange}
                       helperText={errorText}
                       error={!!errorText}
            />

            <Button variant="contained" color="primary"
                    onClick={handleSearch} sx={{margin: "5px"}}>
                Find user by ID
            </Button>

            {foundUser ? <h1>Login: {foundUser.login}</h1> : <h1>Not found</h1>}
        </div>
    </>);


}