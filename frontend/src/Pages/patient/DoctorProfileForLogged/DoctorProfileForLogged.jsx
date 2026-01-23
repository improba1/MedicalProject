import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import styles from '../../all/MyProfile/MyProfile.module.css'; 
import Background from '../../../Components/Background/Background';
import BackBtn from '../../../Components/BackButton/BackButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import { patientMedicalServiceApi } from '../../../Api/patient/medicalServiceApi';

const SectionTitle = ({ title, style }) => (
    <h3 className={styles.sectionTitle} style={style}>{title}</h3>
);

const InfoRow = ({ label, value }) => (
    <div className={styles.infoRow}>
        <span className={styles.label}>{label}:</span>
        <span className={styles.value}>{value || '-'}</span>
    </div>
);

const DoctorProfileForLogged = () => {
    const location = useLocation();
    const navigate = useNavigate();
    
    const doctorData = location.state?.doctorData;

    const [services, setServices] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [isServicesExpanded, setIsServicesExpanded] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        if (!doctorData?.id) return; 

        const loadServices = async () => {
            setIsLoading(true);
            try {
                const response = await patientMedicalServiceApi.search(doctorData.id, searchQuery);
                const servicesArray = Array.isArray(response.data) 
                    ? response.data 
                    : (response.data?.data || []);
                
                setServices(servicesArray);
            } catch (error) {
                console.error("Failed to load services", error);
                setServices([]); 
            } finally {
                setIsLoading(false);
            }
        };

        const delayDebounceFn = setTimeout(() => {
            loadServices();
        }, 500);

        return () => clearTimeout(delayDebounceFn);
        
    }, [doctorData?.id, searchQuery]);

    const handleBookClick = () => {
        navigate('/book-appointment', { state: { doctorData: doctorData } });
    };

    if (!doctorData) {
        return (
            <Background>
                <div style={{color:'white', textAlign:'center', marginTop:'20%'}}>
                    <h3>Doctor data not found.</h3>
                    <p>Please go back and select a doctor again.</p>
                    <button onClick={() => navigate(-1)} style={{marginTop:20, cursor:'pointer', padding: '10px 20px'}}>
                        Go Back
                    </button>
                </div>
            </Background>
        );
    }

    const imageUrl = doctorData.image?.downloadUrl;

    return (
        <Background>
            <div className={styles.topNav}>
                <div className={styles.navLeft}>
                    <BackBtn />
                    <HealthcareTxt />
                </div>
            </div>

            <div className={styles.wrapper}>
                <h1 className={styles.pageTitle}>Specialist Profile</h1>
                
                <div className={styles.contentCard}>
                    <div className={styles.visualColumn}>
                        <div className={styles.photoContainer}>
                            {imageUrl ? (
                                <img 
                                    src={imageUrl} 
                                    alt="Profile" 
                                    className={styles.profileImage}
                                    onError={(e) => { e.target.onerror = null; e.target.src = 'https://via.placeholder.com/200?text=No+Photo'; }}
                                />
                            ) : (
                                <div className={styles.placeholderImage}>No Photo</div>
                            )}
                        </div>
                        
                        <h2 className={styles.userName}>{doctorData.firstname} {doctorData.lastname}</h2>
                        <span className={styles.userRole}>{doctorData.specialization || 'Doctor'}</span>
                        
                        <div className={styles.ratingBadge}>
                            <span>⭐ {doctorData.rating || 0}</span>
                        </div>
                    </div>

                    <div className={styles.detailsColumn}>
                        <div className={styles.detailsScrollArea}>
                            
                            <SectionTitle title="Professional Information" />
                            <div className={styles.infoGrid}>
                                <InfoRow label="Specialization" value={doctorData.specialization} />
                                <InfoRow label="Qualification" value={doctorData.qualification} />
                                <InfoRow label="Experience" value={doctorData.experienceYears ? `${doctorData.experienceYears} years` : '0 years'} />
                                <InfoRow label="Started working" value={doctorData.startDate} />
                            </div>

                            <div className={styles.divider} />
                            
                            <div 
                                onClick={() => setIsServicesExpanded(!isServicesExpanded)}
                                className={styles.collapsibleHeader} 
                                style={{
                                    display: 'flex', 
                                    justifyContent: 'space-between', 
                                    alignItems: 'center', 
                                    cursor: 'pointer',
                                    marginBottom: '10px',
                                    userSelect: 'none'
                                }}
                            >
                                <SectionTitle title="Medical Services" style={{ margin: 0 }} />
                                <div style={{ 
                                    transform: isServicesExpanded ? 'rotate(180deg)' : 'rotate(0deg)', 
                                    transition: 'transform 0.3s ease' 
                                }}>
                                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#5b4cc4" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                        <polyline points="6 9 12 15 18 9"></polyline>
                                    </svg>
                                </div>
                            </div>
                            
                            {isServicesExpanded && (
                                <div className={styles.servicesContainer} style={{ animation: 'fadeIn 0.3s ease-in-out' }}>
                                    <input 
                                        type="text"
                                        className={styles.serviceSearchInput}
                                        placeholder="Search services..."
                                        value={searchQuery}
                                        onChange={(e) => setSearchQuery(e.target.value)}
                                        onClick={(e) => e.stopPropagation()} 
                                    />
                                    <div className={styles.servicesGrid}>
                                        {isLoading ? (
                                            <div style={{textAlign: 'center', padding: '10px', color: '#666'}}>Loading...</div>
                                        ) : services.length > 0 ? (
                                            services.map(service => (
                                                <div key={service.id} className={styles.serviceCard}>
                                                    <div className={styles.serviceInfo}>
                                                        <h4>{service.name}</h4>
                                                        {service.description && <p>{service.description}</p>}
                                                        <span className={styles.servicePrice}>${service.price}</span>
                                                    </div>
                                                </div>
                                            ))
                                        ) : (
                                            <div style={{color: '#888', textAlign: 'center', padding: '20px'}}>
                                                No services found.
                                            </div>
                                        )}
                                    </div>
                                </div>
                            )}
                            <div className={styles.divider} />
                            <SectionTitle title="Contact Details" />
                            <div className={styles.infoGrid}>
                                <InfoRow label="Email" value={doctorData.email} />
                                <InfoRow label="Phone" value={doctorData.phone} />
                            </div>
                        </div>
                        
                        <div className={styles.actionArea}>
                            <button 
                                className={styles.editButton} 
                                onClick={handleBookClick}
                            >
                                Book Appointment
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </Background>
    );
};

export default DoctorProfileForLogged;