package com.example.demo.service.patient;

import com.example.demo.model.Patient;
import com.example.demo.model.Visit;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.logout.LogoutService;
import com.example.demo.service.visit.VisitService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final VisitService visitService;
    private final LogoutService logoutService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Patient getById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
    }

    @Override
    public Patient create(Patient patient) {
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void delete(UUID id, HttpServletRequest request, HttpServletResponse response) {
        Patient patient = getById(id);
        patientRepository.delete(patient);
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    @Override
    public Patient deactivatePatientById(UUID id, HttpServletRequest request, HttpServletResponse response) {
        Patient patient = getById(id);
        patient.setActive(false);
        Patient updated = patientRepository.save(patient);
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return updated;
    }

    @Override
    public List<Visit> getVisits(UUID patientId) {
        Patient authenticated = getAuthenticatedPatient();

        if (!authenticated.getId().equals(patientId)) {
            throw new SecurityException("Access denied");
        }

        return visitService.searchVisitsForAuthenticatedPatient(null, null, null,null);
    }


    @Override
    public Patient getAuthenticatedPatient() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated patient not found"));
    }

    // повертаємо ентіті — контролер робить mapping
    @Override
    public Patient getCurrentPatient() {
        return getAuthenticatedPatient();
    }

    // сервіс оновлює ентіті, але дані доходять у вигляді вже зміненого Patient
    @Override
    public Patient updateCurrentPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatientProfile(HttpServletRequest request, HttpServletResponse response) {
        Patient currentPatient = getAuthenticatedPatient();
        currentPatient.setActive(false);
        patientRepository.save(currentPatient);
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }
}