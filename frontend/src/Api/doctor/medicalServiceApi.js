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

export const medicalServiceApi = {
    create: async (data) => {
        // data = { name, description, price }
        return $api.post('/doctor/medical-services/create', data);
    },
    update: async (id, data) => {
        // data = { name, description, price, active }
        return $api.put(`/doctor/medical-services/update/${id}`, data);
    },
    delete: async (id) => {
        return $api.delete(`/doctor/medical-services/delete/${id}`);
    },

    search: async (doctorId) => {
        const body = {
            doctorId: doctorId
            // name: "",
            // active: true,
            // minPrice: 0,
            // maxPrice: 0
        };
        return $api.post('/doctor/medical-services/search', body);
    }
};