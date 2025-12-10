import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import LogOutBtn from '../../Components/LogOutButton/LogOutButton';
import styles from './MyProfile.module.css';
import BackBtn from '../../Components/BackButton/BackButton';
import ProfileInput from '../../Components/ProfileInput/ProfileInput';
import HealthcareTxt from '../../Components/HealthcareText/Healthcare';
import Background from '../../Components/Background/Background';


const MyProfile = () => {
    return(
            <Background>
                 <LogOutBtn/>
                 <BackBtn/>
                 <HealthcareTxt/>
                 <span className={styles.yourprofile}>Your profile</span>
                 <div className={styles.info}>
                    <ProfileInput value="Agnieszka" valueName="First name" />
                    <ProfileInput value="Sakowicz" valueName="Last name" />
                    <ProfileInput value="33" valueName="Age" />
                    <ProfileInput value="agnieszka@gmail.com" valueName="Email" />
                    <ProfileInput value="+48238498" valueName="Phone" />
                    <ProfileInput value="agnieszka123" valueName="Login" />
                 </div>
                 <button className={styles.button}>
                    Edit profile
                 </button>
            </Background>
            

           
    )

}

export default MyProfile