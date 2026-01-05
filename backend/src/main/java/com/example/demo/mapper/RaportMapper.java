package com.example.demo.mapper;

import com.example.demo.dto.request.raport.CreateRaportRequest;
import com.example.demo.dto.request.raport.UpdateRaportRequest;
import com.example.demo.dto.response.RaportResponse;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.Raport;
import com.example.demo.model.Visit;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RaportMapper {

    public RaportResponse toResponse(Raport raport) {
        return RaportResponse.builder()
                .id(raport.getId())
                .disease(raport.getDisease())
                .symptoms(raport.getSymptoms())
                .price(raport.getPrice())
                .notes(raport.getNotes())
                .createdAt(raport.getCreatedAt())
                .doctorId(raport.getDoctor().getId())
                .patientId(raport.getPatient().getId())
                .visitId(raport.getVisit().getId())
                .visitStatus(raport.getVisitStatus().name())
                .appointmentTime(raport.getVisit().getAppointmentTime())
                .build();
    }

    public List<RaportResponse> toResponseList(List<Raport> raports) {
        return raports.stream()
                .map(this::toResponse)
                .toList();
    }

    public Raport toEntity(CreateRaportRequest request, Doctor doctor, Patient patient, Visit visit) {
        return Raport.builder()
                .doctor(doctor)
                .patient(patient)
                .visit(visit)
                .disease(request.getDisease())
                .symptoms(request.getSymptoms())
                .price(request.getPrice())
                .notes(request.getNotes())
                .createdAt(LocalDateTime.now())
                .visitStatus(VisitStatus.valueOf(request.getVisitStatus()))
                .build();
    }

    public Raport updateEntity(Raport raport, UpdateRaportRequest request, Doctor doctor, Patient patient, Visit visit) {
        raport.setDoctor(doctor);
        raport.setPatient(patient);
        raport.setVisit(visit);
        if (request.getDisease() != null)
            raport.setDisease(request.getDisease());
        if (request.getSymptoms() != null)
            raport.setSymptoms(request.getSymptoms());
        if (request.getPrice() != null)
            raport.setPrice(request.getPrice());
        if (request.getNotes() != null)
            raport.setNotes(request.getNotes());
        if (request.getVisitStatus() != null)
            raport.setVisitStatus(VisitStatus.valueOf(request.getVisitStatus()));
        return raport;
    }
}