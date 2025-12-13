import React, { useState } from 'react';
import Background from '../../Components/Background/Background';
import LogOutBtn from '../../Components/LogOutButton/LogOutButton';
import BackBtn from '../../Components/BackButton/BackButton';
import styles from './ManageSchedule.module.css';
import HealthcareTxt from '../../Components/HealthcareText/Healthcare';
import MyProfileBtn from '../../Components/MyProfileButton/MyProfileButton';

const ManageSchedule = () => {
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [timeSlots, setTimeSlots] = useState([]);
    const [newTimeSlot, setNewTimeSlot] = useState('');
    const [selectedTimeSlots, setSelectedTimeSlots] = useState([]);
    
    const timeOptions = [
        '08:00', '08:30', '09:00', '09:30', '10:00', '10:30',
        '11:00', '11:30', '12:00', '12:30', '13:00', '13:30',
        '14:00', '14:30', '15:00', '15:30', '16:00', '16:30',
        '17:00', '17:30', '18:00'
    ];

    // Добавить временной слот
    const addTimeSlot = (time) => {
        if (!timeSlots.includes(time)) {
            setTimeSlots([...timeSlots, time].sort());
        }
    };

    // Удалить временной слот
    const removeTimeSlot = (time) => {
        setTimeSlots(timeSlots.filter(slot => slot !== time));
    };

    // Выбрать время для массового добавления
    const handleTimeSelect = (time) => {
        if (selectedTimeSlots.includes(time)) {
            setSelectedTimeSlots(selectedTimeSlots.filter(t => t !== time));
        } else {
            setSelectedTimeSlots([...selectedTimeSlots, time]);
        }
    };

    // Добавить выбранные времена
    const addSelectedTimes = () => {
        const newSlots = [...new Set([...timeSlots, ...selectedTimeSlots])].sort();
        setTimeSlots(newSlots);
        setSelectedTimeSlots([]);
    };

    // Сохранить расписание
    const saveSchedule = () => {
        const schedule = {
            date: selectedDate.toISOString().split('T')[0],
            slots: timeSlots
        };
        console.log('Saving schedule:', schedule);
        alert(`Schedule saved for ${schedule.date} with ${schedule.slots.length} slots`);
    };

    // Форматирование даты
    const formatDate = (date) => {
        return date.toLocaleDateString('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    // Получить дату в формате YYYY-MM-DD для input type="date"
    const getDateString = (date) => {
        return date.toISOString().split('T')[0];
    };

    // Создать массив дней недели
    const getWeekDays = () => {
        const today = new Date();
        const days = [];
        
        for (let i = 0; i < 7; i++) {
            const day = new Date(today);
            day.setDate(today.getDate() + i);
            days.push(day);
        }
        
        return days;
    };

    return (
        <Background>
            <LogOutBtn />
            <BackBtn />
            <HealthcareTxt></HealthcareTxt>
            <MyProfileBtn></MyProfileBtn>
            
            <div >
                {/* Заголовок */}
                <h1 className={styles.pageTitle}>Manage Schedule</h1>
                <p className={styles.pageSubtitle}>Set your available time slots for appointments</p>
                
                <div className={styles.content}>
                    {/* Левая часть - Календарь */}
                    <div className={styles.calendarSection}>
                        <h2 className={styles.sectionTitle}>Select Date</h2>
                        
                        {/* Простой выбор даты */}
                        <div className={styles.dateInputContainer}>
                            <label className={styles.dateLabel}>Choose a date:</label>
                            <input
                                type="date"
                                value={getDateString(selectedDate)}
                                onChange={(e) => {
                                    setSelectedDate(new Date(e.target.value));
                                    setTimeSlots([]);
                                }}
                                min={getDateString(new Date())}
                                className={styles.dateInput}
                            />
                        </div>
                        
                        {/* Быстрые даты на неделю вперед */}
                        <div className={styles.quickDates}>
                            <h3 className={styles.subtitle}>Quick Dates</h3>
                            <div className={styles.daysGrid}>
                                {getWeekDays().map((day, index) => (
                                    <button
                                        key={index}
                                        className={`${styles.dayButton} ${
                                            getDateString(day) === getDateString(selectedDate) ? styles.daySelected : ''
                                        }`}
                                        onClick={() => {
                                            setSelectedDate(day);
                                            setTimeSlots([]);
                                        }}
                                    >
                                        <span className={styles.dayName}>
                                            {day.toLocaleDateString('en-US', { weekday: 'short' })}
                                        </span>
                                        <span className={styles.dayNumber}>
                                            {day.getDate()}
                                        </span>
                                    </button>
                                ))}
                            </div>
                        </div>
                        
                        {/* Выбранная дата */}
                        <div className={styles.selectedDate}>
                            <span className={styles.dateLabel}>Selected Date:</span>
                            <span className={styles.dateValue}>{formatDate(selectedDate)}</span>
                        </div>
                    </div>

                    {/* Правая часть - Выбор времени */}
                    <div className={styles.timeSection}>
                        <h2 className={styles.sectionTitle}>Available Time Slots</h2>
                        
                        {/* Быстрый выбор времени */}
                        <div className={styles.quickSelection}>
                            <h3 className={styles.subtitle}>Quick Selection</h3>
                            <div className={styles.timeGrid}>
                                {timeOptions.map((time) => (
                                    <button
                                        key={time}
                                        className={`${styles.timeOption} ${
                                            selectedTimeSlots.includes(time) ? styles.selected : ''
                                        }`}
                                        onClick={() => handleTimeSelect(time)}
                                    >
                                        {time}
                                    </button>
                                ))}
                            </div>
                            
                            <button 
                                className={styles.addSelectedBtn}
                                onClick={addSelectedTimes}
                                disabled={selectedTimeSlots.length === 0}
                            >
                                Add Selected Times ({selectedTimeSlots.length})
                            </button>
                        </div>

                        {/* Добавление конкретного времени */}
                        <div className={styles.customTime}>
                            <h3 className={styles.subtitle}>Add Custom Time</h3>
                            <div className={styles.customInput}>
                                <input
                                    type="time"
                                    value={newTimeSlot}
                                    onChange={(e) => setNewTimeSlot(e.target.value)}
                                    step="1800" // 30 минут
                                    className={styles.timeInput}
                                />
                                <button 
                                    className={styles.addBtn}
                                    onClick={() => newTimeSlot && addTimeSlot(newTimeSlot)}
                                    disabled={!newTimeSlot}
                                >
                                    Add
                                </button>
                            </div>
                        </div>

                        {/* Список выбранных слотов */}
                        <div className={styles.selectedSlots}>
                            <h3 className={styles.subtitle}>
                                Selected Slots for {selectedDate.toLocaleDateString()}
                                <span className={styles.slotCount}>({timeSlots.length})</span>
                            </h3>
                            
                            {timeSlots.length === 0 ? (
                                <p className={styles.noSlots}>No time slots selected yet</p>
                            ) : (
                                <div className={styles.slotsList}>
                                    {timeSlots.map((slot, index) => (
                                        <div key={index} className={styles.slotItem}>
                                            <span className={styles.slotTime}>{slot}</span>
                                            <button 
                                                className={styles.removeBtn}
                                                onClick={() => removeTimeSlot(slot)}
                                            >
                                                ×
                                            </button>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>

                        {/* Кнопка сохранения */}
                        <button 
                            className={styles.saveBtn}
                            onClick={saveSchedule}
                            disabled={timeSlots.length === 0}
                        >
                            Save Schedule for {selectedDate.toLocaleDateString()}
                        </button>
                    </div>
                </div>
            </div>
        </Background>
    );
};

export default ManageSchedule;