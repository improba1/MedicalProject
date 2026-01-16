package com.example.demo.repository.specification.raport;

import com.example.demo.model.Raport;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class RaportSpecifications {

    public static Specification<Raport> hasVisit(UUID visitId) {
        return (root, query, cb) ->
                visitId == null ? cb.conjunction()
                        : cb.equal(root.get("visit").get("id"), visitId);
    }

    public static Specification<Raport> hasDoctor(UUID doctorId) {
        return (root, query, cb) ->
                doctorId == null ? cb.conjunction()
                        : cb.equal(root.get("doctor").get("id"), doctorId);
    }

    public static Specification<Raport> hasPatient(UUID patientId) {
        return (root, query, cb) ->
                patientId == null ? cb.conjunction()
                        : cb.equal(root.get("patient").get("id"), patientId);
    }

    public static Specification<Raport> hasDiseaseLike(String disease) {
        return (root, query, cb) ->
                disease == null || disease.isBlank()
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("disease")), "%" + disease.toLowerCase() + "%");
    }

    public static Specification<Raport> hasReceipt(String receipt) {
        return (root, query, cb) ->
                receipt == null || receipt.isBlank()
                        ? cb.conjunction()
                        : cb.equal(root.get("paymentReceipt"), receipt);
    }

    public static Specification<Raport> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min != null && max != null)
                return cb.between(root.get("totalPrice"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("totalPrice"), min);
            return cb.lessThanOrEqualTo(root.get("totalPrice"), max);
        };
    }

    public static Specification<Raport> createdBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from != null && to != null)
                return cb.between(root.get("createdAt"), from, to);
            if (from != null)
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            return cb.lessThanOrEqualTo(root.get("createdAt"), to);
        };
    }

    public static Specification<Raport> orderByCreatedDesc() {
        return (root, query, cb) -> {
            assert query != null;
            query.orderBy(cb.desc(root.get("createdAt")));
            return cb.conjunction();
        };
    }
}