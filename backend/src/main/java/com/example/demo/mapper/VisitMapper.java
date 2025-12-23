package com.example.demo.mapper;

import com.example.demo.dto.request.visit.CreateVisitRequest;
import com.example.demo.dto.request.visit.UpdateVisitRequest;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VisitMapper {

    // ============================
    // ENTITY → RESPONSE DTO
    // ============================
    public VisitResponse toResponse(Visit visit) {
        return VisitResponse.builder()
                .id(visit.getId())
                .doctorId(visit.getDoctor().getId())
                .patientId(visit.getPatient().getId())
                .appointmentTime(visit.getAppointmentTime())
                .status(visit.getStatus().name())
                .raportId(visit.getRaport() != null ? visit.getRaport().getId() : null)
                .build();
    }

    public List<VisitResponse> toResponseList(List<Visit> visits) {
        return visits.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ============================
    // CREATE DTO → ENTITY
    // ============================
    public Visit fromCreateRequest(CreateVisitRequest request) {
        // Ми не звертаємося до БД в мапері — але створюємо легкі об'єкти з id,
        // які сервіс потім резолвить повністю через репозиторії.
        Doctor doctor = new Doctor();
        doctor.setId(request.getDoctorId());

        Patient patient = new Patient();
        patient.setId(request.getPatientId());

        Raport raport = null;
        if (request.getRaportId() != null) {
            raport = new Raport();
            raport.setId(request.getRaportId());
        }

        return Visit.builder()
                .doctor(doctor)
                .patient(patient)
                .raport(raport)
                .appointmentTime(request.getAppointmentTime())
                .status(VisitStatus.valueOf(request.getVisitStatus().toUpperCase()))
                .build();
    }

    // ============================
    // UPDATE DTO → часткова ENTITY (має id та поля, які треба оновити)
    // ============================
    public Visit fromUpdateRequest(java.util.UUID id, UpdateVisitRequest request) {
        Visit v = new Visit();
        v.setId(id);

        if (request.getNewAppointmentTime() != null) {
            v.setAppointmentTime(request.getNewAppointmentTime());
        }

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            v.setStatus(VisitStatus.valueOf(request.getStatus().toUpperCase()));
        }

        return v;
    }
}