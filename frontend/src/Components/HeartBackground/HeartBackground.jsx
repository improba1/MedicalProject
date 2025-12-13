import React from 'react';
import styles from './HeartBackground.module.css';
import heartImg from '../../Assets/icons/heartIcon.svg'; 

const HeartBackground = () => {
    return (
        <div className={styles.container}>
            <img 
                src={heartImg} 
                alt="" 
                className={styles.heart} 
            />
        </div>
        
    );
};

export default HeartBackground;