package com.example.demo.dto.request.raport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RaportCreateRequest {

    @NotNull
    private UUID visitId;

    @NotBlank
    private String disease;

    @NotBlank
    private String treatmentPlan;

    private String doctorNotes;
}
