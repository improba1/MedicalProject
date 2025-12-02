package com.example.demo.service.doctor;

import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.dto.request.doctor.UpdateDoctorRequest;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.model.Doctor;
import com.example.demo.model.DoctorAvailability;
import com.example.demo.model.Visit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DoctorService {

    // 🔹 CRUD (для адміна)
    Doctor getById(UUID id);
    List<Doctor> getAll();
    Doctor create(Doctor doctor);
    Doctor update(Doctor doctor);
    void delete(UUID id);

    // 🔹 Універсальний пошук
    List<Doctor> searchDoctors(DoctorSearchRequest request);

    // 🔹 Візити та доступність
    List<Visit> getVisits(UUID doctorId);
    List<DoctorAvailability> getAvailability(UUID doctorId);
    Visit cancelVisit(UUID visitId);
    Visit rescheduleVisit(UUID visitId, LocalDateTime newTime);

    // 🔹 Бізнес-логіка для профілю лікаря
    DoctorResponse getCurrentDoctorProfile();
    Doctor getAuthenticatedDoctor();
    DoctorResponse updateDoctorProfile(UpdateDoctorRequest request);
    void deleteDoctorProfile(HttpServletRequest request, HttpServletResponse response);
}
