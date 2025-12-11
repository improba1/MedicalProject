import React, { useState } from 'react';
import HealthcareTxt from '../../Components/HealthcareText/Healthcare';
import Background from '../../Components/Background/Background';
import BackBtn from '../../Components/BackButton/BackButton';
import LogOutBtn from '../../Components/LogOutButton/LogOutButton';
import styles from './MyPatients.module.css';
import PatientsList from '../../Components/PatientList/PatientList';

const MyPatients = () => {
    return (
        <Background>
            
            <BackBtn></BackBtn>
            <LogOutBtn></LogOutBtn>
            <HealthcareTxt></HealthcareTxt>
            <h1 className={styles.pageTitle}>My Patients</h1>
            <p className={styles.pageSubtitle}>Manage your patients and their records</p>
            
            <PatientsList />
            <div className={styles.filters}>
                <input 
                    type="text" 
                    placeholder="Search patients..." 
                    className={styles.searchInput}
                />
            </div>
        </Background>
    );
};

export default MyPatients;