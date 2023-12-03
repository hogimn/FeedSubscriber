import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {RESTFUL_URL} from './Constant';

function Endpoint() {
    const [endpoints, setEndpoints] = useState([]);
    const [newEndpoint, setNewEndpoint] = useState('');

    const fetchEndpoints = () => {
        const token = sessionStorage.getItem('id_token')
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        axios.get(`${RESTFUL_URL}/endpoints`, {headers: headers})
            .then(response => {
                setEndpoints(response.data);
            })
            .catch(error => {
                console.error('Error fetching endpoints:', error);
            });
    };

    const addEndpoint = () => {
        const token = sessionStorage.getItem('id_token')
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        axios.post(`${RESTFUL_URL}/endpoint`, {url: newEndpoint}, {headers: headers})
            .then(() => {
                fetchEndpoints();
                setNewEndpoint('');
            })
            .catch(error => {
                console.error('Error adding endpoint:', error);
            });
    };

    const deleteEndpointAndRelatedRss = (url) => {
        const token = sessionStorage.getItem('id_token')
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        axios.delete(`${RESTFUL_URL}/rss_items`, {headers: headers, data: {url: url}})
            .then(() => {
                axios.delete(`${RESTFUL_URL}/endpoint`, {headers: headers, data: {url: url}})
                    .then(() => {
                        fetchEndpoints();
                    })
                    .catch(error => {
                        console.error('Error deleting endpoint:', error);
                    });
            })
            .catch(error => {
                console.error('Error deleting rss items:', error);
            });
    };

    useEffect(() => {
        fetchEndpoints();
    }, []);

    return (
        <div>
            <h2>Manage Endpoints</h2>

            <div>
                <input
                    type="text"
                    placeholder="Add a new endpoint"
                    value={newEndpoint}
                    onChange={(e) => setNewEndpoint(e.target.value)}
                />
                <button onClick={addEndpoint}>Add</button>
            </div>

            <ul>
                {endpoints.map((endpoint, index) => (
                    <li key={index}>
                        <span>{endpoint.url} </span>
                        <button onClick={() => deleteEndpointAndRelatedRss(endpoint.url)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default Endpoint;
