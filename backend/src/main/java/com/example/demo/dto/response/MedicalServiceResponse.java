package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class MedicalServiceResponse {
    private UUID id;
    private UUID doctorId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean active;
}