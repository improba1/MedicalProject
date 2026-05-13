import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './PublicFindDoctors.module.css';
import { publicDoctorApi } from '../../../Api/all/publicDoctorApi';
import { FaCheckCircle } from "react-icons/fa";
import doctorsImage1 from '../../../Assets/doctors1.jpg';
import doctorsImage2 from '../../../Assets/doctor2.jpg';
import Register from '../LoginForm/LoginForm';

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
                <div className={styles.logo}>Health<span>Care</span></div>
                <div className={styles.authButtons}>
                    <Link to="/login" className={styles.loginLink}>Log In</Link>
                    <Link to="/signUpForm">
                        <button className={styles.signupBtn}>Sign Up</button>
                    </Link>
                </div>
            </nav>

            <div className={styles.contentWrapper}>
                
                <section className={styles.splitSection}>
                    <div className={styles.imageColumn}>
                        <img src={doctorsImage1} className={styles.placeholderLargeImage}/>
                    </div>
                    <div className={styles.textColumn}>
                        <h4 className={styles.subHeading}>ABOUT US</h4>
                        <h2 className={styles.mainHeading}>What Makes Us Special</h2>
                        <p className={styles.description}>
                            At HealthCare, we are dedicated to providing compassionate and high-quality healthcare services to our community.
                        </p>
                        
                        <div className={styles.featureList}>
                            <div className={styles.featureItem}>
                                <FaCheckCircle className={styles.checkIcon} />
                                <div>
                                    <h5>Patient-Centered Care</h5>
                                    <p>We believe in personalized care, focusing on your needs.</p>
                                </div>
                            </div>
                            <div className={styles.featureItem}>
                                <FaCheckCircle className={styles.checkIcon} />
                                <div>
                                    <h5>State-of-the-Art Facilities</h5>
                                    <p>Our modern facilities are equipped with state-of-the-art technology.</p>
                                </div>
                            </div>
                            <div className={styles.featureItem}>
                                <FaCheckCircle className={styles.checkIcon} />
                                <div>
                                    <h5>Experienced Healthcare Team</h5>
                                    <p>Our team of experienced physicians is committed to delivering quality.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                {/* СЕКЦИЯ 2: ПОЧЕМУ МЫ */}
                <section className={`${styles.splitSection} ${styles.reverse} ${styles.whyChooseBg}`}>
                    <div className={styles.textColumn}>
                        <h4 className={styles.subHeading}>WHY CHOOSE US</h4>
                        <h2 className={styles.mainHeading}>Why Choose HealthCare</h2>
                        <p className={styles.description}>
                            At HealthCare, we are dedicated to providing compassionate and high-quality healthcare services to our community.
                        </p>
                        
                        <ul className={styles.bulletList}>
                            <li><FaCheckCircle className={styles.checkIconSmall} /> Professional Medical Team</li>
                            <li><FaCheckCircle className={styles.checkIconSmall} /> 24/7 Medical Support</li>
                            <li><FaCheckCircle className={styles.checkIconSmall} /> 95% Patient Satisfaction Rate</li>
                            <li><FaCheckCircle className={styles.checkIconSmall} /> Cutting-Edge Medical Technology</li>
                        </ul>

                        <Link to='/login' className={styles.primaryBtn}>Book Appointment Now</Link>
                    </div>
                </section>

                {/* СЕКЦИЯ 3: ВРАЧИ И ПОИСК */}
                <section className={styles.doctorsSection}>
                    <div className={styles.sectionHeader}>
                        <h4 className={styles.subHeading}>OUR DOCTORS</h4>
                        <h2 className={styles.mainHeading}>Meet Our Professional Doctors</h2>
                        <p className={styles.descriptionCentered}>
                            Get to know the dedicated physicians who form the backbone of MedifyCare.
                        </p>
                    </div>

                    {/* Поиск оставил, чтобы не ломать функционал */}
                    <div className={styles.searchContainer}>
                        <input 
                            type="text" 
                            placeholder="Search doctor, specialization..." 
                            className={styles.searchInput}
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>

                    <div className={styles.grid}>
                        {filteredDoctors.length > 0 ? (
                            filteredDoctors.map((doc) => (
                                <DoctorCard key={doc.id} doctor={doc} />
                            ))
                        ) : (
                            <div className={styles.noData}>No doctors found matching your search.</div>
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
        return !!localStorage.getItem('access_token'); 
    };

    const handleBookClick = (e) => {
        e.stopPropagation();
        if (!checkIsLoggedIn()) {
            navigate('/login');
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
            </div>

            <div className={styles.cardContent}>
                <h3 className={styles.docName}>{doctor.firstname} {doctor.lastname}</h3>
                <span className={styles.specialization}>{doctor.specialization}</span>
                
                <div className={styles.ratingBox}>
                    <span className={styles.star}>⭐</span>
                    <span className={styles.ratingNumber}>{doctor.rating || '5.0'}</span>
                    <span className={styles.reviews}>(120 Reviews)</span>
                </div>
            </div>
        </div>
    );
};

export default Home;