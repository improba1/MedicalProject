import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './BookAppointment.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { doctorApi } from '../../../Api/doctor/AllDoctorsApi';

const BookAppointment = () => {
    const navigate = useNavigate();
    const dropdownRef = useRef(null); // Реф для отслеживания клика вне списка
    
    const [allSpecializations, setAllSpecializations] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [isOpen, setIsOpen] = useState(false);
    const [selectedSpec, setSelectedSpec] = useState(null);
    const [loading, setLoading] = useState(true);

    // Закрытие при клике вне компонента
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    useEffect(() => {
        const fetchSpecializations = async () => {
            try {
                const response = await doctorApi.getAllDoctors();
                
                if (response.status === 0 && response.data) {
                    const uniqueSpecs = [
                        ...new Set(response.data.map(doctor => doctor.specialization))
                    ].filter(Boolean).sort(); 
                    
                    setAllSpecializations(uniqueSpecs);
                }
            } catch (error) {
                console.error("Ошибка при загрузке специализаций:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchSpecializations();
    }, []);

    const filteredSpecs = allSpecializations.filter(spec =>
        spec?.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const handleSelect = (spec) => {
        setSearchQuery(spec);
        setSelectedSpec(spec);
        setIsOpen(false);
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithProfile />
                
                <main className={styles.mainContent}>
                    <h1 className={styles.title}>New appointment</h1>
                    
                    <div className={styles.card}>
                        <div className={styles.selectWrapper} ref={dropdownRef}>
                            <div className={styles.inputContainer}>
                                <input
                                    type="text"
                                    className={styles.selectInput}
                                    placeholder={loading ? "Loading..." : "Select specialization"}
                                    value={searchQuery}
                                    autoComplete="off"
                                    onChange={(e) => {
                                        setSearchQuery(e.target.value);
                                        setIsOpen(true);
                                        setSelectedSpec(null);
                                    }}
                                    onClick={() => setIsOpen(true)} // Открываем при клике
                                    onFocus={() => setIsOpen(true)} // Открываем при фокусе
                                    disabled={loading}
                                />
                                <div 
                                    className={`${styles.arrow} ${isOpen ? styles.arrowOpen : ''}`}
                                    onClick={() => setIsOpen(!isOpen)}
                                >
                                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                        <path d="M6 9l6 6 6-6" />
                                    </svg>
                                </div>
                            </div>

                            {/* Выпадающий список */}
                            {isOpen && (
                                <ul className={styles.dropdown}>
                                    {filteredSpecs.length > 0 ? (
                                        filteredSpecs.map((spec, index) => (
                                            <li 
                                                key={index} 
                                                className={styles.dropdownItem}
                                                onClick={() => handleSelect(spec)}
                                            >
                                                {spec}
                                            </li>
                                        ))
                                    ) : (
                                        <li className={styles.noResults}>
                                            {loading ? "Loading..." : "No results found"}
                                        </li>
                                    )}
                                </ul>
                            )}
                        </div>

                        <button 
                            className={styles.nextBtn}
                            onClick={() => navigate('/book-appointment/select-doctor', { state: { specialization: selectedSpec } })}
                            disabled={!selectedSpec || loading}
                        >
                            Next step
                        </button>
                    </div>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default BookAppointment;