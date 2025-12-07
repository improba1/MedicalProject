package com.example.demo.repository;

import com.example.demo.enums.VisitStatus;
import com.example.demo.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {
    List<Visit> findByDoctorId(UUID doctorId);
    List<Visit> findByPatientId(UUID patientId);

    List<Visit> findByStatus(VisitStatus status);

    // пошук візитів у певному часовому діапазоні
    List<Visit> findByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);

    // пошук майбутніх візитів пацієнта
    @Query("SELECT v FROM Visit v WHERE v.patient.id = :patientId AND v.appointmentTime > CURRENT_TIMESTAMP")
    List<Visit> findUpcomingVisitsByPatient(UUID patientId);

    // пошук завершених візитів лікаря
    @Query("SELECT v FROM Visit v WHERE v.doctor.id = :doctorId AND v.status = 'COMPLETED'")
    List<Visit> findCompletedVisitsByDoctor(UUID doctorId);
}