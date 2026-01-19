package com.example.demo.dto.request.doctor_availability;

import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DoctorAvailabilityUpdateRequest {

    @Future(message = "Availability time must be in the future")
    private LocalDateTime newAvailableTime;
}