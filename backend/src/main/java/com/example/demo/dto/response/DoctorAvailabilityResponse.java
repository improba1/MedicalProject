package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class DoctorAvailabilityResponse {
    private UUID id;
    private UUID doctorId;
    private LocalDateTime availableTime;
    private boolean isActive;
}