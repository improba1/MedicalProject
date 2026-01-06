import styles from './EditProfile.module.css';
import Background from '../../../Components/Background/Background';
import BackBtn from '../../../Components/BackButton/BackButton';
import { profileApi } from '../../../Api/all/profileApi';
import React, { useState, useEffect } from 'react';
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

    const [formData, setFormData] = useState({
        // Admin
        firstname: '', lastname: '', birthDate: '', sex: '',
        // Doctor
        qualification: '', rating: '', image: '',
        // Patient & Common
        nickname: '', email: '', phone: '', address: ''
    });

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const userRole = localStorage.getItem('role');

                setRole(userRole);

                
                let data;
                if (userRole === 'PATIENT') {
                    const response = await profileApi.getPatientProfile();
                    data = response.data;
                } else if (userRole === 'DOCTOR') {
                    const response = await profileApi.getDoctorProfile();
                    data = response.data;
                } else if (userRole === 'ADMIN') {
                    const response = await profileApi.getAdminProfile();
                    data = response.data;
                }

                setFormData({
                    firstname: data.firstname || '',
                    lastname: data.lastname ||  '',
                    birthDate: data.birthDate || '',
                    sex: data.sex || '',
                    qualification: data.qualification || '',
                    rating: data.rating || '',
                    image: data.image || '',
                    nickname: data.nickname || data.username || data.login || '',
                    email: data.email || '',
                    phone: data.phone || data.phoneNumber || '',
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

    const handleSave = async () => {
        setLoading(true); 

        try {
            let requestBody = {};

            if (role === 'DOCTOR') {
                requestBody = {
                    address: formData.address,
                    qualification: formData.qualification,
                    rating: Number(formData.rating), 
                    image: formData.image
                };
                
                console.log("Doctor sent:", requestBody);
                await profileApi.updateDoctorProfile(requestBody);

            } else if (role === 'PATIENT') {
                requestBody = {
                    email: formData.email,
                    phone: formData.phone,
                    address: formData.address,
                    nickname: formData.nickname
                };

                console.log("Отправляем пациента:", requestBody);
                await profileApi.updatePatientProfile(requestBody);

            } else if (role === 'ADMIN') {
                requestBody = {
                    nickname: formData.nickname,
                    firstname: formData.firstname,
                    lastname: formData.lastname,
                    birthDate: formData.birthDate, 
                    sex: formData.sex,             
                    email: formData.email,
                    phone: formData.phone,
                    address: formData.address
                };

                console.log("Отправляем админа:", requestBody);
                await profileApi.updateAdminProfile(requestBody);
            }

            navigate('/my-profile');

        } catch (error) {
            console.error("Ошибка при обновлении профиля:", error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <Background><div style={{color:'white', textAlign:'center', marginTop:'20%'}}>Loading...</div></Background>;
    }

    return (
        <Background>
            <BackBtn />
            <div className={styles.container}>
                <span className={styles.title}>Edit Profile</span>

                <div className={styles.formWrapper}>
                    
                    {/* --- ЛЕВАЯ КОЛОНКА --- */}
                    <div className={styles.column}>
                        {role === 'ADMIN' && (
                            <>
                                <FormInput label="First Name" name="firstname" value={formData.firstname} onChange={handleChange} />
                                <FormInput label="Last Name" name="lastname" value={formData.lastname} onChange={handleChange} />
                                <FormInput label="Birth Date" type="date" name="birthDate" value={formData.birthDate} onChange={handleChange} />
                                <FormInput 
                                    label="Sex" 
                                    type="select" 
                                    name="sex" 
                                    value={formData.sex} 
                                    onChange={handleChange}
                                    options={[
                                        { value: "", label: "Select Sex", disabled: true },
                                        { value: "MALE", label: "Male" },
                                        { value: "FEMALE", label: "Female" }
                                    ]}
                                />
                            </>
                        )}

                        {role === 'DOCTOR' && (
                            <>
                                <FormInput label="Qualification" name="qualification" value={formData.qualification} onChange={handleChange} />
                                <FormInput label="Rating" type="number" name="rating" value={formData.rating} onChange={handleChange} />
                            </>
                        )}

                        {role === 'PATIENT' && (
                            <>
                                <FormInput label="Login" name="nickname" value={formData.nickname} onChange={handleChange} />
                                <FormInput label="Email" type="email" name="email" value={formData.email} onChange={handleChange} />
                            </>
                        )}
                    </div>

                    {/* --- ПРАВАЯ КОЛОНКА --- */}
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
                                <FormInput label="Image URL" name="image" value={formData.image} onChange={handleChange} />
                                <FormInput label="Address" name="address" value={formData.address} onChange={handleChange} />
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