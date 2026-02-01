import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import BackBtn from '../../../Components/BackButton/BackButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import Background from '../../../Components/Background/Background';
import styles from './Conclusion.module.css';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const Conclusion = () => {
    const location = useLocation();
    const { reportData, patientName, doctorName, hasLabTest, hasReferral } = location.state || {}; // Destructure safe

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
                        <InfoRow label="Patient" value={patientName || "-"} />
                        {/*<InfoRow label="Doctor" value={doctorName || "-"} />*/}
                        <InfoRow label="Anamnesis" value={reportData?.anamnesis || "-"} />
                        <InfoRow label="Symptoms" value={reportData?.symptoms || "-"} />
                        <InfoRow label="Diagnosis" value={reportData?.diagnosis || "-"} />
                        <InfoRow label="Treatment" value={reportData?.treatment || "-"} />
                        <InfoRow label="Notes" value={reportData?.additional || "-"} />

                        <div className={styles.infoRow}>
                            <span className={styles.label}>Lab test referral: </span>
                            {hasLabTest ? (
                                <Link
                                    to="/lab-test-ticket"
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className={styles.documentLink}
                                    state={{
                                        doctorName: doctorName,
                                        analysis: reportData?.diagnosis || "Blood Test",
                                        date: new Date().toLocaleDateString('en-GB')
                                    }}
                                >
                                    document
                                </Link>
                            ) : (
                                <span className={styles.value}>-</span>
                            )}
                        </div>

                        <div className={styles.infoRow}>
                            <span className={styles.label}>Referral to a specialist: </span>
                            {hasReferral ? (
                                <Link
                                    to="/refferal-ticket"
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className={styles.documentLink}
                                    state={{
                                        doctorName: doctorName,
                                        date: new Date().toLocaleDateString('en-GB')
                                    }}
                                >
                                    document
                                </Link>
                            ) : (
                                <span className={styles.value}>-</span>
                            )}
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