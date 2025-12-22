import React from 'react';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom'; 
import styles from './LogOutButton.module.css';
import logoutIcon from '../../Assets/icons/logoutIcon.svg';
import { authApi } from '../../Api/all/authApi';

const LogOutButton = () => {

    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await authApi.logout(); 
            console.log("Logged out from server");

        } catch (error) {
            console.error("Logout error (server side):", error);
        } finally {
            localStorage.removeItem('access_token');
            localStorage.removeItem('refresh_token');
            localStorage.removeItem('role');
            localStorage.removeItem('userId');
            navigate('/');
        }
    };


    return (
        <div className={styles.button} onClick={handleLogout}>
            <span className={styles.text}>Log Out</span>
            <img src={logoutIcon} alt="logout icon" className={styles.icon}/>
        </div>
    );
};

export default LogOutButton;