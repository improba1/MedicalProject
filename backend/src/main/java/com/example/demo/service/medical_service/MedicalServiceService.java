package com.example.demo.service.medical_service;

import com.example.demo.model.MedicalService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface MedicalServiceService {

    MedicalService getById(UUID id);
    MedicalService getByIdForAuthenticatedDoctor(UUID id);

    List<MedicalService> searchForAdmin(UUID doctorId, String name, Boolean active, BigDecimal minPrice, BigDecimal maxPrice);
    List<MedicalService> searchForAuthenticatedDoctor(String name, Boolean active, BigDecimal minPrice, BigDecimal maxPrice);
    List<MedicalService> searchForPatient(UUID doctorId, String name, BigDecimal minPrice, BigDecimal maxPrice);

    MedicalService createForDoctor(UUID doctorId, MedicalService service);

    MedicalService updateForDoctor(UUID doctorId, MedicalService service);

    void delete(UUID id);

    MedicalService createForAuthenticatedDoctor(MedicalService service);

    MedicalService updateForAuthenticatedDoctor(MedicalService update);

    void deleteForAuthenticatedDoctor(UUID id);
}