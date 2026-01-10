import axios from 'axios';

// Создаем экземпляр с базовым URL проекта
const $api = axios.create({
    baseURL: '/api/v1'
});

// Интерцептор для автоматической подстановки токена авторизации
$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const profileApi = {
    /**
     * Получение данных профиля пациента для приветствия
     * Использует эндпоинт: GET /admin/patients/get/{id}
     * @param {string} patientId - UUID пациента
     * @returns {Promise} - данные пациента (firstname, lastname и т.д.)
     */
    getPatientProfile: async (patientId) => {
        // Эндпоинт соответствует скриншоту Swagger
        const response = await $api.get(`/admin/patients/get/${patientId}`);
        
        // Возвращает объект ответа, где данные лежат в поле .data
        return response.data;
    }
};