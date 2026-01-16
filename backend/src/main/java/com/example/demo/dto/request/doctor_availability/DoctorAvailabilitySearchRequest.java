package com.example.demo.dto.request.doctor_availability;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DoctorAvailabilitySearchRequest {
    private UUID doctorId;
    private LocalDateTime from;
    private LocalDateTime to;
    private Boolean active;
}