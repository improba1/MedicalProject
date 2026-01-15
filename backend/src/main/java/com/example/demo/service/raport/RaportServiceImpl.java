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

    // ==========================
    // 🔹 Helpers
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

    private boolean isAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("admin:update"));
    }


    // ==========================
    // 🔹 Basic CRUD
    // ==========================

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
    public Raport update(UUID id, Raport raportUpdate) {
        Raport raport = getById(id);

        // 🔥 1. Адмін може редагувати завжди
        if (!isAdmin()) {

            // 🔥 2. Лікар може редагувати тільки свій рапорт
            UUID currentDoctorId = getCurrentDoctorId();
            if (!raport.getVisit().getDoctor().getId().equals(currentDoctorId)) {
                throw new IllegalStateException("You cannot edit another doctor's raport");
            }

            // 🔥 3. Заборона редагування після COMPLETED
            if (raport.getVisit().getStatus().name().equals("COMPLETED")) {
                throw new IllegalStateException("Cannot edit raport after visit is completed");
            }

            // 🔥 4. Заборона редагування після 24 годин
            LocalDateTime limit = raport.getCreatedAt().plusHours(24);
            if (LocalDateTime.now().isAfter(limit)) {
                throw new IllegalStateException("Raport can only be edited within 24 hours after creation");
            }
        }

        // 🔥 5. Оновлення дозволених полів
        if (raportUpdate.getDisease() != null)
            raport.setDisease(raportUpdate.getDisease());

        if (raportUpdate.getTreatmentPlan() != null)
            raport.setTreatmentPlan(raportUpdate.getTreatmentPlan());

        if (raportUpdate.getDoctorNotes() != null)
            raport.setDoctorNotes(raportUpdate.getDoctorNotes());

        return raportRepository.save(raport);
    }

    @Override
    public void delete(UUID id) {
        raportRepository.delete(getById(id));
    }

    // ==========================
    // 🔹 By visit
    // ==========================

    @Override
    public Raport getByVisitId(UUID visitId) {
        return raportRepository.findByVisitId(visitId);
    }

    // ==========================
    // 🔹 Filters
    // ==========================

    @Override
    public List<Raport> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return raportRepository.findByCreatedAtBetween(start, end);
    }

    @Override
    public List<Raport> getByDoctorAndDate(UUID doctorId, LocalDateTime start, LocalDateTime end) {
        return raportRepository.findByCreatedAtBetween(start, end)
                .stream()
                .filter(r -> r.getVisit().getDoctor().getId().equals(doctorId))
                .toList();
    }

    @Override
    public List<Raport> getByPatientAndDate(UUID patientId, LocalDateTime start, LocalDateTime end) {
        return raportRepository.findByCreatedAtBetween(start, end)
                .stream()
                .filter(r -> r.getVisit().getPatient().getId().equals(patientId))
                .toList();
    }

    // ==========================
    // 🔹 For authenticated patient
    // ==========================

    @Override
    public Raport getRaportByVisitForUser(UUID visitId) {
        UUID currentUserId = getCurrentUserId();
        Raport raport = raportRepository.findByVisitId(visitId);

        if (raport == null || !raport.getVisit().getPatient().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Raport not found for this user and visit");
        }

        return raport;
    }

    @Override
    public List<Raport> getUserRaports() {
        UUID currentUserId = getCurrentUserId();

        return raportRepository.findAll()
                .stream()
                .filter(r -> r.getVisit().getPatient().getId().equals(currentUserId))
                .toList();
    }

    // ==========================
    // 🔹 For authenticated doctor
    // ==========================

    @Override
    public Raport getOwnRaportByVisit(UUID visitId) {
        UUID currentDoctorId = getCurrentDoctorId();
        Raport raport = raportRepository.findByVisitId(visitId);

        if (raport == null || !raport.getVisit().getDoctor().getId().equals(currentDoctorId)) {
            throw new EntityNotFoundException("Raport not found for this doctor and visit");
        }

        return raport;
    }

    @Override
    public List<Raport> getDoctorRaports() {
        UUID currentDoctorId = getCurrentDoctorId();

        return raportRepository.findAll()
                .stream()
                .filter(r -> r.getVisit().getDoctor().getId().equals(currentDoctorId))
                .toList();
    }

    @Override
    public List<Raport> getOwnRaportsByPatient(UUID patientId) {
        UUID currentDoctorId = getCurrentDoctorId();

        return raportRepository.findAll()
                .stream()
                .filter(r -> r.getVisit().getPatient().getId().equals(patientId))
                .filter(r -> r.getVisit().getDoctor().getId().equals(currentDoctorId))
                .toList();
    }
}