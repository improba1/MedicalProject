import React from 'react';
import { Link } from 'react-router-dom';
import styles from './ProfileInput.module.css';

const ProfileInput = (props) => {
    return (
        <div className={styles.input}>
            <span className={styles.text}>
                <strong>{props.valueName}</strong>: {props.value}
            </span>
        </div>
    );
};

export default ProfileInput;