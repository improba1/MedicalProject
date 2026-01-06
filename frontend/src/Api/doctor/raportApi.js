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

export const reportApi = {
    getAllReports: async () => {
        const response = await $api.get('/doctors/me/visits/search');
        return response.data;
    },

    getReportDetails: async (visitId) => {
        const response = await $api.get(`/doctor/me/raports/visit/${visitId}`); 
        return response.data;
    }
};