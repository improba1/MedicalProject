package com.example.demo.repository.specification.visit;

import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Visit;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class VisitSpecifications {

    public static Specification<Visit> hasDoctor(UUID doctorId) {
        return (root, query, cb) ->
                doctorId == null ? cb.conjunction()
                        : cb.equal(root.get("doctor").get("id"), doctorId);
    }

    public static Specification<Visit> hasPatient(UUID patientId) {
        return (root, query, cb) ->
                patientId == null ? cb.conjunction()
                        : cb.equal(root.get("patient").get("id"), patientId);
    }

    public static Specification<Visit> hasStatus(VisitStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction()
                        : cb.equal(root.get("status"), status);
    }

    public static Specification<Visit> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return cb.conjunction();
            if (start != null && end != null)
                return cb.between(root.get("createdAt"), start, end);
            if (start != null)
                return cb.greaterThanOrEqualTo(root.get("createdAt"), start);
            return cb.lessThanOrEqualTo(root.get("createdAt"), end);
        };
    }

    public static Specification<Visit> orderByCreatedDesc() {
        return (root, query, cb) -> {
            assert query != null;
            query.orderBy(cb.desc(root.get("createdAt")));
            return cb.conjunction();
        };
    }
}