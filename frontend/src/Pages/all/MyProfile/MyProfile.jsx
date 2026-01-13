import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import styles from './MyProfile.module.css';
import BackBtn from '../../../Components/BackButton/BackButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import Background from '../../../Components/Background/Background';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import { profileApi } from '../../../Api/all/profileApi';

const MyProfile = () => {
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [role, setRole] = useState('');

    useEffect(() => {
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
                
                if (response) {
                    setProfile(response.data);
                }
            } catch (error) {
                console.error("Error loading profile:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, []);

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

                            {role === 'DOCTOR' && profile.specialization && (
                                <>
                                    <div className={styles.divider} />
                                    <SectionTitle title="Professional Details" />
                                    <div className={styles.infoGrid}>
                                        <InfoRow label="Specialization" value={profile.specialization} />
                                        <InfoRow label="Qualification" value={profile.qualification} />
                                        <InfoRow label="Experience" value={`${profile.experienceYears} years`} />
                                        <InfoRow label="Works here from" value={profile.startDate} />
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