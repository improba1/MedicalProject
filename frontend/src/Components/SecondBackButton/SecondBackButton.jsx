import React from 'react';
import { useNavigate } from 'react-router-dom';
import { IoIosArrowBack } from "react-icons/io";
import styles from './SecondBackButton.module.css';

const SecondBackButton = ({ className = '' }) => {
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1);
    };

    return (
        <button 
            type="button"
            onClick={goBack}
            className={`${styles.button} ${className}`}
        >
            <IoIosArrowBack size={40} style={{ color: 'inherit' }} /> 
        </button>
    );
};

export default SecondBackButton;