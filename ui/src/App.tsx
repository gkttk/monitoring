import {BrowserRouter, Route, Routes} from 'react-router-dom';

import './App.css';
import Login from "./components/Login";
import ContentPage from "./components/ContentPage";
import {useState} from "react";
import {UserContext} from "./util/UserContext";


function App() {

    const [loggedUser, setLoggedUser] = useState({});

    return (<UserContext.Provider value={{loggedUser, setLoggedUser}}>
            <div className="App">
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<Login/>}/>
                        <Route path="/content" element={<ContentPage/>}/>
                    </Routes>
                </BrowserRouter>
            </div>
        </UserContext.Provider>);
}

export default App;