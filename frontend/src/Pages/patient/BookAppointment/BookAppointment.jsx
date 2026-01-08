import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './BookAppointment.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';

const BookAppointment = () => {
    const navigate = useNavigate();
    
    // Состояния для поиска и выбора
    const [searchQuery, setSearchQuery] = useState('');
    const [isOpen, setIsOpen] = useState(false);
    const [selectedSpec, setSelectedSpec] = useState(null);

    // В будущем этот массив будет приходить из API
    const allSpecializations = [
        "Cardiology",
        "Neurology",
        "Therapy",
        "Dermatology",
        "Pediatrics",
        "Ophthalmology",
        "Surgery",
        "Psychiatry"
    ];

    // Фильтрация списка на основе ввода
    const filteredSpecs = allSpecializations.filter(spec =>
        spec.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const handleSelect = (spec) => {
        setSearchQuery(spec);
        setSelectedSpec(spec);
        setIsOpen(false);
    };

    const handleNextStep = () => {
        if (selectedSpec) {
            // Переход к выбору врача с передачей выбранной специализации
            navigate('/book-appointment/select-doctor', { state: { specialization: selectedSpec } });
        }
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithProfile />
                
                <main className={styles.mainContent}>
                    <h1 className={styles.title}>New appointment</h1>
                    
                    <div className={styles.card}>
                        <div className={styles.selectWrapper}>
                            <div className={styles.inputContainer}>
                                <input
                                    type="text"
                                    className={styles.selectInput}
                                    placeholder="Select specialization"
                                    value={searchQuery}
                                    onChange={(e) => {
                                        setSearchQuery(e.target.value);
                                        setIsOpen(true);
                                        setSelectedSpec(null); // Сбрасываем выбор при новом поиске
                                    }}
                                    onFocus={() => setIsOpen(true)}
                                />
                                <div className={`${styles.arrow} ${isOpen ? styles.arrowOpen : ''}`}>
                                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                        <path d="M6 9l6 6 6-6" />
                                    </svg>
                                </div>
                            </div>

                            {/* Выпадающий список */}
                            {isOpen && filteredSpecs.length > 0 && (
                                <ul className={styles.dropdown}>
                                    {filteredSpecs.map((spec, index) => (
                                        <li 
                                            key={index} 
                                            className={styles.dropdownItem}
                                            onClick={() => handleSelect(spec)}
                                        >
                                            {spec}
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>

                        <button 
                            className={styles.nextBtn}
                            onClick={handleNextStep}
                            disabled={!selectedSpec}
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