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
                // 1. Параллельная загрузка визитов и врачей
                const [visitsResponse, doctorsResponse] = await Promise.all([
                    upcomingVisitsApi.getUpcomingVisits(),
                    publicDoctorApi.getAllDoctors()
                ]);

                const rawVisits = visitsResponse.data || visitsResponse || [];
                const doctorsList = doctorsResponse.data?.data || [];

                // Создаем Map для быстрого поиска врача по ID
                const doctorsMap = new Map(doctorsList.map(doc => [doc.id, doc]));

                // 2. Обогащаем визиты данными о врачах
                const enrichedVisits = rawVisits.map(visit => {
                    const doctor = doctorsMap.get(visit.doctorId);
                    return {
                        ...visit,
                        doctorName: doctor ? `${doctor.firstname} ${doctor.lastname}` : "Unknown Doctor",
                        specialization: doctor ? doctor.specialization : "General",
                        doctorImage: doctor?.image?.downloadUrl
                    };
                });

                setVisits(enrichedVisits);
            } catch (error) {
                console.error("Failed to load upcoming visits", error);
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
                // Remove cancelled visit from list immediately
                setVisits(prev => prev.filter(v => v.id !== visitId));
            } catch (error) {
                console.error("Failed to cancel visit", error);
                alert("Failed to cancel visit");
            }
        }
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithProfile />
                <main className={styles.content}>
                    <div className={styles.header}>
                        <button className={styles.backBtn} onClick={() => navigate('/patient')}>← Back</button>
                        <h1>Upcoming Visits</h1>
                    </div>

                    {loading ? (
                        <div className={styles.statusMsg}>Loading...</div>
                    ) : visits.length > 0 ? (
                        <div className={styles.list}>
                            {visits.map(visit => (
                                <div key={visit.id} className={styles.card}>
                                    <div className={styles.dateSide}>
                                        <div className={styles.day}>{new Date(visit.appointmentTime).getDate()}</div>
                                        <div className={styles.month}>
                                            {new Date(visit.appointmentTime).toLocaleString('en-US', { month: 'short' })}
                                        </div>
                                        <div className={styles.time}>
                                            {new Date(visit.appointmentTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                        </div>
                                    </div>

                                    <div className={styles.infoSide}>
                                        <div className={styles.doctorHeader}>
                                            <h3>{visit.doctorName}</h3>
                                            <span className={styles.spec}>{visit.specialization}</span>
                                        </div>

                                        {/* Список услуг */}
                                        {visit.services && visit.services.length > 0 && (
                                            <div className={styles.servicesList}>
                                                {visit.services.map((service, index) => (
                                                    <span key={index} className={styles.serviceTag}>
                                                        {service.serviceName}
                                                        {index < visit.services.length - 1 && ", "}
                                                    </span>
                                                ))}
                                            </div>
                                        )}
                                    </div>

                                    <div className={styles.statusSide}>
                                        <span className={`${styles.status} ${styles[visit.status?.toLowerCase()] || styles.pending}`}>
                                            {visit.status}
                                        </span>
                                        <span className={styles.price}>${visit.totalPrice || 0}</span>

                                        {/* Show Cancel button if relevant status */}
                                        {['PENDING', 'PAID', 'APPROVED', 'PLANNED'].includes(visit.status) && (
                                            <button
                                                className={styles.cancelBtn}
                                                onClick={() => handleCancel(visit.id)}
                                            >
                                                Cancel
                                            </button>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.statusMsg}>No upcoming visits found.</div>
                    )}
                </main>
            </div>
        </AnimatedPage>
    );
};

export default UpcomingVisits;