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

export const doctorsNameApi = {
    getDoctorName: async () => {
        const response = await $api.get('/doctor/me/profile/get');
        return response.data;
    }
};
