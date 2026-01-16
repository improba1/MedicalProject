package com.example.demo.repository.specification.doctor;

import com.example.demo.enums.Specialization;
import com.example.demo.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecificationBuilder {

    public static Specification<Doctor> build(
            String name,
            Specialization specialization,
            Double rating,
            Boolean active
    ) {
        return Specification.allOf(
                DoctorSpecifications.hasNameLike(name),
                DoctorSpecifications.hasSpecialization(specialization),
                DoctorSpecifications.hasRating(rating),
                DoctorSpecifications.isActive(active),
                DoctorSpecifications.orderByLastname()
        );
    }

    public static Specification<Doctor> buildForPublic(
            String name,
            Specialization specialization,
            Double rating
    ) {
        return Specification.allOf(
                DoctorSpecifications.hasNameLike(name),
                DoctorSpecifications.hasSpecialization(specialization),
                DoctorSpecifications.hasRating(rating),
                DoctorSpecifications.onlyActive(),
                DoctorSpecifications.orderByLastname()
        );
    }
}