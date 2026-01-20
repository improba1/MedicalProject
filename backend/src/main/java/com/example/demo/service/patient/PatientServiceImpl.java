package com.example.demo.service.patient;

import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.specification.patient.PatientSpecificationBuilder;
import com.example.demo.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
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
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

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
    public void delete(UUID id) {
        Patient patient = getById(id);
        patientRepository.delete(patient);
    }

    @Override
    public List<Patient> searchPatientsForCurrentDoctor(String name) {
        UUID doctorId = currentUserService.getAuthenticatedDoctor().getId();

        return patientRepository.findAll(
                PatientSpecificationBuilder.forDoctor(doctorId, name)
        );
    }

    @Override
        public List<Patient> searchPatientsForAdmin(String name) {
        return patientRepository.findAll(
                PatientSpecificationBuilder.forAdmin(name)
        );
    }


    @Override
    public Patient deactivatePatientById(UUID id) {
        Patient patient = getById(id);
        patient.setActive(false);
        return patientRepository.save(patient);
    }


    @Override
    public Patient getAuthenticatedPatient() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated patient not found"));
    }

    @Override
    public Patient getCurrentPatient() {
        return getAuthenticatedPatient();
    }

    @Override
    public Patient updateCurrentPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient deactivateCurrentPatient(Patient patient) {
        patient.setActive(false);
        return patientRepository.save(patient);
    }
}