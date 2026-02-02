import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import styles from './BookAppointment.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { availableScheduleApi } from '../../../Api/patient/AvailableScheduleApi';
import { visitApi } from '../../../Api/patient/visitApi';
import { paymentApi } from '../../../Api/patient/paymentApi';
import { servicesApi } from '../../../Api/patient/ServicesApi';
import { cartApi } from '../../../Api/patient/CartApi';

const BookAppointment = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const doctor = location.state?.doctorData;

    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
    const [slots, setSlots] = useState([]);
    const [services, setServices] = useState([]); // Список доступных услуг
    const [selectedTime, setSelectedTime] = useState(null);
    const [selectedServiceIds, setSelectedServiceIds] = useState([]); // Выбранные услуги (массив)
    const [symptoms, setSymptoms] = useState('');
    const [loading, setLoading] = useState(false);

    // Загрузка слотов при смене даты
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
                    setSlots([]);
                } finally {
                    setLoading(false);
                }
            };
            fetchSlots();
        }
    }, [doctor, selectedDate]);

    // Загрузка услуг врача
    useEffect(() => {
        if (doctor) {
            const fetchServices = async () => {
                try {
                    const response = await servicesApi.searchServices(doctor.id);
                    setServices(response.data || []);
                } catch (error) {
                    console.error("Error fetching services", error);
                }
            };
            fetchServices();
        }
    }, [doctor]);

    const toggleService = (id) => {
        setSelectedServiceIds(prev => {
            if (prev.includes(id)) {
                return prev.filter(itemId => itemId !== id);
            } else {
                return [...prev, id];
            }
        });
    };

    const handleBooking = async () => {
        if (!selectedTime || selectedServiceIds.length === 0) {
            alert("Please select time and at least one service.");
            return;
        }

        setLoading(true);
        try {
            // 1. Создаем визит
            const payload = {
                doctorId: doctor.id,
                appointmentTime: selectedTime,
                patientSymptoms: symptoms
            };
            const visitResponse = await visitApi.createVisit(payload);
            const createdVisitId = visitResponse.data.id || visitResponse.id;

            if (createdVisitId) {
                // 2. Добавляем услуги в корзину
                for (const serviceId of selectedServiceIds) {
                    await cartApi.addItem(createdVisitId, serviceId);
                }

                // 3. Переходим на экран оплаты
                navigate('/payment-page', { state: { visitId: createdVisitId } });
            }
        } catch (error) {
            console.error(error);
            alert("Failed to process booking. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithProfile />
                <main className={styles.content}>
                    <div className={styles.bookingCard}>
                        <h1 className={styles.title}>Book an Appointment</h1>

                        <div className={styles.doctorBrief}>
                            <div className={styles.miniAvatar}>
                                {doctor?.firstname?.[0]}{doctor?.lastname?.[0]}
                            </div>
                            <div>
                                <h3>Dr. {doctor?.firstname} {doctor?.lastname}</h3>
                                <span>{doctor?.specialization}</span>
                            </div>
                        </div>

                        {/* Шаг 1: Дата */}
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

                        {/* Шаг 2: Время */}
                        <div className={styles.formSection}>
                            <label>2. Select Available Time</label>
                            <div className={styles.slotsGrid}>
                                {loading ? <p>Loading slots...</p> :
                                    slots.length > 0 ? slots.map((slot) => (
                                        <button
                                            key={slot.id}
                                            className={`${styles.slotBtn} ${selectedTime === slot.availableTime ? styles.activeSlot : ''}`}
                                            onClick={() => setSelectedTime(slot.availableTime)}
                                        >
                                            {new Date(slot.availableTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                        </button>
                                    )) : <p className={styles.noSlots}>No slots available for this date.</p>}
                            </div>
                        </div>

                        {/* Шаг 3: Выбор услуги (Множественный) */}
                        <div className={styles.formSection}>
                            <label>3. Select Services</label>
                            <div className={styles.servicesGrid}>
                                {services.length > 0 ? services.map((service) => {
                                    const isSelected = selectedServiceIds.includes(service.id);
                                    return (
                                        <div
                                            key={service.id}
                                            className={`${styles.serviceCard} ${isSelected ? styles.activeService : ''}`}
                                            onClick={() => toggleService(service.id)}
                                        >
                                            <span className={styles.serviceName}>{service.name}</span>
                                            <span className={styles.servicePrice}>{service.price} PLN</span>
                                        </div>
                                    );
                                }) : <p className={styles.noSlots}>No services available.</p>}
                            </div>
                        </div>

                        {/* Шаг 4: Симптомы */}
                        <div className={styles.formSection}>
                            <label>4. Describe your symptoms</label>
                            <textarea
                                className={styles.textarea}
                                placeholder="Write briefly what bothers you..."
                                value={symptoms}
                                onChange={(e) => setSymptoms(e.target.value)}
                            />
                        </div>

                        <button
                            className={styles.confirmBtn}
                            disabled={!selectedTime || !selectedServiceIds.length || loading}
                            onClick={handleBooking}
                        >
                            {loading ? "Processing..." : "Proceed to Payment"}
                        </button>
                    </div>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default BookAppointment;