package com.example.demo.dto.request.payment;

import com.example.demo.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentSearchRequest {
    private UUID visitId;
    private UUID patientId;
    private PaymentStatus status;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private LocalDateTime start;
    private LocalDateTime end;
}