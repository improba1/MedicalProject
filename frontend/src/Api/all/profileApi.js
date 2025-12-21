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

export const profileApi = {
    getPatientProfile: async () => {
        const response = await $api.get('/patient/me/profile'); 
        return response.data;
    },

    getDoctorProfile: async () => {
        const response = await $api.get('/doctor/me/profile'); 
        return response.data;
    },

    getAdminProfile: async () => {
        const response = await $api.get('/admin/me/profile/get'); 
        return response.data;
    },

    updatePatientProfile: (data) => $api.put('/patient/me/profile/update', data),
    
    updateDoctorProfile: (data) => $api.put('/doctor/me/profile/update', data), 
    
    updateAdminProfile: (data) => $api.put('/admmin/me/profile/update', data),
};