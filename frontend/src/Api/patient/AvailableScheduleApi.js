import axios from 'axios';

const $api = axios.create({
    baseURL: '/api/v1'
});

$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`; // Авторизация пациента
    }
    return config;
});

export const availableScheduleApi = {
    /**
     * Поиск свободных слотов врача
     * Эндпоинт: GET /patient/availabilities/search
     */
    searchSlots: async (doctorId, date) => {
        // Устанавливаем диапазон: от начала выбранного дня до его конца
        const from = `${date}T00:00:00Z`;
        const to = `${date}T23:59:59Z`;
        
        const response = await $api.get('/patient/availabilities/search', {
            params: { doctorId, from, to }
        });
        return response.data; // Возвращает только активные будущие слоты
    }
};