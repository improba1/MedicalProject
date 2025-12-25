import React, { useState } from 'react';
import styles from './LoginForm.module.css';
import { FaUser } from "react-icons/fa";
import { RiLockPasswordFill } from "react-icons/ri";
import { Link } from 'react-router-dom';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import BackButton from '../../../Components/SecondBackButton/SecondBackButton';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../../../Api/all/authApi';


const LoginForm = () => {

    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError(''); 

        try {
            const data = await authApi.login(login, password);
            localStorage.setItem('access_token', data.access_token);
            localStorage.setItem('refresh_token', data.refresh_token);
            // localStorage.setItem('role', data.refresh_token);
            // localStorage.setItem('userId', data.refresh_token);

            if (data.role === 'DOCTOR') {
                navigate('/doc-home-page'); 
            } else if(data.role === 'PATIENT'){
                // navigate('/patient-home-page');
            }else if(data.role ==='ADMIN'){
                navigate('/admin');
            }

        } catch (err) {
            console.error("Login error:", err);
            setError('Wrong password or login');
        }
    };

    return(
            <AnimatedPage>
                <div className={styles.pageContainer}>
                    <div className={styles.wrapper}>


                        <form action="" onSubmit={handleLogin}> 


                                <div className={styles.header}>
                                    <BackButton className={styles.backBtn} />
                                    <h1 className={styles.title}>Sign In</h1>
                                </div>

                                    <div className={styles.inputBox}>



                                        <input required type="text" placeholder="Enter your login" value={login} onChange={(e) => setLogin(e.target.value)}></input>



                                        <FaUser className={styles.icon}/>
                                    </div>
                                    <div className={styles.inputBox}>



                                        <input required type="password" placeholder="Enter your password" value={password} onChange={(e) => setPassword(e.target.value)}></input>



                                        <RiLockPasswordFill className={styles.icon}/>
                                    </div>



                                {error && <div style={{color: 'red', marginTop: '10px', textAlign: 'center'}}>{error}</div>}



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

