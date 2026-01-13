package com.example.demo.service.visit;

import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Visit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VisitService {

    // ============================================================
    // 🔹 BASIC CRUD (ADMIN / SYSTEM)
    // ============================================================

    Visit getById(UUID id);
    Visit createVisit(Visit visit);
    Visit updateVisit(Visit visitUpdate);
    void delete(UUID id);

    // ============================================================
    // 🔹 PATIENT ACTIONS
    // ============================================================

    Visit createVisitForAuthenticatedPatient(Visit visit);
    Visit cancelVisit(UUID visitId);
    Visit rescheduleVisit(UUID visitId, LocalDateTime newTime);

    List<Visit> searchVisitsForAuthenticatedPatient(
            UUID doctorId,
            VisitStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    // ============================================================
    // 🔹 DOCTOR ACTIONS
    // ============================================================

    Visit cancelVisitByDoctor(UUID visitId);
    Visit rescheduleVisitByDoctor(UUID visitId, LocalDateTime newTime);

    List<Visit> searchVisitsForAuthenticatedDoctor(
            UUID patientId,
            VisitStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    // ============================================================
    // 🔹 ADMIN ACTIONS
    // ============================================================

    List<Visit> searchVisitsForAdmin(
            UUID doctorId,
            UUID patientId,
            VisitStatus status,
            LocalDateTime start,
            LocalDateTime end
    );
}