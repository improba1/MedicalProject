import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import styles from './DoctorHomePage.module.css';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import Background from '../../../Components/Background/Background';

const MyProfile = (props) => {
    return(
            <Background>
                 <LogOutBtn/>
                 <MyProfileBtn/>
                 <HealthcareTxt/>
                 <span className={styles.welcomeback}>Welcome back, <span className={styles.dr}>{props.name}!</span></span>
                 <span className={styles.txt}>Your dashboard is centralized for optimal workflow. Review your daily schedule, access patient histories, and complete visit reports.</span>
                 <div className={styles.newraportbtnbackground}>
                    <Link to="/new-raport">
                        <button className={styles.newraportbtn}>New raport</button>
                    </Link>
                 </div>
                 <div className={styles.buttonsbackgound}>
                    <Link to="/manage-schedule">
                        <button className={styles.button1}>Manage schedule</button>
                    </Link>
                    <Link to="/my-patients">
                        <button className={styles.button2}>My patients</button>
                    </Link>
                 </div>
            </Background>
           
    )

}

export default MyProfile