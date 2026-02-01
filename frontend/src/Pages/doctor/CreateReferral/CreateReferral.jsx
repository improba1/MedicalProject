import React from 'react';
import styles from './CreateReferral.module.css';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import BackBtn from '../../../Components/BackButton/BackButton';
import { Link, useLocation } from 'react-router-dom';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const CreateReferral = () => {
    const location = useLocation();
    const { reportId } = location.state || {};

    const handleSave = () => {
        if (reportId) {
            localStorage.setItem(`referralCreated_${reportId}`, 'true');
        }
    };

    return (
        <Background>
            <LogOutBtn></LogOutBtn>
            <HealthcareTxt></HealthcareTxt>
            <BackBtn></BackBtn>
            <MyProfileBtn></MyProfileBtn>
            <text className={styles.text}>Referral to a specialist</text>
            <div className={styles.placeholder}>
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

export default CreateReferral;