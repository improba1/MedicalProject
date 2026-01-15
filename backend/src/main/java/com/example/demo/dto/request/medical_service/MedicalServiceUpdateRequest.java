package com.example.demo.dto.request.medical_service;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicalServiceUpdateRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean active;
}