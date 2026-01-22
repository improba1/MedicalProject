import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './UpcomingVisits.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithProfile from '../../../Components/HeaderWithProfile/HeaderWithProfile';
import { upcomingVisitsApi } from '../../../Api/patient/UpcomingVisitsApi';
import { publicDoctorApi } from '../../../Api/all/publicDoctorApi';

const UpcomingVisits = () => {
    const navigate = useNavigate();
    const [visits, setVisits] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            try {
                const [visitsResponse, doctorsResponse] = await Promise.all([
                    upcomingVisitsApi.getUpcomingVisits(),
                    publicDoctorApi.getAllDoctors()
                ]);

                const rawVisits = visitsResponse.data || visitsResponse || [];
                const doctorsList = doctorsResponse.data?.data || [];
                const doctorsMap = new Map(doctorsList.map(doc => [doc.id, doc]));

                const enrichedVisits = rawVisits.map(visit => {
                    const doctor = doctorsMap.get(visit.doctorId);
                    return {
                        ...visit,
                        doctorName: doctor ? `${doctor.firstname} ${doctor.lastname}` : "Doctor",
                        specialization: doctor ? doctor.specialization : "Specialist"
                    };
                });

                setVisits(enrichedVisits);
            } catch (error) {
                console.error("Failed to load visits", error);
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, []);

    const handleCancel = async (visitId) => {
        if (window.confirm("Are you sure you want to cancel this visit?")) {
            try {
                await upcomingVisitsApi.cancelVisit(visitId);
                setVisits(prev => prev.filter(v => v.id !== visitId));
                alert("Visit cancelled");
            } catch (error) {
                alert("Failed to cancel visit");
            }
        }
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithProfile />
                <main className={styles.content}>
                    <div className={styles.titleSection}>
                        <button className={styles.backBtn} onClick={() => navigate('/patient')}>
                            ← Back
                        </button>
                        <h1 className={styles.title}>Upcoming Visits</h1>
                    </div>

                    {loading ? (
                        <div className={styles.statusMsg}>Loading...</div>
                    ) : visits.length > 0 ? (
                        <div className={styles.list}>
                            {visits.map(visit => (
                                <div key={visit.id} className={styles.card}>
                                    {/* Блок даты слева */}
                                    <div className={styles.dateSide}>
                                        <div className={styles.day}>{new Date(visit.appointmentTime).getDate()}</div>
                                        <div className={styles.month}>
                                            {new Date(visit.appointmentTime).toLocaleString('en-US', { month: 'short' }).toUpperCase()}
                                        </div>
                                        <div className={styles.time}>
                                            {new Date(visit.appointmentTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                        </div>
                                    </div>

                                    {/* Основная инфо */}
                                    <div className={styles.infoSide}>
                                        <div className={styles.doctorHeader}>
                                            <h3>{visit.doctorName}</h3>
                                            <span className={styles.spec}>{visit.specialization}</span>
                                        </div>
                                        <div className={styles.servicesList}>
                                            {visit.services?.map((s, i) => s.serviceName).join(", ")}
                                        </div>
                                    </div>

                                    {/* Правая часть: Статус и Цена */}
                                    <div className={styles.statusSide}>
                                        <span className={`${styles.status} ${styles[visit.status?.toLowerCase()] || styles.scheduled}`}>
                                            {visit.status || 'SCHEDULED'}
                                        </span>
                                        <span className={styles.price}>${visit.totalPrice || 0}</span>
                                    </div>

                                    {/* Кнопка отмены как на предыдущих схемах */}
                                    <div className={styles.divider}></div>
                                    <div className={styles.cancelSection}>
                                        <button className={styles.cancelBtn} onClick={() => handleCancel(visit.id)}>
                                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                                <path d="M3 6h18M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2M10 11v6M14 11v6" />
                                            </svg>
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.statusMsg}>No upcoming visits.</div>
                    )}
                </main>
            </div>
        </AnimatedPage>
    );
};

export default UpcomingVisits;