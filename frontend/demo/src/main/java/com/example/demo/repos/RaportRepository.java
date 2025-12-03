package com.example.demo.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Raport;

@Repository
public interface RaportRepository extends JpaRepository<Raport, String> {
    List<Raport> findByActiveTrue(); 
    List<Raport> findByActiveFalse();
    List<Raport> findAll();
    Optional<Raport> findById(UUID id); 
    void deleteById(UUID id); 
    Optional<Raport> findByPatientId(UUID id);
    
}
