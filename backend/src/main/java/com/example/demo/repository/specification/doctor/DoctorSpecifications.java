package com.example.demo.repository.specification.doctor;

import com.example.demo.enums.Specialization;
import com.example.demo.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DoctorSpecifications {

    public static Specification<Doctor> hasNameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }

            String[] parts = name.trim().toLowerCase().split("\\s+");

            if (parts.length == 1) {
                String value = "%" + parts[0] + "%";
                return cb.or(
                        cb.like(cb.lower(root.get("firstname")), value),
                        cb.like(cb.lower(root.get("lastname")), value)
                );
            }

            String first = "%" + parts[0] + "%";
            String second = "%" + parts[1] + "%";

            return cb.or(
                    cb.and(
                            cb.like(cb.lower(root.get("firstname")), first),
                            cb.like(cb.lower(root.get("lastname")), second)
                    ),
                    cb.and(
                            cb.like(cb.lower(root.get("firstname")), second),
                            cb.like(cb.lower(root.get("lastname")), first)
                    )
            );
        };
    }

    public static Specification<Doctor> hasSpecialization(Specialization specialization) {
        return (root, query, cb) -> {
            if (specialization == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("specialization"), specialization);
        };
    }

    public static Specification<Doctor> hasRatingGreaterThan(Double rating) {
        return (root, query, cb) -> {
            if (rating == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("rating"), rating);
        };
    }

    public static Specification<Doctor> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<Doctor> hasExperienceGreaterThan(Integer years) {
        return (root, query, cb) -> {
            if (years == null || years <= 0) {
                return cb.conjunction();
            }
            LocalDate thresholdDate = LocalDate.now().minusYears(years);
            return cb.lessThanOrEqualTo(root.get("startDate"), thresholdDate);
        };
    }

    public static Specification<Doctor> orderByLastname() {
        return (root, query, cb) -> {
            assert query != null;
            query.distinct(true);
            query.orderBy(cb.asc(root.get("lastname")));
            return cb.conjunction();
        };
    }
}