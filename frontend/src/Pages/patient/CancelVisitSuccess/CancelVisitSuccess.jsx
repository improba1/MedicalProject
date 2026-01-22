import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './CancelVisitSuccess.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';

const CancelVisitSuccess = () => {
    const navigate = useNavigate();

    return (
        <AnimatedPage>
            <div className={styles.container}>
                <div className={styles.card}>
                    <div className={styles.iconWrapper}>
                        <svg width="50" height="50" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M18 6L6 18M6 6L18 18" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round" />
                        </svg>
                    </div>
                    <h1 className={styles.title}>Visit Cancelled</h1>
                    <p className={styles.text}>
                        Your appointment has been successfully cancelled. <br />
                        The money will be refunded within a week.
                    </p>

                    <button className={styles.homeBtn} onClick={() => navigate('/patient')}>
                        Back to Home
                    </button>
                </div>
            </div>
        </AnimatedPage>
    );
};

export default CancelVisitSuccess;
