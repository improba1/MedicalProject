package com.example.demo.mapper.doctor;

import com.example.demo.dto.response.DoctorAvailabilityResponse;
import com.example.demo.model.DoctorAvailability;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorAvailabilityMapper {

    public DoctorAvailabilityResponse toResponse(DoctorAvailability availability) {
        return DoctorAvailabilityResponse.builder()
                .id(availability.getId())
                .doctorId(availability.getDoctor().getId())
                .availableTime(availability.getAvailableTime())
                .build();
    }

    public List<DoctorAvailabilityResponse> toResponseList(List<DoctorAvailability> availabilities) {
        return availabilities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}