package com.example.demo.mapper;

import com.example.demo.dto.request.patient.RegisterPatientRequest;
import com.example.demo.dto.request.patient.UpdatePatientRequest;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.model.Patient;
import com.example.demo.model.Raport;
import com.example.demo.model.Visit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PatientMapper {

    public Patient toEntity(RegisterPatientRequest request) {
        return Patient.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .phone(request.getPhone())
                .address(request.getAddress())
                .isActive(true)
                .build();
    }

    public void updateEntity(Patient patient, UpdatePatientRequest request) {
        if (request.getEmail() != null) patient.setEmail(request.getEmail());
        if (request.getNickname() != null) patient.setNickname(request.getNickname());
        if (request.getPhone() != null) patient.setPhone(request.getPhone());
        if (request.getAddress() != null) patient.setAddress(request.getAddress());
    }

    public PatientResponse toResponse(Patient patient) {
        List<UUID> visitIds = patient.getVisits() != null
                ? patient.getVisits().stream().map(Visit::getId).collect(Collectors.toList())
                : List.of();

        List<UUID> raportIds = patient.getRaports() != null
                ? patient.getRaports().stream().map(Raport::getId).collect(Collectors.toList())
                : List.of();

        return PatientResponse.builder()
                .id(patient.getId())
                .firstname(patient.getFirstname())
                .lastname(patient.getLastname())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .visitIds(visitIds)
                .raportIds(raportIds)
                .build();
    }

    public List<PatientResponse> toResponseList(List<Patient> patients) {
        return patients.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
