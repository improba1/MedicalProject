import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import styles from './LoginForm.module.css';
import { authApi } from '../../../Api/all/authApi';
import { profileApi } from '../../../Api/all/profileApi';

const LoginForm = ({ onSwitch }) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    
    const navigate = useNavigate();
    const location = useLocation();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError(''); 
        try {
            const data = await authApi.login(login, password);
            localStorage.setItem('access_token', data.access_token);
            localStorage.setItem('refresh_token', data.refresh_token);
            localStorage.setItem('role', data.role);
            localStorage.setItem('userId', data.id);

            let profileData;
            const origin = location.state?.from;
            const savedDoctorData = location.state?.doctorData;
            
            if (origin && savedDoctorData && data.role === 'PATIENT') {
                navigate(origin, { state: { doctorData: savedDoctorData } });
            } else {
                if (data.role === 'DOCTOR') {
                    const profileRes = await profileApi.getDoctorProfile();
                    profileData = profileRes.data;
                    localStorage.setItem('userName', profileData.firstname);
                    navigate('/doc-home-page')
                } else if (data.role === 'PATIENT') {
                    const profileRes = await profileApi.getPatientProfile();
                    profileData = profileRes.data;
                    localStorage.setItem('userName', profileData.firstname);
                    navigate('/patient');
                } else if (data.role === 'ADMIN') {
                    const profileRes = await profileApi.getAdminProfile();
                    profileData = profileRes.data;
                    localStorage.setItem('userName', profileData.firstname);
                    navigate('/admin');
                } 
            }
        } catch (err) {
            console.error("Login error:", err);
            setError('Wrong password or login');
        }
    };

    return(
        <div className={styles.glassPanel}> 
            <form onSubmit={handleLogin}> 
                
                <div className={styles.header}>
                    {/* Убрали BackButton отсюда */}
                    <h1 className={styles.title}>Welcome back</h1>
                    <p className={styles.subtitle}>Your journey to better health starts here.</p>
                </div>

                <div className={styles.inputsColumn}>
                    <div className={styles.inputBox}>
                        <input required type="text" placeholder="Enter your login" value={login} onChange={(e) => setLogin(e.target.value)} />
                        
                    </div>
                    
                    <div className={styles.inputBox}>
                        <input required type="password" placeholder="Enter your password" value={password} onChange={(e) => setPassword(e.target.value)} />
                        
                    </div>
                </div>

                {error && <div className={styles.errorText}>{error}</div>}
                
                <button type="submit" className={styles.submitBtn}>Log In</button>

                <div className={styles.switchLinkContainer}>
                    <p>Don't have an account? 
                        <span className={styles.switchLink} onClick={onSwitch}>
                            Sign Up
                        </span>
                    </p>
                </div>
            </form>
        </div>
    )
}

export default LoginForm;