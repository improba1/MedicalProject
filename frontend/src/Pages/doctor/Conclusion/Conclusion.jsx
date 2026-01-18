import React from 'react';
import { Link } from 'react-router-dom';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import BackBtn from '../../../Components/BackButton/BackButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import Background from '../../../Components/Background/Background';
import styles from './Conclusion.module.css';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const Conclusion = () => {
    const docLinks = {
        labTest: "/files/lab-test-referral.txt", 
        specialist: "/files/specialist-referral.txt"
    };

    return (
        <Background>
            <div className={styles.navHeader}>
                <div className={styles.navLeft}>
                    <BackBtn />
                    <HealthcareTxt />
                    <MyProfileBtn></MyProfileBtn>
                </div>
                <LogOutBtn />
            </div>

            <div className={styles.container}>
                <h1 className={styles.pageTitle}>Conclusion</h1>

                <div className={styles.contentRow}>
                    
                    <div className={styles.infoCard}>
                        <InfoRow label="Patient" value="Full name" />
                        <InfoRow label="Doctor" value="Dr. House" />
                        <InfoRow label="Anamnesis" value="Patient complains of..." />
                        <InfoRow label="Symptoms" value="Cough, high fever..." />
                        <InfoRow label="Diagnosis" value="Acute Bronchitis" />
                        <InfoRow label="Treatment" value="Antibiotics, rest..." />
                        <InfoRow label="Notes" value="(optional)" />
                        
                        <div className={styles.infoRow}>
                            <span className={styles.label}>Lab test referral: </span>
                            <a 
                                href={docLinks.labTest} 
                                target="_blank" 
                                rel="noopener noreferrer" 
                                className={styles.documentLink}
                            >
                                document.txt (optional)
                            </a>
                        </div>

                        <div className={styles.infoRow}>
                            <span className={styles.label}>Referral to a specialist: </span>
                            <a 
                                href={docLinks.specialist} 
                                target="_blank" 
                                rel="noopener noreferrer" 
                                className={styles.documentLink}
                            >
                                document.txt (optional)
                            </a>
                        </div>
                    </div>
     
                    <div className={styles.buttonContainer}>
                        <Link to="/raport-added"> 
                            <button className={styles.saveBtn}>
                                Save
                            </button>
                        </Link>
                    </div>

                </div>
            </div>
        </Background>
    );
};

const InfoRow = ({ label, value }) => (
    <div className={styles.infoRow}>
        <span className={styles.label}>{label}: </span>
        <span className={styles.value}>{value}</span>
    </div>
);

export default Conclusion;