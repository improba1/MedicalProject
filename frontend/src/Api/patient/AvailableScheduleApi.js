import axios from 'axios';

// Создаем экземпляр axios с базовым URL вашего API
const $api = axios.create({
    baseURL: 'http://localhost:8080/api/v1' 
});

// Интерцептор для автоматической подстановки JWT-токена в заголовки
$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const availableScheduleApi = {
    /**
     * Получение всех активных слотов для записи к конкретному врачу.
     * Использует эндпоинт: GET /admin/doctors/availability/get/{doctorId}/active
     *
     */
    getAvailableSlots: async (doctorId) => {
        // Запрос возвращает список доступного времени (appointmentTime)
        const response = await $api.get(`/admin/doctors/availability/get/${doctorId}/active`);
        return response.data;
    },

    /**
     * Бронирование выбранного времени пациентом.
     * Использует эндпоинт: POST /patient/me/visits/book/{doctorId}
     *
     * @param {string} doctorId - UUID врача
     * @param {string} appointmentTime - выбранная дата и время в формате ISO
     */
    bookAppointment: async (doctorId, appointmentTime) => {
        // Передаем выбранное время в теле запроса
        const response = await $api.post(`/patient/me/visits/book/${doctorId}`, {
            appointmentTime: appointmentTime
        });
        return response.data;
    }
};