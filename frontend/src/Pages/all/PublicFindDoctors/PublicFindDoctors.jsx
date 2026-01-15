import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './PublicFindDoctors.module.css';
import Background from '../../../Components/Background/Background';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import { publicDoctorApi } from '../../../Api/all/publicDoctorApi';

const Home = () => {
    const navigate = useNavigate();
    const [doctors, setDoctors] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const loadDoctors = async () => {
            try {
                const response = await publicDoctorApi.getAllDoctors();
                setDoctors(response.data.data || []);
            } catch (error) {
                console.error("Failed to load doctors", error);
            }
        };
        loadDoctors();
    }, []);

    const filteredDoctors = doctors.filter(doc => 
        doc.lastname.toLowerCase().includes(searchTerm.toLowerCase()) ||
        doc.specialization.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className={styles.pageContainer}>
            <nav className={styles.navbar}>
                <div className={styles.authButtons}>
                    <Link to="/login" className={styles.loginLink}>Log In</Link>
                    <Link to="/signUpForm">
                        <button className={styles.signupBtn}>Sign Up</button>
                    </Link>
                </div>
            </nav>

            <div className={styles.scrollWrapper}>
                <header className={styles.heroSection}>
                    <h1 className={styles.heroTitle}>
                        Your Health, <br />
                        <span className={styles.highlight}>Our Priority.</span>
                    </h1>
                    <p className={styles.heroSubtitle}>
                        Connect with top-rated specialists in seconds. <br/>
                        Modern healthcare management powered by AI.
                    </p>

                    <div className={styles.searchContainer}>
                        <div className={styles.inputWrapper}>
                            <svg className={styles.searchIcon} width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                            </svg>
                            <input 
                                type="text" 
                                placeholder="Search doctor, specialization..." 
                                className={styles.heroInput}
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                            />
                        </div>
                        <button className={styles.heroSearchBtn}>Find</button>
                    </div>
                </header>

                <section className={styles.doctorsSection}>
                    <div className={styles.sectionHeader}>
                        <h2>Top Rated Specialists</h2>
                    </div>

                    <div className={styles.grid}>
                        {filteredDoctors.length > 0 ? (
                            filteredDoctors.map((doc) => (
                                <DoctorCard key={doc.id} doctor={doc} />
                            ))
                        ) : (
                            <div className={styles.noData}>No doctors found.</div>
                        )}
                    </div>
                </section>
            </div>
        </div>
    );
};

const DoctorCard = ({ doctor }) => {
    const navigate = useNavigate();
    const imageUrl = doctor.image?.downloadUrl;

    const checkIsLoggedIn = () => {
        return !!localStorage.getItem('acces_token'); 
    };

    const handleBookClick = (e) => {
        e.stopPropagation();

        if (!checkIsLoggedIn()) {
            navigate('/signUpForm');
        } else {
            console.log("User is logged in, proceed to booking");
        }
    };

    return (
        <div 
            className={styles.card} 
            onClick={() => navigate('/doctor-profile-public', { state: { doctorData: doctor } })}
        >
            <div className={styles.imageWrapper}>
                {imageUrl ? (
                    <img src={imageUrl} alt="Doc" className={styles.docImage} />
                ) : (
                    <div className={styles.placeholderImage}>
                        {doctor.firstname[0]}{doctor.lastname[0]}
                    </div>
                )}
                <div className={styles.ratingBadge}>
                    ⭐ {doctor.rating}
                </div>
            </div>

            <div className={styles.cardContent}>
                <h3 className={styles.docName}>{doctor.firstname} {doctor.lastname}</h3>
                <span className={styles.specialization}>{doctor.specialization}</span>
                
                <div className={styles.tags}>
                    <span className={styles.tag}>{doctor.experienceYears} Years Exp.</span>
                </div>

                <button className={styles.bookBtn} onClick={handleBookClick}>
                    Book Appointment
                </button>
                
            </div>
        </div>
    );
};

export default Home;