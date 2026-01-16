package com.example.demo.service.visit;

import com.example.demo.enums.Role;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.Visit;
import com.example.demo.model.VisitServiceItem;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.repository.specification.visit.VisitSpecificationBuilder;
import com.example.demo.service.auth.CurrentUserService;
import com.example.demo.service.slot.SlotService;
import com.example.demo.service.visit_service_item.VisitServiceItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final CurrentUserService currentUserService;
    private final VisitServiceItemService visitServiceItemService;

    private Visit getVisit(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
    }

    private void assertDoctorOwnsVisit(Visit visit) {
        if (!visit.getDoctor().getId().equals(currentUserService.getDoctorId())) {
            throw new EntityNotFoundException("Visit not found for this doctor");
        }
    }

    private boolean canModifyCart(Visit visit) {
        if (visit == null) return false;
        if (visit.isCartLocked()) return false;
        if (visit.getStatus() == VisitStatus.PAID
                || visit.getStatus() == VisitStatus.COMPLETED
                || visit.getStatus() == VisitStatus.CANCELED) {
            return false;
        }

        Role role = currentUserService.getAuthenticatedUser().getRole();
        return switch (role) {
            case PATIENT -> visit.getPatient().getId().equals(currentUserService.getPatientId());
            case DOCTOR -> visit.getDoctor().getId().equals(currentUserService.getDoctorId());
            case SUPERADMIN, ADMIN -> true;
        };
    }

    @Override
    public Visit getById(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Visit not found with id: " + id)
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Visit getByIdForAuthenticatedDoctor(UUID id) {
        UUID doctorId = currentUserService.getAuthenticatedDoctor().getId();

        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found with id: " + id));

        if (!visit.getDoctor().getId().equals(doctorId)) {
            throw new AccessDeniedException("You can access only your own visits");
        }

        return visit;
    }

    @Override
    @Transactional(readOnly = true)
    public Visit getByIdForAuthenticatedPatient(UUID id) {
        UUID patientId = currentUserService.getAuthenticatedPatient().getId();

        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found with id: " + id));

        if (!visit.getPatient().getId().equals(patientId)) {
            throw new AccessDeniedException("You can access only your own visits");
        }

        return visit;
    }



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
        if (visit.getStatus() == null) {
            visit.setStatus(VisitStatus.SCHEDULED);
        }
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit createVisitForAuthenticatedPatient(Visit visit) {
        UUID patientId = currentUserService.getPatientId();

        if (!visit.getPatient().getId().equals(patientId)) {
            throw new EntityNotFoundException("Cannot create visit for another patient");
        }

        Doctor doctor = doctorRepository.findById(visit.getDoctor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Patient patient = (Patient) userRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        slotService.occupy(doctor.getId(), visit.getAppointmentTime());
        visit.setDoctor(doctor);
        visit.setPatient(patient);
        if (visit.getStatus() == null) {
            visit.setStatus(VisitStatus.SCHEDULED);
        }
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit cancelVisit(UUID visitId) {
        Visit visit = getVisit(visitId);

        if (!visit.getPatient().getId().equals(currentUserService.getPatientId())) {
            throw new AccessDeniedException("Visit not found for this patient");
        }

        visitStatusService.cancel(visit, Role.PATIENT);
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public Visit rescheduleVisit(UUID visitId, LocalDateTime newTime) {
        Visit visit = getVisit(visitId);
        if (!visit.getPatient().getId().equals(currentUserService.getPatientId())) {
            throw new AccessDeniedException("Visit not found for this patient");
        }
        visitStatusService.reschedule(visit, newTime, Role.PATIENT);
        return visitRepository.save(visit);
    }

    @Override
    public List<Visit> searchForPatient(UUID doctorId, VisitStatus status, LocalDateTime start, LocalDateTime end) {
        UUID patientId = currentUserService.getAuthenticatedPatient().getId();
        if (doctorId != null && !visitRepository.existsByDoctorIdAndPatientId(doctorId, patientId)) {
            throw new AccessDeniedException("You have no visits with this doctor");
        }
        return visitRepository.findAll(
                VisitSpecificationBuilder.build(
                        patientId, doctorId, status, start, end
                )
        );
    }

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

    @Override
    public List<Visit> searchForDoctor(UUID patientId, VisitStatus status, LocalDateTime start, LocalDateTime end) {
        UUID doctorId = currentUserService.getAuthenticatedDoctor().getId();
        if (patientId != null && !visitRepository.existsByDoctorIdAndPatientId(doctorId, patientId)) {
            throw new AccessDeniedException("You have no visits with this patient");
        }

        return visitRepository.findAll(
                VisitSpecificationBuilder.build(
                        doctorId, patientId, status, start, end
                )
        );
    }

    @Transactional
    @Override
    public Visit updateVisit(Visit update) {
        Visit visit = getVisit(update.getId());
        if (update.getAppointmentTime() != null && !update.getAppointmentTime().equals(visit.getAppointmentTime())) {
            visitStatusService.reschedule(visit, update.getAppointmentTime(), Role.ADMIN);
        }
        if (update.getStatus() != null) {
            switch (update.getStatus()) {
                case CANCELED -> visitStatusService.cancel(visit, Role.ADMIN);
                case COMPLETED -> visitStatusService.complete(visit, Role.ADMIN);
                case PAID -> visitStatusService.pay(visit, Role.ADMIN);
                default -> {}
            }
        }
        return visitRepository.save(visit);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Visit visit = getVisit(id);
        LocalDateTime appointmentTime = visit.getAppointmentTime();
        UUID doctorId = visit.getDoctor().getId();
        if (appointmentTime.isAfter(LocalDateTime.now())) {
            slotService.free(doctorId, appointmentTime);
        }
        visitRepository.delete(visit);
    }

    @Override
    public List<Visit> searchForAdmin(UUID doctorId, UUID patientId, VisitStatus status, LocalDateTime start, LocalDateTime end) {
        return visitRepository.findAll(
                VisitSpecificationBuilder.build(
                        doctorId, patientId, status, start, end
                )
        );
    }

    @Transactional
    @Override
    public Visit addItemToVisit(UUID visitId, UUID medicalServiceId, Integer quantity) {
        Visit visit = getVisit(visitId);
        if (!canModifyCart(visit)) {
            throw new AccessDeniedException("You cannot modify this visit");
        }
        visitServiceItemService.addItemToVisit(visitId, medicalServiceId, quantity);
        return visitRepository.findById(visitId).orElse(visit);
    }

    @Transactional
    @Override
    public Visit removeItemFromVisit(UUID visitId, UUID itemId) {
        Visit visit = getVisit(visitId);
        if (!canModifyCart(visit)) {
            throw new AccessDeniedException("You cannot modify this visit");
        }
        visitServiceItemService.removeItem(itemId);
        return visitRepository.findById(visitId).orElse(visit);
    }

    @Transactional
    @Override
    public Visit clearCart(UUID visitId) {
        Visit visit = getVisit(visitId);
        if (!canModifyCart(visit)) {
            throw new AccessDeniedException("You cannot modify this visit");
        }
        visitServiceItemService.clearItemsForVisit(visitId);
        return visitRepository.findById(visitId).orElse(visit);
    }

    @Transactional
    @Override
    public Visit updateItemQuantity(UUID visitId, UUID itemId, Integer quantity) {
        Visit visit = getVisit(visitId);
        if (!canModifyCart(visit)) {
            throw new AccessDeniedException("You cannot modify this visit");
        }
        visitServiceItemService.updateItemQuantity(itemId, quantity);
        return visitRepository.findById(visitId).orElse(visit);
    }

    @Override
    public BigDecimal calculateTotalPrice(UUID visitId) {
        List<VisitServiceItem> items = visitServiceItemService.getItemsForVisit(visitId);
        return items.stream()
                .map(i -> i.getPriceAtMomentOfPurchase()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    @Override
    public Visit lockCart(UUID visitId) {
        Visit visit = getVisit(visitId);
        if (visit.isCartLocked()) {
            return visit;
        }
        if (visit.getStatus() == VisitStatus.PAID || visit.getStatus() == VisitStatus.COMPLETED || visit.getStatus() == VisitStatus.CANCELED) {
            throw new IllegalStateException("Cart cannot be locked in current visit status");
        }
        if (visit.getServices() == null || visit.getServices().isEmpty()) {
            throw new IllegalStateException("Cannot lock empty cart");
        }
        visit.setCartLocked(true);
        return visitRepository.save(visit);
    }
}