import axios from 'axios';

const $api = axios.create({
    baseURL: '/api/v1'
});

$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const visitApi = {
    createVisit: async (payload) => {
        const response = await $api.post('/patient/me/visits/create', payload);
        return response.data;
    }
};