package com.example.demo.mapper;

import com.example.demo.dto.request.visit.VisitCreateByAdminRequest;
import com.example.demo.dto.request.visit.VisitCreateByPatientRequest;
import com.example.demo.dto.request.visit.VisitUpdateRequest;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VisitMapper {

    private final VisitServiceItemMapper itemMapper;

    /* ========= RESPONSE ========= */

    public VisitResponse toResponse(Visit visit) {
        return VisitResponse.builder()
                .id(visit.getId())
                .doctorId(visit.getDoctor() != null ? visit.getDoctor().getId() : null)
                .patientId(visit.getPatient() != null ? visit.getPatient().getId() : null)
                .appointmentTime(visit.getAppointmentTime())
                .status(visit.getStatus() != null ? visit.getStatus().name() : null)
                .raportId(
                        visit.getRaport() != null
                                ? visit.getRaport().getId()
                                : null
                )
                .services(itemMapper.toResponseList(visit.getServices()))
                .totalPrice(visit.getTotalPrice())
                .build();
    }

    public List<VisitResponse> toResponseList(List<Visit> visits) {
        return visits.stream()
                .map(this::toResponse)
                .toList();
    }

    /* ========= CREATE ========= */

    public Visit fromPatientCreateRequest(VisitCreateByPatientRequest req) {
        Visit visit = new Visit();

        // doctor і patient будуть підставлені в service
        Doctor doctor = new Doctor();
        doctor.setId(req.getDoctorId());
        visit.setDoctor(doctor);

        visit.setAppointmentTime(req.getAppointmentTime());
        visit.setPatientSymptoms(req.getPatientSymptoms());

        return visit;
    }

    public Visit fromAdminCreateRequest(VisitCreateByAdminRequest req) {
        Visit visit = new Visit();

        Doctor doctor = new Doctor();
        doctor.setId(req.getDoctorId());
        visit.setDoctor(doctor);

        Patient patient = new Patient();
        patient.setId(req.getPatientId());
        visit.setPatient(patient);

        visit.setAppointmentTime(req.getAppointmentTime());
        visit.setPatientSymptoms(req.getPatientSymptoms());

        return visit;
    }

    /* ========= UPDATE ========= */

    public Visit toUpdateEntity(UUID id, VisitUpdateRequest request) {
        Visit visit = new Visit();
        visit.setId(id);

        if (request.getNewAppointmentTime() != null) {
            visit.setAppointmentTime(request.getNewAppointmentTime());
        }

        if (request.getVisitStatus() != null && !request.getVisitStatus().isBlank()) {
            visit.setStatus(
                    VisitStatus.valueOf(request.getVisitStatus().toUpperCase())
            );
        }

        return visit;
    }
}