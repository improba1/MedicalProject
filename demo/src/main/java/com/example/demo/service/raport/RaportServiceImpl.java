package com.example.demo.service.raport;

import com.example.demo.dto.response.RaportResponse;
import com.example.demo.mapper.raport.RaportMapper;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.Raport;
import com.example.demo.repository.RaportRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RaportServiceImpl implements RaportService {

    private final RaportRepository raportRepository;
    private final UserRepository userRepository;
    private final RaportMapper raportMapper;

    @Override
    public Raport getById(UUID id) {
        return raportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Raport not found with id: " + id));
    }

    @Override
    public List<Raport> getAll() {
        return raportRepository.findAll();
    }

    @Override
    public Raport create(Raport raport) {
        raport.setCreatedAt(LocalDateTime.now());
        return raportRepository.save(raport);
    }

    @Override
    public Raport update(UUID id, Raport raport) {
        Raport existing = getById(id);
        existing.setDiagnosis(raport.getDiagnosis());
        existing.setSymptoms(raport.getSymptoms());
        existing.setPrice(raport.getPrice());
        existing.setNotes(raport.getNotes());
        existing.setDoctor(raport.getDoctor());
        existing.setPatient(raport.getPatient());
        existing.setVisit(raport.getVisit());
        return raportRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        Raport raport = getById(id);
        raportRepository.delete(raport);
    }

    @Override
    public List<Raport> getByDoctorId(UUID doctorId) {
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        return raportRepository.findByDoctor(doctor);
    }

    @Override
    public List<Raport> getByPatientId(UUID patientId) {
        Patient patient = new Patient();
        patient.setId(patientId);
        return raportRepository.findByPatient(patient);
    }

    @Override
    public Raport getByVisitId(UUID visitId) {
        return raportRepository.findByVisitId(visitId);
    }

    // ==========================
    // 🔹 Методи для користувача
    // ==========================

    @Override
    public RaportResponse getRaportByVisitForUser(UUID visitId) {
        UUID currentUserId = getCurrentUserId();
        Raport raport = raportRepository.findByVisitId(visitId);
        if (raport == null || !raport.getPatient().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Raport not found for this user and visit");
        }
        return raportMapper.toResponse(raport);
    }

    @Override
    public List<RaportResponse> getUserRaports() {
        UUID currentUserId = getCurrentUserId();
        Patient patient = new Patient();
        patient.setId(currentUserId);
        return raportMapper.toResponseList(raportRepository.findByPatient(patient));
    }

    // ==========================
    // 🔹 Хелпер для отримання поточного користувача
    // ==========================
    private UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // у JWT username = email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"))
                .getId();
    }
}