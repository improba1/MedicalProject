package com.example.demo.dto.request.doctor_availability;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DoctorAvailabilityCreateRequest {

    @NotNull
    @Future(message = "Availability must be in the future")
    private LocalDateTime availableTime;
}