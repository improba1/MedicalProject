import React from 'react';
import styles from './SecondMyProfileButton.module.css';
import { Link } from 'react-router-dom';
import logoutIcon from '../../Assets/icons/ProfileIcon.svg';

const SecondMyProfileButton = () => {
    return (
        <Link to="/my-profile" className={styles.button}>
            <img src={logoutIcon} alt="my profile icon" className={styles.icon}/>
        </Link>
    );
};

export default SecondMyProfileButton;