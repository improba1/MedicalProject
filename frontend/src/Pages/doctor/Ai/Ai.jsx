import React from 'react';
import styles from './Ai.module.css';
import Background from '../../../Components/Background/Background';
import BackBtn from '../../../Components/BackButton/BackButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const Ai = () => {
    return (
        <Background>
            <BackBtn></BackBtn>
            <HealthcareTxt></HealthcareTxt>
            <LogOutBtn></LogOutBtn>
            <MyProfileBtn></MyProfileBtn>
            <text className={styles.title}>Make diagnosis with AI</text>
            <div className={styles.placeholder}>
                <input className={styles.input1} type="text" placeholder="Write symptoms"></input>
                <button className={styles.button}>Generate</button>
                <input className={styles.input2} type="text" placeholder="Possible diagnosis is..."></input>
            </div>
        </Background>
    );
};

export default Ai;