package com.example.demo.repository.specification.visit;

import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Visit;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VisitSpecification {

    public static Specification<Visit> byFilters(
            UUID doctorId,
            UUID patientId,
            VisitStatus status,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (doctorId != null) {
                predicates.add(
                        cb.equal(root.get("doctor").get("id"), doctorId)
                );
            }

            if (patientId != null) {
                predicates.add(
                        cb.equal(root.get("patient").get("id"), patientId)
                );
            }

            if (status != null) {
                predicates.add(
                        cb.equal(root.get("status"), status)
                );
            }

            if (start != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("appointmentTime"), start
                        )
                );
            }

            if (end != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("appointmentTime"), end
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}