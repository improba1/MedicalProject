import axios from 'axios';

const $api = axios.create({
    // Указываем полный путь к вашему бэкенду
    baseURL: '/api/v1' 
});

// Интерцептор для добавления токена авторизации
$api.interceptors.request.use((config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const addDoctorApi = {
    /**
     * Создание нового врача с поддержкой MultipartFile
     * Использует эндпоинт: POST /admin/doctors/create
     * @param {FormData} formData - объект, содержащий файл и данные врача
     */
    createDoctor: async (formData) => {
        const response = await $api.post('/admin/doctors/create', formData, {
            headers: {
                // Указываем тип контента для передачи файлов
                'Content-Type': 'multipart/form-data'
            }
        });
        
        return response.data;
    }
};