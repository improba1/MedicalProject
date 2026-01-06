import React, { useState, useEffect } from 'react';
import {useNavigate } from 'react-router-dom';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import Background from '../../../Components/Background/Background';
import BackBtn from '../../../Components/BackButton/BackButton';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import styles from './MyPatients.module.css';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import { reportApi } from '../../../Api/doctor/raportApi';  

const MyPatients = () => {
    const navigate = useNavigate();
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await reportApi.getAllReports();  
                const list = response.data || [];
                const sortedList = list.sort((a, b) => 
                    new Date(b.appointmentTime) - new Date(a.appointmentTime)
                );

                setAppointments(sortedList);
            } catch (error) {
                console.error("Error fetching data:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const getCardStyle = (statusString) => {
        const status = statusString ? statusString.toUpperCase() : 'UNKNOWN';
        if (status === 'SCHEDULED' || status === 'COMPLETED' || status === 'CONFIRMED') {
            return `${styles.card} ${styles.statusNormal}`;
        } 
        else {
            return `${styles.card} ${styles.statusRed}`;
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'No date';
        return new Date(dateString).toLocaleString('en-US', {
            month: 'short', 
            day: 'numeric', 
            year: 'numeric',
            hour: '2-digit', 
            minute: '2-digit',
            hour12: false  
        });
    };

    const filteredList = appointments.filter(item => {
        const term = searchTerm.toLowerCase();
        const pid = item.patientId ? item.patientId.toLowerCase() : '';
        const stat = item.status ? item.status.toLowerCase() : '';
        
        return pid.includes(term) || stat.includes(term);
    });

    return (
        <Background>
            <BackBtn />
            <LogOutBtn />
            <HealthcareTxt />
            <MyProfileBtn />
            
            <h1 className={styles.pageTitle}>My Appointments</h1>
            <p className={styles.pageSubtitle}>Overview of your visits history</p>
            
            <div className={styles.contentContainer}>
                <input 
                    type="text" 
                    placeholder="Search by Patient ID or Status..." 
                    className={styles.searchInput}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />

                {loading ? (
                    <div style={{color: 'white', textAlign: 'center', marginTop: '20px'}}>
                        Loading data...
                    </div>
                ) : (
                    <div className={styles.listWrapper}>
                        {filteredList.length === 0 ? (
                            <div style={{color: 'white', textAlign: 'center', opacity: 0.8}}>
                                No appointments found.
                            </div>
                        ) : (
                            filteredList.map((item) => (
                                <div 
                                    key={item.id} 
                                    className={getCardStyle(item.status)}
                                    onClick={() => navigate('/appointment-details', { state: { visit: item } })}  
                                >
                                    <div className={styles.infoColumn}>
                                        <span className={styles.mainTitle}>
                                            {formatDate(item.appointmentTime)}
                                        </span>
                                        
                                        <span className={styles.subText}>
                                            Patient: {item.patientId}
                                        </span>

                                        {item.raportId && (
                                            <span style={{fontSize: '11px', color: '#999'}}>
                                                Raport #: {item.raportId.slice(0, 8)}...
                                            </span>
                                        )}
                                    </div>

                                    <div className={styles.statusColumn}>
                                        <span className={styles.statusTag}>
                                            {item.status || "Unknown"}
                                        </span>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                )}
            </div>
        </Background>
    );
};

export default MyPatients;