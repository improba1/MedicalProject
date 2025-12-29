import React, { useState } from 'react';
import styles from './AddDoctor.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithoutProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { useNavigate } from 'react-router-dom';

const AddDoctor = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [specialization, setSpecialization] = useState('');
    const [experience, setExperience] = useState('');
    
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        const newDoctor = {
            firstName, lastName, specialization, experience, email, phone, login, password
        };
        console.log(newDoctor);
        navigate('/admin'); 
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithoutProfile />

                <main className={styles.mainContent}>
                    <h1 className={styles.title}>New doctor</h1>

                    <form onSubmit={handleSubmit} className={styles.formWrapper}>
                        <div className={styles.inputsContainer}>
                            <div className={styles.column}>
                                <input 
                                    className={styles.input} 
                                    type="text" 
                                    placeholder="first name"
                                    value={firstName}
                                    onChange={(e) => setFirstName(e.target.value)}
                                    required
                                />
                                <input 
                                    className={styles.input} 
                                    type="text" 
                                    placeholder="last name"
                                    value={lastName}
                                    onChange={(e) => setLastName(e.target.value)}
                                    required
                                />
                                <input 
                                    className={styles.input} 
                                    type="text" 
                                    placeholder="specialization"
                                    value={specialization}
                                    onChange={(e) => setSpecialization(e.target.value)}
                                    required
                                />
                                <input 
                                    className={styles.input} 
                                    type="number" 
                                    placeholder="experience years"
                                    value={experience}
                                    onChange={(e) => setExperience(e.target.value)}
                                    required
                                />
                            </div>

                            <div className={styles.column}>
                                <input 
                                    className={styles.input} 
                                    type="email" 
                                    placeholder="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                                <input 
                                    className={styles.input} 
                                    type="tel" 
                                    placeholder="phone"
                                    value={phone}
                                    onChange={(e) => setPhone(e.target.value)}
                                    required
                                />
                                <input 
                                    className={styles.input} 
                                    type="text" 
                                    placeholder="login"
                                    value={login}
                                    onChange={(e) => setLogin(e.target.value)}
                                    required
                                />
                                <input 
                                    className={styles.input} 
                                    type="password" 
                                    placeholder="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                            </div>
                        </div>

                        <button type="submit" className={styles.createBtn}>
                            Create
                        </button>
                    </form>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default AddDoctor;