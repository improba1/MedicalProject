import axios from 'axios';

const $api = axios.create({
    baseURL: '/api/v1'
});

// ОБЯЗАТЕЛЬНО: Добавляем токен в каждый запрос
$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const doctorApi = {
    getAllDoctors: async () => {
        const response = await $api.get('/doctors/getAll');
        return response.data; 
    },
    // Этот эндпоинт пригодится нам для поиска врачей по специализации
    searchDoctors: async (searchData) => {
        const response = await $api.post('/doctors/search', searchData);
        return response.data;
    }
};