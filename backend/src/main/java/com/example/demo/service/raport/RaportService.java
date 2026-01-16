package com.example.demo.service.raport;

import com.example.demo.model.Raport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RaportService {

    // ----------- BASIC CRUD -----------
    Raport getRaportById(UUID id);
    Raport getRaportByIdForAuthenticatedDoctor(UUID id);
    Raport getRaportByIdForAuthenticatedPatient(UUID id);
    Raport create(Raport raport);
    Raport update(UUID id, Raport raport);
    void delete(UUID id);

    List<Raport> searchForPatient(
            UUID visitId,
            UUID doctorId,
            String disease,
            String receipt,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime from,
            LocalDateTime to
    );
    List<Raport> searchForAdmin(
            UUID visitId,
            UUID doctorId,
            UUID patientId,
            String disease,
            String receipt,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime from,
            LocalDateTime to
    );
    List<Raport> searchForDoctor(
            UUID visitId,
            UUID patientId,
            String disease,
            String receipt,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime from,
            LocalDateTime to
    );
}