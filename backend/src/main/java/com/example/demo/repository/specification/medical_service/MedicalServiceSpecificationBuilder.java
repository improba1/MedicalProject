package com.example.demo.repository.specification.medical_service;

import com.example.demo.model.MedicalService;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class MedicalServiceSpecificationBuilder {

    public static Specification<MedicalService> buildForAdmin(
            UUID doctorId,
            String name,
            Boolean active,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return Specification.allOf(
                MedicalServiceSpecifications.hasDoctor(doctorId),
                MedicalServiceSpecifications.hasNameLike(name),
                MedicalServiceSpecifications.isActive(active),
                MedicalServiceSpecifications.priceBetween(minPrice, maxPrice),
                MedicalServiceSpecifications.orderByNameAsc()
        );
    }

    public static Specification<MedicalService> buildForDoctor(
            UUID doctorId,
            String name,
            Boolean active,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return Specification.allOf(
                MedicalServiceSpecifications.hasDoctor(doctorId),
                MedicalServiceSpecifications.hasNameLike(name),
                MedicalServiceSpecifications.isActive(active),
                MedicalServiceSpecifications.priceBetween(minPrice, maxPrice),
                MedicalServiceSpecifications.orderByNameAsc()
        );
    }

    public static Specification<MedicalService> buildForPatient(
            UUID doctorId,
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return Specification.allOf(
                MedicalServiceSpecifications.hasDoctor(doctorId),
                MedicalServiceSpecifications.hasNameLike(name),
                MedicalServiceSpecifications.priceBetween(minPrice, maxPrice),
                MedicalServiceSpecifications.onlyActive(),
                MedicalServiceSpecifications.orderByNameAsc()
        );
    }
}