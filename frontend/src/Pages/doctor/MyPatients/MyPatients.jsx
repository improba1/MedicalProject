import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import Background from '../../../Components/Background/Background';
import BackBtn from '../../../Components/BackButton/BackButton';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import styles from './MyPatients.module.css';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import { doctorVisitApi } from '../../../Api/doctor/visitApi';

const MyPatients = () => {
    const navigate = useNavigate();
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await doctorVisitApi.getUpcomingVisits();
                const visits = response.data || [];

                const enrichedVisits = await Promise.all(visits.map(async (visit) => {
                    try {
                        if (visit.patientId) {
                            const patientRes = await doctorVisitApi.getPatientById(visit.patientId);
                            return { ...visit, patientData: patientRes.data };
                        }
                        return visit;
                    } catch (err) {
                        console.error(`Failed to load patient ${visit.patientId}`, err);
                        return visit;
                    }
                }));

                const sortedList = enrichedVisits.sort((a, b) =>
                    new Date(a.appointmentTime) - new Date(b.appointmentTime)
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

    const getCardStyle = (item) => {
        const status = item.status ? item.status.toUpperCase() : 'UNKNOWN';
        const appointmentDate = new Date(item.appointmentTime);
        const now = new Date();

        if (status === 'COMPLETED') {
            return `${styles.card} ${styles.statusGray}`;
        }

        if (appointmentDate < now) {
            return `${styles.card} ${styles.statusGray}`;
        }

        if (status === 'SCHEDULED' || status === 'CONFIRMED') {
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
        const pName = item.patientData ? item.patientData.firstname.toLowerCase() : '';
        const pLast = item.patientData ? item.patientData.lastname.toLowerCase() : '';
        const stat = item.status ? item.status.toLowerCase() : '';

        // Filter out cancelled visits
        if (stat === 'cancelled' || stat === 'canceled') return false;

        return pid.includes(term) || stat.includes(term) || pName.includes(term) || pLast.includes(term);
    });

    return (
        <Background>
            <BackBtn />
            <LogOutBtn />
            <HealthcareTxt />
            <MyProfileBtn />

            <h1 className={styles.pageTitle}>Active Appointments</h1>
            <p className={styles.pageSubtitle}>Upcoming visits schedule</p>

            <div className={styles.contentContainer}>
                <input
                    type="text"
                    placeholder="Search by Name or ID..."
                    className={styles.searchInput}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />

                {loading ? (
                    <div style={{ color: 'white', textAlign: 'center', marginTop: '20px' }}>
                        Loading schedule...
                    </div>
                ) : (
                    <div className={styles.listWrapper}>
                        {filteredList.length === 0 ? (
                            <div style={{ color: 'white', textAlign: 'center', opacity: 0.8 }}>
                                No active appointments found.
                            </div>
                        ) : (
                            filteredList.map((item) => (
                                <div
                                    key={item.id}
                                    className={getCardStyle(item)}
                                    onClick={() => navigate('/appointment-details', { state: { visit: item } })}
                                >
                                    <div className={styles.infoColumn}>
                                        <span className={styles.mainTitle}>
                                            {formatDate(item.appointmentTime)}
                                        </span>

                                        <span className={styles.diseaseTitle}>
                                            {item.patientData
                                                ? `${item.patientData.firstname} ${item.patientData.lastname}`
                                                : "Loading Name..."
                                            }
                                        </span>

                                        <span className={styles.subText}>
                                            ID: <span style={{ fontFamily: 'monospace' }}>{item.patientId.slice(0, 8)}...</span>
                                        </span>

                                        {item.services && item.services.length > 0 && (
                                            <span style={{ fontSize: '12px', color: '#ccc', marginTop: '5px' }}>
                                                Service: {item.services[0].serviceName || "Consultation"}
                                            </span>
                                        )}
                                    </div>

                                    <div className={styles.statusColumn}>
                                        <span className={styles.statusTag}>
                                            {item.status || "SCHEDULED"}
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