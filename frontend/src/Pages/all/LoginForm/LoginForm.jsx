import React, { useState } from 'react';
import styles from './LoginForm.module.css';
import { FaUser } from "react-icons/fa";
import { RiLockPasswordFill } from "react-icons/ri";
import { Link } from 'react-router-dom';
import AnimatedPage from '../../Components/AnimatedPage/AnimatedPage';
import BackButton from '../../Components/BackButton/BackButton';


const LoginForm = () => {
    return(
            <AnimatedPage>
                <div className={styles.pageContainer}>
                    <div className={styles.wrapper}>
                        <form action=""> 
                                <div className={styles.header}>
                                    <BackButton className={styles.backBtn} />
                                    <h1 className={styles.title}>Sign In</h1>
                                </div>

                                    <div className={styles.inputBox}>
                                        <input type="text" placeholder="Enter your login"></input>
                                        <FaUser className={styles.icon}/>
                                    </div>
                                    <div className={styles.inputBox}>
                                        <input type="text" placeholder="Enter your password"></input>
                                        <RiLockPasswordFill className={styles.icon}/>
                                    </div>

                                <button type="submit" className={styles.submitBtn}>Sign In</button>

                                <div className={styles.registerLink}>
                                    <p>Don't have an account? <Link className={styles.transLink} to="/signUpForm">Sign Up</Link></p>
                                </div>
                        </form>
                    </div>
                </div>
            </AnimatedPage>
    )

}

export default LoginForm

