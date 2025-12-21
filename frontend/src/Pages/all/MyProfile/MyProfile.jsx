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
                // const userRole = localStorage.getItem('role');
                const userRole = 'PATIENT'; 
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
                setProfile(data); 

            } catch (error) {
                console.error("Error loading profile:", error);
            } finally {
                setLoading(false); 
            }
        };

        fetchProfile();
    }, []);

    if (loading) {
        return <Background><div style={{color: 'white', textAlign: 'center', marginTop: '20%'}}>Loading...</div></Background>;
    }

    if (!profile) {
        return <Background><div style={{color: 'white'}}>Error loading profile</div></Background>;
    }

    return(
            <Background>
                 <LogOutBtn/>
                 <BackBtn/>
                 <HealthcareTxt/>
                 <MyProfileBtn></MyProfileBtn>
                 <span className={styles.yourprofile}>Your profile</span>
                 <div className={styles.container}>
                                 
                                 <div className={styles.contentRow}>
                                     
                                     <div className={styles.infoCard}>
                                    
                                        <InfoRow value={profile.firstname} label="First name" />
                                        <InfoRow value={profile.lastname} label="Last name" />
                                        <InfoRow value={profile.nickname} label="Login" />
                                        <InfoRow value={profile.email} label="Email" />
                                        <InfoRow value={profile.phone} label="Phone" />
                                        <InfoRow value={profile.address} label="Address" />
                                        <InfoRow value={profile.birthDate} label="Birth day" />
                                        <InfoRow value={profile.age} label="Age" />
                                        <InfoRow value={profile.sex} label="Sex" />

                                        {role === 'DOCTOR' && profile.specialization && (
                                            <>
                                                <InfoRow value={profile.specialization} label="Specialization" />
                                                <InfoRow value={profile.qualification} label="Qualification" />
                                                <InfoRow value={profile.startDate} label="Start date" />
                                                <InfoRow value={profile.experienceYears} label="Experience years" />
                                                <InfoRow value={profile.rating} label="Rating" />
                                            </>
                                        )}
                                        {/* "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        "fileName": "string",
                                        "fileType": "string",
                                        "downloadUrl": "string",
                                        "doctorId": "3fa85f64-5717-4562-b3fc-2c963f66afa6" */}
                                     </div>
                                 </div>
                             </div>
                <Link to="/edit-profile">
                    <button className={styles.button}>
                        Edit profile
                    </button>
                </Link>
            </Background>
            

           
    )

}
const InfoRow = ({ label, value }) => (
    <div className={styles.infoRow}>
        <span className={styles.label}>{label}: </span>
        <span className={styles.value}>{value || '-'}</span>
    </div>
);

export default MyProfile