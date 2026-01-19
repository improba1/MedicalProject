import React, { useState } from 'react';
import styles from './AddDoctor.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithoutProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { useNavigate } from 'react-router-dom';
import { addDoctorApi } from '../../../Api/admin/addDoctorApi';

// Список специализаций (в точности как в Java Enum)
const SPECIALIZATIONS = [
    "CARDIOLOGIST",
    "DERMATOLOGIST",
    "NEUROLOGIST",
    "PEDIATRICIAN",
    "PSYCHIATRIST",
    "SURGEON",
    "ORTHOPEDIST",
    "OPHTHALMOLOGIST",
    "GYNECOLOGIST",
    "UROLOGIST"
];

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
    
    const [imageFile, setImageFile] = useState(null);
    const [dragging, setDragging] = useState(false);
    const [loading, setLoading] = useState(false);
    const [errorMsg, setErrorMsg] = useState('');
    const navigate = useNavigate();

    const handleFile = (file) => {
        if (file && file.type.startsWith('image/')) {
            setImageFile(file);
        } else {
            setErrorMsg("Please upload a valid image file.");
        }
    };
    const handleFileChange = (e) => handleFile(e.target.files[0]);
    const handleDragOver = (e) => { e.preventDefault(); setDragging(true); };
    const handleDragLeave = (e) => { e.preventDefault(); setDragging(false); };
    const handleDrop = (e) => { e.preventDefault(); setDragging(false); handleFile(e.dataTransfer.files[0]); };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!sex) { setErrorMsg('Please select a gender'); return; }
        if (!specialization) { setErrorMsg('Please select a specialization'); return; }

        setLoading(true);
        setErrorMsg('');
        
        const formData = new FormData();
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
            specialization, // Теперь это строка из ENUM
            qualification, 
            startDate: startDate || null, 
            rating: Number(rating)
        };

        // Log data to console to verify it's not empty before sending
        console.log("Sending doctor data:", doctorData);

        // Append JSON data as a Blob with application/json type
        formData.append("doctor", new Blob([JSON.stringify(doctorData)], { type: 'application/json' }));
        
        if (imageFile) {
            formData.append("image", imageFile);
        }
        
        try {
            const response = await addDoctorApi.createDoctor(formData);
            if (response && (response.data || response.status === 0)) {
                navigate('/admin'); 
            }
        } catch (err) {
            console.error("API Error:", err);
            // Display specific error message from backend if available
            const message = err.response?.data?.message || "Server error. Check console for details.";
            // Optionally, list specific validation errors
            if (err.response?.data?.errors) {
                 const validationErrors = err.response.data.errors.map(e => `${e.field}: ${e.message}`).join(', ');
                 setErrorMsg(`${message} (${validationErrors})`);
            } else {
                 setErrorMsg(message);
            }
        } finally {
            setLoading(false);
        }
    };

    // Helper для красивого отображения (CARDIOLOGIST -> Cardiologist)
    const formatEnum = (str) => str.charAt(0) + str.slice(1).toLowerCase();

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithoutProfile />

                <main className={styles.mainContent}>
                    <h1 className={styles.title}>New Doctor Profile</h1>

                    <form onSubmit={handleSubmit} className={styles.formWrapper}>
                        <div className={styles.inputsContainer}>
                            
                            {/* --- Left Column: Personal Info --- */}
                            <div className={styles.column}>
                                <div className={styles.sectionTitle}>Personal Information</div>
                                
                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>First Name</label>
                                    <input className={styles.input} type="text" placeholder="e.g. John" value={firstname} onChange={(e) => setFirstname(e.target.value)} required />
                                </div>

                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Last Name</label>
                                    <input className={styles.input} type="text" placeholder="e.g. Doe" value={lastname} onChange={(e) => setLastname(e.target.value)} required />
                                </div>

                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Gender</label>
                                    <select required className={styles.selectField} value={sex} onChange={(e) => setSex(e.target.value)} style={{ color: sex ? 'white' : '#888' }}>
                                        <option value="" disabled>Select Gender</option>
                                        <option value="MALE">Male</option>
                                        <option value="FEMALE">Female</option>
                                    </select>
                                </div>

                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Date of Birth</label>
                                    <input className={styles.input} type="date" value={birthDate} onChange={(e) => setBirthDate(e.target.value)} required />
                                </div>

                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Address</label>
                                    <input className={styles.input} type="text" placeholder="Full residential address" value={address} onChange={(e) => setAddress(e.target.value)} required />
                                </div>
                            </div>

                            {/* --- Right Column: Professional & Account --- */}
                            <div className={styles.column}>
                                <div className={styles.sectionTitle}>Professional Details</div>
                                
                                {/* --- ИЗМЕНЕНИЕ ЗДЕСЬ: Выпадающий список Специализаций --- */}
                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Specialization</label>
                                    <select 
                                        required 
                                        className={styles.selectField} 
                                        value={specialization} 
                                        onChange={(e) => setSpecialization(e.target.value)}
                                        style={{ color: specialization ? 'white' : '#888' }}
                                    >
                                        <option value="" disabled>Select Specialization</option>
                                        {SPECIALIZATIONS.map((spec) => (
                                            <option key={spec} value={spec}>
                                                {formatEnum(spec)}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Qualification</label>
                                    <input className={styles.input} type="text" placeholder="e.g. PhD, MD" value={qualification} onChange={(e) => setQualification(e.target.value)} required />
                                </div>

                                <div style={{display:'flex', gap: 15}}>
                                    <div className={styles.inputGroup}>
                                        <label className={styles.label}>Start Date</label>
                                        <input className={styles.input} type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} required />
                                    </div>
                                    <div className={styles.inputGroup}>
                                        <label className={styles.label}>Initial Rating</label>
                                        <input className={styles.input} type="number" min="1" max="5" step="0.1" value={rating} onChange={(e) => setRating(e.target.value)} required />
                                    </div>
                                </div>

                                <div className={styles.sectionTitle} style={{marginTop: 20}}>Account Credentials</div>

                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Email</label>
                                    <input className={styles.input} type="email" placeholder="doctor@example.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
                                </div>

                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Phone</label>
                                    <input className={styles.input} type="tel" placeholder="111111111" value={phone} onChange={(e) => setPhone(e.target.value)} required />
                                </div>

                                <div style={{display:'flex', gap: 15}}>
                                    <div className={styles.inputGroup}>
                                        <label className={styles.label}>Username</label>
                                        <input className={styles.input} type="text" placeholder="Login" value={nickname} onChange={(e) => setNickname(e.target.value)} required />
                                    </div>
                                    <div className={styles.inputGroup}>
                                        <label className={styles.label}>Password</label>
                                        <input className={styles.input} type="password" placeholder="******" value={password} onChange={(e) => setPassword(e.target.value)} required />
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* --- Photo Upload --- */}
                        <div className={styles.inputGroup} style={{marginBottom: 40, width: '100%', maxWidth: '600px'}}>
                            <label className={styles.label}>Profile Photo</label>
                            <div 
                                className={`${styles.fileUploadContainer} ${dragging ? styles.dragActive : ''}`}
                                onDragOver={handleDragOver}
                                onDragLeave={handleDragLeave}
                                onDrop={handleDrop}
                            >
                                <label htmlFor="file-upload" className={styles.fileUploadBtn}>
                                    {imageFile ? (
                                        <span style={{color: '#4BB543', display:'flex', alignItems:'center', gap:10}}>
                                            ✓ {imageFile.name}
                                        </span>
                                    ) : (
                                        <span>📷 Drag & Drop photo here or <u>Click to upload</u></span>
                                    )}
                                </label>
                                <input id="file-upload" type="file" accept="image/*" onChange={handleFileChange} className={styles.hiddenFileInput} />
                            </div>
                        </div>

                        {errorMsg && <div className={styles.errorMessage}>{errorMsg}</div>}

                        <button type="submit" className={styles.createBtn} disabled={loading}>
                            {loading ? "Creating..." : "Create Doctor"}
                        </button>
                    </form>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default AddDoctor;