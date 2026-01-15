package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RaportResponse {

    private UUID id;
    private UUID visitId;
    private String disease;
    private String symptoms;
    private String treatmentPlan;
    private String doctorNotes;
    private BigDecimal totalPrice;
    private String servicesSnapshot;
    private LocalDateTime createdAt;
    private String paymentReceipt;
}