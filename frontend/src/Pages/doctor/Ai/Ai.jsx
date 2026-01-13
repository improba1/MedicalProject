import React, { useState } from 'react';
import styles from './Ai.module.css';
import Background from '../../../Components/Background/Background';
import BackBtn from '../../../Components/BackButton/BackButton';
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';

const AnimatedInput = ({ label, value, onChange, type = "text", placeholder, className, options }) => {
    const [isFocused, setIsFocused] = useState(false);

    const showLabel = isFocused || value !== "";

    return (
        <div className={styles.inputGroup}>
            <label className={`${styles.label} ${showLabel ? styles.labelVisible : ''}`}>
                {label}
            </label>

            {type === 'select' ? (
                <select 
                    className={className} 
                    value={value}
                    onChange={onChange}
                    onFocus={() => setIsFocused(true)}
                    onBlur={() => setIsFocused(false)}
                >
                    {options.map(opt => (
                        <option key={opt.value} value={opt.value} disabled={opt.disabled}>
                            {opt.label}
                        </option>
                    ))}
                </select>
            ) : (
                <input 
                    className={className} 
                    type={type} 
                    value={value}
                    onChange={onChange}
                    placeholder={isFocused ? "" : placeholder} 
                    onFocus={() => setIsFocused(true)}
                    onBlur={() => setIsFocused(false)}
                />
            )}
        </div>
    );
};

const Ai = () => {
    const [formData, setFormData] = useState({
        symptoms: '',
        sex: '',
        age: ''
    });

    const handleChange = (e, name) => {
        setFormData(prev => ({ ...prev, [name]: e.target.value }));
    };

    return (
        <Background>
            <BackBtn />
            <HealthcareTxt />
            <LogOutBtn />
            <MyProfileBtn />
            
            <div className={styles.wrapper}>
                <h1 className={styles.title}>Make diagnosis with AI</h1>
                
                <div className={styles.container}>
                    
                    <AnimatedInput 
                        label="Symptoms"
                        value={formData.symptoms}
                        onChange={(e) => handleChange(e, 'symptoms')}
                        className={styles.inputFull}
                        placeholder="Describe what you feel..."
                    />

                    <div className={styles.row}>
                        <AnimatedInput 
                            label="Sex"
                            type="select"
                            value={formData.sex}
                            onChange={(e) => handleChange(e, 'sex')}
                            className={styles.selectInput}
                            options={[
                                { value: "", label: "Sex", disabled: true }, 
                                { value: "MALE", label: "Male" },
                                { value: "FEMALE", label: "Female" }
                            ]}
                        />

                        <AnimatedInput 
                            label="Age"
                            type="number"
                            value={formData.age}
                            onChange={(e) => handleChange(e, 'age')}
                            className={styles.inputSmall}
                            placeholder="Age"
                        />
                    </div>

                    <button className={styles.button}>
                        Generate Diagnosis
                    </button>

                    <div className={styles.resultContainer}>
                        <label className={styles.labelResult}>AI Result:</label>
                        <input 
                            className={styles.inputResult} 
                            type="text" 
                            placeholder="Possible diagnosis will appear here..." 
                            readOnly 
                        />
                    </div>
                </div>
            </div>
        </Background>
    );
};

export default Ai;