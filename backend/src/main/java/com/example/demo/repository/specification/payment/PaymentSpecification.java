package com.example.demo.repository.specification.payment;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Payment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentSpecification {

    public static Specification<Payment> byFilters(
            UUID visitId,
            UUID doctorId,
            UUID patientId,
            PaymentStatus status,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (visitId != null) {
                predicates.add(
                        cb.equal(root.get("visit").get("id"), visitId)
                );
            }

            if (doctorId != null) {
                predicates.add(
                        cb.equal(root.get("visit").get("doctor").get("id"), doctorId)
                );
            }

            if (patientId != null) {
                predicates.add(
                        cb.equal(root.get("visit").get("patient").get("id"), patientId)
                );
            }

            if (status != null) {
                predicates.add(
                        cb.equal(root.get("status"), status)
                );
            }

            if (minAmount != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("amount"), minAmount)
                );
            }

            if (maxAmount != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("amount"), maxAmount)
                );
            }

            if (start != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("createdAt"), start)
                );
            }

            if (end != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("createdAt"), end)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}