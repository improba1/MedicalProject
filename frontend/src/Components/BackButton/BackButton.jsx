import React from 'react';
import { useNavigate } from 'react-router-dom';
import { IoIosArrowBack } from "react-icons/io";
import styles from './BackButton.module.css';

const BackButton = ({ className = '' }) => {
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1);
    };

    return (
        <button 
            type="button"
            onClick={goBack}
            // 2. ВАЖНО: Склеиваем стандартный класс кнопки + внешний класс для позиционирования
            // Если className не передать, будет просто styles.button
            className={`${styles.button} ${className}`}
        >
            {/* 3. Убрали color="white", теперь цвет берется из CSS (color: inherit) */}
            <IoIosArrowBack size={40} style={{ color: 'inherit' }} /> 
        </button>
    );
};

export default BackButton;