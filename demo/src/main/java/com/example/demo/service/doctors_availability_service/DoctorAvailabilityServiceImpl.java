package com.example.demo.service.doctors_availability_service;

import com.example.demo.dto.request.doctor_availability.AddAvailabilityRequest;
import com.example.demo.dto.request.doctor_availability.UpdateAvailabilityRequest;
import com.example.demo.model.Doctor;
import com.example.demo.model.DoctorAvailability;
import com.example.demo.repository.DoctorAvailabilityRepository;
import com.example.demo.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public DoctorAvailability getById(UUID id) {
        return availabilityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Availability not found"));
    }

    @Override
    public DoctorAvailability create(DoctorAvailability availability) {
        return availabilityRepository.save(availability);
    }

    @Override
    public DoctorAvailability update(UUID id, UpdateAvailabilityRequest request) {
        DoctorAvailability availability = getById(id);
        availability.setAvailableTime(request.getNewAvailableTime());
        return availabilityRepository.save(availability);
    }

    @Override
    public void delete(UUID id) {
        availabilityRepository.deleteById(id);
    }

    @Override
    public List<DoctorAvailability> getByDoctor(UUID doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }

    @Override
    public DoctorAvailability addAvailability(UUID doctorId, AddAvailabilityRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        DoctorAvailability availability = DoctorAvailability.builder()
                .doctor(doctor)
                .availableTime(request.getAvailableTime())
                .build();

        return availabilityRepository.save(availability);
    }

    // 🔹 Оновлення слота конкретного лікаря
    @Override
    public DoctorAvailability updateForDoctor(UUID doctorId, UUID availabilityId, UpdateAvailabilityRequest request) {
        DoctorAvailability availability = getById(availabilityId);

        if (!availability.getDoctor().getId().equals(doctorId)) {
            throw new EntityNotFoundException("Availability not found for this doctor");
        }

        availability.setAvailableTime(request.getNewAvailableTime());
        return availabilityRepository.save(availability);
    }

    // 🔹 Видалення слота конкретного лікаря
    @Override
    public void deleteForDoctor(UUID doctorId, UUID availabilityId) {
        DoctorAvailability availability = getById(availabilityId);

        if (!availability.getDoctor().getId().equals(doctorId)) {
            throw new EntityNotFoundException("Availability not found for this doctor");
        }

        availabilityRepository.delete(availability);
    }



    // 🔹 На конкретний день
    @Override
    public List<DoctorAvailability> getByDay(UUID doctorId, int year, int month, int day) {
        LocalDateTime start = LocalDateTime.of(year, month, day, 0, 0);
        LocalDateTime end = start.plusDays(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    // 🔹 На конкретний місяць
    @Override
    public List<DoctorAvailability> getByMonth(UUID doctorId, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    // 🔹 На конкретний рік
    @Override
    public List<DoctorAvailability> getByYear(UUID doctorId, int year) {
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = start.plusYears(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    // 🔹 На сьогодні
    @Override
    public List<DoctorAvailability> getToday(UUID doctorId) {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusDays(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    // 🔹 На найближчу годину
    @Override
    public List<DoctorAvailability> getNextHour(UUID doctorId) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    // 🔹 На цей тиждень
    @Override
    public List<DoctorAvailability> getThisWeek(UUID doctorId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(java.time.DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusWeeks(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }

    // 🔹 На наступний тиждень
    @Override
    public List<DoctorAvailability> getNextWeek(UUID doctorId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(java.time.DayOfWeek.MONDAY).plusWeeks(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusWeeks(1);
        return availabilityRepository.findByDoctorIdAndAvailableTimeBetween(doctorId, start, end);
    }
}