import React from 'react';
import { Link } from 'react-router-dom';
import styles from './NewRaport.module.css';
import Background from '../../Components/Background/Background';
import LogOutBtn from '../../Components/LogOutButton/LogOutButton';
import HealthcareTxt from '../../Components/HealthcareText/Healthcare';
import BackBtn from '../../Components/BackButton/BackButton';

const NewRaport = (props) => {
    return (
        <Background>
            <LogOutBtn></LogOutBtn>
            <HealthcareTxt></HealthcareTxt>
            <BackBtn></BackBtn>
            <span className={styles.newraport}>New raport: <span className={styles.patientname}>{props.name}</span></span>
            <div className={styles.placeholder}>
                <input className={styles.anamnesis} type="text" placeholder="Anamnesis"></input>
                <input className={styles.symptoms} type="text" placeholder="Symptoms"></input>
                <input className={styles.diagnosis} type="text" placeholder="Diagnosis"></input>
                <input className={styles.treatment} type="text" placeholder="Treatment"></input>
            </div>
            <textarea className={styles.additional} type="text" placeholder="Additional notes"></textarea>

            <Link>
                <button className={styles.lab}>Lab test referral</button>
            </Link>
            <Link>
                <button className={styles.create}>Create referral</button>
            </Link>
            <span className={styles.text}>You can use the AI assistant to analyze patient-reported symptoms. The system will provide a list of likely diagnoses to support your clinical decision-making</span>
            <button className={styles.ai}>AI assistant</button>
            <button className={styles.next}>Next step</button>
        </Background>
    );
};

export default NewRaport;