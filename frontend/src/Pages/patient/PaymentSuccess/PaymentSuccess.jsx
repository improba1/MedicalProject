import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './PaymentSuccess.module.css'; // Используй стили ниже
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';

const PaymentSuccess = () => {
    const navigate = useNavigate();

    return (
        <AnimatedPage>
            <div className={styles.container}>
                <div className={styles.card}>
                    <div className={styles.iconWrapper}>
                        <svg width="60" height="60" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M20 6L9 17L4 12" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"/>
                        </svg>
                    </div>
                    <h1 className={styles.title}>Payment Successful!</h1>
                    <p className={styles.text}>
                        Your appointment has been confirmed. <br/>
                        You can view the details in your dashboard.
                    </p>
                    
                    <button className={styles.homeBtn} onClick={() => navigate('/patient')}>
                        Back to Home
                    </button>
                </div>
            </div>
        </AnimatedPage>
    );
};

export default PaymentSuccess;