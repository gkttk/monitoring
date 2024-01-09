import {useApolloClient} from "@apollo/client";
import {USERS_QUERY} from "../../graphql/queries";
import React, {useState} from "react";
import {PuffLoader} from "react-spinners";
import {UserType} from "../../interfaces/interfaces";
import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {sendQuery} from "../../util/utils";

export default function UsersTab() {

    const navigate = useNavigate()
    const [users, setUsers] = useState([])
    const [loading, setLoading] = useState(false)
    const client = useApolloClient()


    function handleClick() {
        setLoading(true)
        sendQuery(client, USERS_QUERY)
            .then(response => {
                if (response.errors) {
                    console.log(response.errors)
                    navigate("/")
                } else {
                    setUsers(response.data.users)
                }
            })
            .finally(() => setLoading(false))

    }

    if (loading) return <PuffLoader color="#36d7b7" size={120}/>

    return (<>
        <h1>Users</h1>

        <Button variant="contained" color="primary" onClick={handleClick}>Get users</Button>
        {users && <ul>
            {users.map((user: UserType) => (<li style={{display: "block"}} key={user.id}>Login: {user.login}</li>))}
        </ul>}
    </>);


}