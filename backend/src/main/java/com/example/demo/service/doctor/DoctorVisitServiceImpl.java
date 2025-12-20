package com.example.demo.service.doctor;

import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Doctor;
import com.example.demo.model.Visit;
import com.example.demo.repository.DoctorRepository;
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
public class DoctorVisitServiceImpl implements DoctorVisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public List<Visit> getVisits(UUID doctorId) {
        return visitRepository.findByDoctorId(doctorId);
    }

    @Override
    public Visit cancelVisit(UUID visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
        visit.setStatus(VisitStatus.CANCELED);
        return visitRepository.save(visit);
    }

    @Override
    public Visit rescheduleVisit(UUID visitId, LocalDateTime newTime) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
        visit.setAppointmentTime(newTime);
        visit.setStatus(VisitStatus.RESCHEDULED);
        return visitRepository.save(visit);
    }

    // ==========================
    // 🔹 Методи для залогованого лікаря
    // ==========================

    private Doctor getAuthenticatedDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"));
    }

    @Override
    public List<Visit> getOwnVisits() {
        Doctor currentDoctor = getAuthenticatedDoctor();
        return visitRepository.findByDoctorId(currentDoctor.getId());
    }

    @Override
    public Visit cancelOwnVisit(UUID visitId) {
        Doctor currentDoctor = getAuthenticatedDoctor();
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));

        if (!visit.getDoctor().getId().equals(currentDoctor.getId())) {
            throw new EntityNotFoundException("This visit does not belong to the authenticated doctor");
        }

        visit.setStatus(VisitStatus.CANCELED);
        return visitRepository.save(visit);
    }

    @Override
    public Visit rescheduleOwnVisit(UUID visitId, LocalDateTime newTime) {
        Doctor currentDoctor = getAuthenticatedDoctor();
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));

        if (!visit.getDoctor().getId().equals(currentDoctor.getId())) {
            throw new EntityNotFoundException("This visit does not belong to the authenticated doctor");
        }
        visit.setAppointmentTime(newTime);
        visit.setStatus(VisitStatus.RESCHEDULED);
        return visitRepository.save(visit);
    }

    @Override
    public Visit getOwnVisitById(UUID visitId) {
        Doctor currentDoctor = getAuthenticatedDoctor();

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));

        if (!visit.getDoctor().getId().equals(currentDoctor.getId())) {
            throw new EntityNotFoundException("This visit does not belong to the authenticated doctor");
        }
        return visit;
    }
}