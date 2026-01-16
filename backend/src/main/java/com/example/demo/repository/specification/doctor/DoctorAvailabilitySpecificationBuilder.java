package com.example.demo.repository.specification.doctor;

import com.example.demo.model.DoctorAvailability;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class DoctorAvailabilitySpecificationBuilder {

    public static Specification<DoctorAvailability> build(
            UUID doctorId,
            Boolean active,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return Specification.allOf(
                DoctorAvailabilitySpecifications.hasDoctor(doctorId),
                DoctorAvailabilitySpecifications.isActive(active),
                DoctorAvailabilitySpecifications.availableFrom(from),
                DoctorAvailabilitySpecifications.availableTo(to),
                DoctorAvailabilitySpecifications.orderByTimeAsc()
        );
    }
}