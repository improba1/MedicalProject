import React from 'react';
import styles from './HeaderWithProfile.module.css';

import HealthcareTxt from '../SecondHealthcareText/SecondHealthcareText';
import LogOutBtn from '../SecondLogOutButton/SecondLogOutButton';
import MyProfileBtn from '../SecondMyProfileButton/SecondMyProfileButton';


const HeaderWithProfile = () => {
    return (
        <header className={styles.header}>
            <div className={styles.leftGroup}>
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