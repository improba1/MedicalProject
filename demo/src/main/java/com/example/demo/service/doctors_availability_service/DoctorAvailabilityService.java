package com.example.demo.service.doctors_availability_service;

import com.example.demo.dto.request.doctor_availability.AddAvailabilityRequest;
import com.example.demo.dto.request.doctor_availability.UpdateAvailabilityRequest;
import com.example.demo.model.DoctorAvailability;

import java.util.List;
import java.util.UUID;

public interface DoctorAvailabilityService {
    DoctorAvailability getById(UUID id);
    DoctorAvailability create(DoctorAvailability availability);
    DoctorAvailability update(UUID id, UpdateAvailabilityRequest request);
    void delete(UUID id);

    List<DoctorAvailability> getByDoctor(UUID doctorId);

    DoctorAvailability addAvailability(UUID doctorId, AddAvailabilityRequest request);

    // 🔹 Додаткові методи для безпечної роботи лікаря
    DoctorAvailability updateForDoctor(UUID doctorId, UUID availabilityId, UpdateAvailabilityRequest request);
    void deleteForDoctor(UUID doctorId, UUID availabilityId);

    // 🔹 Пошук за датами
    List<DoctorAvailability> getByDay(UUID doctorId, int year, int month, int day);
    List<DoctorAvailability> getByMonth(UUID doctorId, int year, int month);
    List<DoctorAvailability> getByYear(UUID doctorId, int year);
    List<DoctorAvailability> getToday(UUID doctorId);
    List<DoctorAvailability> getNextHour(UUID doctorId);
    List<DoctorAvailability> getThisWeek(UUID doctorId);
    List<DoctorAvailability> getNextWeek(UUID doctorId);
}