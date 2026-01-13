package com.example.demo.service.doctor;

import com.example.demo.enums.Role;
import com.example.demo.model.Doctor;
import com.example.demo.model.DoctorAvailability;
import com.example.demo.repository.DoctorAvailabilityRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.auth.CurrentUserService;
import com.example.demo.service.slot.SlotService;
import com.example.demo.service.visit.VisitStatusService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    private LocalDateTime startOfDay(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 0, 0);
    }

    private LocalDateTime startOfToday() {
        return LocalDate.now().atStartOfDay();
    }

    private LocalDateTime startOfWeek(LocalDate date) {
        LocalDate monday = date.with(DayOfWeek.MONDAY);
        return monday.atStartOfDay();
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

    // ==========================
    // 🔹 Отримання слотів (адмін)
    // ==========================

    @Override
    public List<DoctorAvailability> getByDoctor(UUID doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<DoctorAvailability> getActiveSlots(UUID doctorId) {
        return availabilityRepository.findByDoctorIdAndIsActiveTrue(doctorId);
    }

    @Override
    public List<DoctorAvailability> getByDay(UUID doctorId, int year, int month, int day) {
        LocalDateTime start = startOfDay(year, month, day);
        LocalDateTime end = start.plusDays(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    @Override
    public List<DoctorAvailability> getByMonth(UUID doctorId, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    @Override
    public List<DoctorAvailability> getByYear(UUID doctorId, int year) {
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = start.plusYears(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    @Override
    public List<DoctorAvailability> getToday(UUID doctorId) {
        LocalDateTime start = startOfToday();
        LocalDateTime end = start.plusDays(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    @Override
    public List<DoctorAvailability> getNextHour(UUID doctorId) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    @Override
    public List<DoctorAvailability> getThisWeek(UUID doctorId) {
        LocalDateTime start = startOfWeek(LocalDate.now());
        LocalDateTime end = start.plusWeeks(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    @Override
    public List<DoctorAvailability> getNextWeek(UUID doctorId) {
        LocalDateTime start = startOfWeek(LocalDate.now()).plusWeeks(1);
        LocalDateTime end = start.plusWeeks(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    // ==========================
    // 🔹 Отримання для залогованого лікаря
    // ==========================

    @Override
    public List<DoctorAvailability> getOwnAvailabilities() {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return getByDoctor(doctor.getId());
    }

    @Override
    public List<DoctorAvailability> getOwnActiveSlots() {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return getActiveSlots(doctor.getId());
    }

    @Override
    public List<DoctorAvailability> getOwnByDay(int year, int month, int day) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return getByDay(doctor.getId(), year, month, day);
    }

    @Override
    public List<DoctorAvailability> getOwnByMonth(int year, int month) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return getByMonth(doctor.getId(), year, month);
    }

    @Override
    public List<DoctorAvailability> getOwnByYear(int year) {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return getByYear(doctor.getId(), year);
    }

    @Override
    public List<DoctorAvailability> getOwnToday() {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return getToday(doctor.getId());
    }

    @Override
    public List<DoctorAvailability> getOwnNextHour() {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return getNextHour(doctor.getId());
    }

    @Override
    public List<DoctorAvailability> getOwnThisWeek() {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return getThisWeek(doctor.getId());
    }

    @Override
    public List<DoctorAvailability> getOwnNextWeek() {
        Doctor doctor = currentUserService.getAuthenticatedDoctor();
        return getNextWeek(doctor.getId());
    }
}