package com.example.demo.service.visit;

import com.example.demo.dto.response.VisitResponse;
import com.example.demo.model.Visit;

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
    VisitResponse bookVisit(UUID doctorId, String appointmentTime);

    VisitResponse rescheduleVisit(UUID visitId, String newTime);

    void cancelVisit(UUID visitId);

    List<VisitResponse> getUserVisits();

    List<VisitResponse> getUpcomingUserVisits();
}