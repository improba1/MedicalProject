import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';

import styles from './PatientHomePage.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithProfile from '../../../Components/HeaderWithProfile/HeaderWithProfile';
import HeartBackground from '../../../Components/HeartBackground/HeartBackground';

import { profileApi } from '../../../Api/patient/profileApi';
import { visitApi } from '../../../Api/patient/visitApi';
import { doctorApi } from '../../../Api/doctor/doctorApi';

const Card = ({ data, onClick }) => (
    <button className={styles.card} onClick={onClick} type="button">
        <div className={styles.cardHeader}>
            <div className={styles.iconBox}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#F5782D" strokeWidth="2">
                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                    <polyline points="14 2 14 8 20 8"></polyline>
                    <line x1="16" y1="13" x2="8" y2="13"></line>
                    <line x1="16" y1="17" x2="8" y2="17"></line>
                </svg>
            </div>
            <span className={styles.date}>{data.date}</span>
        </div>
        <div className={styles.cardBody}>
            <h3 className={styles.cardTitle}>{data.title}</h3>
            <p className={styles.cardText}>{data.time}</p>
            <p className={styles.cardText}>{data.specialization}</p>
            <p className={styles.cardDoctor}>{data.doctor}</p>
        </div>
    </button>
);

const PatientHomePage = () => {
    const navigate = useNavigate();
    const [patientName, setPatientName] = useState('');
    const [upcomingVisits, setUpcomingVisits] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const profileRes = await profileApi.getPatientProfile();
                setPatientName(profileRes.data.firstname); 

                const visitsRes = await visitApi.getUpcomingVisits();
                const visitsData = visitsRes.data;

                const fullVisits = await Promise.all(
                    visitsData.map(async (visit) => {
                        try {
                            const docRes = await doctorApi.getDoctorById(visit.doctorId);
                            const doc = docRes.data;
                            const dateObj = new Date(visit.appointmentTime);
                            
                            return {
                                id: visit.id,
                                date: dateObj.toLocaleDateString('ru-RU'),
                                time: dateObj.toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' }),
                                title: "Clinic Visit",
                                specialization: doc.specialization,
                                doctor: `Dr. ${doc.lastname}`
                            };
                        } catch (err) {
                            return null;
                        }
                    })
                );

                setUpcomingVisits(fullVisits.filter(v => v !== null));
            } catch (error) {
                console.error("Error loading home page data:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeartBackground />
                <HeaderWithProfile />

                <main className={styles.mainContent}>
                    <section className={styles.hero}>
                        <h1 className={styles.welcome}>
                            Hello, <span className={styles.orange}>{patientName || 'Patient'}</span>
                        </h1>
                        <p className={styles.subtext}>
                            Take charge of your health. Book an appointment with a specialist at your convenience!
                        </p>
                        <button className={styles.bookBtn} onClick={() => navigate('/book-appointment')}>
                            Book appointment
                        </button>
                    </section>

                    <div className={styles.bottomSection}>
                        <div className={styles.historyCard}>
                            <p className={styles.historyText}>All of your appointments and consultations will be stored here:</p>
                            <button className={styles.historyBtn} onClick={() => navigate('/appointment-history')}>
                                Appointment history
                            </button>
                        </div>

                        <div className={styles.sliderContainer}>
                            <h2 className={styles.sliderTitle}>Upcoming appointments</h2>
                            <div className={styles.swiperWrapper}>
                                {loading ? (
                                    <div className={styles.emptyLine} /> 
                                ) : upcomingVisits.length > 0 ? (
                                    <Swiper
                                        modules={[Navigation]}
                                        spaceBetween={15}
                                        slidesPerView={3.2}
                                        navigation={true}
                                        className={styles.mySwiper}
                                    >
                                        {upcomingVisits.map((visit) => (
                                            <SwiperSlide key={visit.id}>
                                                <Card 
                                                    data={visit} 
                                                    onClick={() => navigate(`/visit-details/${visit.id}`)} 
                                                />
                                            </SwiperSlide>
                                        ))}
                                    </Swiper>
                                ) : (
                                    <div className={styles.emptyLine} />
                                )}
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default PatientHomePage;