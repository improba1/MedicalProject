import React from 'react';
import styles from './HeaderWithoutProfile.module.css';

import BackBtn from '../SecondBackButton/SecondBackButton'; 
import HealthcareTxt from '../SecondHealthcareText/SecondHealthcareText';
import LogOutBtn from '../SecondLogOutButton/SecondLogOutButton';

const HeaderWithoutProfile = () => {
    return (
        <header className={styles.header}>
            <div className={styles.leftGroup}>
                <BackBtn />
                <HealthcareTxt />
            </div>
            <div className={styles.rightGroup}>
                <LogOutBtn />
            </div>
        </header>
    );
};

export default HeaderWithoutProfile;