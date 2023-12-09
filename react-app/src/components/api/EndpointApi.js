import axios from 'axios';
import {RESTFUL_URL} from '../../utils/Constant';

const EndpointApi = {
    fetchEndpoints: (callback) => {
        const token = sessionStorage.getItem('id_token');
        const headers = {
            'Authorization': `Bearer ${token}`
        };

        axios.get(`${RESTFUL_URL}/endpoints`, {headers})
            .then(response => {
                callback(response.data);
            })
            .catch(error => {
                console.error('Error fetching endpoints:', error);
            });
    },

    addEndpoint: (newEndpoint, callback) => {
        const token = sessionStorage.getItem('id_token');
        const headers = {
            'Authorization': `Bearer ${token}`
        };

        axios.post(`${RESTFUL_URL}/endpoint`, {url: newEndpoint}, {headers})
            .then(() => {
                callback();
            })
            .catch(error => {
                console.error('Error adding endpoint:', error);
            });
    },

    deleteEndpointAndRelatedRss: (url, callback) => {
        const token = sessionStorage.getItem('id_token');
        const headers = {
            'Authorization': `Bearer ${token}`
        };

        axios.delete(`${RESTFUL_URL}/rss_items`, {headers, data: {url}})
            .then(() => {
                axios.delete(`${RESTFUL_URL}/endpoint`, {headers, data: {url}})
                    .then(() => {
                        EndpointApi.fetchEndpoints(callback);
                    })
                    .catch(error => {
                        console.error('Error deleting endpoint:', error);
                    });
            })
            .catch(error => {
                console.error('Error deleting rss items:', error);
            });
    },
};

export default EndpointApi;
