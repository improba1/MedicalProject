package com.example.demo.dto.request.doctor_availability;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DoctorAvailabilitySearchRequest {
    private LocalDateTime from;
    private LocalDateTime to;
    private Boolean active;
}