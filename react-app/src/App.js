import React from 'react';
import {BrowserRouter as Router, Link, Route, Routes} from 'react-router-dom';
import {AppBar, Container, CssBaseline, List, ListItem, ListItemText, Toolbar, Typography} from '@mui/material';
import Home from "./Home";
import Endpoint from "./Endpoint";


function Menu() {
    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6">
                    FeedSubscriber
                </Typography>
                <List component="nav" style={{display: 'flex'}}>
                    <ListItem button component={Link} to="/">
                        <ListItemText primary="Home"/>
                    </ListItem>
                    <ListItem button component={Link} to="/endpoint">
                        <ListItemText primary="Endpoint"/>
                    </ListItem>
                </List>
            </Toolbar>
        </AppBar>
    );
}

function App() {
    return (
        <Router>
            <CssBaseline/>
            <Menu/>
            <Container>
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/endpoint" element={<Endpoint/>}/>
                </Routes>
            </Container>
        </Router>
    );
}

export default App;
