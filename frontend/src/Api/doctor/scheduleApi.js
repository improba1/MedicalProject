import axios from 'axios';

const $api = axios.create({
    baseURL: '/api/v1/doctors/me' 
});

$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const scheduleApi = {
    addAvailability: async (doctorId, dateTimeString) => {
        const response = await $api.post('/availability/create', { 
            doctorId: doctorId,
            availableTime: dateTimeString
        });
        return response.data;
    }
};