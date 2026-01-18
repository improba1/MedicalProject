import axios from 'axios';

const $api = axios.create({
    baseURL: '/api/v1'
});

// Добавляем токен к каждому запросу
$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const doctorVisitApi = {
    getUpcomingVisits: async () => {
        const today = new Date();
        const nextYear = new Date();
        nextYear.setFullYear(today.getFullYear() + 1); // Берем запас на 1 год вперед

        const body = {
            // Фильтруем только запланированные (активные)
            status: 'SCHEDULED', 
            // Обязательные поля для поиска по времени
            start: today.toISOString(),
            end: nextYear.toISOString() 
        };

        const response = await $api.post('/doctor/me/visits/search', body);
        return response.data;
    },

    getPatientById: async (patientId) => {
        // Запрос: /api/v1/doctor/patients/get/{patientId}
        const response = await $api.get(`/doctor/patients/get/${patientId}`);
        return response.data; 
    }
};