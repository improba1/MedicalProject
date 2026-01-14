import axios from 'axios';

const $api = axios.create({
    baseURL: '/api/v1' 
});

export const publicDoctorApi = {
    getAllDoctors: async () => {
        return $api.get('/doctors/get-all');
    },

    searchDoctors: async (searchParams) => {
        return $api.post('/doctors/search', searchParams);
    }
};