package com.example.demo.repository.specification.doctor;

import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecificationBuilder {

    public static Specification<Doctor> build(DoctorSearchRequest request) {
        Specification<Doctor> spec = (root, query, cb) -> cb.conjunction();

        if (request.getName() != null && !request.getName().isBlank()) {
            spec = spec.and(DoctorSpecifications.hasNameLike(request.getName()));
        }
        if (request.getSpecialization() != null) {
            spec = spec.and(DoctorSpecifications.hasSpecialization(request.getSpecialization()));
        }
        if (request.getRating() != null) {
            spec = spec.and(DoctorSpecifications.hasRatingGreaterThan(request.getRating()));
        }
        return spec;
    }
}