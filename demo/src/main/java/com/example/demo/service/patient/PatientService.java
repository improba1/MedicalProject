package com.example.demo.service.patient;

import com.example.demo.model.Patient;
import com.example.demo.model.Visit;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    Patient getById(UUID id);
    Patient create(Patient patient);
    Patient update(Patient patient);
    void delete(UUID id);

    List<Visit> getVisits(UUID patientId);
}