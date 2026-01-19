package com.example.demo.repository;

import com.example.demo.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID>, JpaSpecificationExecutor<Visit> {

    boolean existsByDoctorIdAndAppointmentTime(UUID doctorId, LocalDateTime appointmentTime);

    boolean existsByDoctorIdAndPatientId(UUID doctorId, UUID patientId);

    boolean existsByIdAndPatientId(UUID visitId, UUID patientId);

    boolean existsByIdAndDoctorId(UUID visitId, UUID doctorId);

    Optional<Visit> findByDoctorIdAndAppointmentTime(UUID doctorId, LocalDateTime appointmentTime);

    @Query("""
    select v from Visit v
    left join fetch v.services
    where v.doctor.id = :doctorId
""")
    List<Visit> findAllWithServicesByDoctorId(UUID doctorId);
}