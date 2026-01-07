import React from 'react';
import styles from './Healthcare.module.css';
import { Link } from 'react-router-dom';

const Healthcare = () => {
    let link;
    if (localStorage.role === 'DOCTOR'){
        link = '/doc-home-page';
    }else if (localStorage.role === 'PATIENT'){
        link = '/patient';
    }else if(localStorage.role === 'ADMIN'){
        link = '/admin';
    }
    return (
        <Link to={link}>
            <span className={styles.healthcare}>Healthcare</span>
        </Link>
    );
};

export default Healthcare;