package com.example.demo.service.doctor;

import com.example.demo.dto.request.doctor_availability.DoctorAvailabilitySearchRequest;
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

    List<DoctorAvailability> getByDoctor(UUID doctorId); // всі слоти
    List<DoctorAvailability> searchForAdmin(UUID doctorId, DoctorAvailabilitySearchRequest req);
    List<DoctorAvailability> searchForAuthenticatedDoctor(DoctorAvailabilitySearchRequest req);
    List<DoctorAvailability> searchPublic(DoctorAvailabilitySearchRequest req);
}