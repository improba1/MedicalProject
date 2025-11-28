import React, { useState } from 'react';
import styles from './SignUp.module.css';
import { Link } from 'react-router-dom';
import AnimatedPage from '../../Components/AnimatedPage/AnimatedPage';
import BackButton from '../../Components/BackButton/BackButton';


const SignUpForm = () => {
    return(
            <AnimatedPage>
                <div className={styles.wrapper}>
                    <form action=""> 
                            <div className={styles.header}>
                                <BackButton className={styles.backBtn} />
                                <h1 className={styles.title}>Sign Up</h1>
                            </div>

                                <div className={styles.inputBox}>
                                    <input type="text" placeholder="First name"></input>
                                </div>
                                <div className={styles.inputBox}>
                                    <input type="text" placeholder="Last name"></input>
                                </div>
                                <div className={styles.inputBox}>
                                    <input type="text" placeholder="Age"></input>
                                </div>
                                <div className={styles.inputBox}>
                                    <input type="text" placeholder="Email"></input>
                                </div>
                                <div className={styles.inputBox}>
                                    <input type="text" placeholder="Phone"></input>
                                </div>
                                <div className={styles.inputBox}>
                                    <input type="text" placeholder="Login"></input>
                                </div>
                                <div className={styles.inputBox}>
                                    <input type="text" placeholder="Password"></input>
                                </div>

                            <button type="submit" className={styles.submitBtn}>Sign Up</button>

                            <div className={styles.loginLink}>
                                <p>Already have an account? <Link to="/login" className={styles.link}>Login</Link></p>
                            </div>
                    </form>
            </div>
            </AnimatedPage>
    )

}

export default SignUpForm;

