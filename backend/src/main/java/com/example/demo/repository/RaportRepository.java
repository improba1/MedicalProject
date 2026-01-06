package com.example.demo.repository;

import com.example.demo.model.Raport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RaportRepository extends JpaRepository<Raport, UUID> {

    // Знайти всі рапорти певного лікаря
    List<Raport> findByDoctorId(UUID doctorId);

    // Знайти всі рапорти певного пацієнта
    List<Raport> findByPatientId(UUID patientId);

    // Знайти всі рапорти за візитом
    Raport findByVisitId(UUID visitId);

    // 🔥 Нові фільтри
    List<Raport> findByPatientIdAndCreatedAtBetween(UUID patientId, LocalDateTime start, LocalDateTime end);

    List<Raport> findByDoctorIdAndCreatedAtBetween(UUID doctorId, LocalDateTime start, LocalDateTime end);

    List<Raport> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Raport> findByVisitIdAndDoctorId(UUID visitId, UUID doctorId);

    List<Raport> findByVisitIdAndPatientId(UUID visitId, UUID patientId);
}