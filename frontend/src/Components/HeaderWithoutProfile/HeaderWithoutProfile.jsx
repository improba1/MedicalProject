import React from 'react';
import styles from './HeaderWithoutProfile.module.css';

import BackBtn from '../BackButton/BackButton'; 
import HealthcareTxt from '../HealthcareText/Healthcare';
import LogOutBtn from '../LogOutButton/LogOutButton';

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