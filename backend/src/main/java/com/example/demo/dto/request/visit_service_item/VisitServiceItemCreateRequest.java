package com.example.demo.dto.request.visit_service_item;

import lombok.Data;

import java.util.UUID;

@Data
public class VisitServiceItemCreateRequest {
    private UUID visitId;
    private UUID medicalServiceId;
    private Integer quantity;
}