package com.example.demo.repository;

import com.example.demo.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, UUID> {

    List<DoctorAvailability> findByDoctorId(UUID doctorId);

    List<DoctorAvailability> findByDoctorIdAndAvailableTimeAfter(UUID doctorId, LocalDateTime date);

    boolean existsByDoctorIdAndAvailableTime(UUID doctorId, LocalDateTime time);

    Optional<DoctorAvailability> findByDoctorIdAndAvailableTime(UUID doctorId, LocalDateTime time);

    Optional<DoctorAvailability> findByDoctorIdAndAvailableTimeAndIsActiveTrue(UUID doctorId, LocalDateTime availableTime);

    // 🔹 Пошук у діапазоні дат
    List<DoctorAvailability> findByDoctorIdAndAvailableTimeBetween(UUID doctorId,
                                                                   LocalDateTime start,
                                                                   LocalDateTime end);
}