import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import backgroud from '../../Assets/background2.png';
import LogOutBtn from '../../Components/LogOutButton/LogOutButton';
import styles from './MyProfile.module.css';
import BackBtn from '../../Components/BackButton/BackButton';
import ProfileInput from '../../Components/ProfileInput/ProfileInput';

const MyProfile = () => {
    return(
            <div alt="background" className={styles.background}>
                 <LogOutBtn/>
                 <BackBtn/>
                 <span className={styles.healthcare}>Healthcare</span>
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
            </div>
            

           
    )

}

export default MyProfile