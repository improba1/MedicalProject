package com.example.demo.service.patient;

import com.example.demo.model.Patient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.UUID;

public interface PatientService {

    // 🔹 Базові CRUD-операції
    Patient getById(UUID id);

    Patient create(Patient patient);

    Patient update(Patient patient);

    void delete(UUID id);

    Patient deactivateCurrentPatient(Patient patient);

    List<Patient> searchPatientsForCurrentDoctor(String name);

    List<Patient> searchPatientsForAdmin(String name);

    // 🔹 Методи для роботи з профілем поточного пацієнта
    Patient getCurrentPatient();

    Patient updateCurrentPatient(Patient patient);

    Patient deactivatePatientById(UUID id);

    // 🔹 Отримати автентифікованого пацієнта
    Patient getAuthenticatedPatient();
}