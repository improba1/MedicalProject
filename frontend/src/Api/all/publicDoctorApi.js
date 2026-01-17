import axios from 'axios';

const $api = axios.create({
    baseURL: '/api/v1' 
});

export const publicDoctorApi = {
    getAllDoctors: async () => {
        return $api.post('/doctors/public/search', {});
    },

    searchDoctors: async (searchParams) => {
        return $api.post('/doctors/public/search', searchParams);
    }
};