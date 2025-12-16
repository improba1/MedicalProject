package com.example.demo.service.doctor;

import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.dto.request.doctor.UpdateDoctorRequest;
import com.example.demo.model.Doctor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    Doctor getById(UUID id);
    List<Doctor> getAll();
    Doctor create(Doctor doctor);
    Doctor update(Doctor doctor);
    void delete(UUID id);
    List<Doctor> searchDoctors(DoctorSearchRequest request);
    Doctor getCurrentDoctor();
    Doctor updateCurrentDoctor(Doctor doctor);
    Doctor updateOwnProfileWithImage(Doctor doctor, MultipartFile file);
    Doctor updateDoctorWithImage(UUID doctorId, UpdateDoctorRequest request, MultipartFile file);
    void deactivateDoctorProfile(HttpServletRequest request, HttpServletResponse response);
}
