import React from 'react';
import styles from './LabTest.module.css';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import BackBtn from '../../../Components/BackButton/BackButton';
import { Link, useLocation } from 'react-router-dom';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const LabTest = () => {
    const location = useLocation();
    const { reportId } = location.state || {};

    const handleSave = () => {
        if (reportId) {
            localStorage.setItem(`labTestCreated_${reportId}`, 'true');
        }
    };

    return (
        <Background>
            <LogOutBtn></LogOutBtn>
            <HealthcareTxt></HealthcareTxt>
            <BackBtn></BackBtn>
            <MyProfileBtn></MyProfileBtn>
            <text className={styles.text}>Lab test referral</text>
            <div className={styles.placeholder}>
                <input className={styles.input} type="text" placeholder="Analysis"></input>
                <input className={styles.input} type="date"></input>
                <input className={styles.input} type="time"></input>
                <input className={styles.input} type="text" placeholder="Cabinet"></input>
                <input className={styles.input} type="text" placeholder="Doctor's name"></input>
            </div>
            <Link to={reportId ? `/new-raport/${reportId}` : "/new-raport"}>
                <button className={styles.button} onClick={handleSave}>Save</button>
            </Link>
        </Background>
    );
};

export default LabTest;