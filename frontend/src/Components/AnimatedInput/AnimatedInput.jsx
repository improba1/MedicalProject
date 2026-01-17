import React, { useState } from 'react';
import styles from './AnimatedInput.module.css';

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

export default AnimatedInput;
