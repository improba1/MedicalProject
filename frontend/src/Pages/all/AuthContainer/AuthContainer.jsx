import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import styles from './Auth.module.css';
import SignUpForm from '../SignUpForm/SignUp'; 
import LoginForm from '../LoginForm/LoginForm';    
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';

const AuthContainer = () => {
    const location = useLocation();
    const initialIsLogin = location.pathname === '/login'; 
    const [isLoginView, setIsLoginView] = useState(initialIsLogin);

    const switchToLogin = () => setIsLoginView(true);
    const switchToSignUp = () => setIsLoginView(false);

    useEffect(() => {
        setIsLoginView(location.pathname === '/login');
    }, [location.pathname]);

    return (
            <div className={styles.pageBackground}>
                
                {/* 1. Вот наша стеклянная панель. Она статична и блюр тут будет работать отлично */}
                <div className={styles.glassPanelStatic}>
                    
                    {/* 2. А вот это контейнер, который анимирует появление контента */}
                    <div className={styles.simpleFadeContainer} key={isLoginView ? 'login' : 'signup'}>
                        {isLoginView ? (
                            <LoginForm onSwitch={switchToSignUp} />
                        ) : (
                            <SignUpForm onSwitch={switchToLogin} />
                        )}
                    </div>

                </div>
                
            </div>
    );
};

export default AuthContainer;