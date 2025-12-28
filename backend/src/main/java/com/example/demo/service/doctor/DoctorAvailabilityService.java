package com.example.demo.service.doctor;

import com.example.demo.model.DoctorAvailability;

import java.util.List;
import java.util.UUID;

public interface DoctorAvailabilityService {

    // 🔹 CRUD для адміна
    DoctorAvailability createForDoctor(UUID doctorId, DoctorAvailability availability);
    DoctorAvailability updateForDoctor(UUID doctorId, DoctorAvailability availability);
    void deleteForDoctor(UUID doctorId, UUID availabilityId);

    // 🔹 CRUD для залогованого лікаря
    DoctorAvailability getById(UUID id);
    DoctorAvailability create(DoctorAvailability availability);
    DoctorAvailability updateExisting(DoctorAvailability availability);
    void deleteOwn(UUID availabilityId);

    // ==========================
    // 🔹 Слоти залогованого лікаря
    // ==========================

    List<DoctorAvailability> getOwnAvailabilities();     // всі слоти
    List<DoctorAvailability> getOwnActiveSlots();        // тільки активні слоти

    List<DoctorAvailability> getOwnByDay(int year, int month, int day);
    List<DoctorAvailability> getOwnByMonth(int year, int month);
    List<DoctorAvailability> getOwnByYear(int year);
    List<DoctorAvailability> getOwnToday();
    List<DoctorAvailability> getOwnNextHour();
    List<DoctorAvailability> getOwnThisWeek();
    List<DoctorAvailability> getOwnNextWeek();

    // ==========================
    // 🔹 Адмінські/загальні методи
    // ==========================

    List<DoctorAvailability> getByDoctor(UUID doctorId); // всі слоти
    List<DoctorAvailability> getActiveSlots(UUID doctorId); // тільки активні слоти

    List<DoctorAvailability> getByDay(UUID doctorId, int year, int month, int day);
    List<DoctorAvailability> getByMonth(UUID doctorId, int year, int month);
    List<DoctorAvailability> getByYear(UUID doctorId, int year);
    List<DoctorAvailability> getToday(UUID doctorId);
    List<DoctorAvailability> getNextHour(UUID doctorId);
    List<DoctorAvailability> getThisWeek(UUID doctorId);
    List<DoctorAvailability> getNextWeek(UUID doctorId);
}