package com.example.demo.mapper;

import com.example.demo.dto.request.visit.VisitCreateRequest;
import com.example.demo.dto.request.visit.VisitUpdateRequest;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.demo.enums.VisitStatus.SCHEDULED;

@Component
public class VisitMapper {

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

    public Visit fromCreateRequest(VisitCreateRequest request) {
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
                .status(SCHEDULED)
                .build();
    }

    public Visit toUpdateEntity(UUID id, VisitUpdateRequest request) {
        Visit visit = new Visit();
        visit.setId(id);

        if (request.getNewAppointmentTime() != null) {
            visit.setAppointmentTime(request.getNewAppointmentTime());
        }

        if (request.getVisitStatus() != null && !request.getVisitStatus().isBlank()) {
            visit.setStatus(VisitStatus.valueOf(request.getVisitStatus().toUpperCase()));
        }

        return visit;
    }
}