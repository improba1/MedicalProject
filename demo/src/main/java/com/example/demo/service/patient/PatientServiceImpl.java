package com.example.demo.service.patient;

import com.example.demo.model.Patient;
import com.example.demo.model.Visit;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.VisitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    @Override
    public Patient getById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
    }

    @Override
    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void delete(UUID id) {
        patientRepository.deleteById(id);
    }

    @Override
    public List<Visit> getVisits(UUID patientId) {
        return visitRepository.findByPatientId(patientId);
    }
}