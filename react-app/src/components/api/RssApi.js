import axios from "axios";
import {RESTFUL_URL} from "../../utils/Constant";

const RssApi = {
    fetchRssItems: (callback) => {
        const token = sessionStorage.getItem('id_token')
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        axios
            .get(`${RESTFUL_URL}/rss_items`, {
                headers: headers
            })
            .then((response) => {
                callback(response.data);
            })
            .catch((error) => {
                console.error('Error fetching RSS feeds:', error);
            });
    }
}

export default RssApi;