package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RaportResponse {
    private UUID id;
    private UUID visitId;
    private UUID doctorId;
    private UUID patientId;
    private String diagnosis;
    private String symptoms;
    private Double price;
    private LocalDateTime createdAt;
    private String notes;
}