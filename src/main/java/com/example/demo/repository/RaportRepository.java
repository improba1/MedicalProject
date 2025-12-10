package com.example.demo.repository;

import com.example.demo.model.Raport;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RaportRepository extends JpaRepository<Raport, UUID> {

    // Знайти всі рапорти певного лікаря
    List<Raport> findByDoctorId(UUID doctorId);

    // Знайти всі рапорти певного пацієнта
    List<Raport> findByPatientId(UUID patientId);

    // Знайти всі рапорти за візитом
    Raport findByVisitId(UUID visitId);
}