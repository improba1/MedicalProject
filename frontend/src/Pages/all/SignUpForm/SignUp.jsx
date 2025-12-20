import React, { useState } from 'react';
import styles from './SignUp.module.css';
import { Link } from 'react-router-dom';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import BackButton from '../../../Components/BackButton/BackButton';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../../../Api/authApi';


const SignUpForm = () => {

    // 1. Состояние (State) для хранения того, что вводит пользователь
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastNAme] = useState('');
    const [age, setAge] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault(); // Чтобы страница не перезагружалась
        setError(''); // Очищаем старые ошибки

        try {
            // axios.post('куда', { данные })
            const data = await authApi.register(firstName, lastName, age, email, phone, login, password);
            //localStorage.setItem('token', data.token);
            //localStorage.setItem('role', data.role);

            if (data.role === 'DOCTOR') {
                 navigate('/login'); 
            } else {
                setError('Wrong input');
            }

        } catch (err) {
            console.error("Login error:", err);
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

                                    <div className={styles.inputBox}>
                                        <input required type="text" placeholder="First name" value={firstName} onChange={(e) => setFirstName(e.target.value)}></input>
                                    </div>
                                    <div className={styles.inputBox}>
                                        <input required type="text" placeholder="Last name" value={lastName} onChange={(e) => setLastNAme(e.target.value)}></input>
                                    </div>
                                    <div className={styles.inputBox}>
                                        <input required type="text" placeholder="Age" value={age} onChange={(e) => setAge(e.target.value)}></input>
                                    </div>
                                    <div className={styles.inputBox}>
                                        <input required type="text" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)}></input>
                                    </div>
                                    <div className={styles.inputBox}>
                                        <input required type="text" placeholder="Phone" value={phone} onChange={(e) => setPhone(e.target.value)}></input>
                                    </div>
                                    <div className={styles.inputBox}>
                                        <input required type="text" placeholder="Login" value={login} onChange={(e) => setLogin(e.target.value)}></input>
                                    </div>
                                    <div className={styles.inputBox}>
                                        <input required type="text" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)}></input>
                                    </div>

                                    {error && <div style={{color: 'red'}}></div>}

                                <button type="submit" className={styles.submitBtn}>Sign Up</button>

                                <div className={styles.loginLink}>
                                    <p>Already have an account? <Link to="/login" className={styles.link}>Login</Link></p>
                                </div>
                        </form>
                    </div>
                </div>
            </AnimatedPage>
    )

}

export default SignUpForm;

