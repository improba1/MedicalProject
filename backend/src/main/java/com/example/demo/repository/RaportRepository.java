package com.example.demo.repository;

import com.example.demo.model.Raport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RaportRepository extends JpaRepository<Raport, UUID> {

    Raport findByVisitId(UUID visitId);

    List<Raport> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}