import React from 'react';
import styles from './LogOutButton.module.css';
import logoutIcon from '../../Assets/icons/logOutIcon/logoutIcon.svg';

const LogOutButton = () => {
    const handleClick = () => {

    };

    return (
        <button className={styles.button} onClick={handleClick} type='button'>
            <span className={styles.text}>Log Out</span>
            <img src={logoutIcon} alt="logout icon" className={styles.icon}/>
        </button>
    );
};

export default LogOutButton;