import React, { useState } from 'react';
import styles from './AddDoctor.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithoutProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { useNavigate } from 'react-router-dom';
import { addDoctorApi } from '../../../Api/admin/addDoctorApi';

const AddDoctor = () => {
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [nickname, setNickname] = useState('');
    const [phone, setPhone] = useState('');
    const [password, setPassword] = useState('');
    const [birthDate, setBirthDate] = useState('');
    const [sex, setSex] = useState('MALE');
    const [address, setAddress] = useState('');
    const [specialization, setSpecialization] = useState('');
    const [qualification, setQualification] = useState('');
    const [startDate, setStartDate] = useState('');
    const [rating, setRating] = useState(5);
    const [image, setImage] = useState(''); // Здесь будет храниться Base64 строка

    const [loading, setLoading] = useState(false);
    const [errorMsg, setErrorMsg] = useState('');

    const navigate = useNavigate();

    // Функция для обработки выбора файла
    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                // Сохраняем результат (Base64 строку) в состояние
                setImage(reader.result);
            };
            reader.readAsDataURL(file);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setErrorMsg('');
        
        const newDoctor = {
            email: email,
            nickname: nickname,
            phone: phone,
            password: password,
            firstname: firstname,
            lastname: lastname,
            birthDate: birthDate === '' ? null : birthDate,
            sex: sex,
            address: address,
            specialization: specialization,
            qualification: qualification,
            startDate: startDate === '' ? null : startDate,
            rating: Number(rating),
            image: image === '' ? null : image
        };
        
        try {
            const response = await addDoctorApi.createDoctor(newDoctor);
            if (response && (response.data || response.status === 0)) {
                navigate('/admin'); 
            }
        } catch (err) {
            const message = err.response?.data?.message || "Server error (500). Please check your data.";
            setErrorMsg(message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithoutProfile />

                <main className={styles.mainContent}>
                    <h1 className={styles.title}>New doctor</h1>

                    <form onSubmit={handleSubmit} className={styles.formWrapper}>
                        <div className={styles.inputsContainer}>
                            <div className={styles.column}>
                                <input className={styles.input} type="text" placeholder="first name" value={firstname} onChange={(e) => setFirstname(e.target.value)} required />
                                <input className={styles.input} type="text" placeholder="last name" value={lastname} onChange={(e) => setLastname(e.target.value)} required />
                                
                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Birth date</label>
                                    <input className={styles.input} type="date" value={birthDate} onChange={(e) => setBirthDate(e.target.value)} required />
                                </div>
                                                                
                                <select className={styles.input} value={sex} onChange={(e) => setSex(e.target.value)}>
                                    <option value="MALE">MALE</option>
                                    <option value="FEMALE">FEMALE</option>
                                </select>
                                <input className={styles.input} type="text" placeholder="address" value={address} onChange={(e) => setAddress(e.target.value)} required />
                                
                                {/* Кнопка выбора фото вместо текстового поля */}
                                <div className={styles.fileUploadContainer}>
                                    <label htmlFor="file-upload" className={styles.fileUploadBtn}>
                                        {image ? "Photo attached ✓" : "Choose doctor photo"}
                                    </label>
                                    <input 
                                        id="file-upload" 
                                        type="file" 
                                        accept="image/*" 
                                        onChange={handleFileChange} 
                                        className={styles.hiddenFileInput}
                                    />
                                </div>

                                <input className={styles.input} type="number" placeholder="rating (1-5)" min="1" max="5" step="0.1" value={rating} onChange={(e) => setRating(e.target.value)} required />
                            </div>

                            <div className={styles.column}>
                                <input className={styles.input} type="text" placeholder="specialization" value={specialization} onChange={(e) => setSpecialization(e.target.value)} required />
                                <input className={styles.input} type="text" placeholder="qualification" value={qualification} onChange={(e) => setQualification(e.target.value)} required />
                                
                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Start date</label>
                                    <input className={styles.input} type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} required />
                                </div>    

                                <input className={styles.input} type="email" placeholder="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                                <input className={styles.input} type="tel" placeholder="phone" value={phone} onChange={(e) => setPhone(e.target.value)} required />
                                <input className={styles.input} type="text" placeholder="nickname (login)" value={nickname} onChange={(e) => setNickname(e.target.value)} required />
                                <input className={styles.input} type="password" placeholder="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                            </div>
                        </div>

                        {errorMsg && <div className={styles.errorMessage}>{errorMsg}</div>}

                        <button type="submit" className={styles.createBtn} disabled={loading}>
                            {loading ? "Creating..." : "Create"}
                        </button>
                    </form>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default AddDoctor;