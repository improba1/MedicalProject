package com.example.demo.dto.request.raport;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateRaportRequest {

    @NotNull
    private UUID visitId;

    @NotBlank
    private String disease;

    @NotBlank(message = "Visit status is required")
    private String visitStatus;

    @NotBlank
    private String symptoms;

    @NotNull
    @DecimalMin("0.0")
    private Double price;

    private String notes;
}