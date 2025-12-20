import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import BackBtn from '../../../Components/BackButton/BackButton'; 
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import styles from './AppointmentDetails.module.css';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const AppointmentDetails = () => {
    const location = useLocation();
    const { visit } = location.state || {}; 

    if (!visit) return <div style={{color:'white', padding:50}}>No data</div>;

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
                <h1 className={styles.pageTitle}>Appointment details</h1>
                
                {/* Контейнер для контента (Карточка слева + Кнопка справа) */}
                <div className={styles.contentRow}>
                    
                    {/* Левая часть: Карточка с информацией */}
                    <div className={styles.infoCard}>
                        <InfoRow label="Specialization" value={visit.specialization} />
                        <InfoRow label="Doctor" value={visit.doctor} />
                        <InfoRow label="Date" value={visit.date} />
                        <InfoRow label="Time" value={visit.time} />
                        <InfoRow label="Payment method" value={visit.paymentMethod || "Card"} />
                        
                        {/* Ссылка на документ */}
                        <div className={styles.infoRow}>
                            <span className={styles.label}>Raport: </span>
                            {/* target="_blank" открывает в новой вкладке */}
                            <a 
                                href={visit.reportUrl || "#"} 
                                target="_blank" 
                                rel="noopener noreferrer" 
                                className={styles.documentLink}
                            >
                                document
                            </a>
                        </div>
                    </div>
                    <Link to="/edit-raport">
                        <button className={styles.editBtn}>
                            Edit raport
                        </button>
                    </Link>
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

export default AppointmentDetails;