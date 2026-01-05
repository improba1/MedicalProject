import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './RemoveDoctor.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithoutProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { deleteDoctorApi } from '../../../Api/admin/deleteDoctorApi';

const RemoveDoctor = () => {
    const [doctorId, setDoctorId] = useState(''); // Состояние для ID доктора
    const [loading, setLoading] = useState(false);
    const [errorMsg, setErrorMsg] = useState('');
    const [successMsg, setSuccessMsg] = useState('');
    
    const navigate = useNavigate();

    const handleRemove = async (e) => {
        e.preventDefault();
        setErrorMsg('');
        setSuccessMsg('');

        if (!doctorId.trim()) {
            setErrorMsg('Please enter a doctor ID');
            return;
        }

        // Подтверждение перед удалением
        if (!window.confirm(`Are you sure you want to permanently delete doctor with ID: ${doctorId}?`)) {
            return;
        }

        setLoading(true);

        try {
            const result = await deleteDoctorApi.hardDeleteDoctor(doctorId);
            
            // Если статус в ответе 0 (как на скриншотах), значит удаление успешно
            if (result.status === 0) {
                setSuccessMsg('Doctor has been successfully removed');
                setDoctorId(''); // Очищаем поле
                // Опционально: возвращаем админа назад через пару секунд
                setTimeout(() => navigate('/admin'), 2000);
            } else {
                setErrorMsg(result.message || 'Error occurred during deletion');
            }
        } catch (err) {
            console.error("Delete error:", err);
            setErrorMsg(err.response?.data?.message || 'Failed to remove doctor. Make sure the ID is correct.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithoutProfile />
                
                <main className={styles.mainContent}>
                    <div className={styles.titleBlock}>
                        <h1 className={styles.title}>Remove doctor</h1>
                    </div>
                    <div className={styles.actionCard}>
                        <div className={styles.inputBox}>
                            <input 
                                type="text" 
                                placeholder="Enter doctor's UUID (ID)"
                                value={doctorId}
                                onChange={(e) => setDoctorId(e.target.value)}
                                disabled={loading}
                            />
                        </div>

                        {errorMsg && <p style={{color: '#ff4d4d', textAlign: 'center', margin: '0'}}>{errorMsg}</p>}
                        {successMsg && <p style={{color: '#2ecc71', textAlign: 'center', margin: '0'}}>{successMsg}</p>}

                        <button 
                            className={styles.removeBtn} 
                            onClick={handleRemove}
                            disabled={loading}
                        >
                            {loading ? 'Processing...' : 'Remove'}
                        </button>
                    </div>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default RemoveDoctor;