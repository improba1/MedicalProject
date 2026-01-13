package com.example.demo.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class VisitServiceItemResponse {
    private UUID id;
    private UUID visitId;
    private UUID medicalServiceId;
    private String serviceName;
    private BigDecimal priceAtMomentOfPurchase;
    private Integer quantity;
}