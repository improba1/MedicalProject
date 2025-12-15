import React from 'react';
import { useNavigate } from 'react-router-dom';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import BackBtn from '../../../Components/BackButton/BackButton'; 
import styles from './PatientDetails.module.css';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const PatientDetails = () => {
    const navigate = useNavigate();

    const upcomingVisits = [
        { 
            date: "15/10/2025", 
            title: "Clinic Visit Appointment", 
            time: "10:00", 
            specialization: "Cardiology",
            doctor: "Dr. House", 
            paymentMethod: "Card",
            reportUrl: "/files/report-1.pdf" 
        },
        { 
            date: "22/10/2025", 
            title: "Check-up", 
            time: "14:30", 
            specialization: "Therapy", 
            doctor: "Dr. Wilson", 
            paymentMethod: "Insurance",
            reportUrl: "/files/report-2.pdf"
        },
        { 
            date: "01/11/2025", 
            title: "Clinic Visit Appointment", 
            time: "09:15", 
            specialization: "Cardiology", 
            doctor: "Dr. House", 
            paymentMethod: "Cash",
            reportUrl: "/files/report-3.pdf"
        },
    ];

    const historyVisits = [
        { 
            date: "01/09/2025", 
            title: "Clinic Visit Appointment", 
            time: "11:00", 
            specialization: "Cardiology", 
            doctor: "Dr. House", 
            paymentMethod: "Card",
            reportUrl: "/files/history-1.pdf"
        },
    ];

    const handleCardClick = (visitData) => {
        navigate('/appointment-details', { state: { visit: visitData } });
    };

    return (
        <Background>
            <div className={styles.header}>
                 <div className={styles.headerLeft}>
                    <BackBtn />
                    <HealthcareTxt />
                    <MyProfileBtn></MyProfileBtn>
                </div>
                <LogOutBtn />
            </div>

            <div className={styles.mainContent}>
                <h1 className={styles.patientName}>Patient: John Doe</h1>

                <div className={styles.section}>
                    <h2 className={styles.sectionTitle}>Upcoming visits</h2>
                    <div className={styles.grid}>
                        {upcomingVisits.map((item, index) => (
                            <Card key={index} data={item} onClick={() => handleCardClick(item)} />
                        ))}
                    </div>
                </div>

                <div className={styles.section}>
                    <h2 className={styles.sectionTitle}>Appointment history</h2>
                    <div className={styles.grid}>
                        {historyVisits.map((item, index) => (
                            <Card key={index} data={item} onClick={() => handleCardClick(item)} />
                        ))}
                    </div>
                </div>
            </div>
        </Background>
    );
};

const Card = ({ data, onClick }) => (
    <div className={styles.card} onClick={onClick}>
        <div className={styles.cardHeader}>
            <div className={styles.iconBox}>📄</div>
            <span className={styles.date}>{data.date}</span>
        </div>
        <div className={styles.cardBody}>
            <h3 className={styles.cardTitle}>{data.title}</h3>
            <p className={styles.cardText}>{data.time}</p>
            <p className={styles.cardText}>{data.specialization}</p>
            <p className={styles.cardDoctor}>{data.doctor}</p>
        </div>
    </div>
);

export default PatientDetails;