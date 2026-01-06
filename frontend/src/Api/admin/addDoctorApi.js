import axios from 'axios';

const $api = axios.create({
    baseURL: 'http://localhost:8080/api/v1'
});

$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const addDoctorApi = {
    createDoctor: async (doctorData) => {
        const response = await $api.post('/doctors', doctorData);
        
        return response.data;
    }
};