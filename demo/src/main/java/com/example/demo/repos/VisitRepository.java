package com.example.demo.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, String> {
    List<Visit> findByActiveTrue(); 
    List<Visit> findByActiveFalse();
    List<Visit> findAll();
    Optional<Visit> findById(UUID id); 
    void deleteById(UUID id); 
}
