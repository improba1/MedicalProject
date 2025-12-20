package com.example.demo.repository.specification.doctor;

import com.example.demo.enums.Specialization;
import com.example.demo.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecifications {

    public static Specification<Doctor> hasNameLike(String name) {
        return (root, query, cb) -> {
            String[] parts = name.trim().toLowerCase().split("\\s+");
            if (parts.length == 1) {
                return cb.or(
                        cb.like(cb.lower(root.get("firstname")), "%" + parts[0] + "%"),
                        cb.like(cb.lower(root.get("lastname")), "%" + parts[0] + "%")
                );
            } else if (parts.length >= 2) {
                return cb.or(
                        cb.and(
                                cb.like(cb.lower(root.get("firstname")), "%" + parts[0] + "%"),
                                cb.like(cb.lower(root.get("lastname")), "%" + parts[1] + "%")
                        ),
                        cb.and(
                                cb.like(cb.lower(root.get("firstname")), "%" + parts[1] + "%"),
                                cb.like(cb.lower(root.get("lastname")), "%" + parts[0] + "%")
                        )
                );
            }
            return cb.conjunction();
        };
    }

    public static Specification<Doctor> hasSpecialization(Specialization specialization) {
        return (root, query, cb) -> cb.equal(root.get("specialization"), specialization);
    }

    public static Specification<Doctor> hasRatingGreaterThan(double rating) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("rating"), rating);
    }

    public static Specification<Doctor> hasExperienceGreaterThan(int years) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("experienceYears"), years);
    }
}