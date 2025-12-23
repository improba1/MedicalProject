package com.example.demo.mapper;

import com.example.demo.dto.request.doctor_availability.AddAvailabilityRequest;
import com.example.demo.dto.request.doctor_availability.UpdateAvailabilityRequest;
import com.example.demo.dto.response.DoctorAvailabilityResponse;
import com.example.demo.model.DoctorAvailability;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorAvailabilityMapper {

    // 🔹 Ентіті → DTO (Response)
    public DoctorAvailabilityResponse toResponse(DoctorAvailability availability) {
        if (availability == null) return null;
        return DoctorAvailabilityResponse.builder()
                .id(availability.getId())
                .doctorId(availability.getDoctorId())
                .availableTime(availability.getAvailableTime())
                .isActive(availability.isActive())
                .build();
    }

    public List<DoctorAvailabilityResponse> toResponseList(List<DoctorAvailability> availabilities) {
        return availabilities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 🔹 DTO (AddAvailabilityRequest) → Ентіті
    public DoctorAvailability toEntity(AddAvailabilityRequest request) {
        if (request == null) return null;
        return DoctorAvailability.builder()
                .isActive(true)
                .availableTime(request.getAvailableTime())
                .build();
    }

    // 🔹 DTO (UpdateAvailabilityRequest) → Ентіті (оновлення існуючого слота)
    public DoctorAvailability toEntity(UpdateAvailabilityRequest request) {
        if (request == null) return null;
        return DoctorAvailability.builder()
                .isActive(request.isActive())
                .availableTime(request.getNewAvailableTime())
                .build();
    }
}