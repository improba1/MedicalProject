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

export const availableScheduleApi = {
    searchSlots: async (doctorId, date) => {
        const body = {
            doctorId: doctorId,
            from: `${date}T00:00:00Z`,
            to: `${date}T23:59:59Z`,
            active: true 
        };
        
        const response = await $api.get('/patient/availabilities/search', body);
        return response.data;
    }
};