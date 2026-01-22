import React, { useState } from 'react';
import styles from './SignUp.module.css';
import { Link } from 'react-router-dom';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import BackButton from '../../../Components/BackButton/BackButton';
import { useNavigate, useLocation } from 'react-router-dom';
import { authApi } from '../../../Api/all/authApi';
import { profileApi } from '../../../Api/all/profileApi';


const SignUpForm = () => {

    const [firstname, setFirstName] = useState('');
    const [lastname, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [nickname, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [sex, setSex] = useState(''); 
    const [birthDate, setBirthDay] = useState('');
    const [address, setAddress] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const location = useLocation();

    const handleRegister = async (e) => {
        e.preventDefault(); 
        setError(''); 

        const userData = {
            firstname: firstname, 
            lastname: lastname,
            email: email,
            phone: phone,         
            nickname: nickname,    
            password: password,
            sex: sex,             
            birthDate: birthDate,  
            address: address
        };

        try {
            const data = await authApi.register(userData);
            localStorage.setItem('access_token', data.access_token);
            localStorage.setItem('refresh_token', data.refresh_token);
            localStorage.setItem('role', data.role);
            localStorage.setItem('userId', data.id);

            const origin = location.state?.from;
            const savedDoctorData = location.state?.doctorData;

            if (origin && savedDoctorData && data.role === 'PATIENT') {
            navigate(origin, { state: { doctorData: savedDoctorData } });
            return;
            } 

            let profileData;

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

        } catch (err) {
            setError('Wrong input');
        }
    };

    return(
            <AnimatedPage>
                <div className={styles.pageContainer}>
                    <div className={styles.wrapper}>
                        <form action="" onSubmit={handleRegister}> 
                                <div className={styles.header}>
                                    <BackButton className={styles.backBtn} />
                                    <h1 className={styles.title}>Sign Up</h1>
                                </div>

                                   <div className={styles.inputsContainer}>
                                        <div className={styles.inputBox}>
                                            <input required type="text" placeholder="First name" value={firstname} onChange={(e) => setFirstName(e.target.value)} />
                                        </div>
                                        <div className={styles.inputBox}>
                                            <input required type="text" placeholder="Last name" value={lastname} onChange={(e) => setLastName(e.target.value)} />
                                        </div>

                                        <div className={styles.inputBox}>
                                            <input required type="text" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
                                        </div>
                                        <div className={styles.inputBox}>
                                            <input required type="text" placeholder="Phone" value={phone} onChange={(e) => setPhone(e.target.value)} />
                                        </div>

                                        <div className={styles.inputBox}>
                                            <select 
                                                required 
                                                value={sex} 
                                                onChange={(e) => setSex(e.target.value)}
                                                style={{ color: sex ? 'white' : 'grey' }}
                                            >
                                                <option value="" disabled>Select Gender</option>
                                                <option value="MALE">Male</option>
                                                <option value="FEMALE">Female</option>
                                            </select>
                                        </div>
                                        <div className={styles.inputBox}>
                                            <input required 
                                                type={(birthDate || document.activeElement === document.getElementById('dateInput')) ? "date" : "text"}
                                                id="dateInput" 
                                                placeholder="Birthday" 
                                                value={birthDate} 
                                                onChange={(e) => setBirthDay(e.target.value)}
                                                onFocus={(e) => e.target.type = 'date'}
                                                onBlur={(e) => { if (!e.target.value) e.target.type = 'text'; }}
                                                style={{ color: 'white' }} 
                                            />
                                        </div>

                                        <div className={styles.inputBox}>
                                            <input required type="text" placeholder="Login" value={nickname} onChange={(e) => setLogin(e.target.value)} />
                                        </div>
                                        <div className={styles.inputBox}>
                                            <input required type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                                        </div>

                                        <div className={`${styles.inputBox} ${styles.fullWidth}`}>
                                            <input required type="text" placeholder="Address" value={address} onChange={(e) => setAddress(e.target.value)} />
                                        </div>
                                    </div>
                                    {error && <div style={{color: 'red', marginTop: '10px', textAlign: 'center'}}>{error}</div>}
                                <button type="submit" className={styles.submitBtn}>Sign Up</button>
                                <div className={styles.loginLink}>
                                    <p>Already have an account? 
                                        <Link 
                                            to="/login" 
                                            className={styles.link} 
                                            state={location.state}
                                        >
                                            Login
                                        </Link>
                                    </p>
                                </div>
                        </form>
                    </div>
                </div>
            </AnimatedPage>
    )

}

export default SignUpForm;

