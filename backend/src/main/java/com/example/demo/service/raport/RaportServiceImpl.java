package com.example.demo.service.raport;

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
        existing.setDisease(raport.getDisease());
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
        return raportRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Raport> getByPatientId(UUID patientId) {
        return raportRepository.findByPatientId(patientId);
    }

    @Override
    public Raport getByVisitId(UUID visitId) {
        return raportRepository.findByVisitId(visitId);
    }

    // ==========================
    // 🔹 Методи для користувача (повертають ентіті)
    // ==========================

    @Override
    public Raport getRaportByVisitForUser(UUID visitId) {
        UUID currentUserId = getCurrentUserId();
        Raport raport = raportRepository.findByVisitId(visitId);
        if (raport == null || !raport.getPatient().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Raport not found for this user and visit");
        }
        return raport;
    }

    @Override
    public List<Raport> getUserRaports() {
        UUID currentUserId = getCurrentUserId();
        return raportRepository.findByPatientId(currentUserId);
    }

    // ==========================
    // 🔹 Методи для лікаря (повертають ентіті)
    // ==========================

    @Override
    public Raport getOwnRaportByVisit(UUID visitId) {
        UUID currentDoctorId = getCurrentDoctorId();
        Raport raport = raportRepository.findByVisitId(visitId);
        if (raport == null || !raport.getDoctor().getId().equals(currentDoctorId)) {
            throw new EntityNotFoundException("Raport not found for this doctor and visit");
        }
        return raport;
    }

    @Override
    public List<Raport> getDoctorRaports() {
        UUID currentDoctorId = getCurrentDoctorId();
        return raportRepository.findByDoctorId(currentDoctorId);
    }

    @Override
    public List<Raport> getOwnRaportsByPatient(UUID patientId) {
        UUID currentDoctorId = getCurrentDoctorId();
        return raportRepository.findByPatientId(patientId)
                .stream()
                .filter(r -> r.getDoctor().getId().equals(currentDoctorId))
                .toList();
    }

    // ==========================
    // 🔹 Хелпери для отримання поточного користувача/лікаря
    // ==========================
    private UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // у JWT username = email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"))
                .getId();
    }

    private UUID getCurrentDoctorId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // у JWT username = email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"))
                .getId();
    }
}