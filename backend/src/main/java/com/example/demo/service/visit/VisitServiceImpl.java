package com.example.demo.service.visit;

import com.example.demo.enums.Role;
import com.example.demo.enums.VisitStatus;
import com.example.demo.model.*;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.RaportRepository;
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
import java.util.stream.Collectors;

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
    private final RaportRepository  raportRepository;

    private Visit getVisit(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
    }

    private void assertDoctorOwnsVisit(Visit visit) {
        if (!visit.getDoctor().getId().equals(currentUserService.getDoctorId())) {
            throw new EntityNotFoundException("Visit not found for this doctor");
        }
    }

    private Visit getVisitAndAssertDoctor(UUID visitId) {
        UUID doctorId = currentUserService.getDoctorId();
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit not found"));
        if (!visit.getDoctor().getId().equals(doctorId)) {
            throw new AccessDeniedException("You can access only your own visits");
        }
        if (visit.getStatus() != VisitStatus.PAID &&
                visit.getStatus() != VisitStatus.COMPLETED) {
            throw new IllegalStateException("Visit must be paid before processing");
        }
        return visit;
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

    private String buildServicesSnapshot(List<VisitServiceItem> items) {
        return items.stream()
                .map(item -> String.format(
                        "serviceId=%s; name=%s; price=%s; qty=%d",
                        item.getService().getId(),
                        item.getServiceName(),
                        item.getPriceAtMomentOfPurchase(),
                        item.getQuantity()
                ))
                .collect(Collectors.joining("\n"));
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

    @Override
    @Transactional
    public Visit createByAdmin(Visit visit) {

        Patient patient = userRepository.findById(visit.getPatient().getId())
                .map(Patient.class::cast)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(visit.getDoctor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        slotService.occupy(doctor.getId(), visit.getAppointmentTime());

        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setStatus(VisitStatus.SCHEDULED);
        visit.setCartLocked(false);

        return visitRepository.save(visit);
    }

    @Override
    @Transactional
    public Visit createByPatient(Visit visit) {

        UUID patientId = currentUserService.getPatientId();

        Patient patient = userRepository.findById(patientId)
                .map(Patient.class::cast)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        UUID doctorId = visit.getDoctor().getId();

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        slotService.occupy(doctor.getId(), visit.getAppointmentTime());

        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setStatus(VisitStatus.SCHEDULED);
        visit.setCartLocked(false);

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

    @Override
    public Raport updateRaport(UUID visitId, Raport raportUpdate) {
        Visit visit = getVisitAndAssertDoctor(visitId);

        Raport raport = visit.getRaport();
        if (raport == null) {
            raport = Raport.builder()
                    .visit(visit)
                    .doctor(visit.getDoctor())
                    .patient(visit.getPatient())
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        if (raportUpdate.getDisease() != null)
            raport.setDisease(raportUpdate.getDisease());
        if (raportUpdate.getTreatmentPlan() != null)
            raport.setTreatmentPlan(raportUpdate.getTreatmentPlan());
        if (raportUpdate.getDoctorNotes() != null)
            raport.setDoctorNotes(raportUpdate.getDoctorNotes());

        raportRepository.save(raport);

        visit.setRaport(raport);
        visitRepository.save(visit);

        return raport;
    }

    @Override
    public Visit completeVisit(UUID visitId) {
        Visit visit = getVisitAndAssertDoctor(visitId);
        visitStatusService.complete(visit, Role.DOCTOR);
        return visitRepository.save(visit);
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

    @Override
    @Transactional
    public void lockCart(UUID visitId) {
        Visit visit = getVisit(visitId);
        if (visit.isCartLocked()) {
            return;
        }
        List<VisitServiceItem> services = visit.getServices();
        if (services == null || services.isEmpty()) {
            throw new IllegalStateException("Cannot lock empty cart");
        }
        visit.setCartLocked(true);
        Raport raport = Raport.builder()
                .visit(visit)
                .doctor(visit.getDoctor())
                .patient(visit.getPatient())
                .disease("")
                .symptoms(visit.getPatientSymptoms())
                .totalPrice(calculateTotalPrice(visit.getId()))
                .servicesSnapshot(buildServicesSnapshot(services))
                .createdAt(LocalDateTime.now())
                .build();

        visit.setRaport(raport);
        visitRepository.save(visit);
    }
}