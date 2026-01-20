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

export const patientMedicalServiceApi = {
    search: async (doctorId, name = '') => {
        // Отправляем тело запроса согласно твоему примеру
        const body = {
            doctorId: doctorId,
            name: name, // Для поиска
            // active: true,
            // minPrice: 0,
            // maxPrice: 0 // 0 обычно значит "не ограничивать", если бек так настроен
        };
        
        return $api.post('/patient/medical-services/search', body);
    }
};