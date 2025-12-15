import React from 'react';
import styles from './HeaderWithProfile.module.css';

import BackBtn from '../BackButton/BackButton'; 
import HealthcareTxt from '../HealthcareText/Healthcare';
import LogOutBtn from '../LogOutButton/LogOutButton';
import MyProfileBtn from '../MyProfileButton/MyProfileButton';


const HeaderWithProfile = () => {
    return (
        <header className={styles.header}>
            <div className={styles.leftGroup}>
                <BackBtn />
                <HealthcareTxt />
            </div>
            <div className={styles.rightGroup}>
                <MyProfileBtn />
                <LogOutBtn />
            </div>
        </header>
    );
};

export default HeaderWithProfile;