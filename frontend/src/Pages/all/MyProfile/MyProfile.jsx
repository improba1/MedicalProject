import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import styles from './MyProfile.module.css';
import BackBtn from '../../../Components/BackButton/BackButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import Background from '../../../Components/Background/Background';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import { profileApi } from '../../../Api/all/profileApi';
import { medicalServiceApi } from '../../../Api/doctor/medicalServiceApi';

const MyProfile = () => {
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [role, setRole] = useState('');
    
    // --- State для услуг ---
    const [services, setServices] = useState([]); // Храним услуги отдельно
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingService, setEditingService] = useState(null);
    const [formData, setFormData] = useState({ name: '', description: '', price: '' });

    // Функция загрузки услуг (отдельно от профиля)
    const fetchServices = async (doctorId) => {
        try {
            const response = await medicalServiceApi.search(doctorId);
            
            // БЫЛО (Ошибка):
            // setServices(response.data || []); 

            // СТАЛО (Правильно):
            // response.data — это JSON от сервера. 
            // response.data.data — это массив услуг внутри JSON.
            setServices(response.data.data || []); 
            
        } catch (error) {
            console.error("Error loading services:", error);
            setServices([]); // На всякий случай сбрасываем в пустой массив при ошибке
        }
    };

    const fetchProfile = async () => {
        try {
            const userRole = localStorage.getItem('role');
            setRole(userRole);
            let response;

            if (userRole === 'PATIENT') {
                response = await profileApi.getPatientProfile();
            } else if (userRole === 'DOCTOR') {
                response = await profileApi.getDoctorProfile();
            } else if (userRole === 'ADMIN') {
                response = await profileApi.getAdminProfile();
            }
            
            if (response && response.data) {
                setProfile(response.data);
                if (userRole === 'DOCTOR') {
                    fetchServices(response.data.id);
                }
            }
        } catch (error) {
            console.error("Error loading profile:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProfile();
    }, []);

    const openAddModal = () => {
        setEditingService(null);
        setFormData({ name: '', description: '', price: '' });
        setIsModalOpen(true);
    };

    const openEditModal = (service) => {
        setEditingService(service);
        setFormData({ 
            name: service.name, 
            description: service.description, 
            price: service.price 
        });
        setIsModalOpen(true);
    };

    const handleDeleteService = async (id) => {
        if (window.confirm('Are you sure you want to delete this service?')) {
            try {
                await medicalServiceApi.delete(id);
                // Обновляем список услуг
                if (profile) fetchServices(profile.id);
            } catch (error) {
                console.error(error);
                alert('Failed to delete service');
            }
        }
    };

    const handleSaveService = async () => {
        try {
            const payload = {
                name: formData.name,
                description: formData.description,
                price: parseFloat(formData.price),
                active: true
            };

            if (editingService) {
                await medicalServiceApi.update(editingService.id, payload);
            } else {
                await medicalServiceApi.create(payload);
            }
            
            setIsModalOpen(false);
            // Обновляем список услуг
            if (profile) fetchServices(profile.id);
        } catch (error) {
            console.error(error);
            alert('Failed to save service');
        }
    };

    if (loading) return <Background><div className={styles.loading}>Loading...</div></Background>;
    if (!profile) return <Background><div className={styles.error}>Error loading profile</div></Background>;

    const imageUrl = profile.image?.downloadUrl;

    return (
        <Background>
            <div className={styles.topNav}>
                <div className={styles.navLeft}>
                    <BackBtn />
                    <HealthcareTxt />
                    <MyProfileBtn />
                </div>
                <LogOutBtn />
            </div>

            <div className={styles.wrapper}>
                <div className={styles.contentCard}>
                    <div className={styles.visualColumn}>
                        {role === 'DOCTOR' ? (
                            <div className={styles.photoContainer}>
                                {imageUrl ? (
                                    <img 
                                        src={imageUrl} 
                                        alt="Profile" 
                                        className={styles.profileImage}
                                        onError={(e) => { e.target.onerror = null; e.target.src = 'https://via.placeholder.com/200?text=No+Photo'; }}
                                    />
                                ) : (
                                    <div className={styles.placeholderImage}>No Photo</div>
                                )}
                            </div>
                        ) : (
                            <div className={styles.avatarPlaceholder}>
                                {profile.firstname?.[0]}{profile.lastname?.[0]}
                            </div>
                        )}
                        
                        <h2 className={styles.userName}>{profile.firstname} {profile.lastname}</h2>
                        <span className={styles.userRole}>{role}</span>
                        
                        {role === 'DOCTOR' && (
                            <div className={styles.ratingBadge}>
                                <span>⭐ {profile.rating || 0}</span>
                            </div>
                        )}
                    </div>

                    <div className={styles.detailsColumn}>
                        <div className={styles.detailsScrollArea}>
                            <SectionTitle title="Personal Information" />
                            <div className={styles.infoGrid}>
                                <InfoRow label="Login" value={profile.nickname} />
                                <InfoRow label="Email" value={profile.email} />
                                <InfoRow label="Phone" value={profile.phone} />
                                <InfoRow label="Address" value={profile.address} />
                                <InfoRow label="Birth Date" value={profile.birthDate} />
                                <InfoRow label="Age" value={profile.age} />
                                <InfoRow label="Sex" value={profile.sex} />
                            </div>

                            {role === 'DOCTOR' && (
                                <>
                                    <div className={styles.divider} />
                                    <SectionTitle title="Professional Details" />
                                    <div className={styles.infoGrid}>
                                        <InfoRow label="Specialization" value={profile.specialization} />
                                        <InfoRow label="Qualification" value={profile.qualification} />
                                        <InfoRow label="Experience" value={`${profile.experienceYears} years`} />
                                        <InfoRow label="Works here from" value={profile.startDate} />
                                    </div>

                                    <div className={styles.divider} />
                                    <SectionTitle title="Medical Services" />
                                    
                                    <div className={styles.servicesGrid}>
                                        {/* Используем state services вместо profile.medicalServices */}
                                        {services.map(service => (
    <div key={service.id} className={styles.serviceCard}>
        <div className={styles.serviceInfo}>
            <h4>{service.name}</h4>
            <p>{service.description}</p>
            <span className={styles.servicePrice}>${service.price}</span>
        </div>
        <div className={styles.serviceActions}>
            {/* Кнопка Редактировать */}
            <button onClick={() => openEditModal(service)} className={styles.iconBtn}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    <path d="M18.5 2.50001C18.8978 2.10219 19.4374 1.87869 20 1.87869C20.5626 1.87869 21.1022 2.10219 21.5 2.50001C21.8978 2.89784 22.1213 3.4374 22.1213 4.00001C22.1213 4.56262 21.8978 5.10219 21.5 5.50001L12 15L8 16L9 12L18.5 2.50001Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                </svg>
            </button>
            
            {/* Кнопка Удалить */}
            <button onClick={() => handleDeleteService(service.id)} className={`${styles.iconBtn} ${styles.deleteBtn}`}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M3 6H5H21" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    <path d="M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    <path d="M10 11V17" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    <path d="M14 11V17" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                </svg>
            </button>
        </div>
    </div>
))}
                                        
                                        <button onClick={openAddModal} className={styles.addServiceBtn}>
                                            + Add New Service
                                        </button>
                                    </div>
                                </>
                            )}
                        </div>
                        
                        <div className={styles.actionArea}>
                            <Link to="/edit-profile">
                                <button className={styles.editButton}>Edit Profile</button>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>

            {isModalOpen && (
                <div className={styles.modalOverlay}>
                    <div className={styles.modal}>
                        <h3>{editingService ? 'Edit Service' : 'New Service'}</h3>
                        
                        <div className={styles.inputGroup}>
                            <label>Service Name</label>
                            <input 
                                className={styles.modalInput}
                                value={formData.name}
                                onChange={e => setFormData({...formData, name: e.target.value})}
                                placeholder="e.g. Consultation"
                            />
                        </div>
                        
                        <div className={styles.inputGroup}>
                            <label>Description</label>
                            <input 
                                className={styles.modalInput}
                                value={formData.description}
                                onChange={e => setFormData({...formData, description: e.target.value})}
                                placeholder="Short description"
                            />
                        </div>

                        <div className={styles.inputGroup}>
                            <label>Price ($)</label>
                            <input 
                                type="number"
                                className={styles.modalInput}
                                value={formData.price}
                                onChange={e => setFormData({...formData, price: e.target.value})}
                                placeholder="0.00"
                            />
                        </div>

                        <div className={styles.modalActions}>
                            <button onClick={() => setIsModalOpen(false)} className={styles.cancelBtn}>Cancel</button>
                            <button onClick={handleSaveService} className={styles.saveBtn}>Save</button>
                        </div>
                    </div>
                </div>
            )}
        </Background>
    );
};

const SectionTitle = ({ title }) => (
    <h3 className={styles.sectionTitle}>{title}</h3>
);

const InfoRow = ({ label, value }) => (
    <div className={styles.infoRow}>
        <span className={styles.label}>{label}:</span>
        <span className={styles.value}>{value || '-'}</span>
    </div>
);

export default MyProfile;