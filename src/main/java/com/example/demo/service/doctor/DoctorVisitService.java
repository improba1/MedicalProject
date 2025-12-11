package com.example.demo.service.doctor;

import com.example.demo.model.Visit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DoctorVisitService {
    // 🔹 Візити та доступність
    List<Visit> getVisits(UUID doctorId);
    Visit cancelVisit(UUID visitId);
    Visit rescheduleVisit(UUID visitId, LocalDateTime newTime);
    List<Visit> getOwnVisits();
    Visit cancelOwnVisit(UUID visitId);
    Visit rescheduleOwnVisit(UUID visitId, LocalDateTime newTime);
    Visit getOwnVisitById(UUID visitId);
}