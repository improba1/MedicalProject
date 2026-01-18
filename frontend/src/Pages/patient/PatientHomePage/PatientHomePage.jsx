import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './PatientHomePage.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithProfile from '../../../Components/HeaderWithProfile/HeaderWithProfile';
import { publicDoctorApi } from '../../../Api/all/publicDoctorApi';

const PatientHomePage = () => {
    const navigate = useNavigate();
    const [doctors, setDoctors] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [showDropdown, setShowDropdown] = useState(false);
    
    // Состояния для управления результатами поиска
    const [isSpecializationActive, setIsSpecializationActive] = useState(false);
    const [activeSpecName, setActiveSpecName] = useState('');
    
    const dropdownRef = useRef(null);

    const specializations = [
        "CARDIOLOGIST", "DERMATOLOGIST", "NEUROLOGIST", 
        "PEDIATRICIAN", "PSYCHIATRIST", "SURGEON", 
        "ORTHOPEDIST", "OPHTHALMOLOGIST", "GYNECOLOGIST", "UROLOGIST"
    ];

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

        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setShowDropdown(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    // 1. Логика фильтрации специализаций для выпадающего списка
    // Если пользователь вводит что-то, что не похоже на специализацию (фамилию), список пустеет и скрывается
    const filteredSpecsSuggestions = specializations.filter(spec =>
        spec.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // 2. Логика отображения врачей в гриде
    const getDisplayedDoctors = () => {
        // Если была нажата кнопка Find по специализации
        if (isSpecializationActive) {
            return doctors.filter(doc => doc.specialization === activeSpecName);
        }
        // В остальных случаях (поиск по фамилии в реальном времени)
        return doctors.filter(doc => 
            doc.lastname.toLowerCase().includes(searchTerm.toLowerCase())
        );
    };

    const handleSelectSpecialization = (spec) => {
        setSearchTerm(spec);
        setShowDropdown(false);
    };

    const handleFindClick = () => {
        // Если текст в инпуте в точности совпадает с одной из специализаций
        if (specializations.includes(searchTerm.toUpperCase())) {
            setIsSpecializationActive(true);
            setActiveSpecName(searchTerm.toUpperCase());
        } else {
            // Если ищем просто по тексту (фамилии), сбрасываем режим специализации
            setIsSpecializationActive(false);
        }
        setShowDropdown(false);
    };

    const handleClearSearch = () => {
        setSearchTerm('');
        setIsSpecializationActive(false);
        setActiveSpecName('');
        setShowDropdown(false);
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithProfile />

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

                        <div className={styles.searchContainer} ref={dropdownRef}>
                            <div className={styles.inputWrapper}>
                                <svg className={styles.searchIcon} width="20" height="20" viewBox="0 0 24 24" fill="none">
                                    <path d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                </svg>
                                
                                <input 
                                    type="text" 
                                    placeholder="Search doctor or select specialization..." 
                                    className={styles.heroInput}
                                    value={searchTerm}
                                    onChange={(e) => {
                                        setSearchTerm(e.target.value);
                                        // Если пользователь стирает или меняет текст, сбрасываем заголовок "Specialization:"
                                        if (isSpecializationActive) setIsSpecializationActive(false);
                                    }}
                                    onFocus={() => setShowDropdown(true)}
                                />

                                {searchTerm && (
                                    <button className={styles.clearBtn} onClick={handleClearSearch}>
                                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                            <path d="M18 6L6 18M6 6l12 12" />
                                        </svg>
                                    </button>
                                )}
                            </div>
                            <button className={styles.heroSearchBtn} onClick={handleFindClick}>Find</button>

                            {/* Dropdown скрывается, если нет совпадений по специализациям (т.е. вводится фамилия) */}
                            {showDropdown && filteredSpecsSuggestions.length > 0 && (
                                <ul className={styles.dropdown}>
                                    {filteredSpecsSuggestions.map((spec, index) => (
                                        <li 
                                            key={index} 
                                            className={styles.dropdownItem}
                                            onClick={() => handleSelectSpecialization(spec)}
                                        >
                                            {spec}
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>
                    </header>

                    <section className={styles.doctorsSection}>
                        <div className={styles.sectionHeader}>
                            <h2>
                                {isSpecializationActive 
                                    ? `Specialization: ${activeSpecName}` 
                                    : "Top Rated Specialists"}
                            </h2>
                        </div>

                        <div className={styles.grid}>
                            {getDisplayedDoctors().length > 0 ? (
                                getDisplayedDoctors().map((doc) => (
                                    <DoctorCard key={doc.id} doctor={doc} />
                                ))
                            ) : (
                                <div className={styles.noData}>No doctors found.</div>
                            )}
                        </div>
                    </section>
                </div>
            </div>
        </AnimatedPage>
    );
};

const DoctorCard = ({ doctor }) => {
    const navigate = useNavigate();
    const imageUrl = doctor.image?.downloadUrl;

    const handleBookClick = (e) => {
        e.stopPropagation();
        // Мы предполагаем, что пользователь уже залогинен, 
        // так как он находится на домашней странице пациента
        navigate('/book-appointment', { state: { doctorData: doctor } });
    };

    return (
        <div 
            className={styles.card} 
            onClick={() => navigate('/doctor-profile-logged', { state: { doctorData: doctor } })}
        >
            <div className={styles.imageWrapper}>
                {imageUrl ? (
                    <img src={imageUrl} alt="Doctor" className={styles.docImage} />
                ) : (
                    <div className={styles.placeholderImage}>
                        {doctor.firstname[0]}{doctor.lastname[0]}
                    </div>
                )}
                <div className={styles.ratingBadge}>⭐ {doctor.rating}</div>
            </div>

            <div className={styles.cardContent}>
                <h3 className={styles.docName}>{doctor.firstname} {doctor.lastname}</h3>
                <span className={styles.specialization}>{doctor.specialization}</span>
                <div className={styles.tags}>
                    <span className={styles.tag}>{doctor.experienceYears} Yrs Exp.</span>
                </div>
                <button className={styles.bookBtn} onClick={handleBookClick}>
                    Book Appointment
                </button>
            </div>
        </div>
    );
};

export default PatientHomePage;