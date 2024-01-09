import React, {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {PuffLoader} from "react-spinners";
import {UserContext} from "../util/UserContext";
import {Button} from "@mui/material";
import {useApolloClient} from "@apollo/client";
import {LOG_IN, VALIDATE_TOKEN_QUERY} from "../graphql/queries";
import {AuthRequest} from "../interfaces/interfaces";
import {sendQuery} from "../util/utils";


const Login = () => {

    const [loginError, setLoginError] = useState("")
    const [passwordError, setPasswordError] = useState("")
    const [userError, setUserError] = useState("")

    const [login, setLogin] = useState("")
    const [password, setPassword] = useState("")
    const [loading, setLoading] = useState(false);

    const {setLoggedUser} = useContext(UserContext)

    const navigate = useNavigate();
    const client = useApolloClient()


    useEffect(() => {

        const userDataValue = localStorage.getItem("user_data");

        if (userDataValue) {
            setLoading(true)
            const userData = JSON.parse(userDataValue)
            sendQuery(client, VALIDATE_TOKEN_QUERY, {data: userData})
                .then(response => {
                    if (response.errors) {
                        console.log(response.errors);
                        localStorage.removeItem("user_data");
                        setUserError("Your token is expired, please log in again")
                    } else {
                        setLoggedUser({"login": userData.login, "admin": userData.admin})
                        navigate("/content")
                    }

                })
                .finally(() => {
                    setLoading(false)
                })

        }
    }, []);


    async function fetchUserData(request: AuthRequest) {

        const {data} = await client.query({
            query: LOG_IN, variables: {authRequest: request},
        });
        return data;
    }

    function logIn(login: string, password: string) {

        setLoading(true)
        if (isCredentialsValid(login, password)) {

            fetchUserData({login, password})
                .then(data => {
                    const {__typename, ...userData} = data.auth;
                    localStorage.setItem("user_data", JSON.stringify(userData))
                    setLoggedUser({"login": userData.login, "admin": userData.admin})
                    navigate("/content")
                })
                .catch(() => {
                    setUserError("Incorrect login or password.")
                }).finally(() => {
                setLoading(false)
            })
        }

    }

    const isCredentialsValid = (login: string, password: string) => {

        setLoginError("")
        setPasswordError("")
        setUserError("")

        if ("" === login) {
            setLoginError("Please enter your login")
            return false;
        }

        if ("" === password) {
            setPasswordError("Please enter your password")
            return false;
        }

        return true;
    }

    const onButtonClick = () => {
        isCredentialsValid(login, password) && logIn(login, password);
    }

    if (loading) return <PuffLoader color="#36d7b7" size={120}/>;


    return <div className={"mainContainer"}>
        <div className={"titleContainer"}>
            <div>Login</div>
        </div>
        <br/>
        {userError && <label className="errorLabel" style={{color: "red", fontSize: "12px"}}>{userError}</label>}
        <div className={"inputContainer"}>
            <input
                value={login}
                placeholder="Enter your login here"
                onChange={ev => setLogin(ev.target.value)}
                className={"inputBox"}/>
            <label className="errorLabel">{loginError}</label>
        </div>
        <br/>
        <div className={"inputContainer"}>
            <input
                value={password}
                placeholder="Enter your password here"
                onChange={ev => setPassword(ev.target.value)}
                className={"inputBox"}/>
            <label className="errorLabel">{passwordError}</label>
        </div>
        <br/>
        <Button variant="contained" color="primary"
                onClick={onButtonClick}>
            Log in
        </Button>

    </div>
}

export default Login