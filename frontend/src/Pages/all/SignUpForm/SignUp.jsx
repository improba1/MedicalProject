import React, { useState, useEffect, useRef } from 'react';
import styles from './SignUp.module.css';
import { Link, useNavigate, useLocation } from 'react-router-dom';
// Твои импорты API и компонентов (оставил как было)
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import { authApi } from '../../../Api/all/authApi';
import { profileApi } from '../../../Api/all/profileApi';

const SignUpForm = ({ onSwitch }) => {
    // Состояния для 1-го шага
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const [firstname, setFirstName] = useState('');
    const [lastname, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [sex, setSex] = useState(''); 
    const [birthDate, setBirthDay] = useState('');
    const [address, setAddress] = useState('');
    
    // Состояния для 2-го шага
    const [nickname, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState(''); // Новое поле
    
    // Состояние шага и UI
    const [step, setStep] = useState(1);
    const [error, setError] = useState('');
    
    const navigate = useNavigate();
    const location = useLocation();

    const handleNextStep = () => {
        // Получаем все инпуты и селекты только из первого шага
        const inputs = step1Ref.current.querySelectorAll('input, select');
        let isValid = true;

        for (let input of inputs) {
            if (!input.checkValidity()) {
                input.reportValidity(); // Заставляет браузер показать подсказку "Please fill out this field"
                isValid = false;
                break; // Останавливаем цикл на первой же ошибке
            }
        }

        // Если все поля заполнены верно — переключаем шаг
        if (isValid) {
            setError('');
            setStep(2);
        }
    };
    const handlePrevStep = () => setStep(1);

    const dropdownRef = useRef(null);
    const step1Ref = useRef(null);

    // Хук для отслеживания клика вне элемента
    useEffect(() => {
        const handleClickOutside = (event) => {
            // Если меню открыто, и клик был НЕ по элементу с dropdownRef (и не по его детям)
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsDropdownOpen(false); // Закрываем меню
            }
        };

        // Вешаем слушатель событий при монтировании компонента
        document.addEventListener('mousedown', handleClickOutside);
        
        // Очищаем слушатель при размонтировании
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleRegister = async (e) => {
        e.preventDefault(); 
        setError(''); 

        if (password !== repeatPassword) {
            setError('Passwords do not match');
            return;
        }

        const userData = {
            firstname, lastname, email, phone, nickname, password, sex, birthDate, address
        };

        try {
            const data = await authApi.register(userData);
            localStorage.setItem('access_token', data.access_token);
            localStorage.setItem('refresh_token', data.refresh_token);
            localStorage.setItem('role', data.role);
            localStorage.setItem('userId', data.id);

            // ... твоя логика редиректа ...
            navigate('/patient'); // Для примера сократил
        } catch (err) {
            setError('Wrong input or user already exists');
        }
    };

    return(
        <div className={styles.glassPanel}>
            <form onSubmit={handleRegister}>
                
                {/* ВЕРНУЛИ ЗАГОЛОВОК */}
                <div className={styles.header}>
                    <div>
                        <h1 className={styles.title}>Create an account</h1>
                        <p className={styles.subtitle}>Your journey to better health starts here.</p>
                    </div>
                </div>

                {/* ВЕРНУЛИ ОКНО СЛАЙДЕРА */}
                <div className={styles.sliderWindow}>
                    <div className={`${styles.sliderTrack} ${step === 2 ? styles.step2Active : ''}`}>
                        
                        {/* ШАГ 1 */}
                        <div className={styles.stepPane} ref={step1Ref}>
                            <div className={styles.inputsGrid}>
                                <input required type="text" placeholder="First name" value={firstname} onChange={(e) => setFirstName(e.target.value)} />
                                <input required type="text" placeholder="Birthday" onFocus={(e) => e.target.type = 'date'} onBlur={(e) => { if (!e.target.value) e.target.type = 'text'; }} value={birthDate} onChange={(e) => setBirthDay(e.target.value)} />
                                <input required type="text" placeholder="Last name" value={lastname} onChange={(e) => setLastName(e.target.value)} />
                                <input required type="text" placeholder="Address" value={address} onChange={(e) => setAddress(e.target.value)} />
                                <input required type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
                                
                                <div className={styles.customSelectContainer} ref={dropdownRef}>
                                    <div className={`${styles.customSelectInput} ${isDropdownOpen ? styles.active : ''}`} onClick={() => setIsDropdownOpen(!isDropdownOpen)} style={{ color: sex ? '#333' : 'grey' }}>
                                        {sex === 'MALE' ? 'Male' : sex === 'FEMALE' ? 'Female' : 'Select gender'}
                                        <span className={`${styles.arrow} ${isDropdownOpen ? styles.arrowOpen : ''}`}></span>
                                    </div>
                                    {isDropdownOpen && (
                                        <div className={styles.customOptionsContainer}>
                                            <div className={styles.customOption} onClick={() => { setSex('MALE'); setIsDropdownOpen(false); }}>Male</div>
                                            <div className={styles.customOption} onClick={() => { setSex('FEMALE'); setIsDropdownOpen(false); }}>Female</div>
                                        </div>
                                    )}
                                </div>
                            </div>
                            <div className={styles.inputsColumn}>
                                <input required type="tel" placeholder="Phone" value={phone} onChange={(e) => setPhone(e.target.value)} />
                            </div>
                            
                            <div className={styles.actionsBox}>
                                <button type="button" className={styles.primaryBtn} onClick={handleNextStep}>Next step</button>
                                <button type="button" className={styles.arrowBtn} onClick={handleNextStep}>›</button>
                            </div>
                        </div>

                        {/* ШАГ 2 */}
                        <div className={styles.stepPane}>
                            <div className={styles.inputsColumn}>
                                <input required type="text" placeholder="Login" value={nickname} onChange={(e) => setLogin(e.target.value)} />
                                <input required type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                                <input required type="password" placeholder="Repeat password" value={repeatPassword} onChange={(e) => setRepeatPassword(e.target.value)} />
                            </div>
                            <div className={styles.actionsBoxReverse}>
                                <button type="button" className={styles.arrowBtn} onClick={handlePrevStep}>‹</button>
                                <button type="submit" className={styles.primaryBtn}>Create account</button>
                            </div>
                        </div>

                    </div>
                </div>

                {error && <div className={styles.errorText}>{error}</div>}

                <div className={styles.loginLink}>
                   <p>Already have an account? <span className={styles.switchLink} onClick={onSwitch}>Login</span></p>
               </div>
           </form>
       </div>
    )
}

export default SignUpForm;