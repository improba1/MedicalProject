import React from 'react';
import styles from './LabTest.module.css';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import BackBtn from '../../../Components/BackButton/BackButton';
import { Link } from 'react-router-dom';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const LabTest = () => {
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
                <Link to="/new-raport">
                    <button className={styles.button}>Save</button>
                </Link>
            </Background>
    );
};

export default LabTest;