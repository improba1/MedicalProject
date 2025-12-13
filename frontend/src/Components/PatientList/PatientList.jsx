import React, { useState } from 'react';
import styles from './PatientList.module.css';
import { useNavigate } from 'react-router-dom';

const PatientsList = () => {
    const navigate = useNavigate();
    const [patients, setPatients] = useState([
        { id: 1, firstName: "John", lastName: "Doe", age: 45, lastVisit: "2024-01-15" },
        { id: 2, firstName: "Anna", lastName: "Smith", age: 32, lastVisit: "2024-01-10" },
        { id: 3, firstName: "Michael", lastName: "Johnson", age: 58, lastVisit: "2024-01-05" },
        { id: 4, firstName: "Sarah", lastName: "Williams", age: 29, lastVisit: "2023-12-28" },
        { id: 5, firstName: "Robert", lastName: "Brown", age: 41, lastVisit: "2023-12-20" },
        { id: 6, firstName: "Emily", lastName: "Davis", age: 36, lastVisit: "2023-12-15" },
        { id: 7, firstName: "David", lastName: "Miller", age: 50, lastVisit: "2023-12-10" },
        { id: 8, firstName: "Lisa", lastName: "Wilson", age: 27, lastVisit: "2023-12-05" },
    ]);

    const handlePatientClick = (patientId) => {
        console.log('Selected patient:', patientId);
        navigate(`/my-patients/${patientId}`);
    };

    const handleAddPatient = () => {
        const newPatient = {
            id: patients.length + 1,
            firstName: `New`,
            lastName: `Patient`,
            age: Math.floor(Math.random() * 50) + 20,
            lastVisit: new Date().toISOString().split('T')[0]
        };
        setPatients([...patients, newPatient]);
    };

    return (
        <div className={styles.patientsContainer}>
            
            <div className={styles.patientsList}>
                {patients.length === 0 ? (
                    <div className={styles.emptyList}>No patients yet</div>
                ) : (
                    patients.map((patient) => (
                        <div 
                            key={patient.id} 
                            className={styles.patientCard}
                            onClick={() => handlePatientClick(patient.id)}
                        >
                            <div className={styles.patientName}>
                                {patient.firstName} <span className={styles.patientLastName}>{patient.lastName}</span>
                            </div>
                            <div className={styles.patientInfo}>
                                <span>Age: {patient.age}</span>
                                <span>Last visit: {patient.lastVisit}</span>
                            </div>
                        </div>
                    ))
                )}
            </div>
            
            <button 
                className={styles.addPatientBtn}
                onClick={handleAddPatient}
            >
                <span>+</span> Add New Patient
            </button>
        </div>
    );
};

export default PatientsList;