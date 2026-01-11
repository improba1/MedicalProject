package com.example.demo.repository.specification.patient;

import com.example.demo.model.Patient;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class PatientSpecificationBuilder {

    public static Specification<Patient> forDoctor(UUID doctorId, String name) {
        return Specification.allOf(
                PatientSpecifications.belongsToDoctor(doctorId),
                PatientSpecifications.hasNameLike(name),
                PatientSpecifications.orderByLastname()
        );
    }

    public static Specification<Patient> forAdmin(String name) {
        return Specification.allOf(
                PatientSpecifications.hasNameLike(name),
                PatientSpecifications.orderByLastname()
        );
    }
}