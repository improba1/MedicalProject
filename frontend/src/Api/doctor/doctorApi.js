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

export const doctorApi = {
    getDoctorById: async (id) => {
        const response = await $api.get(`/admin/doctors/get/${id}`);
        return response.data;
    }
};