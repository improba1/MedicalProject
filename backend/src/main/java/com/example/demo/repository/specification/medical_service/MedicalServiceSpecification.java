package com.example.demo.repository.specification.medical_service;

import com.example.demo.model.MedicalService;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicalServiceSpecification {

    public static Specification<MedicalService> search(
            UUID doctorId,
            Boolean active,
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔹 doctorId
            if (doctorId != null) {
                predicates.add(cb.equal(root.get("doctor").get("id"), doctorId));
            }

            // 🔹 active
            if (active != null) {
                predicates.add(cb.equal(root.get("active"), active));
            }

            // 🔹 name (LIKE, case-insensitive)
            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            // 🔹 minPrice
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("price"), minPrice
                ));
            }

            // 🔹 maxPrice
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("price"), maxPrice
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}