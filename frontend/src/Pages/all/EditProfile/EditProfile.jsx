import React from 'react';
import styles from './EditProfile.module.css';
import Background from '../../../Components/Background/Background';
import BackBtn from '../../../Components/BackButton/BackButton';
import { Link } from 'react-router-dom';

const EditProfile = () => {
    return (
        <Background>
            <BackBtn></BackBtn>
            <span className={styles.title}>Edit profile</span>
            <div className={styles.placeholder1}>
                <input className={styles.input} type="text" placeholder="First name"></input>
                <input className={styles.input} type="text" placeholder="Last name"></input>
                <input className={styles.input} type="text" placeholder="Specialization"></input>
                <input className={styles.input} type="text" placeholder="Experience years"></input>
            </div>
            <div className={styles.placeholder2}>
                <input className={styles.input} type="email" placeholder="Email"></input>
                <input className={styles.input} type="phone" placeholder="Phone"></input>
                <input className={styles.input} type="text" placeholder="Login"></input>
                <input className={styles.input} type="password" placeholder="Password"></input>
            </div>
            <Link to="/my-profile">
                <button className={styles.button}>Save</button>
            </Link>
        </Background>
    );
};

export default EditProfile;