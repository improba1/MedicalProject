import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import styles from './NewRaport.module.css';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import BackBtn from '../../../Components/BackButton/BackButton';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import AnimatedInput from '../../../Components/AnimatedInput/AnimatedInput';
import { aiApi } from '../../../Api/doctor/aiApi';

const NewRaport = (props) => {
    const [aiFormData, setAiFormData] = useState({
        symptoms: '',
        sex: '',
        age: ''
    });
    const [aiResult, setAiResult] = useState('');
    const [loading, setLoading] = useState(false);

    const handleAiChange = (e, name) => {
        setAiFormData(prev => ({ ...prev, [name]: e.target.value }));
    };

    const handleGenerateDiagnosis = async () => {
        // Basic Validation
        if (!aiFormData.age || !aiFormData.sex || !aiFormData.symptoms) {
            setAiResult("Please fill in all fields (Symptoms, Sex, Age).");
            return;
        }

        setLoading(true);
        try {
            // Data Formatting
            // 1. Age: Convert to integer
            const ageInt = parseInt(aiFormData.age, 10);

            // 2. Gender: Map 'MALE'/'FEMALE' to 'male'/'female'
            const gender = aiFormData.sex.toLowerCase();

            // 3. Symptoms: Convert string to array (split by comma)
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

            <span className={styles.newraport}>New raport: <span className={styles.patientname}>{props.name}</span></span>
            <div className={styles.placeholder}>
                <input className={styles.anamnesis} type="text" placeholder="Anamnesis"></input>
                <input className={styles.symptoms} type="text" placeholder="Symptoms"></input>
                <input className={styles.diagnosis} type="text" placeholder="Diagnosis"></input>
                <input className={styles.treatment} type="text" placeholder="Treatment"></input>
            </div>
            <textarea className={styles.additional} type="text" placeholder="Additional notes"></textarea>

            <Link to="/lab-test">
                <button className={styles.lab}>Lab test referral</button>
            </Link>
            <Link to="/create-referral">
                <button className={styles.create}>Create referral</button>
            </Link>

            {/* AI Section */}
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

            <Link to="/conclusion">
                <button className={styles.next}>Next step</button>
            </Link>
        </Background>
    );
};

export default NewRaport;