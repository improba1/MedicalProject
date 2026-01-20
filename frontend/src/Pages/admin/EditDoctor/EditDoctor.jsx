import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import styles from './EditDoctor.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithoutProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { adminDoctorApi } from '../../../Api/admin/doctorApi';

const EditDoctor = () => {
    const { id } = useParams();
    const location = useLocation();
    const navigate = useNavigate();

    // Пытаемся взять данные из state роутера, если их нет - загрузим по ID
    const initialData = location.state?.doctor;

    // Read-only данные (для контекста)
    const [staticInfo, setStaticInfo] = useState(initialData || {});
    
    // Редактируемые данные
    const [address, setAddress] = useState(initialData?.address || '');
    const [qualification, setQualification] = useState(initialData?.qualification || '');
    const [rating, setRating] = useState(initialData?.rating || 0);
    
    // Фото
    const [imageFile, setImageFile] = useState(null);
    const [previewUrl, setPreviewUrl] = useState(initialData?.image?.downloadUrl || null);

    const [loading, setLoading] = useState(false);
    const [errorMsg, setErrorMsg] = useState('');

    // Если зашли по прямой ссылке и нет данных в state, грузим их
    useEffect(() => {
        if (!initialData && id) {
            const loadDoctor = async () => {
                try {
                    setLoading(true);
                    const response = await adminDoctorApi.getDoctorById(id);
                    const doc = response.data;
                    
                    setStaticInfo(doc);
                    setAddress(doc.address);
                    setQualification(doc.qualification);
                    setRating(doc.rating);
                    setPreviewUrl(doc.image?.downloadUrl);
                } catch (error) {
                    console.error("Failed to load doctor", error);
                    setErrorMsg("Doctor not found");
                } finally {
                    setLoading(false);
                }
            };
            loadDoctor();
        }
    }, [id, initialData]);

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            if (!file.type.startsWith('image/')) {
                setErrorMsg("Please upload a valid image file.");
                return;
            }
            setImageFile(file);
            // Создаем временный URL для превью
            setPreviewUrl(URL.createObjectURL(file));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setErrorMsg('');

        try {
            const formData = new FormData();
            
            // Добавляем строковые поля
            formData.append('address', address);
            formData.append('qualification', qualification);
            formData.append('rating', Number(rating)); // Убеждаемся что это число

            // Добавляем файл ТОЛЬКО если он был выбран
            if (imageFile) {
                formData.append('image', imageFile);
            }

            // Отправляем запрос
            await adminDoctorApi.updateDoctor(id, formData);
            
            // Успех -> назад к списку
            navigate('/admin/remove-doctor');

        } catch (err) {
            console.error(err);
            setErrorMsg(err.response?.data?.message || "Failed to update doctor.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithoutProfile />

                <main className={styles.mainContent}>
                    <h1 className={styles.title}>Edit Doctor Profile</h1>

                    <form onSubmit={handleSubmit} className={styles.formWrapper}>
                        
                        {/* --- Read Only Info (Кто это?) --- */}
                        <div className={styles.readOnlySection}>
                            <div className={styles.inputGroup}>
                                <label className={styles.label}>Full Name</label>
                                <input 
                                    className={styles.inputReadOnly} 
                                    value={`${staticInfo.firstname || ''} ${staticInfo.lastname || ''}`} 
                                    readOnly 
                                />
                            </div>
                            <div className={styles.inputGroup}>
                                <label className={styles.label}>Specialization</label>
                                <input 
                                    className={styles.inputReadOnly} 
                                    value={staticInfo.specialization || ''} 
                                    readOnly 
                                />
                            </div>
                            <div className={styles.inputGroup}>
                                <label className={styles.label}>Email</label>
                                <input 
                                    className={styles.inputReadOnly} 
                                    value={staticInfo.email || ''} 
                                    readOnly 
                                />
                            </div>
                            <div className={styles.inputGroup}>
                                <label className={styles.label}>Username</label>
                                <input 
                                    className={styles.inputReadOnly} 
                                    value={staticInfo.nickname || ''} 
                                    readOnly 
                                />
                            </div>
                        </div>

                        {/* --- Editable Fields --- */}
                        <div className={styles.editSection}>
                            
                            {/* Левая колонка: Текстовые поля */}
                            <div style={{display:'flex', flexDirection:'column', gap: 20}}>
                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Address</label>
                                    <input 
                                        className={styles.input} 
                                        type="text" 
                                        value={address} 
                                        onChange={(e) => setAddress(e.target.value)} 
                                        required 
                                    />
                                </div>

                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Qualification</label>
                                    <input 
                                        className={styles.input} 
                                        type="text" 
                                        value={qualification} 
                                        onChange={(e) => setQualification(e.target.value)} 
                                        required 
                                    />
                                </div>

                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Rating (0 - 5)</label>
                                    <input 
                                        className={styles.input} 
                                        type="number" 
                                        min="0" 
                                        max="5" 
                                        step="0.1"
                                        value={rating} 
                                        onChange={(e) => setRating(e.target.value)} 
                                        required 
                                    />
                                </div>
                            </div>

                            {/* Правая колонка: Фото */}
                            <div className={styles.photoSection}>
                                <label className={styles.label} style={{marginLeft: 0}}>Profile Photo</label>
                                
                                <img 
                                    src={previewUrl || 'https://via.placeholder.com/150'} 
                                    alt="Preview" 
                                    className={styles.previewImage}
                                />

                                <label htmlFor="imageUpload" className={styles.fileUploadLabel}>
                                    Change Photo
                                </label>
                                <input 
                                    id="imageUpload" 
                                    type="file" 
                                    accept="image/*" 
                                    onChange={handleFileChange} 
                                    className={styles.hiddenInput} 
                                />
                            </div>
                        </div>

                        {errorMsg && <div className={styles.errorMessage}>{errorMsg}</div>}

                        <div className={styles.actionButtons}>
                            <button 
                                type="button" 
                                className={styles.cancelBtn}
                                onClick={() => navigate(-1)}
                            >
                                Cancel
                            </button>
                            <button 
                                type="submit" 
                                className={styles.saveBtn} 
                                disabled={loading}
                            >
                                {loading ? "Saving..." : "Save Changes"}
                            </button>
                        </div>

                    </form>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default EditDoctor;