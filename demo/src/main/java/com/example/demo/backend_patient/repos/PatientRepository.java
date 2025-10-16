package com.example.demo.backend_patient.repos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Patient;


@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    List<Patient> findByActiveTrue(); 
    List<Patient> findByActiveFalse();
    List<Patient> findAll();
    Optional<Patient> findById(String id); 
    void deleteById(String id); 
    Optional<Patient> findByLogin(String login);
}