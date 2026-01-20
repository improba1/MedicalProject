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

export const paymentApi = {
    // Получить ссылку на оплату для конкретного визита
    payForVisit: async (visitId) => {
        // POST /api/v1/patient/payment/pay/{visitId}
        const response = await $api.post(`/patient/payment/pay/${visitId}`);
        return response.data;
    },
    initiatePayment: async (visitId) => {
        const response = await $api.post(`/patient/payment/pay/${visitId}`);
        return response.data;
    }
};