package com.example.demo.service.visit;

import com.example.demo.model.Visit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VisitService {

    Visit getById(UUID id);

    Visit create(Visit visit);

    Visit update(Visit visit);

    void delete(UUID id);

    List<Visit> getByDoctor(UUID doctorId);

    List<Visit> getByPatient(UUID patientId);

    // 🔹 нові методи для пацієнта
    Visit bookVisit(UUID doctorId, LocalDateTime appointmentTime);

    Visit rescheduleVisit(UUID visitId, LocalDateTime newTime);

    Visit cancelVisit(UUID visitId);

    List<Visit> getUserVisits();

    List<Visit> getUpcomingUserVisits();
}