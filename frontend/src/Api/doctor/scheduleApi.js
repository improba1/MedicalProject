import axios from 'axios';

const $api = axios.create({
    baseURL: '/api/v1/doctors/me' // Или твой полный адрес
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
        // dateTimeString должен быть формата "2023-12-31T14:30:00"
        const response = await $api.post('/availability', { 
            doctorId: doctorId,
            availableTime: dateTimeString
        });
        return response.data;
    }
};