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
        existing.setVisitStatus(raport.getVisitStatus());

        return raportRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        raportRepository.delete(getById(id));
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
    // 🔥 Нові фільтри
    // ==========================

    @Override
    public List<Raport> getByPatientAndDate(UUID patientId, LocalDateTime start, LocalDateTime end) {
        return raportRepository.findByPatientIdAndCreatedAtBetween(patientId, start, end);
    }

    @Override
    public List<Raport> getByDoctorAndDate(UUID doctorId, LocalDateTime start, LocalDateTime end) {
        return raportRepository.findByDoctorIdAndCreatedAtBetween(doctorId, start, end);
    }

    @Override
    public List<Raport> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return raportRepository.findByCreatedAtBetween(start, end);
    }

    @Override
    public List<Raport> getByVisitAndDoctor(UUID visitId, UUID doctorId) {
        return raportRepository.findByVisitIdAndDoctorId(visitId, doctorId);
    }

    @Override
    public List<Raport> getByVisitAndPatient(UUID visitId, UUID patientId) {
        return raportRepository.findByVisitIdAndPatientId(visitId, patientId);
    }

    // ==========================
    // 🔹 Методи для користувача
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
    // 🔹 Методи для лікаря
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
    // 🔹 Хелпери
    // ==========================

    private UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"))
                .getId();
    }

    private UUID getCurrentDoctorId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"))
                .getId();
    }
}