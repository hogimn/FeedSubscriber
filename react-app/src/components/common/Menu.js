import React from 'react';
import {AppBar, Button, List, ListItem, ListItemText, Toolbar, Typography} from '@mui/material';
import {Link} from 'react-router-dom';

function Menu() {
    const handleLogout = () => {
        sessionStorage.clear()
        window.location.href = 'http://localhost:9001/auth/logout';
    };

    return (
        <AppBar position="static">
            <Toolbar style={{display: 'flex', justifyContent: 'space-between'}}>
                <div style={{display: 'flex', alignItems: 'center'}}>
                    <Typography variant="h6">FeedSubscriber</Typography>
                    <List component="nav" style={{display: 'flex', marginLeft: '20px'}}>
                        <ListItem button component={Link} to="/home">
                            <ListItemText primary="Home"/>
                        </ListItem>
                        <ListItem button component={Link} to="/endpoint">
                            <ListItemText primary="Endpoint"/>
                        </ListItem>
                    </List>
                </div>
                <Button color="inherit" onClick={handleLogout}>
                    Logout
                </Button>
            </Toolbar>
        </AppBar>
    );
}

export default Menu;
