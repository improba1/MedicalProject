package com.example.demo.mapper;

import com.example.demo.dto.request.doctor_availability.AddAvailabilityRequest;
import com.example.demo.dto.request.doctor_availability.UpdateAvailabilityRequest;
import com.example.demo.dto.response.DoctorAvailabilityResponse;
import com.example.demo.model.DoctorAvailability;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DoctorAvailabilityMapper {

    // ------------------ CREATE ------------------
    public DoctorAvailability toEntity(AddAvailabilityRequest request) {
        DoctorAvailability availability = new DoctorAvailability();
        availability.setAvailableTime(request.getAvailableTime());
        availability.setActive(true);
        return availability;
    }

    // ------------------ UPDATE ------------------
    public DoctorAvailability toUpdateEntity(
            UUID availabilityId,
            UpdateAvailabilityRequest request
    ) {
        DoctorAvailability availability = new DoctorAvailability();
        availability.setId(availabilityId);

        // 🔹 Оновлення часу
        if (request.getNewAvailableTime() != null) {
            availability.setAvailableTime(request.getNewAvailableTime());
        }

        // 🔹 Оновлення статусу (активний/неактивний)
        availability.setActive(request.isActive());

        return availability;
    }

    // ------------------ RESPONSE ------------------
    public DoctorAvailabilityResponse toResponse(DoctorAvailability availability) {
        return DoctorAvailabilityResponse.builder()
                .id(availability.getId())
                .doctorId(availability.getDoctorId())
                .availableTime(availability.getAvailableTime())
                .isActive(availability.isActive())
                .build();
    }

    public List<DoctorAvailabilityResponse> toResponseList(
            List<DoctorAvailability> availabilities
    ) {
        return availabilities.stream()
                .map(this::toResponse)
                .toList();
    }
}
