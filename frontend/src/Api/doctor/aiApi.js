import axios from 'axios';

const BASE_URL = '/assistant'; // CRA proxy
const $api = axios.create({ baseURL: BASE_URL });

export const aiApi = {
    generateDiagnosis: async (data) => {
        const response = await $api.post('/diagnose', data);
        return response.data;
    }
};
