package com.example.demo.repository.specification.payment;

import com.example.demo.enums.PaymentStatus;
import com.example.demo.model.Payment;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentSpecifications {

    public static Specification<Payment> hasVisit(UUID visitId) {
        return (root, query, cb) ->
                visitId == null ? cb.conjunction()
                        : cb.equal(root.get("visit").get("id"), visitId);
    }

    public static Specification<Payment> hasPatient(UUID patientId) {
        return (root, query, cb) ->
                patientId == null ? cb.conjunction()
                        : cb.equal(root.get("visit").get("patient").get("id"), patientId);
    }

    public static Specification<Payment> hasStatus(PaymentStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction()
                        : cb.equal(root.get("status"), status);
    }

    public static Specification<Payment> amountBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min != null && max != null)
                return cb.between(root.get("amount"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("amount"), min);
            return cb.lessThanOrEqualTo(root.get("amount"), max);
        };
    }

    public static Specification<Payment> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return cb.conjunction();
            if (start != null && end != null)
                return cb.between(root.get("createdAt"), start, end);
            if (start != null)
                return cb.greaterThanOrEqualTo(root.get("createdAt"), start);
            return cb.lessThanOrEqualTo(root.get("createdAt"), end);
        };
    }

    public static Specification<Payment> orderByCreatedDesc() {
        return (root, query, cb) -> {
            assert query != null;
            query.orderBy(cb.desc(root.get("createdAt")));
            return cb.conjunction();
        };
    }
}