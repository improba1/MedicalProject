package com.example.demo.service.visit;

import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Raport;
import com.example.demo.model.Visit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VisitService {
    Visit getById(UUID id);
    Visit getByIdForAuthenticatedDoctor(UUID id);
    Visit getByIdForAuthenticatedPatient(UUID id);
    Visit updateVisit(Visit visitUpdate);
    void delete(UUID id);

    Visit createByAdmin(Visit visit);
    Visit createByPatient(Visit visit);
    Visit cancelVisit(UUID visitId);
    Visit rescheduleVisit(UUID visitId, LocalDateTime newTime);

    List<Visit> searchForPatient(UUID doctorId, VisitStatus status, LocalDateTime start, LocalDateTime end);

    Visit cancelVisitByDoctor(UUID visitId);
    Visit rescheduleVisitByDoctor(UUID visitId, LocalDateTime newTime);

    List<Visit> searchForDoctor(UUID patientId, VisitStatus status, LocalDateTime start, LocalDateTime end);

    List<Visit> searchForAdmin(UUID doctorId, UUID patientId, VisitStatus status, LocalDateTime start, LocalDateTime end);

    Visit addItemToVisit(UUID visitId, UUID medicalServiceId, Integer quantity);
    Visit removeItemFromVisit(UUID visitId, UUID itemId);
    Visit clearCart(UUID visitId);
    Visit updateItemQuantity(UUID visitId, UUID itemId, Integer quantity);
    BigDecimal calculateTotalPrice(UUID visitId);
    void lockCart(UUID visitId);


    Raport updateRaport(UUID visitId, Raport raportUpdate);
    Visit completeVisit(UUID visitId);
}