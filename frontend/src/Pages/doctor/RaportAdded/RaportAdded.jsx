import React from 'react';
import styles from './RaportAdded.module.css';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import BackBtn from '../../../Components/BackButton/BackButton';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import { Link } from 'react-router-dom';

const RaportAdded = () => {
    return (
        <Background>
            <LogOutBtn></LogOutBtn>
            <HealthcareTxt></HealthcareTxt>
            <MyProfileBtn></MyProfileBtn>
            <BackBtn></BackBtn>

            <span className={styles.title}>Congratulations!</span>
            <span className={styles.subtitle}>The raport is successfully added!</span>
            <Link to="/doc-home-page">
                <button className={styles.button}>Go to home page</button>
            </Link>        
        </Background>
    );
};

export default RaportAdded;