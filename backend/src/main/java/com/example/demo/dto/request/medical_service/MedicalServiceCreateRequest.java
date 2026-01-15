package com.example.demo.dto.request.medical_service;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicalServiceCreateRequest {
    private String name;
    private String description;
    private BigDecimal price;
}