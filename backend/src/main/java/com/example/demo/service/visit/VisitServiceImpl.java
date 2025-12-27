package com.example.demo.service.visit;

import com.example.demo.model.*;
import com.example.demo.repository.*;
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
    private final DoctorAvailabilityRepository availabilityRepository;

    @Override
    public Visit getById(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
    }

    @Transactional
    @Override
    public Visit createVisit(Visit visit) {
        if (visit.getDoctor() == null || visit.getDoctor().getId() == null) {
            throw new EntityNotFoundException("Doctor id is required in visit entity");
        }
        if (visit.getPatient() == null || visit.getPatient().getId() == null) {
            throw new EntityNotFoundException("Patient id is required in visit entity");
        }
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
        DoctorAvailability availability = availabilityRepository
                .findByDoctorIdAndAvailableTimeAndIsActiveTrue(
                        doctorId,
                        visit.getAppointmentTime()
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Doctor does not have an available slot at this time"
                ));
        availability.setActive(false);
        availabilityRepository.save(availability);
        visit.setDoctor(doctor);
        visit.setPatient(patient);
        visit.setRaport(raport);
        if (visit.getStatus() == null) {
            throw new IllegalStateException("Visit status must be set");
        }
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit updateVisit(Visit visitUpdate) {
        Visit visit = visitRepository.findById(visitUpdate.getId())
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));

        UUID doctorId = visit.getDoctor().getId();

        if (visitUpdate.getAppointmentTime() != null &&
                !visitUpdate.getAppointmentTime().equals(visit.getAppointmentTime())) {

            LocalDateTime newTime = visitUpdate.getAppointmentTime();

            DoctorAvailability newSlot = availabilityRepository
                    .findByDoctorIdAndAvailableTimeAndIsActiveTrue(doctorId, newTime)
                    .orElseThrow(() -> new IllegalStateException(
                            "Doctor does not have an available slot at the new time"
                    ));

            availabilityRepository.findByDoctorIdAndAvailableTime(
                    doctorId,
                    visit.getAppointmentTime()
            ).ifPresent(slot -> {
                slot.setActive(true);
                availabilityRepository.save(slot);
            });

            newSlot.setActive(false);
            availabilityRepository.save(newSlot);

            visit.setAppointmentTime(newTime);
        }

        if (visitUpdate.getStatus() != null) {
            visit.setStatus(visitUpdate.getStatus());
        }

        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
        availabilityRepository.findByDoctorIdAndAvailableTime(
                visit.getDoctor().getId(),
                visit.getAppointmentTime()
        ).ifPresent(slot -> {
            slot.setActive(true);
            availabilityRepository.save(slot);
        });
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

    @Transactional
    @Override
    public Visit rescheduleVisit(UUID visitId, LocalDateTime newTime) {
        UUID currentUserId = getCurrentUserId();
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
        if (!visit.getPatient().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Visit not found for this user");
        }
        UUID doctorId = visit.getDoctor().getId();
        availabilityHelper(newTime, visit, doctorId);
        visit.setStatus(com.example.demo.enums.VisitStatus.RESCHEDULED);
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit cancelVisit(UUID visitId) {
        UUID currentUserId = getCurrentUserId();
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));

        if (!visit.getPatient().getId().equals(currentUserId)) {
            throw new EntityNotFoundException("Visit not found for this user");
        }
        availabilityRepository.findByDoctorIdAndAvailableTime(
                visit.getDoctor().getId(),
                visit.getAppointmentTime()
        ).ifPresent(slot -> {
            slot.setActive(true);
            availabilityRepository.save(slot);
        });
        visit.setStatus(com.example.demo.enums.VisitStatus.CANCELED);
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

    private UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"))
                .getId();
    }

    private void availabilityHelper(LocalDateTime newTime, Visit visit, UUID doctorId) {
        availabilityRepository.findByDoctorIdAndAvailableTime(doctorId, visit.getAppointmentTime())
                .ifPresent(slot -> {
                    slot.setActive(true);
                    availabilityRepository.save(slot);
                });

        DoctorAvailability newSlot = availabilityRepository
                .findByDoctorIdAndAvailableTimeAndIsActiveTrue(doctorId, newTime)
                .orElseThrow(() -> new IllegalStateException("Doctor is not available at this time"));

        newSlot.setActive(false);
        availabilityRepository.save(newSlot);
        visit.setAppointmentTime(newTime);
    }
}