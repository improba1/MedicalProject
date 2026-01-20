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

export const upcomingVisitsApi = {
    // Реальный запрос к бэкенду
    getUpcomingVisits: async () => {
        try {
            const response = await $api.post('/patient/me/visits/search', {
                // Пустое тело запроса для получения всех визитов или фильтр по статусу
                status: null
            });
            return response.data;
        } catch (error) {
            console.warn("Backend error, using mock data...");
            // Заглушка на случай проблем с бэкендом
            return {
                data: [
                    {
                        id: "mock-1",
                        doctorName: "Dr. Gregory House",
                        specialization: "DIAGNOSTICIAN",
                        appointmentTime: "2026-01-25T14:00:00Z",
                        status: "PAID",
                        totalPrice: 150,
                        services: [{ serviceName: "General Consultation" }]
                    }
                ]
            };
        }
    },

    cancelVisit: async (visitId) => {
        return $api.put(`/patient/me/visits/cancel/${visitId}`);
    }
};