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

export const addDoctorApi = {
    createDoctor: async (formData) => {
        const response = await $api.post('/admin/doctors/create', formData);
        
        return response.data;
    }
};