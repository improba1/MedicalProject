import axios from 'axios';

// Если ты настроила proxy в package.json, то полный путь писать не надо
const BASE_URL = '/api/v1/auth'; 

export const authApi = {
    login: async (username, password) => {
        const response = await axios.post(`${BASE_URL}/authenticate`, {
            username, 
            password
        });
        return response.data; 
    },

    register: async (userData) => {
        const response = await axios.post(`${BASE_URL}/register`, userData);
        return response.data;
    }
};