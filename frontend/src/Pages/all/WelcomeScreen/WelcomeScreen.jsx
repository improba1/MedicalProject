import React from 'react';
import { Link } from 'react-router-dom';
import styles from './WelcomeScreen.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import icon from '../../Assets/applicationIcon.png';


const WelcomeScreen = () => {
    return(
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <div className={styles.wrapper}>
                    
                    <div className={styles.logoContainer}>
                        <img src={icon} alt="Healthcare Logo" className={styles.logo} />
                    </div>

                    <div className={styles.textBlock}>
                        <h1 className={styles.title}>Let's get started!</h1>
                        <p className={styles.subtitle}>Login to Stay healthy and fit</p>
                    </div>

                    <div className={styles.buttonGroup}>
                        <Link to="/login" className={styles.signInBtn}>
                            Sign In
                        </Link>
                        
                        <Link to="/signUpForm" className={styles.signUpBtn}>
                            Sign Up
                        </Link>
                    </div>

                </div>
            </div>
        </AnimatedPage>
    )
}

export default WelcomeScreen;