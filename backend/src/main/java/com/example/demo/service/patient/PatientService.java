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

    // hard delete з логаутом
    void delete(UUID id, HttpServletRequest request, HttpServletResponse response);

    Patient deactivatePatientById(UUID id, HttpServletRequest request, HttpServletResponse response);

    List<Patient> searchPatientsForCurrentDoctor(String name);

    List<Patient> searchPatientsForAdmin(String name);

    // 🔹 Методи для роботи з профілем поточного пацієнта
    Patient getCurrentPatient();

    Patient updateCurrentPatient(Patient patient);

    void deletePatientProfile(HttpServletRequest request, HttpServletResponse response);

    // 🔹 Отримати автентифікованого пацієнта
    Patient getAuthenticatedPatient();
}