import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import LogOutBtn from '../../Components/LogOutButton/LogOutButton';
import styles from './MyProfile.module.css';
import BackBtn from '../../Components/BackButton/BackButton';
import HealthcareTxt from '../../Components/HealthcareText/Healthcare';
import Background from '../../Components/Background/Background';
import MyProfileBtn from '../../Components/MyProfileButton/MyProfileButton';


const MyProfile = () => {
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
                                       <InfoRow value="Agnieszka" label="First name" />
                                       <InfoRow value="Sakowicz" label="Last name" />
                                       <InfoRow value="33" label="Age" />
                                       <InfoRow value="agnieszka@gmail.com" label="Email" />
                                       <InfoRow value="+48238498" label="Phone" />
                                       <InfoRow value="agnieszka123" label="Login" />
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
        <span className={styles.value}>{value}</span>
    </div>
);

export default MyProfile