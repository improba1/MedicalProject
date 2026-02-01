import React, { useState, useEffect } from 'react';
import { Link, useParams, useLocation } from 'react-router-dom';
import styles from './NewRaport.module.css';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import BackBtn from '../../../Components/BackButton/BackButton';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import AnimatedInput from '../../../Components/AnimatedInput/AnimatedInput';
import { aiApi } from '../../../Api/doctor/aiApi';
import { doctorsNameApi } from '../../../Api/doctor/doctorsNameApi';

const NewRaport = (props) => {
    const { id } = useParams();
    const location = useLocation();
    const { patientName: statePatientName } = location.state || {}; // Get patient name from state

    // Identify patient name
    const [patientName, setPatientName] = useState(statePatientName || props.name || "Unknown Patient");
    const [doctorName, setDoctorName] = useState("Dr. Unknown");

    const [formData, setFormData] = useState({
        anamnesis: '',
        symptoms: '',
        diagnosis: '',
        treatment: '',
        additional: ''
    });

    const [aiFormData, setAiFormData] = useState({
        symptoms: '',
        sex: '',
        age: ''
    });
    const [aiResult, setAiResult] = useState('');
    const [loading, setLoading] = useState(false);

    const [hasLabTest, setHasLabTest] = useState(false);
    const [hasReferral, setHasReferral] = useState(false);

    // Load saved data & fetch doctor name & check referrals
    useEffect(() => {
        const fetchDoctor = async () => {
            try {
                const profile = await doctorsNameApi.getDoctorName();
                if (profile && profile.firstname && profile.lastname) {
                    setDoctorName(`Dr. ${profile.firstname} ${profile.lastname}`);
                }
            } catch (error) {
                console.error("Failed to fetch doctor profile", error);
            }
        };
        fetchDoctor();

        if (id) {
            const savedData = localStorage.getItem(`report_draft_${id}`);
            if (savedData) {
                setFormData(JSON.parse(savedData));
            }
            // Check flags
            if (localStorage.getItem(`labTestCreated_${id}`)) {
                setHasLabTest(true);
            }
            if (localStorage.getItem(`referralCreated_${id}`)) {
                setHasReferral(true);
            }
        }
    }, [id]);

    // Save data on change
    const handleChange = (e) => {
        const { name, value } = e.target;
        const updatedData = { ...formData, [name]: value };
        setFormData(updatedData);
        if (id) {
            localStorage.setItem(`report_draft_${id}`, JSON.stringify(updatedData));
        }
    };

    const handleAiChange = (e, name) => {
        setAiFormData(prev => ({ ...prev, [name]: e.target.value }));
    };

    const handleGenerateDiagnosis = async () => {
        if (!aiFormData.age || !aiFormData.sex || !aiFormData.symptoms) {
            setAiResult("Please fill in all fields (Symptoms, Sex, Age).");
            return;
        }

        setLoading(true);
        try {
            const ageInt = parseInt(aiFormData.age, 10);
            const gender = aiFormData.sex.toLowerCase();
            const symptomsArray = aiFormData.symptoms.split(',').map(s => s.trim()).filter(s => s.length > 0);

            const payload = {
                age: ageInt,
                gender: gender,
                symptoms: symptomsArray
            };

            const result = await aiApi.generateDiagnosis(payload);

            if (result && result.possible_diseases && Array.isArray(result.possible_diseases)) {
                const formattedResult = result.possible_diseases
                    .map(disease => `${disease.icd_code} ${disease.name}`)
                    .join('\n');
                setAiResult(formattedResult);
            } else {
                setAiResult(result.diagnosis || JSON.stringify(result, null, 2));
            }
        } catch (error) {
            console.error("AI Diagnosis Error:", error);
            setAiResult("Error generating diagnosis. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Background>
            <LogOutBtn></LogOutBtn>
            <HealthcareTxt></HealthcareTxt>
            <BackBtn></BackBtn>
            <MyProfileBtn></MyProfileBtn>

            <span className={styles.newraport}>Edit raport: <span className={styles.patientname}>{patientName}</span></span>
            <div className={styles.placeholder}>
                <input
                    className={styles.anamnesis}
                    type="text"
                    placeholder="Anamnesis"
                    name="anamnesis"
                    value={formData.anamnesis}
                    onChange={handleChange}
                ></input>
                <input
                    className={styles.symptoms}
                    type="text"
                    placeholder="Symptoms"
                    name="symptoms"
                    value={formData.symptoms}
                    onChange={handleChange}
                ></input>
                <input
                    className={styles.diagnosis}
                    type="text"
                    placeholder="Diagnosis"
                    name="diagnosis"
                    value={formData.diagnosis}
                    onChange={handleChange}
                ></input>
                <input
                    className={styles.treatment}
                    type="text"
                    placeholder="Treatment"
                    name="treatment"
                    value={formData.treatment}
                    onChange={handleChange}
                ></input>
            </div>
            <textarea
                className={styles.additional}
                type="text"
                placeholder="Additional notes"
                name="additional"
                value={formData.additional}
                onChange={handleChange}
            ></textarea>

            <Link to="/lab-test" state={{ reportId: id }}>
                <button className={styles.lab}>
                    {hasLabTest ? "Lab test Referral (Added)" : "Lab test referral"}
                </button>
            </Link>
            <Link to="/create-referral" state={{ reportId: id }}>
                <button className={styles.create}>
                    {hasReferral ? "Referral (Added)" : "Create referral"}
                </button>
            </Link>

            <div className={styles.aiContainer}>
                <h2 className={styles.aiTitle}>AI Assistant</h2>
                <AnimatedInput
                    label="Symptoms"
                    value={aiFormData.symptoms}
                    onChange={(e) => handleAiChange(e, 'symptoms')}
                    className={styles.aiInputFull}
                    placeholder="Describe symptoms..."
                />
                <div className={styles.aiRow}>
                    <AnimatedInput
                        label="Sex"
                        type="select"
                        value={aiFormData.sex}
                        onChange={(e) => handleAiChange(e, 'sex')}
                        className={styles.aiSelectInput}
                        options={[
                            { value: "", label: "Sex", disabled: true },
                            { value: "MALE", label: "Male" },
                            { value: "FEMALE", label: "Female" }
                        ]}
                    />
                    <AnimatedInput
                        label="Age"
                        type="number"
                        value={aiFormData.age}
                        onChange={(e) => handleAiChange(e, 'age')}
                        className={styles.aiInputSmall}
                        placeholder="Age"
                    />
                </div>
                <button
                    className={styles.aiButton}
                    onClick={handleGenerateDiagnosis}
                    disabled={loading}
                >
                    {loading ? "Analyzing..." : "Generate Diagnosis"}
                </button>
                <div className={styles.aiResultContainer}>
                    <textarea
                        className={styles.aiInputResult}
                        value={aiResult}
                        placeholder="AI suggestions will appear here..."
                        readOnly
                    />
                </div>
            </div>

            <Link
                to="/conclusion"
                state={{
                    reportData: formData,
                    patientName: patientName,
                    doctorName: doctorName,
                    hasLabTest: hasLabTest,
                    hasReferral: hasReferral
                }}
            >
                <button className={styles.next}>Next step</button>
            </Link>
        </Background>
    );
};

export default NewRaport;