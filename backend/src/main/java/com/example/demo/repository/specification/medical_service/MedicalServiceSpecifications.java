package com.example.demo.repository.specification.medical_service;

import com.example.demo.model.MedicalService;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class MedicalServiceSpecifications {

    public static Specification<MedicalService> hasDoctor(UUID doctorId) {
        return (root, query, cb) ->
                doctorId == null ? cb.conjunction()
                        : cb.equal(root.get("doctor").get("id"), doctorId);
    }

    public static Specification<MedicalService> hasNameLike(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank()
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<MedicalService> isActive(Boolean active) {
        return (root, query, cb) ->
                active == null ? cb.conjunction()
                        : cb.equal(root.get("active"), active);
    }

    public static Specification<MedicalService> onlyActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<MedicalService> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min != null && max != null)
                return cb.between(root.get("price"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("price"), min);
            return cb.lessThanOrEqualTo(root.get("price"), max);
        };
    }

    public static Specification<MedicalService> orderByNameAsc() {
        return (root, query, cb) -> {
            assert query != null;
            query.orderBy(cb.asc(root.get("name")));
            return cb.conjunction();
        };
    }
}