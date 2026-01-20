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

// Эндпоинт из image_af0d26.jpg
export const cartApi = {
    addItem: async (visitId, serviceId) => {
        const response = await $api.post(`/patient/me/visits/${visitId}/cart/add/items`, {
            visitId: visitId,
            medicalServiceId: serviceId,
            quantity: 1
        });
        return response.data;
    },
    // Получить товары в корзине
    getCartItems: async (visitId) => {
        const response = await $api.get(`/patient/me/visits/${visitId}/cart/get/items`);
        return response.data;
    }
};