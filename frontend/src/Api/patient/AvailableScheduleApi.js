import axios from 'axios';

const $api = axios.create({
    // Обязательно абсолютный URL, чтобы избежать CORS-путаницы
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
    /**
     * Поиск слотов из patient-doctor-availability-controller
     */
    searchSlots: async (doctorId, date) => {
        // Формируем параметры как в Swagger
        const body = {
            doctorId: doctorId,
            from: `${date}T00:00:00Z`,
            to: `${date}T23:59:59Z`,
            active: true // Добавили обязательное поле из swagger
        };
        
        const response = await $api.get('/patient/availabilities/search', body);
        return response.data;
    }
};