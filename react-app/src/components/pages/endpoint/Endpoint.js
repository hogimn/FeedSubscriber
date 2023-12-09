import React, {useEffect, useState} from 'react';
import EndpointApi from "../../api/EndpointApi";

function Endpoint() {
    const [endpoints, setEndpoints] = useState([]);
    const [newEndpoint, setNewEndpoint] = useState('');

    useEffect(() => {
        EndpointApi.fetchEndpoints(setEndpoints);
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
                <button onClick={
                    () => {
                        EndpointApi.addEndpoint(newEndpoint, () => {
                            EndpointApi.fetchEndpoints(setEndpoints);
                            setNewEndpoint('');
                        });
                    }}
                >
                    Add
                </button>
            </div>

            <ul>
                {endpoints.map((endpoint, index) => (
                    <li key={index}>
                        <span>{endpoint.url} </span>
                        <button onClick={
                            () => EndpointApi
                                .deleteEndpointAndRelatedRss(endpoint.url, setEndpoints)}
                        >
                            Delete
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default Endpoint;
