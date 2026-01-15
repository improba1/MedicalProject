package com.example.demo.repository.specification.doctor;

import com.example.demo.model.DoctorAvailability;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DoctorAvailabilitySpecification {

    public static Specification<DoctorAvailability> search(
            UUID doctorId,
            Boolean active,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (doctorId != null) {
                predicates.add(cb.equal(root.get("doctorId"), doctorId));
            }

            if (active != null) {
                predicates.add(cb.equal(root.get("isActive"), active));
            }

            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("availableTime"), from
                ));
            }

            if (to != null) {
                predicates.add(cb.lessThan(
                        root.get("availableTime"), to
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}