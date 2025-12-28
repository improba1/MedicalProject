package com.example.demo.service.visit;

import com.example.demo.enums.Role;
import com.example.demo.model.*;
import com.example.demo.repository.*;
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
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RaportRepository raportRepository;
    private final SlotService slotService;
    private final VisitStatusService visitStatusService;

    @Override
    public Visit getById(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
    }

    @Transactional
    @Override
    public Visit createVisit(Visit visit) {

        UUID doctorId = visit.getDoctor().getId();
        UUID patientId = visit.getPatient().getId();

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Patient patient = (Patient) userRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        Raport raport = null;
        if (visit.getRaport() != null && visit.getRaport().getId() != null) {
            raport = raportRepository.findById(visit.getRaport().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Raport not found"));
        }

        slotService.occupy(doctorId, visit.getAppointmentTime());

        visit.setDoctor(doctor);
        visit.setPatient(patient);
        visit.setRaport(raport);

        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit updateVisit(Visit visitUpdate) {
        Visit visit = getById(visitUpdate.getId());

        if (visitUpdate.getAppointmentTime() != null &&
                !visitUpdate.getAppointmentTime().equals(visit.getAppointmentTime())) {

            visitStatusService.reschedule(visit, visitUpdate.getAppointmentTime(), Role.ADMIN);
        }

        if (visitUpdate.getStatus() != null) {
            switch (visitUpdate.getStatus()) {
                case CANCELED -> visitStatusService.cancel(visit, Role.ADMIN);
                case COMPLETED -> visitStatusService.complete(visit, Role.ADMIN);
                case PAID -> visitStatusService.pay(visit, Role.ADMIN);
            }
        }

        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Visit visit = getById(id);
        slotService.free(visit.getDoctor().getId(), visit.getAppointmentTime());
        visitRepository.delete(visit);
    }

    @Override
    public List<Visit> getByDoctor(UUID doctorId) {
        return visitRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Visit> getByPatient(UUID patientId) {
        return visitRepository.findByPatientId(patientId);
    }

    @Transactional
    @Override
    public Visit rescheduleVisit(UUID visitId, LocalDateTime newTime) {
        Visit visit = getById(visitId);
        UUID currentUserId = getCurrentUserId();

        if (!visit.getPatient().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Visit not found for this user");
        }

        visitStatusService.reschedule(visit, newTime, Role.PATIENT);
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit cancelVisit(UUID visitId) {
        Visit visit = getById(visitId);
        UUID currentUserId = getCurrentUserId();

        if (!visit.getPatient().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Visit not found for this user");
        }

        visitStatusService.cancel(visit, Role.PATIENT);
        return visitRepository.save(visit);
    }

    @Override
    public List<Visit> getUserVisits() {
        return visitRepository.findByPatientId(getCurrentUserId());
    }

    @Override
    public List<Visit> getUpcomingUserVisits() {
        return visitRepository.findByPatientId(getCurrentUserId())
                .stream()
                .filter(v -> v.getAppointmentTime().isAfter(LocalDateTime.now()))
                .toList();
    }

    private UUID getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"))
                .getId();
    }
}