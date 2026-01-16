package com.example.demo.service.payment;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaymentFilterService {
    Payment getPaymentById(UUID paymentId);
    Payment getPaymentByIdForAuthenticatedPatient(UUID paymentId);

    List<Payment> searchForAdmin(UUID visitId, UUID patientId, PaymentStatus status, BigDecimal minAmount, BigDecimal maxAmount, LocalDateTime start, LocalDateTime end);
    List<Payment> searchForUser(UUID visitId, PaymentStatus status, BigDecimal minAmount, BigDecimal maxAmount, LocalDateTime start, LocalDateTime end);
}
