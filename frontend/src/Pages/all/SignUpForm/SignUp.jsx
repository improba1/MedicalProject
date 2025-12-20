import React, { useState } from 'react';
import styles from './SignUp.module.css';
import { Link } from 'react-router-dom';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import BackButton from '../../../Components/BackButton/BackButton';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../../../Api/authApi';


const SignUpForm = () => {

    const [firstname, setFirstName] = useState('');
    const [lastname, setLastName] = useState('');
    const [age, setAge] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [nickname, setLogin] = useState(''); // Это username
    const [password, setPassword] = useState('');
    
    const [sex, setSex] = useState('MALE'); 
    const [birthDate, setBirthDay] = useState('2000-01-01');
    const [address, setAddress] = useState('Lublin');
    
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault(); 
        setError(''); 

        const userData = {
            firstname: firstname, 
            lastname: lastname,
            age: parseInt(age),    
            email: email,
            phone: phone,         
            nickname: nickname,    
            password: password,
            sex: sex,             
            birthDate: birthDate,  
            address: address
        };

        try {
            console.log("Отправляем данные:", userData);
            const data = await authApi.register(userData);
            navigate('/login'); 

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
                                        <input required type="text" placeholder="First name" value={firstname} onChange={(e) => setFirstName(e.target.value)}></input>
                                    </div>
                                    <div className={styles.inputBox}>
                                        <input required type="text" placeholder="Last name" value={lastname} onChange={(e) => setLastName(e.target.value)}></input>
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
                                        <input required type="text" placeholder="Login" value={nickname} onChange={(e) => setLogin(e.target.value)}></input>
                                    </div>
                                    <div className={styles.inputBox}>
                                        <input required type="text" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)}></input>
                                    </div>

                                    {error && <div style={{color: 'red', marginTop: '10px', textAlign: 'center'}}>{error}</div>}

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

