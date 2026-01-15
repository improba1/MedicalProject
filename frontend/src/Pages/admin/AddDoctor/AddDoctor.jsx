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
    const [sex, setSex] = useState(''); 
    const [address, setAddress] = useState('');
    const [specialization, setSpecialization] = useState('');
    const [qualification, setQualification] = useState('');
    const [startDate, setStartDate] = useState('');
    const [rating, setRating] = useState(1);
    
    // Храним сам файл для MultipartFile и URL для превью
    const [imageFile, setImageFile] = useState(null);
    const [dragging, setDragging] = useState(false);

    const [loading, setLoading] = useState(false);
    const [errorMsg, setErrorMsg] = useState('');

    const navigate = useNavigate();

    // Обработка выбора файла (через проводник или drop)
    const handleFile = (file) => {
        if (file && file.type.startsWith('image/')) {
            setImageFile(file);
        } else {
            setErrorMsg("Please upload a valid image file.");
        }
    };

    const handleFileChange = (e) => {
        handleFile(e.target.files[0]);
    };

    // Drag and Drop обработчики
    const handleDragOver = (e) => {
        e.preventDefault();
        setDragging(true);
    };

    const handleDragLeave = (e) => {
        e.preventDefault();
        setDragging(false);
    };

    const handleDrop = (e) => {
        e.preventDefault();
        setDragging(false);
        const file = e.dataTransfer.files[0];
        handleFile(file);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!sex) {
            setErrorMsg('Please select a gender');
            return;
        }

        setLoading(true);
        setErrorMsg('');
        
        // Создаем FormData для передачи MultipartFile
        const formData = new FormData();
        
        // Данные доктора в формате JSON (Blob используется, если бэкенд ждет @RequestPart)
        const doctorData = {
            email,
            nickname,
            phone,
            password,
            firstname,
            lastname,
            birthDate: birthDate || null,
            sex,
            address,
            specialization,
            qualification,
            startDate: startDate || null,
            rating: Number(rating)
        };

        // Если бэкенд принимает один объект и файл отдельно:
        formData.append("doctor", new Blob([JSON.stringify(doctorData)], { type: 'application/json' }));
        if (imageFile) {
            formData.append("image", imageFile); // Тот самый MultipartFile
        }
        
        try {
            // В апи должен быть метод, принимающий FormData
            const response = await addDoctorApi.createDoctor(formData);
            if (response && (response.data || response.status === 0)) {
                navigate('/admin'); 
            }
        } catch (err) {
            const message = err.response?.data?.message || "Server error. Check if the file size is too large.";
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
                                <input className={styles.input} type="text" placeholder="First name" value={firstname} onChange={(e) => setFirstname(e.target.value)} required />
                                <input className={styles.input} type="text" placeholder="Last name" value={lastname} onChange={(e) => setLastname(e.target.value)} required />
                                
                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Birth date</label>
                                    <input className={styles.input} type="date" value={birthDate} onChange={(e) => setBirthDate(e.target.value)} required />
                                </div>
                                                                
                                <div className={styles.inputBox}>
                                    <select 
                                        required 
                                        className={styles.selectField}
                                        value={sex} 
                                        onChange={(e) => setSex(e.target.value)}
                                        style={{ color: sex ? 'white' : 'grey' }}
                                    >
                                        <option value="" disabled>Select Gender</option>
                                        <option value="MALE">Male</option>
                                        <option value="FEMALE">Female</option>
                                    </select>
                                </div>
                                
                                <input className={styles.input} type="text" placeholder="Address" value={address} onChange={(e) => setAddress(e.target.value)} required />
                                
                                <div 
                                    className={`${styles.fileUploadContainer} ${dragging ? styles.dragActive : ''}`}
                                    onDragOver={handleDragOver}
                                    onDragLeave={handleDragLeave}
                                    onDrop={handleDrop}
                                >
                                    <label htmlFor="file-upload" className={styles.fileUploadBtn}>
                                        {imageFile ? `✓ ${imageFile.name}` : "Drop photo here or Click"}
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
                                <input className={styles.input} type="text" placeholder="Specialization" value={specialization} onChange={(e) => setSpecialization(e.target.value)} required />
                                <input className={styles.input} type="text" placeholder="Qualification" value={qualification} onChange={(e) => setQualification(e.target.value)} required />
                                
                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Start date</label>
                                    <input className={styles.input} type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} required />
                                </div>    

                                <input className={styles.input} type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                                <input className={styles.input} type="tel" placeholder="Phone" value={phone} onChange={(e) => setPhone(e.target.value)} required />
                                <input className={styles.input} type="text" placeholder="Nickname (login)" value={nickname} onChange={(e) => setNickname(e.target.value)} required />
                                <input className={styles.input} type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />
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