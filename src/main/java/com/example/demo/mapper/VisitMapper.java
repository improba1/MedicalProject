package com.example.demo.mapper;

import com.example.demo.dto.request.visit.CreateVisitRequest;
import com.example.demo.dto.request.visit.UpdateVisitRequest;
import com.example.demo.dto.response.VisitResponse;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.Raport;
import com.example.demo.model.Visit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    // 🔹 Створення Visit з DTO
    public Visit fromCreateRequest(CreateVisitRequest request, Doctor doctor, Patient patient, Raport raport) {
        return Visit.builder()
                .doctor(doctor)
                .patient(patient)
                .raport(raport)
                .appointmentTime(request.getAppointmentTime())
                .status(VisitStatus.valueOf(request.getVisitStatus().toUpperCase()))
                .build();
    }

    // 🔹 Оновлення Visit з DTO
    public Visit fromUpdateRequest(Visit visit, UpdateVisitRequest request) {
        if (request.getNewAppointmentTime() != null) {
            visit.setAppointmentTime(request.getNewAppointmentTime());
        }
        if (request.getStatus() != null) {
            visit.setStatus(VisitStatus.valueOf(request.getStatus().toUpperCase()));
        }
        return visit;
    }
}