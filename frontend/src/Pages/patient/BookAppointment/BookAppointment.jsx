import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import styles from './BookAppointment.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { availableScheduleApi } from '../../../Api/patient/AvailableScheduleApi';
import { visitApi } from '../../../Api/patient/visitApi';

const BookAppointment = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const doctor = location.state?.doctorData;

    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
    const [slots, setSlots] = useState([]);
    const [selectedTime, setSelectedTime] = useState(null);
    const [symptoms, setSymptoms] = useState('');
    const [isSuccess, setIsSuccess] = useState(false);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (doctor) {
            const fetchSlots = async () => {
                setLoading(true);
                setSelectedTime(null); 
                try {
                    const response = await availableScheduleApi.searchSlots(doctor.id, selectedDate);
                    const allSlots = response.data || [];

                    const validSlots = allSlots
                        .filter(slot => slot.availableTime.startsWith(selectedDate))
                        .sort((a, b) => new Date(a.availableTime) - new Date(b.availableTime));

                    setSlots(validSlots);
                } catch (error) {
                    console.error("Error fetching slots", error);
                    setSlots([]); 
                } finally {
                    setLoading(false);
                }
            };
            fetchSlots();
        }
    }, [doctor, selectedDate]);

    const handleBooking = async () => {
        if (!selectedTime) return;

        try {
            const payload = {
                doctorId: doctor.id,
                appointmentTime: selectedTime,
                patientSymptoms: symptoms
            };
            await visitApi.createVisit(payload);
            setIsSuccess(true);
        } catch (error) {
            alert("Failed to book appointment. Please try again.");
        }
    };

    if (isSuccess) {
        return (
            <AnimatedPage>
                <div className={styles.successContainer}>
                    <div className={styles.successCard}>
                        <div className={styles.successIcon}>✓</div>
                        <h1>Congratulations!</h1>
                        <p>Your appointment with <strong>Dr. {doctor.lastname}</strong> has been successfully booked.</p>
                        <button className={styles.homeBtn} onClick={() => navigate('/patient')}>
                            Back to Home
                        </button>
                    </div>
                </div>
            </AnimatedPage>
        );
    }

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithProfile />
                <main className={styles.content}>
                    <div className={styles.bookingCard}>
                        <h1 className={styles.title}>Book an Appointment</h1>
                        
                        <div className={styles.doctorBrief}>
                            <div className={styles.miniAvatar}>
                                {doctor?.firstname[0]}{doctor?.lastname[0]}
                            </div>
                            <div>
                                <h3>Dr. {doctor?.firstname} {doctor?.lastname}</h3>
                                <span>{doctor?.specialization}</span>
                            </div>
                        </div>

                        <div className={styles.formSection}>
                            <label>1. Select Date</label>
                            <input 
                                type="date" 
                                className={styles.dateInput}
                                value={selectedDate}
                                min={new Date().toISOString().split('T')[0]}
                                onChange={(e) => setSelectedDate(e.target.value)}
                            />
                        </div>

                        <div className={styles.formSection}>
                            <label>2. Select Available Time</label>
                            <div className={styles.slotsGrid}>
                                {loading ? <p>Loading slots...</p> : 
                                 slots.length > 0 ? slots.map((slot, index) => (
                                    <button 
                                        key={slot.id}
                                        className={`${styles.slotBtn} ${selectedTime === slot.appointmentTime ? styles.activeSlot : ''}`}
                                        onClick={() => setSelectedTime(slot.availableTime)}
                                    >
                                        {new Date(slot.availableTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                                    </button>
                                )) : <p className={styles.noSlots}>No slots available for this date.</p>}
                            </div>
                        </div>

                        <div className={styles.formSection}>
                            <label>3. Describe your symptoms</label>
                            <textarea 
                                className={styles.textarea}
                                placeholder="Write briefly what bothers you..."
                                value={symptoms}
                                onChange={(e) => setSymptoms(e.target.value)}
                            />
                        </div>

                        <button 
                            className={styles.confirmBtn}
                            disabled={!selectedTime}
                            onClick={handleBooking}
                        >
                            Confirm Appointment
                        </button>
                    </div>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default BookAppointment;