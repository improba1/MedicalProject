package com.example.demo.repository;

import com.example.demo.model.Patient;
import com.example.demo.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByEmail(String email);
    // пошук пацієнтів за прізвищем
    List<Patient> findByLastnameContainingIgnoreCase(String lastname);

    // всі візити пацієнта
    @Query("SELECT v FROM Visit v WHERE v.patient.id = :patientId")
    List<Visit> findVisitsByPatientId(UUID patientId);
}
