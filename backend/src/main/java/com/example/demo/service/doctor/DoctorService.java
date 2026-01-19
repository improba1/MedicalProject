package com.example.demo.service.doctor;

import com.example.demo.enums.Specialization;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    Doctor getById(UUID id);
    List<Doctor> searchDoctors(String name, Specialization specialization, Double rating, Boolean active);
    List<Doctor> searchPublicDoctors(String name, Specialization specialization, Double rating);

    // ADMIN
    Doctor create(Doctor doctor, MultipartFile image);
    Doctor updateDoctorByAdmin(UUID doctorId, Doctor updated, MultipartFile image);
    Doctor activateDoctor(UUID id);
    Doctor deactivateDoctor(UUID id);
    void delete(UUID id);

    // DOCTOR (тільки себе)
    Doctor getCurrentDoctor();
    Doctor updateCurrentDoctor(Doctor updated, MultipartFile image);
    Doctor updateCurrentDoctorImage(MultipartFile image);
    void deactivateCurrentDoctor(HttpServletRequest request, HttpServletResponse response);

    Patient getPatientIfDoctorHasAccess(UUID patientId);
}