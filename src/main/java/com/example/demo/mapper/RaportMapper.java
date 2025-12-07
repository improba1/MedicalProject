package com.example.demo.mapper;

import com.example.demo.dto.response.RaportResponse;
import com.example.demo.model.Raport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RaportMapper {

    public RaportResponse toResponse(Raport raport) {
        return RaportResponse.builder()
                .id(raport.getId())
                .diagnosis(raport.getDiagnosis())
                .symptoms(raport.getSymptoms())
                .price(raport.getPrice())
                .notes(raport.getNotes())
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
}