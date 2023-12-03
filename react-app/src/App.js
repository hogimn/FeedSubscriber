import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {Container, CssBaseline} from '@mui/material';
import Home from "./Home";
import Endpoint from "./Endpoint";
import Menu from "./Menu";
import Redirect from "./Redirect";

function App() {
    return (
        <Router>
            <CssBaseline/>
            <Menu/>
            <Container>
                <Routes>
                    <Route path="/home" element={<Home/>}/>
                    <Route path="/endpoint" element={<Endpoint/>}/>
                    <Route path="/" element={<Redirect/>}/>
                    <Route path="/authorized" element={<Redirect/>}/>
                </Routes>
            </Container>
        </Router>
    );
}

export default App;
