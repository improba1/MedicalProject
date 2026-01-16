package com.example.demo.repository.specification.payment;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Payment;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentSpecificationBuilder {

    public static Specification<Payment> build(
            UUID visitId,
            UUID patientId,
            PaymentStatus status,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return Specification.allOf(
                PaymentSpecifications.hasVisit(visitId),
                PaymentSpecifications.hasPatient(patientId),
                PaymentSpecifications.hasStatus(status),
                PaymentSpecifications.amountBetween(minAmount, maxAmount),
                PaymentSpecifications.createdBetween(start, end),
                PaymentSpecifications.orderByCreatedDesc()
        );
    }
}