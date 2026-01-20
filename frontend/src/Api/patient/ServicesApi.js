import axios from 'axios';

// Настраиваем базовый экземпляр
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

// Эндпоинт из image_af059d.png
export const servicesApi = {
    searchServices: async (doctorId) => {
        const response = await $api.post('/patient/medical-services/search', {
            doctorId: doctorId,
            name: null,
            active: true,
            minPrice: null,
            maxPrice: null
        });
        return response.data;
    }
};