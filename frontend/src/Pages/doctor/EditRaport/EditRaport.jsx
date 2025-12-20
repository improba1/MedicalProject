import React from 'react';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import Background from '../../../Components/Background/Background';
import BackBtn from '../../../Components/BackButton/BackButton';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import styles from './EditRaport.module.css';
import { Link } from 'react-router-dom';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const EditRaport = (props) => {
    return (
        <Background>
            <HealthcareTxt></HealthcareTxt>
            <BackBtn></BackBtn>
            <LogOutBtn></LogOutBtn>
            <MyProfileBtn></MyProfileBtn>
            <span className={styles.newraport}>Edit raport: <span className={styles.patientname}>{props.name}</span></span>
            <div className={styles.placeholder}>
                <input className={styles.anamnesis} type="text" placeholder="Anamnesis"></input>
                <input className={styles.symptoms} type="text" placeholder="Symptoms"></input>
                <input className={styles.diagnosis} type="text" placeholder="Diagnosis"></input>
                <input className={styles.treatment} type="text" placeholder="Treatment"></input>
            </div>
            <textarea className={styles.additional} type="text" placeholder="Additional notes"></textarea>

            <Link to="/lab-test">
                <button className={styles.lab}>Lab test referral</button>
            </Link>
            <Link to="/create-referral">
                <button className={styles.create}>Create referral</button>
            </Link>
            <span className={styles.text}>You can use the AI assistant to analyze patient-reported symptoms. The system will provide a list of likely diagnoses to support your clinical decision-making</span>
            <Link to="/ai">
                <button className={styles.ai}>AI assistant</button>
            </Link>
            <Link to="/conclusion">
                <button className={styles.next}>Next step</button>
            </Link>
        </Background>
    );
};

export default EditRaport;