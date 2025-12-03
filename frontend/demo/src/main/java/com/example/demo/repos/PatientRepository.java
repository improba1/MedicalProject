package com.example.demo.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    List<Patient> findByActiveTrue(); 
    List<Patient> findByActiveFalse();
    List<Patient> findAll();
    Optional<Patient> findById(UUID id); 
    void deleteById(UUID id); 
    Optional<Patient> findByLogin(String login);
}