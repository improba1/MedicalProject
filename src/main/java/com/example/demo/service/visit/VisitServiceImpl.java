package com.example.demo.service.visit;

import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.Visit;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Override
    public Visit getById(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
    }

    @Override
    public Visit create(Visit visit) {
        return visitRepository.save(visit);
    }

    @Override
    public Visit update(Visit visit) {
        return visitRepository.save(visit);
    }

    @Override
    public void delete(UUID id) {
        visitRepository.deleteById(id);
    }

    @Override
    public List<Visit> getByDoctor(UUID doctorId) {
        return visitRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Visit> getByPatient(UUID patientId) {
        return visitRepository.findByPatientId(patientId);
    }

    // ==========================
    // 🔹 Методи для пацієнта (повертають ентіті)
    // ==========================

    @Override
    public Visit bookVisit(UUID doctorId, LocalDateTime appointmentTime) {
        UUID currentUserId = getCurrentUserId();
        Patient patient = (Patient) userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Visit visit = Visit.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentTime(appointmentTime)
                .status(VisitStatus.SCHEDULED)
                .build();

        return visitRepository.save(visit);
    }

    @Override
    public Visit rescheduleVisit(UUID visitId, LocalDateTime newTime) {
        UUID currentUserId = getCurrentUserId();
        Visit visit = getById(visitId);

        if (!visit.getPatient().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Visit not found for this user");
        }

        visit.setAppointmentTime(newTime);
        visit.setStatus(VisitStatus.RESCHEDULED);
        return visitRepository.save(visit);
    }

    @Override
    public Visit cancelVisit(UUID visitId) {
        UUID currentUserId = getCurrentUserId();
        Visit visit = getById(visitId);

        if (!visit.getPatient().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Visit not found for this user");
        }

        visit.setStatus(VisitStatus.CANCELED);
        return visitRepository.save(visit);
    }

    @Override
    public List<Visit> getUserVisits() {
        UUID currentUserId = getCurrentUserId();
        return visitRepository.findByPatientId(currentUserId);
    }

    @Override
    public List<Visit> getUpcomingUserVisits() {
        UUID currentUserId = getCurrentUserId();
        return visitRepository.findByPatientId(currentUserId)
                .stream()
                .filter(v -> v.getAppointmentTime().isAfter(LocalDateTime.now()))
                .toList();
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