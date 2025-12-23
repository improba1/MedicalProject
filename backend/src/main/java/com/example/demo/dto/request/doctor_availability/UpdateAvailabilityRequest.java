package com.example.demo.dto.request.doctor_availability;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateAvailabilityRequest {

    @NotNull
    @Future(message = "Availability time must be in the future")
    private LocalDateTime newAvailableTime;

    @NotNull
    private boolean isActive;
}