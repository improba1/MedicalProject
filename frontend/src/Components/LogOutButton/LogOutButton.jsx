import React from 'react';
import { Link } from 'react-router-dom';
import styles from './LogOutButton.module.css';
import logoutIcon from '../../Assets/icons/logoutIcon.svg';

const LogOutButton = () => {
    return (
        <Link to="/" className={styles.button}>
            <span className={styles.text}>Log Out</span>
            <img src={logoutIcon} alt="logout icon" className={styles.icon}/>
        </Link>
    );
};

export default LogOutButton;