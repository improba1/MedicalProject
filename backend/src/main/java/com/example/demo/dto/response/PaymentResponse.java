package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentResponse {

    private UUID id;
    private UUID visitId;
    private BigDecimal amount;
    private String status;
    private String stripeSessionId;
    private String stripePaymentIntentId;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}