package com.example.demo.repository.specification.doctor;

import com.example.demo.dto.request.doctor.DoctorSearchRequest;
import com.example.demo.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecificationBuilder {

    public static Specification<Doctor> build(DoctorSearchRequest request) {
        if (request == null) {
            return Specification.allOf(
                    DoctorSpecifications.isActive(),
                    DoctorSpecifications.orderByLastname()
            );
        }

        return Specification.allOf(
                DoctorSpecifications.isActive(),
                DoctorSpecifications.hasNameLike(request.getName()),
                DoctorSpecifications.hasSpecialization(request.getSpecialization()),
                DoctorSpecifications.hasRatingGreaterThan(request.getRating()),
                DoctorSpecifications.orderByLastname()
        );
    }
}