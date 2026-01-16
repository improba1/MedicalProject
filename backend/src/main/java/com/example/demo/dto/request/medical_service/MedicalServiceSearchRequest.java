package com.example.demo.dto.request.medical_service;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MedicalServiceSearchRequest {
    private UUID doctorId;
    private String name;
    private Boolean active;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}