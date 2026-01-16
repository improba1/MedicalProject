package com.example.demo.service.raport;

import com.example.demo.model.Raport;
import com.example.demo.repository.RaportRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.repository.specification.raport.RaportSpecificationBuilder;
import com.example.demo.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.example.demo.enums.Role.ADMIN;
import static com.example.demo.enums.VisitStatus.COMPLETED;

@Service
@RequiredArgsConstructor
public class RaportServiceImpl implements RaportService {

    private final RaportRepository raportRepository;
    private final CurrentUserService currentUserService;
    private final VisitRepository visitRepository;

    private boolean isAdmin() {
        return currentUserService.getAuthenticatedUser().getRole().equals(ADMIN);
    }

    private UUID getCurrentDoctorId() {
        return currentUserService.getDoctorId();
    }

    private void assertDoctorCanEdit(Raport raport) {
        if (isAdmin()) return;
        UUID doctorId = getCurrentDoctorId();
        if (!raport.getVisit().getDoctor().getId().equals(doctorId)) {
            throw new IllegalStateException("You cannot edit another doctor's raport");
        }
        if (raport.getVisit().getStatus().equals(COMPLETED)) {
            throw new IllegalStateException("Cannot edit raport after visit is completed");
        }
        if (raport.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Raport can only be edited within 24 hours after creation");
        }
    }

    @Override
    public Raport getRaportById(UUID id) {
        return raportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Raport not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Raport getRaportByIdForAuthenticatedDoctor(UUID id) {
        UUID doctorId = currentUserService.getAuthenticatedDoctor().getId();

        Raport raport = raportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Raport not found with id: " + id));

        if (!raport.getVisit().getDoctor().getId().equals(doctorId)) {
            throw new AccessDeniedException("You can access only your own raports");
        }

        return raport;
    }

    @Override
    @Transactional(readOnly = true)
    public Raport getRaportByIdForAuthenticatedPatient(UUID id) {
        UUID patientId = currentUserService.getAuthenticatedPatient().getId();

        Raport raport = raportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Raport not found with id: " + id));

        if (!raport.getVisit().getPatient().getId().equals(patientId)) {
            throw new AccessDeniedException("You can access only your own raports");
        }

        return raport;
    }



    @Override
    public Raport create(Raport raport) {
        raport.setCreatedAt(LocalDateTime.now());
        return raportRepository.save(raport);
    }

    @Override
    public Raport update(UUID id, Raport raportUpdate) {

        Raport raport = getRaportById(id);
        assertDoctorCanEdit(raport);

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
        raportRepository.delete(getRaportById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Raport> searchForAdmin(
            UUID visitId,
            UUID doctorId,
            UUID patientId,
            String disease,
            String receipt,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return raportRepository.findAll(
                RaportSpecificationBuilder.build(
                        visitId, doctorId, patientId, disease, receipt, minPrice, maxPrice, from, to
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Raport> searchForPatient(
            UUID visitId,
            UUID doctorId,
            String disease,
            String receipt,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime from,
            LocalDateTime to
    ) {
        UUID patientId = currentUserService.getAuthenticatedPatient().getId();

        if (doctorId != null &&
                !visitRepository.existsByDoctorIdAndPatientId(doctorId, patientId)) {
            throw new AccessDeniedException("You have no visits with this doctor");
        }

        if (visitId != null &&
                !visitRepository.existsByIdAndPatientId(visitId, patientId)) {
            throw new AccessDeniedException("You can search only your own visits");
        }

        return raportRepository.findAll(
                RaportSpecificationBuilder.build(
                        visitId, patientId, doctorId, disease, receipt, minPrice, maxPrice, from, to
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Raport> searchForDoctor(
            UUID visitId,
            UUID patientId,
            String disease,
            String receipt,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime from,
            LocalDateTime to
    ) {
        UUID doctorId = currentUserService.getAuthenticatedDoctor().getId();

        if (patientId != null &&
                !visitRepository.existsByDoctorIdAndPatientId(doctorId, patientId)) {
            throw new AccessDeniedException("You have no visits with this patient");
        }

        if (visitId != null &&
                !visitRepository.existsByIdAndDoctorId(visitId, doctorId)) {
            throw new AccessDeniedException("You can search only your own visits");
        }

        return raportRepository.findAll(
                RaportSpecificationBuilder.build(
                        visitId, doctorId, patientId, disease, receipt, minPrice, maxPrice, from, to
                )
        );
    }
}