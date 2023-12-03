import {useEffect} from "react";
import {useNavigate, useSearchParams} from "react-router-dom";
import {AUTH_URL} from '../../../utils/Constant';
import axios from 'axios';

const Redirect = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (searchParams?.get('code')) {
            const code = searchParams?.get('code');
            const client = 'react-client';
            const secret = 'react-client';

            const initialUrl = `${AUTH_URL}/oauth2/token?client_id=react-client&redirect_uri=http://localhost:3000/authorized&grant_type=authorization_code`;
            const url = `${initialUrl}&code=${code}`;
            const headers = {
                'Content-type': 'application/json',
                'Authorization': `Basic ${btoa(`${client}:${secret}`)}`
            }

            axios.post(url, "", {headers: headers})
                .then(response => {
                    const token = response.data
                    if (token?.id_token) {
                        sessionStorage.setItem('id_token', token.id_token);
                        navigate('/home');
                    }
                })
                .catch(err => {
                    console.log(err);
                })
        }
    }, []);
    useEffect(() => {
        if (!searchParams?.get('code')) {
            window.location.href = `${AUTH_URL}/oauth2/authorize?response_type=code&client_id=react-client&scope=openid&redirect_uri=http://localhost:3000/authorized`;
        }
    }, []);
    return <p>Redirecting ...</p>
}

export default Redirect;