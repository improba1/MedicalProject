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

export const adminDoctorApi = {
    // Поиск врачей (фильтрация)
    searchDoctors: async (filterData) => {
        // filterData: { name, specialization, isActive, rating }
        const response = await $api.post('/admin/doctors/search', filterData);
        return response.data;
    },

    // Полное удаление
    hardDelete: async (doctorId) => {
        const response = await $api.delete(`/admin/doctors/hard-delete/${doctorId}`);
        return response.data;
    },

    // Деактивация (мягкое удаление/скрытие)
    deactivate: async (doctorId) => {
        const response = await $api.put(`/admin/doctors/${doctorId}/deactivate`);
        return response.data;
    },

    // Активация
    activate: async (doctorId) => {
        const response = await $api.put(`/admin/doctors/${doctorId}/activate`);
        return response.data;
    },

    // ... другие методы ...
    updateDoctor: async (id, formData) => {
        return $api.put(`/admin/doctors/update/${id}`, formData);
    }
};