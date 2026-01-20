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

    // Состояния для управления заголовком и типом фильтрации
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

    // Логика отображения врачей
    const getDisplayedDoctors = () => {
        if (isSpecializationActive) {
            // Если активирован режим специализации, фильтруем строго по ней
            return doctors.filter(doc => doc.specialization.toUpperCase() === activeSpecName.toUpperCase());
        }
        // Иначе фильтруем по фамилии (текстовый поиск)
        return doctors.filter(doc =>
            doc.lastname.toLowerCase().includes(searchTerm.toLowerCase())
        );
    };

    const handleSelectSpecialization = (spec) => {
        setSearchTerm(spec);
        setShowDropdown(false);
    };

    const handleFindClick = () => {
        const upperSearch = searchTerm.toUpperCase();
        // Если введенный текст есть в списке специализаций
        if (specializations.includes(upperSearch)) {
            setIsSpecializationActive(true);
            setActiveSpecName(upperSearch);
        } else {
            // Если это имя или текст не из списка — обычный режим
            setIsSpecializationActive(false);
            setActiveSpecName('');
        }
        setShowDropdown(false);
    };

    const handleClearSearch = () => {
        setSearchTerm('');
        setIsSpecializationActive(false);
        setActiveSpecName('');
        setShowDropdown(false);
    };

    // Предложения специализаций фильтруются по вводу
    const filteredSpecsSuggestions = specializations.filter(spec =>
        spec.toLowerCase().includes(searchTerm.toLowerCase())
    );

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
                            Connect with top-rated specialists in seconds. <br />
                            Modern healthcare management powered by AI.
                        </p>

                        {/* НОВАЯ КНОПКА ЗДЕСЬ */}
                        <div className={styles.centerAction}>
                            <button
                                className={styles.mainVisitsBtn}
                                onClick={() => navigate('/upcoming-visits')}
                            >
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" style={{ marginRight: '8px' }}>
                                    <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                                    <line x1="16" y1="2" x2="16" y2="6"></line>
                                    <line x1="8" y1="2" x2="8" y2="6"></line>
                                    <line x1="3" y1="10" x2="21" y2="10"></line>
                                </svg>
                                Check My Upcoming Visits
                            </button>
                        </div>

                        <div className={styles.searchContainer} ref={dropdownRef}>
                            <div className={styles.inputWrapper}>
                                <svg className={styles.searchIcon} width="20" height="20" viewBox="0 0 24 24" fill="none">
                                    <path d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                                </svg>

                                <input
                                    type="text"
                                    placeholder="Search doctor or select specialization..."
                                    className={styles.heroInput}
                                    value={searchTerm}
                                    onChange={(e) => {
                                        setSearchTerm(e.target.value);
                                        // При начале печати сбрасываем "режим специализации" для заголовка
                                        if (isSpecializationActive) setIsSpecializationActive(false);
                                        setShowDropdown(true);
                                    }}
                                    onFocus={() => setShowDropdown(true)}
                                />

                                {/* Кнопка крестика для очистки */}
                                {searchTerm && (
                                    <button className={styles.clearBtn} onClick={handleClearSearch} type="button">
                                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
                                            <path d="M18 6L6 18M6 6l12 12" />
                                        </svg>
                                    </button>
                                )}
                            </div>
                            <button className={styles.heroSearchBtn} onClick={handleFindClick}>Find</button>

                            {/* Выпадающий список накладывается поверх */}
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

// ... Компонент DoctorCard остается без изменений

const DoctorCard = ({ doctor }) => {
    const navigate = useNavigate();
    const imageUrl = doctor.image?.downloadUrl;

    const handleBookClick = (e) => {
        e.stopPropagation();
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