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

export const deleteDoctorApi = {
    hardDeleteDoctor: async (id) => {
        const response = await $api.delete(`/admin/doctors/hard-delete/${id}`);
        return response.data;
    },
    getDoctorById: async (id) => {
        const response = await $api.get(`/admin/doctors/get/${id}`);
        return response.data;
    },

    // Обновление
    updateDoctor: async (id, formData) => {
        // PUT multipart/form-data
        // Axios сам поставит хедеры
        const response = await $api.put(`/admin/doctors/update/${id}`, formData);
        return response.data;
    }
};