import styles from './EditProfile.module.css';
import Background from '../../../Components/Background/Background';
import BackBtn from '../../../Components/BackButton/BackButton';
import { profileApi } from '../../../Api/all/profileApi';
import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

const FormInput = ({ label, type = "text", name, value, onChange, placeholder, options }) => {
    return (
        <div className={styles.inputGroup}>
            <label className={styles.label}>{label}</label>
            {type === 'select' ? (
                <select className={styles.select} name={name} value={value} onChange={onChange}>
                    {options.map(opt => (
                        <option key={opt.value} value={opt.value} disabled={opt.disabled}>
                            {opt.label}
                        </option>
                    ))}
                </select>
            ) : (
                <input 
                    className={styles.input} 
                    type={type} 
                    name={name} 
                    value={value} 
                    onChange={onChange}
                    placeholder={placeholder}
                />
            )}
        </div>
    );
};

const EditProfile = () => {
    const navigate = useNavigate();
    const [role, setRole] = useState('');
    const [loading, setLoading] = useState(true);
    const [dragActive, setDragActive] = useState(false);
    const fileInputRef = useRef(null);

    const [formData, setFormData] = useState({
        // Admin
        firstname: '', lastname: '', birthDate: '', sex: '',
        // Doctor
        qualification: '', rating: '', 
        image: null, 
        // Patient & Common
        nickname: '', email: '', phone: '', address: ''
    });

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const userRole = localStorage.getItem('role');
                setRole(userRole);
                
                let data;
                if (userRole === 'PATIENT') data = (await profileApi.getPatientProfile()).data;
                else if (userRole === 'DOCTOR') data = (await profileApi.getDoctorProfile()).data;
                else if (userRole === 'ADMIN') data = (await profileApi.getAdminProfile()).data;

                setFormData({
                    firstname: data.firstname || '',
                    lastname: data.lastname || '',
                    birthDate: data.birthDate || '',
                    sex: data.sex || '',
                    qualification: data.qualification || '',
                    rating: data.rating || '',
                    image: data.image?.downloadUrl || '', 
                    nickname: data.nickname || '',
                    email: data.email || '',
                    phone: data.phone || '',
                    address: data.address || ''
                });
            } catch (error) {
                console.error("Error loading profile:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchUserData();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({ ...prevState, [name]: value }));
    };


    const handleDrag = (e) => {
        e.preventDefault();
        e.stopPropagation();
        if (e.type === "dragenter" || e.type === "dragover") {
            setDragActive(true);
        } else if (e.type === "dragleave") {
            setDragActive(false);
        }
    };

    const handleDrop = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setDragActive(false);
        if (e.dataTransfer.files && e.dataTransfer.files[0]) {
            const file = e.dataTransfer.files[0];
            setFormData(prevState => ({ ...prevState, image: file }));
        }
    };

    const handleFileSelect = (e) => {
        if (e.target.files && e.target.files[0]) {
            const file = e.target.files[0];
            setFormData(prevState => ({ ...prevState, image: file }));
        }
    };

    const onButtonClick = () => {
        fileInputRef.current.click();
    };

    const getPreviewUrl = () => {
        const img = formData.image;
        if (!img) return null;
        if (typeof img === 'string') return img;
        return URL.createObjectURL(img);
    };


    const handleSave = async () => {
        setLoading(true); 
        try {
            
            if (role === 'DOCTOR') {
                const data = new FormData();
                
                if (formData.address) data.append('address', formData.address);
                if (formData.qualification) data.append('qualification', formData.qualification);
                if (formData.rating) data.append('rating', formData.rating);
                
                if (formData.image instanceof File) {
                    data.append('image', formData.image);
                }

                await profileApi.updateDoctorProfile(data);

            } else if (role === 'PATIENT') {
                const requestBody = {
                    email: formData.email,
                    phone: formData.phone,
                    address: formData.address,
                    nickname: formData.nickname
                };
                await profileApi.updatePatientProfile(requestBody);

            } else if (role === 'ADMIN') {
                const requestBody = {
                    nickname: formData.nickname,
                    firstname: formData.firstname,
                    lastname: formData.lastname,
                    birthDate: formData.birthDate, 
                    sex: formData.sex,             
                    email: formData.email,
                    phone: formData.phone,
                    address: formData.address
                };
                await profileApi.updateAdminProfile(requestBody);
            }

            navigate('/my-profile');

        } catch (error) {
            console.error("Ошибка при обновлении профиля:", error);
            alert("Error saving profile");
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <Background><div style={{color:'white'}}>Loading...</div></Background>;

    const previewUrl = getPreviewUrl();

    return (
        <Background>
            <BackBtn />
            <div className={styles.container}>
                <span className={styles.title}>Edit Profile</span>

                <div className={styles.formWrapper}>
                    <div className={styles.column}>
                         {role === 'ADMIN' && (
                            <>
                                <FormInput label="First Name" name="firstname" value={formData.firstname} onChange={handleChange} />
                                <FormInput label="Last Name" name="lastname" value={formData.lastname} onChange={handleChange} />
                                <FormInput label="Birth Date" type="date" name="birthDate" value={formData.birthDate} onChange={handleChange} />
                                <FormInput label="Sex" type="select" name="sex" value={formData.sex} onChange={handleChange} options={[{ value: "", label: "Select Sex", disabled: true },{ value: "MALE", label: "Male" },{ value: "FEMALE", label: "Female" }]} />
                            </>
                         )}
                         {role === 'DOCTOR' && (
                            <>
                                <FormInput label="Qualification" name="qualification" value={formData.qualification} onChange={handleChange} />
                                <FormInput label="Rating" type="number" name="rating" value={formData.rating} onChange={handleChange} />
                                <FormInput label="Address" name="address" value={formData.address} onChange={handleChange} />
                            </>
                         )}
                         {role === 'PATIENT' && (
                            <>
                                <FormInput label="Login" name="nickname" value={formData.nickname} onChange={handleChange} />
                                <FormInput label="Email" type="email" name="email" value={formData.email} onChange={handleChange} />
                            </>
                         )}
                    </div>

                    <div className={styles.column}>
                        {role === 'ADMIN' && (
                           <>
                                <FormInput label="Nickname" name="nickname" value={formData.nickname} onChange={handleChange} />
                                <FormInput label="Email" type="email" name="email" value={formData.email} onChange={handleChange} />
                                <FormInput label="Phone" type="tel" name="phone" value={formData.phone} onChange={handleChange} />
                                <FormInput label="Address" name="address" value={formData.address} onChange={handleChange} />
                           </> 
                        )}

                        {role === 'DOCTOR' && (
                            <>
                                <div className={styles.inputGroup}>
                                    <label className={styles.label}>Profile Image</label>
                                    
                                    <div 
                                        className={`${styles.dragDropZone} ${dragActive ? styles.dragActive : ''}`}
                                        onDragEnter={handleDrag}
                                        onDragLeave={handleDrag}
                                        onDragOver={handleDrag}
                                        onDrop={handleDrop}
                                        onClick={onButtonClick}
                                    >
                                        <input 
                                            ref={fileInputRef}
                                            type="file" 
                                            accept="image/*"
                                            className={styles.fileInputHidden}
                                            onChange={handleFileSelect}
                                        />
                                        
                                        {previewUrl ? (
                                            <div className={styles.previewContainer}>
                                                <img src={previewUrl} alt="Preview" className={styles.imagePreview} />
                                                <span className={styles.changeText}>Click or Drop to change</span>
                                            </div>
                                        ) : (
                                            <div className={styles.uploadPlaceholder}>
                                                <span className={styles.uploadIcon}>☁️</span>
                                                <p>Drag & Drop image here <br/> or click to upload</p>
                                            </div>
                                        )}
                                    </div>
                                </div>

                                
                            </>
                        )}
                        
                        {role === 'PATIENT' && (
                            <>
                                <FormInput label="Phone" type="tel" name="phone" value={formData.phone} onChange={handleChange} />
                                <FormInput label="Address" name="address" value={formData.address} onChange={handleChange} />
                            </>
                        )}
                    </div>
                </div>

                <button className={styles.button} onClick={handleSave}>Save Changes</button>
            </div>
        </Background>
    );
};

export default EditProfile;