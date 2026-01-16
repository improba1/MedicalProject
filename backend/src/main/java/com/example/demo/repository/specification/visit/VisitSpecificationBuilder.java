package com.example.demo.repository.specification.visit;

import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Visit;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class VisitSpecificationBuilder {

    public static Specification<Visit> build(
            UUID doctorId,
            UUID patientId,
            VisitStatus status,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return Specification.allOf(
                VisitSpecifications.hasDoctor(doctorId),
                VisitSpecifications.hasPatient(patientId),
                VisitSpecifications.hasStatus(status),
                VisitSpecifications.createdBetween(start, end),
                VisitSpecifications.orderByCreatedDesc()
        );
    }
}