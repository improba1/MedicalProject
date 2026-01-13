import axios from 'axios';

const $api = axios.create({
    baseURL: '/api/v1' // Убедитесь, что порт совпадает с вашим бэкендом
});

$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const deleteDoctorApi = {
    /**
     * Полное удаление доктора по его UUID
     * @param {string} id - UUID доктора
     */
    hardDeleteDoctor: async (id) => {
        const response = await $api.delete(`/admin/doctors/hard-delete/${id}`);
        return response.data;
    }
};