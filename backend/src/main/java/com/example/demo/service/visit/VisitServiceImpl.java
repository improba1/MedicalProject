package com.example.demo.service.visit;

import com.example.demo.enums.Role;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.Visit;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.repository.specification.visit.VisitSpecification;
import com.example.demo.service.slot.SlotService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final SlotService slotService;
    private final VisitStatusService visitStatusService;

    // ============================================================
    // 🔹 AUTH HELPERS
    // ============================================================

    private UUID authenticatedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"))
                .getId();
    }

    private UUID authenticatedDoctorId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"))
                .getId();
    }

    private Visit getVisit(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
    }

    private void assertDoctorOwnsVisit(Visit visit) {
        if (!visit.getDoctor().getId().equals(authenticatedDoctorId())) {
            throw new EntityNotFoundException("Visit not found for this doctor");
        }
    }

    @Override
    public Visit getById(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Visit not found with id: " + id
                ));
    }

    // ============================================================
    // 🔹 CREATE
    // ============================================================

    @Transactional
    @Override
    public Visit createVisit(Visit visit) {
        Doctor doctor = doctorRepository.findById(visit.getDoctor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Patient patient = (Patient) userRepository.findById(visit.getPatient().getId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        slotService.occupy(doctor.getId(), visit.getAppointmentTime());

        visit.setDoctor(doctor);
        visit.setPatient(patient);

        return visitRepository.save(visit);
    }

    // ============================================================
    // 🔹 PATIENT ACTIONS
    // ============================================================

    @Transactional
    @Override
    public Visit createVisitForAuthenticatedPatient(Visit visit) {

        UUID patientId = authenticatedUserId();

        // 🔒 Захист: пацієнт може створювати ТІЛЬКИ для себе
        if (!visit.getPatient().getId().equals(patientId)) {
            throw new EntityNotFoundException("Cannot create visit for another patient");
        }

        Doctor doctor = doctorRepository.findById(visit.getDoctor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Patient patient = (Patient) userRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        // ⏳ слот
        slotService.occupy(doctor.getId(), visit.getAppointmentTime());

        visit.setDoctor(doctor);
        visit.setPatient(patient);

        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit cancelVisit(UUID visitId) {
        Visit visit = getVisit(visitId);

        if (!visit.getPatient().getId().equals(authenticatedUserId())) {
            throw new EntityNotFoundException("Visit not found for this patient");
        }

        visitStatusService.cancel(visit, Role.PATIENT);
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit rescheduleVisit(UUID visitId, LocalDateTime newTime) {
        Visit visit = getVisit(visitId);

        if (!visit.getPatient().getId().equals(authenticatedUserId())) {
            throw new EntityNotFoundException("Visit not found for this patient");
        }

        visitStatusService.reschedule(visit, newTime, Role.PATIENT);
        return visitRepository.save(visit);
    }

    @Override
    public List<Visit> searchVisitsForAuthenticatedPatient(
            UUID doctorId,
            VisitStatus status,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return visitRepository.findAll(
                VisitSpecification.byFilters(
                        doctorId,
                        authenticatedUserId(),
                        status,
                        start,
                        end
                )
        );
    }

    // ============================================================
    // 🔹 DOCTOR ACTIONS
    // ============================================================

    @Transactional
    @Override
    public Visit cancelVisitByDoctor(UUID visitId) {
        Visit visit = getVisit(visitId);
        assertDoctorOwnsVisit(visit);

        visitStatusService.cancel(visit, Role.DOCTOR);
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit rescheduleVisitByDoctor(UUID visitId, LocalDateTime newTime) {
        Visit visit = getVisit(visitId);
        assertDoctorOwnsVisit(visit);

        visitStatusService.reschedule(visit, newTime, Role.DOCTOR);
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public List<Visit> searchVisitsForAuthenticatedDoctor(
            UUID patientId,
            VisitStatus status,
            LocalDateTime start,
            LocalDateTime end
    ) {
        UUID doctorId = authenticatedDoctorId();

        return visitRepository.findAll(
                VisitSpecification.byFilters(
                        doctorId,
                        patientId,
                        status,
                        start,
                        end
                )
        );
    }

    // ============================================================
    // 🔹 ADMIN ACTIONS
    // ============================================================

    @Transactional
    @Override
    public Visit updateVisit(Visit update) {
        Visit visit = getVisit(update.getId());

        if (update.getAppointmentTime() != null &&
                !update.getAppointmentTime().equals(visit.getAppointmentTime())) {
            visitStatusService.reschedule(visit, update.getAppointmentTime(), Role.ADMIN);
        }

        if (update.getStatus() != null) {
            switch (update.getStatus()) {
                case CANCELED -> visitStatusService.cancel(visit, Role.ADMIN);
                case COMPLETED -> visitStatusService.complete(visit, Role.ADMIN);
                case PAID -> visitStatusService.pay(visit, Role.ADMIN);
            }
        }

        return visitRepository.save(visit);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        Visit visit = getVisit(id);

        LocalDateTime appointmentTime = visit.getAppointmentTime();
        UUID doctorId = visit.getDoctor().getId();

        // 1️⃣ Якщо візит у майбутньому → звільняємо слот
        if (appointmentTime.isAfter(LocalDateTime.now())) {
            slotService.free(doctorId, appointmentTime);
        }

        // 2️⃣ Видаляємо візит
        visitRepository.delete(visit);
    }

    @Transactional
    @Override
    public List<Visit> searchVisitsForAdmin(
            UUID doctorId,
            UUID patientId,
            VisitStatus status,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return visitRepository.findAll(
                VisitSpecification.byFilters(
                        doctorId,
                        patientId,
                        status,
                        start,
                        end
                )
        );
    }
}