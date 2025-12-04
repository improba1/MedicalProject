package com.example.demo.mapper.visit;

import com.example.demo.dto.response.VisitResponse;
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
                .build();
    }

    public List<VisitResponse> toResponseList(List<Visit> visits) {
        return visits.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}