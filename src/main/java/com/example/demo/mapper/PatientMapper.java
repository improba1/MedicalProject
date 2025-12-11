package com.example.demo.mapper;

import com.example.demo.dto.request.patient.RegisterPatientRequest;
import com.example.demo.dto.request.patient.UpdatePatientRequest;
import com.example.demo.dto.response.PatientResponse;
import com.example.demo.enums.Role;
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
                .nickname(request.getNickname())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .birthDate(request.getBirthDate())
                .sex(request.getSex())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .address(request.getAddress())
                .role(Role.PATIENT)
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
        return PatientResponse.builder()
                .id(patient.getId())
                .nickname(patient.getNickname())
                .firstname(patient.getFirstname())
                .lastname(patient.getLastname())
                .birthDate(patient.getBirthDate())
                .sex(patient.getSex() != null ? patient.getSex().name() : null)
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .build();
    }

    public List<PatientResponse> toResponseList(List<Patient> patients) {
        return patients.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}