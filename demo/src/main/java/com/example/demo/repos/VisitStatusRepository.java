package com.example.demo.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.VisitStatus;

@Repository
public interface VisitStatusRepository extends JpaRepository<VisitStatus, String> {
    List<VisitStatus> findByActiveTrue(); 
    List<VisitStatus> findByActiveFalse();
    List<VisitStatus> findAll();
    Optional<VisitStatus> findById(UUID id); 
    void deleteById(UUID id); 
    Optional<VisitStatus> findByStatus(String status);
    
}
