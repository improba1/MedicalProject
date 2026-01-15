package com.example.demo.service.medical_service;

import com.example.demo.dto.request.medical_service.MedicalServiceSearchRequest;
import com.example.demo.model.MedicalService;

import java.util.List;
import java.util.UUID;

public interface MedicalServiceService {

    MedicalService getById(UUID id);

    List<MedicalService> search(UUID doctorId, MedicalServiceSearchRequest request);

    MedicalService createForDoctor(UUID doctorId, MedicalService service);

    MedicalService updateForDoctor(UUID doctorId, MedicalService service);

    void delete(UUID id);

    List<MedicalService> searchForAuthenticatedDoctor(MedicalServiceSearchRequest request);

    MedicalService createForAuthenticatedDoctor(MedicalService service);

    MedicalService updateForAuthenticatedDoctor(MedicalService update);

    void deleteForAuthenticatedDoctor(UUID id);
}