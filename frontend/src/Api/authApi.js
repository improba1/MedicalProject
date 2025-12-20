import axios from 'axios';

const BASE_URL = '/api/v1/auth'; 

export const authApi = {
    login: async (login, password) => {
        const response = await axios.post(`${BASE_URL}/authenticate`, {
            login, 
            password
        });
        return response.data; 
    },

    register: async (userData) => {
        const response = await axios.post(`${BASE_URL}/register`, userData);
        return response.data;
    }
};