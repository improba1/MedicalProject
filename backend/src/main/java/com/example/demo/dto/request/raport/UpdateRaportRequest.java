package com.example.demo.dto.request.raport;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateRaportRequest {

    @NotBlank(message = "Disease is required")
    private String disease;

    @NotBlank(message = "Symptoms are required")
    private String symptoms;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private Double price;

    private String notes;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotNull(message = "Visit ID is required")
    private UUID visitId;
}