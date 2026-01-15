package com.example.demo.dto.request.medical_service;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicalServiceSearchRequest {
    private String name;
    private Boolean active;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}