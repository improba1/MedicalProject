package com.example.demo.dto.request.visit;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VisitCreateByPatientRequest {

    @NotNull
    private UUID doctorId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime appointmentTime;

    @NotBlank
    private String patientSymptoms;
}