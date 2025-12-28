package com.example.demo.service.doctor;

import com.example.demo.enums.Role;
import com.example.demo.model.Doctor;
import com.example.demo.model.Visit;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.VisitRepository;

import com.example.demo.service.visit.VisitStatusService;
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
public class DoctorVisitServiceImpl implements DoctorVisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final VisitStatusService visitStatusService;

    @Override
    public List<Visit> getVisits(UUID doctorId) {
        return visitRepository.findByDoctorId(doctorId);
    }

    @Transactional
    @Override
    public Visit cancelVisit(UUID visitId) {
        Visit visit = getById(visitId);
        visitStatusService.cancel(visit, Role.ADMIN);
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit rescheduleVisit(UUID visitId, LocalDateTime newTime) {
        Visit visit = getById(visitId);
        visitStatusService.reschedule(visit, newTime, Role.ADMIN);
        return visitRepository.save(visit);
    }

    // ==========================
    // 🔹 Методи для залогованого лікаря
    // ==========================

    private Visit getById(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
    }

    private Doctor getAuthenticatedDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"));
    }

    @Override
    public List<Visit> getOwnVisits() {
        return visitRepository.findByDoctorId(getAuthenticatedDoctor().getId());
    }

    @Transactional
    @Override
    public Visit cancelOwnVisit(UUID visitId) {
        Visit visit = getById(visitId);
        Doctor doctor = getAuthenticatedDoctor();

        if (!visit.getDoctor().getId().equals(doctor.getId())) {
            throw new EntityNotFoundException("This visit does not belong to the authenticated doctor");
        }

        visitStatusService.cancel(visit, Role.DOCTOR);
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit rescheduleOwnVisit(UUID visitId, LocalDateTime newTime) {
        Visit visit = getById(visitId);
        Doctor doctor = getAuthenticatedDoctor();

        if (!visit.getDoctor().getId().equals(doctor.getId())) {
            throw new EntityNotFoundException("This visit does not belong to the authenticated doctor");
        }

        visitStatusService.reschedule(visit, newTime, Role.DOCTOR);
        return visitRepository.save(visit);
    }

    @Override
    public Visit getOwnVisitById(UUID visitId) {
        Visit visit = getById(visitId);
        Doctor doctor = getAuthenticatedDoctor();

        if (!visit.getDoctor().getId().equals(doctor.getId())) {
            throw new EntityNotFoundException("This visit does not belong to the authenticated doctor");
        }

        return visit;
    }
}