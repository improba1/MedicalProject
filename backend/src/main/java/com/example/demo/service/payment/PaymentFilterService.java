package com.example.demo.service.payment;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaymentFilterService {

    List<Payment> filter(
            UUID visitId,
            UUID doctorId,
            UUID patientId,
            PaymentStatus status,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDateTime start,
            LocalDateTime end
    );
}
