import React from 'react';
import { useLocation } from 'react-router-dom';
import styles from './LabTestTicket.module.css';

const LabTestTicket = () => {
    const location = useLocation();
    const { doctorName, analysis, date } = location.state || {};

    const currentDate = date || "11/05/2026";
    const currentTime = "13:00";

    return (
        <div className={styles.container}>
            <div className={styles.ticket}>
                <div className={styles.row}>
                    <span className={styles.label}>Analysis:</span>
                    <span className={styles.value}>{analysis || "Blood Test"}</span>
                </div>
                <div className={styles.row}>
                    <span className={styles.label}>Date:</span>
                    <span className={styles.value}>{date || "11/05/2026"}</span>
                </div>
                <div className={styles.row}>
                    <span className={styles.label}>Time:</span>
                    <span className={styles.value}>{currentTime || "13:00"}</span>
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

export default LabTestTicket;
