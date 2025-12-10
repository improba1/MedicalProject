package com.example.demo.mapper;

import com.example.demo.dto.request.raport.CreateRaportRequest;
import com.example.demo.dto.request.raport.UpdateRaportRequest;
import com.example.demo.dto.response.RaportResponse;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.Raport;
import com.example.demo.model.Visit;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                .build();
    }

    public List<RaportResponse> toResponseList(List<Raport> raports) {
        return raports.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Raport fromCreateRequest(CreateRaportRequest request, Doctor doctor, Patient patient, Visit visit) {
        return Raport.builder()
                .doctor(doctor)
                .patient(patient)
                .visit(visit)
                .disease(request.getDisease())
                .symptoms(request.getSymptoms())
                .price(request.getPrice())
                .notes(request.getNotes())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Raport fromUpdateRequest(Raport raport, UpdateRaportRequest request, Doctor doctor, Patient patient, Visit visit) {
        raport.setDoctor(doctor);
        raport.setPatient(patient);
        raport.setVisit(visit);
        raport.setDisease(request.getDisease());
        raport.setSymptoms(request.getSymptoms());
        raport.setPrice(request.getPrice());
        raport.setNotes(request.getNotes());
        // createdAt не змінюємо при оновленні
        return raport;
    }
}