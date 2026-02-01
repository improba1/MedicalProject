import React from 'react';
import { useLocation } from 'react-router-dom';
import styles from './RefferalTicket.module.css';

const RefferalTicket = () => {
    const location = useLocation();
    const { doctorName, date } = location.state || {};

    const currentDate = date || "11/05/2026";
    const currentTime = "13:00";

    return (
        <div className={styles.container}>
            <div className={styles.ticket}>
                <div className={styles.row}>
                    <span className={styles.label}>Date:</span>
                    <span className={styles.value}>{date || "11/05/2026"}</span>
                </div>
                <div className={styles.row}>
                    <span className={styles.label}>Time:</span>
                    <span className={styles.value}>{currentTime}</span>
                </div>
                <div className={styles.row}>
                    <span className={styles.label}>Cabinet:</span>
                    <span className={styles.value}>302</span>
                </div>
                <div className={styles.row}>
                    <span className={styles.label}>Doctor's name:</span>
                    <span className={styles.value}>{doctorName || "Dr. House"}</span>
                </div>
            </div>
        </div>
    );
};

export default RefferalTicket;
