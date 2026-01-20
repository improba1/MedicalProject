import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import BackBtn from '../../../Components/BackButton/BackButton'; 
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import styles from './AppointmentDetails.module.css';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import { doctorVisitApi } from '../../../Api/doctor/visitApi'; 

const AppointmentDetails = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { visit } = location.state || {}; 

    const [visitData, setVisitData] = useState(null);
    const [patientName, setPatientName] = useState('Loading...');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDetails = async () => {
            if (!visit?.id) return;

            setLoading(true);

            // 1. Пытаемся загрузить детали визита (ЭТОТ ЗАПРОС МОЖЕТ УПАСТЬ)
            try {
                const visitResponse = await doctorVisitApi.getVisitById(visit.id);
                setVisitData(visitResponse.data);
            } catch (error) {
                console.error("Error fetching visit details (Backend issue):", error);
                // Если бек упал, используем данные из пропсов, чтобы страница не была пустой
                setVisitData(visit);
            }

            // 2. ГРУЗИМ ПАЦИЕНТА ОТДЕЛЬНО
            // Мы берем ID сразу из 'visit', который передали через навигацию.
            // Нам плевать, упал предыдущий запрос или нет.
            if (visit.patientId) {
                try {
                    const patientRes = await doctorVisitApi.getPatientById(visit.patientId);
                    const pData = patientRes.data;
                    if (pData) {
                        setPatientName(`${pData.firstname} ${pData.lastname}`);
                    }
                } catch (err) {
                    console.error("Failed to load patient name", err);
                    setPatientName("Unknown Patient");
                }
            }

            setLoading(false);
        };

        fetchDetails();
    }, [visit]);

    if (!visit) {
        return (
            <Background>
                <div className={styles.centerMessage}>
                    No visit selected. <br/>
                    <button onClick={() => navigate(-1)} className={styles.backLink}>Go Back</button>
                </div>
            </Background>
        );
    }

    if (loading) {
        return <Background><div className={styles.centerMessage}>Loading details...</div></Background>;
    }

    const formatDate = (dateString) => {
        if (!dateString) return "N/A";
        return new Date(dateString).toLocaleString('en-US', {
            weekday: 'short', year: 'numeric', month: 'long', day: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    };

    const hasReport = !!visitData?.raportId;

    return (
        <Background>
            <BackBtn />
            <HealthcareTxt />
            <MyProfileBtn />
            <LogOutBtn />

            <div className={styles.container}>
                <h1 className={styles.pageTitle}>Appointment Details</h1>
                
                <div className={styles.contentCard}>
                    
                    {/* --- Basic Info --- */}
                    <div className={styles.sectionHeader}>Basic Info</div>
                    <div className={styles.gridInfo}>
                        <InfoBox 
                            label="Date & Time" 
                            value={formatDate(visitData.appointmentTime)} 
                            icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>} 
                        />
                        <InfoBox 
                            label="Patient Name" 
                            value={patientName} 
                            icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>} 
                        />
                        <InfoBox 
                            label="Status" 
                            value={visitData.status || "SCHEDULED"} 
                            icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"></polygon></svg>} 
                        />
                        <InfoBox 
                            label="Total Price" 
                            value={`$${visitData.totalPrice || 0}`} 
                            icon={<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><line x1="12" y1="1" x2="12" y2="23"></line><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg>} 
                        />
                    </div>

                    <div className={styles.divider} />

                    {/* --- Services --- */}
                    <div className={styles.sectionHeader}>Medical Services</div>
                    <div className={styles.servicesList}>
                        {visitData.services && visitData.services.length > 0 ? (
                            visitData.services.map((service, index) => (
                                <div key={index} className={styles.serviceItem}>
                                    <span className={styles.serviceName}>{service.serviceName || "Medical Service"}</span>
                                    <span className={styles.servicePrice}>${service.priceAtMomentOfPurchase || 0}</span> 
                                </div>
                            ))
                        ) : (
                            <div className={styles.noData}>No services added yet.</div>
                        )}
                    </div>

                    <div className={styles.divider} />

                    {/* --- Report Status --- */}
                    <div className={styles.reportSection}>
                        {hasReport ? (
                            <div className={styles.reportSuccess}>
                                <div className={styles.reportIcon}>
                                    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#4BB543" strokeWidth="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                                </div>
                                <div>
                                    <h3>Report Created</h3>
                                    <p>Report ID: <span className={styles.mono}>{visitData.raportId.slice(0,8)}...</span></p>
                                </div>
                                <button className={styles.viewReportBtn}>View</button> 
                            </div>
                        ) : (
                            <div className={styles.reportWarning}>
                                <div className={styles.reportIcon}>
                                    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#F5782D" strokeWidth="2"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
                                </div>
                                <div>
                                    <h3>Report Missing</h3>
                                    <p>The patient has not paid or report hasn't been generated.</p>
                                </div>
                            </div>
                        )}
                    </div>

                    <div className={styles.actions}>
                        <Link 
                            to={hasReport ? `/edit-raport/${visitData.raportId}` : `/create-raport/${visitData.id}`}
                            className={styles.actionLink}
                        >
                            <button className={styles.primaryBtn}>
                                {hasReport ? "Edit Report" : "Create Report"}
                            </button>
                        </Link>
                    </div>

                </div>
            </div>
        </Background>
    );
};

const InfoBox = ({ label, value, icon }) => (
    <div className={styles.infoBox}>
        <div className={styles.iconCircle}>{icon}</div>
        <div>
            <span className={styles.boxLabel}>{label}</span>
            <span className={styles.boxValue}>{value}</span>
        </div>
    </div>
);

export default AppointmentDetails;