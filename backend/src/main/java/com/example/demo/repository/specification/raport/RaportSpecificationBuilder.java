package com.example.demo.repository.specification.raport;

import com.example.demo.model.Raport;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class RaportSpecificationBuilder {
    public static Specification<Raport> build(
            UUID visitId,
            UUID doctorId,
            UUID patientId,
            String disease,
            String paymentReceipt,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return Specification.allOf(
                RaportSpecifications.hasVisit(visitId),
                RaportSpecifications.hasDoctor(doctorId),
                RaportSpecifications.hasPatient(patientId),
                RaportSpecifications.hasDiseaseLike(disease),
                RaportSpecifications.hasReceipt(paymentReceipt),
                RaportSpecifications.priceBetween(minPrice, maxPrice),
                RaportSpecifications.createdBetween(from, to),
                RaportSpecifications.orderByCreatedDesc()
        );
    }
}