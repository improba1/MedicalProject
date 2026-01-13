package com.example.demo.repository.specification.patient;

import com.example.demo.model.Patient;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class PatientSpecifications {

    public static Specification<Patient> hasNameLike(String input) {
        return (root, query, cb) -> {
            if (input == null || input.isBlank()) {
                return cb.conjunction();
            }

            String[] parts = input.trim().toLowerCase().split("\\s+");

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

    // 🔐 ТІЛЬКИ пацієнти конкретного лікаря
    public static Specification<Patient> belongsToDoctor(UUID doctorId) {
        return (root, query, cb) -> {
            assert query != null;
            query.distinct(true);

            var visitJoin = root.join("visits");
            return cb.equal(visitJoin.get("doctor").get("id"), doctorId);
        };
    }

    // 🔤 ORDER BY lastname
    public static Specification<Patient> orderByLastname() {
        return (root, query, cb) -> {
            assert query != null;
            query.orderBy(cb.asc(root.get("lastname")));
            return cb.conjunction();
        };
    }
}