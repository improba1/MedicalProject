package com.example.demo.repository.specification.doctor;

import com.example.demo.model.DoctorAvailability;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class DoctorAvailabilitySpecifications {

    public static Specification<DoctorAvailability> hasDoctor(UUID doctorId) {
        return (root, query, cb) ->
                doctorId == null ? cb.conjunction()
                        : cb.equal(root.get("doctorId"), doctorId);
    }

    public static Specification<DoctorAvailability> isActive(Boolean active) {
        return (root, query, cb) ->
                active == null ? cb.conjunction()
                        : cb.equal(root.get("isActive"), active);
    }

    public static Specification<DoctorAvailability> availableFrom(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? cb.conjunction()
                        : cb.greaterThanOrEqualTo(root.get("availableTime"), from);
    }

    public static Specification<DoctorAvailability> availableTo(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? cb.conjunction()
                        : cb.lessThan(root.get("availableTime"), to);
    }

    public static Specification<DoctorAvailability> orderByTimeAsc() {
        return (root, query, cb) -> {
            assert query != null;
            query.orderBy(cb.asc(root.get("availableTime")));
            return cb.conjunction();
        };
    }
}