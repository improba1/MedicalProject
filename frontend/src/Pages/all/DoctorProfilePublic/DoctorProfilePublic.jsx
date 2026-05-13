import React from 'react'; 
import { useLocation, useNavigate, Link } from 'react-router-dom';
import styles from './DoctorProfilePublic.module.css'; // Подключаем НОВЫЙ файл стилей
import BackBtn from '../../../Components/BackButton/BackButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import { FaStar, FaLock } from 'react-icons/fa'; // Добавляем иконки

const DoctorProfilePublic = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const checkIsLoggedIn = () => {
        return !!localStorage.getItem('access_token'); 
    };

    const handleBookClick = (e) => {
        e.stopPropagation();
        if (!checkIsLoggedIn()) {
            navigate('/signUpForm', { 
                state: { 
                    from: '/doctor-profile-logged', 
                    doctorData: doctorData   
                } 
            });
        } else {
            navigate('/doctor-profile-logged', { state: { doctorData: doctorData } });
        }
    };

    const doctorData = location.state?.doctorData;

    if (!doctorData) {
        return (
            <div className={styles.pageContainer}>
                <div className={styles.notFoundMessage}>
                    <h2>Doctor not found.</h2>
                    <button onClick={() => navigate(-1)} className={styles.goBackBtn}>Go Back</button>
                </div>
            </div>
        );
    }

    const imageUrl = doctorData.image?.downloadUrl;

    return (
        <div className={styles.pageContainer}>
            {/* Светлая навигация */}
            <div className={styles.topNav}>
                <div className={styles.navLeft}>
                    <BackBtn />
                    <HealthcareTxt />
                </div>
            </div>

            <div className={styles.wrapper}>
                <h1 className={styles.pageTitle}>Specialist Profile</h1>
                
                <div className={styles.contentCard}>
                    {/* ЛЕВАЯ КОЛОНКА: Фото и базовая инфа */}
                    <div className={styles.visualColumn}>
                        <div className={styles.photoContainer}>
                            {imageUrl ? (
                                <img 
                                    src={imageUrl} 
                                    alt="Profile" 
                                    className={styles.profileImage}
                                    onError={(e) => { e.target.onerror = null; e.target.src = 'https://via.placeholder.com/300x400?text=No+Photo'; }}
                                />
                            ) : (
                                <div className={styles.placeholderImage}>
                                    {doctorData.firstname[0]}{doctorData.lastname[0]}
                                </div>
                            )}
                        </div>
                        
                        <h2 className={styles.userName}>{doctorData.firstname} {doctorData.lastname}</h2>
                        <span className={styles.userRole}>{doctorData.specialization || 'Doctor'}</span>
                        
                        <div className={styles.ratingBadge}>
                            <FaStar className={styles.starIcon} /> 
                            <span>{doctorData.rating || '5.0'}</span>
                        </div>
                    </div>

                    {/* ПРАВАЯ КОЛОНКА: Детали */}
                    <div className={styles.detailsColumn}>
                        <div className={styles.detailsScrollArea}>
                            <SectionTitle title="Professional Information" />
                            <div className={styles.infoGrid}>
                                <InfoRow label="Specialization" value={doctorData.specialization} />
                                <InfoRow label="Qualification" value={doctorData.qualification} />
                                <InfoRow label="Experience" value={`${doctorData.experienceYears || 0} years`} />
                                <InfoRow label="Started working" value={doctorData.startDate} />
                            </div>

                            <div className={styles.divider} />
                            
                            <SectionTitle title="Medical Services" />
                            <div className={styles.loginGate}>
                                <FaLock className={styles.loginGateIcon} />
                                <h4>Full Service List Hidden</h4>
                                <p>Please log in or register to view the complete list of medical services and prices.</p>
                                <Link to="/login" className={styles.loginGateBtn}>
                                    Log In to View
                                </Link>
                            </div>

                            <div className={styles.divider} />

                            <SectionTitle title="Contact Details" />
                            <div className={styles.infoGrid}>
                                <InfoRow label="Email" value={doctorData.email} />
                                <InfoRow label="Phone" value={doctorData.phone} />
                            </div>
                        </div>
                        
                        <div className={styles.actionArea}>
                            <button 
                                className={styles.bookButton} 
                                onClick={handleBookClick}
                            >
                                Book Appointment
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

const SectionTitle = ({ title }) => (
    <h3 className={styles.sectionTitle}>{title}</h3>
);

const InfoRow = ({ label, value }) => (
    <div className={styles.infoRow}>
        <span className={styles.label}>{label}:</span>
        <span className={styles.value}>{value || '-'}</span>
    </div>
);

export default DoctorProfilePublic;