package com.example.demo.service.doctor;

import com.example.demo.model.DoctorAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DoctorAvailabilityService {

    DoctorAvailability createForDoctor(UUID doctorId, DoctorAvailability availability);
    DoctorAvailability updateForDoctor(UUID doctorId, DoctorAvailability availability);
    void deleteForDoctor(UUID doctorId, UUID availabilityId);

    DoctorAvailability getById(UUID id);
    DoctorAvailability getByIdForAuthenticatedDoctor(UUID id);
    DoctorAvailability create(DoctorAvailability availability);
    DoctorAvailability updateExisting(DoctorAvailability availability);
    void deleteOwn(UUID availabilityId);

    List<DoctorAvailability> searchForAdmin(UUID doctorId, Boolean active, LocalDateTime from, LocalDateTime to);
    List<DoctorAvailability> searchForAuthenticatedDoctor(Boolean active, LocalDateTime from, LocalDateTime to);
    List<DoctorAvailability> searchForAuthenticatedPatient(UUID doctorId, LocalDateTime from, LocalDateTime to);
}