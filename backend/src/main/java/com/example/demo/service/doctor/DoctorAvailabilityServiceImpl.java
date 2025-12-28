package com.example.demo.service.doctor;

import com.example.demo.model.Doctor;
import com.example.demo.model.DoctorAvailability;
import com.example.demo.repository.DoctorAvailabilityRepository;
import com.example.demo.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;

    // ==========================
    // 🔥 УТИЛІТА: авто-деактивація минулих слотів
    // ==========================
    private void deactivatePastSlots(UUID doctorId) {
        LocalDateTime now = LocalDateTime.now();

        List<DoctorAvailability> activeSlots =
                availabilityRepository.findByDoctorIdAndIsActiveTrue(doctorId);

        activeSlots.stream()
                .filter(slot -> slot.getAvailableTime().isBefore(now))
                .forEach(slot -> {
                    slot.setActive(false);
                    availabilityRepository.save(slot);
                });
    }

    private Doctor getAuthenticatedDoctor() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated doctor not found"));
    }

    @Override
    public DoctorAvailability getById(UUID id) {
        return availabilityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Availability not found"));
    }

    // ==========================
    // 🔹 CRUD для адміна
    // ==========================

    @Override
    public DoctorAvailability createForDoctor(UUID doctorId, DoctorAvailability availability) {
        availability.setDoctorId(doctorId);
        return availabilityRepository.save(availability);
    }

    @Override
    public DoctorAvailability updateForDoctor(UUID doctorId, DoctorAvailability availability) {
        DoctorAvailability existing = getById(availability.getId());

        if (!existing.getDoctorId().equals(doctorId)) {
            throw new EntityNotFoundException("This availability does not belong to the specified doctor");
        }

        existing.setAvailableTime(availability.getAvailableTime());
        return availabilityRepository.save(existing);
    }

    @Override
    public void deleteForDoctor(UUID doctorId, UUID availabilityId) {
        DoctorAvailability availability = getById(availabilityId);

        if (!availability.getDoctorId().equals(doctorId)) {
            throw new EntityNotFoundException("Availability not found for this doctor");
        }

        availabilityRepository.delete(availability);
    }

    // ==========================
    // 🔹 CRUD для залогованого лікаря
    // ==========================

    @Override
    public DoctorAvailability create(DoctorAvailability availability) {
        Doctor doctor = getAuthenticatedDoctor();
        availability.setDoctorId(doctor.getId());
        return availabilityRepository.save(availability);
    }

    @Override
    public DoctorAvailability updateExisting(DoctorAvailability availability) {
        Doctor doctor = getAuthenticatedDoctor();
        DoctorAvailability existing = getById(availability.getId());

        if (!existing.getDoctorId().equals(doctor.getId())) {
            throw new EntityNotFoundException("This availability does not belong to the authenticated doctor");
        }

        existing.setAvailableTime(availability.getAvailableTime());
        return availabilityRepository.save(existing);
    }

    @Override
    public void deleteOwn(UUID availabilityId) {
        Doctor doctor = getAuthenticatedDoctor();
        DoctorAvailability availability = getById(availabilityId);

        if (!availability.getDoctorId().equals(doctor.getId())) {
            throw new EntityNotFoundException("Availability not found for this doctor");
        }

        availabilityRepository.delete(availability);
    }

    // ==========================
    // 🔹 Отримання слотів (з авто-деактивацією)
    // ==========================

    @Override
    public List<DoctorAvailability> getByDoctor(UUID doctorId) {
        deactivatePastSlots(doctorId);
        return availabilityRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<DoctorAvailability> getOwnAvailabilities() {
        Doctor doctor = getAuthenticatedDoctor();
        return getByDoctor(doctor.getId());
    }

    // ==========================
    // 🔥 Тільки активні слоти
    // ==========================

    @Override
    public List<DoctorAvailability> getActiveSlots(UUID doctorId) {
        deactivatePastSlots(doctorId);
        return availabilityRepository.findByDoctorIdAndIsActiveTrue(doctorId);
    }

    @Override
    public List<DoctorAvailability> getOwnActiveSlots() {
        Doctor doctor = getAuthenticatedDoctor();
        return getActiveSlots(doctor.getId());
    }

    // ==========================
    // 🔹 Фільтри для залогованого лікаря
    // ==========================

    @Override
    public List<DoctorAvailability> getOwnByDay(int year, int month, int day) {
        Doctor doctor = getAuthenticatedDoctor();
        return getByDay(doctor.getId(), year, month, day);
    }

    @Override
    public List<DoctorAvailability> getOwnByMonth(int year, int month) {
        Doctor doctor = getAuthenticatedDoctor();
        return getByMonth(doctor.getId(), year, month);
    }

    @Override
    public List<DoctorAvailability> getOwnByYear(int year) {
        Doctor doctor = getAuthenticatedDoctor();
        return getByYear(doctor.getId(), year);
    }

    @Override
    public List<DoctorAvailability> getOwnToday() {
        Doctor doctor = getAuthenticatedDoctor();
        return getToday(doctor.getId());
    }

    @Override
    public List<DoctorAvailability> getOwnNextHour() {
        Doctor doctor = getAuthenticatedDoctor();
        return getNextHour(doctor.getId());
    }

    @Override
    public List<DoctorAvailability> getOwnThisWeek() {
        Doctor doctor = getAuthenticatedDoctor();
        return getThisWeek(doctor.getId());
    }

    @Override
    public List<DoctorAvailability> getOwnNextWeek() {
        Doctor doctor = getAuthenticatedDoctor();
        return getNextWeek(doctor.getId());
    }

    // ==========================
    // 🔹 Фільтри по датах (адмінські)
    // ==========================

    @Override
    public List<DoctorAvailability> getByDay(UUID doctorId, int year, int month, int day) {
        deactivatePastSlots(doctorId);
        LocalDateTime start = LocalDateTime.of(year, month, day, 0, 0);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, start.plusDays(1));
    }

    @Override
    public List<DoctorAvailability> getByMonth(UUID doctorId, int year, int month) {
        deactivatePastSlots(doctorId);
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, start.plusMonths(1));
    }

    @Override
    public List<DoctorAvailability> getByYear(UUID doctorId, int year) {
        deactivatePastSlots(doctorId);
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, start.plusYears(1));
    }

    @Override
    public List<DoctorAvailability> getToday(UUID doctorId) {
        deactivatePastSlots(doctorId);
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, start.plusDays(1));
    }

    @Override
    public List<DoctorAvailability> getNextHour(UUID doctorId) {
        deactivatePastSlots(doctorId);
        LocalDateTime start = LocalDateTime.now();
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, start.plusHours(1));
    }

    @Override
    public List<DoctorAvailability> getThisWeek(UUID doctorId) {
        deactivatePastSlots(doctorId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(java.time.DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, start.plusWeeks(1));
    }

    @Override
    public List<DoctorAvailability> getNextWeek(UUID doctorId) {
        deactivatePastSlots(doctorId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(java.time.DayOfWeek.MONDAY).plusWeeks(1).toLocalDate().atStartOfDay();
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, start.plusWeeks(1));
    }
}