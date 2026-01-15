package com.example.demo.service.doctor;

import com.example.demo.dto.request.doctor_availability.DoctorAvailabilitySearchRequest;
import com.example.demo.enums.Role;
import com.example.demo.model.Doctor;
import com.example.demo.model.DoctorAvailability;
import com.example.demo.repository.DoctorAvailabilityRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.repository.specification.doctor.DoctorAvailabilitySpecification;
import com.example.demo.service.auth.CurrentUserService;
import com.example.demo.service.slot.SlotService;
import com.example.demo.service.visit.VisitStatusService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;
    private final VisitRepository visitRepository;
    private final VisitStatusService visitStatusService;
    private final SlotService slotService;
    private final CurrentUserService currentUserService;

    // ==========================
    // 🔹 Хелпери
    // ==========================

    private void ensureNotPast(DoctorAvailability slot) {
        if (slot.getAvailableTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot modify past availability. Create a new slot instead.");
        }
    }

    private void ensureNoVisit(DoctorAvailability slot) {
        boolean hasVisit = visitRepository.existsByDoctorIdAndAppointmentTime(
                slot.getDoctorId(),
                slot.getAvailableTime()
        );

        if (hasVisit) {
            throw new IllegalStateException("Cannot delete availability with an active visit.");
        }
    }

    private void updateActiveStatus(DoctorAvailability slot) {
        slot.setActive(slot.getAvailableTime().isAfter(LocalDateTime.now()));
    }

    // ==========================
    // 🔹 Отримання по id
    // ==========================

    @Override
    public DoctorAvailability getById(UUID id) {
        return availabilityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Availability not found"));
    }

    // ==========================
    // 🔹 CRUD для адміна (по doctorId)
    // ==========================

    @Override
    public DoctorAvailability createForDoctor(UUID doctorId, DoctorAvailability availability) {
        availability.setDoctorId(doctorId);
        updateActiveStatus(availability);
        return availabilityRepository.save(availability);
    }

    @Override
    @Transactional
    public DoctorAvailability updateForDoctor(UUID doctorId, DoctorAvailability availability) {
        DoctorAvailability existing = getById(availability.getId());

        if (!existing.getDoctorId().equals(doctorId)) {
            throw new EntityNotFoundException("This availability does not belong to the specified doctor");
        }

        ensureNotPast(existing);

        LocalDateTime oldTime = existing.getAvailableTime();
        LocalDateTime newTime = availability.getAvailableTime();

        if (!oldTime.equals(newTime)) {

            visitRepository.findByDoctorIdAndAppointmentTime(
                    doctorId,
                    oldTime
            ).ifPresent(visit -> visitStatusService.reschedule(visit, newTime, Role.ADMIN));

            slotService.reschedule(doctorId, oldTime, newTime);
        }

        existing.setAvailableTime(newTime);
        updateActiveStatus(existing);

        return availabilityRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteForDoctor(UUID doctorId, UUID availabilityId) {
        DoctorAvailability slot = getById(availabilityId);

        if (!slot.getDoctorId().equals(doctorId)) {
            throw new EntityNotFoundException("Availability not found for this doctor");
        }

        ensureNoVisit(slot);

        availabilityRepository.delete(slot);
    }

    // ==========================
    // 🔹 CRUD для залогованого лікаря
    // ==========================

    @Override
    public DoctorAvailability create(DoctorAvailability availability) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        availability.setDoctorId(doctor.getId());
        updateActiveStatus(availability);
        return availabilityRepository.save(availability);
    }

    @Override
    @Transactional
    public DoctorAvailability updateExisting(DoctorAvailability availability) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        DoctorAvailability existing = getById(availability.getId());

        if (!existing.getDoctorId().equals(doctor.getId())) {
            throw new EntityNotFoundException("This availability does not belong to the authenticated doctor");
        }

        ensureNotPast(existing);

        LocalDateTime oldTime = existing.getAvailableTime();
        LocalDateTime newTime = availability.getAvailableTime();

        if (!oldTime.equals(newTime)) {

            visitRepository.findByDoctorIdAndAppointmentTime(
                    existing.getDoctorId(),
                    oldTime
            ).ifPresent(visit -> visitStatusService.reschedule(visit, newTime, Role.DOCTOR));

            slotService.reschedule(existing.getDoctorId(), oldTime, newTime);
        }

        existing.setAvailableTime(newTime);
        updateActiveStatus(existing);

        return availabilityRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteOwn(UUID availabilityId) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        DoctorAvailability slot = getById(availabilityId);

        if (!slot.getDoctorId().equals(doctor.getId())) {
            throw new EntityNotFoundException("Availability not found for this doctor");
        }

        ensureNoVisit(slot);

        availabilityRepository.delete(slot);
    }
    @Override
    public List<DoctorAvailability> getByDoctor(UUID doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailability> searchForAdmin(UUID doctorId, DoctorAvailabilitySearchRequest req) {
        return availabilityRepository.findAll(
                DoctorAvailabilitySpecification.search(
                        doctorId,
                        req.getActive(),
                        req.getFrom(),
                        req.getTo()
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailability> searchForAuthenticatedDoctor(DoctorAvailabilitySearchRequest req) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();

        return availabilityRepository.findAll(
                DoctorAvailabilitySpecification.search(
                        doctor.getId(),
                        req.getActive(),
                        req.getFrom(),
                        req.getTo()
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailability> searchPublic(DoctorAvailabilitySearchRequest req) {
        return availabilityRepository.findAll(
                DoctorAvailabilitySpecification.search(
                        null,
                        true, // 🔒 тільки активні
                        req.getFrom() != null ? req.getFrom() : LocalDateTime.now(),
                        req.getTo()
                )
        );
    }
}