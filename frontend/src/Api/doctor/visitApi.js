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

export const doctorVisitApi = {
    getUpcomingVisits: async () => {
        const today = new Date();
        const nextYear = new Date();
        nextYear.setFullYear(today.getFullYear() + 1); 

        const body = {
            // status: 'SCHEDULED', 
            // start: today.toISOString(),
            // end: nextYear.toISOString() 
        };

        const response = await $api.post('/doctor/me/visits/search', body);
        return response.data;
    },

    getPatientById: async (patientId) => {
        const response = await $api.get(`/doctor/patients/get/${patientId}`);
        return response.data; 
    }
};